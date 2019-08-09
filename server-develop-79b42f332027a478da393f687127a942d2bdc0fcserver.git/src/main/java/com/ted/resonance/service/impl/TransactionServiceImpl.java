package com.ted.resonance.service.impl;

import com.ted.resonance.entity.AddressInfo;
import com.ted.resonance.entity.Coupon;
import com.ted.resonance.entity.Team;
import com.ted.resonance.entity.Transaction;
import com.ted.resonance.entity.valueobject.Nonce;
import com.ted.resonance.entity.valueobject.TransactionParams;
import com.ted.resonance.repository.AddressInfoRepo;
import com.ted.resonance.repository.CouponRepo;
import com.ted.resonance.repository.TeamRepo;
import com.ted.resonance.repository.TransactionRepo;
import com.ted.resonance.service.AddressInfoService;
import com.ted.resonance.service.CouponService;
import com.ted.resonance.service.TransactionService;
import com.ted.resonance.service.UpdateBlockService;
import com.ted.resonance.utils.CacheUtils;
import com.ted.resonance.utils.ContractUtils;
import com.ted.resonance.utils.CouponGenerateStrategy;
import com.ted.resonance.utils.exceptions.CommonException;
import com.ted.resonance.utils.exceptions.SendTransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Int;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);
    @Autowired
    private CouponRepo couponRepo;
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private AddressInfoService addressInfoService;
    @Autowired
    private TeamRepo teamRepo;
    @Autowired
    private AddressInfoRepo addressInfoRepo;
    @Autowired
    private CouponService couponService;
    @Autowired @Qualifier("etcClient")
    private Web3j etcClient;

    @Autowired @Qualifier("ethClient")
    private Web3j ethClient;

    @Value("${etcContractAddress}") private String etcContractAddress;
    @Value("${ethContractAddress}") private String ethContractAddress;
    @Value("${workerAddress}") private String workerAddress;


    @Override
    public Nonce getNonce(String address) throws Exception {
        Nonce nonce = new Nonce();
        nonce.setEtcNonce(etcClient.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount().intValue());
        nonce.setEthNonce(ethClient.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).send().getTransactionCount().intValue());
        return nonce;
    }

    @Override
    @Transactional
    public Coupon sendResonance(TransactionParams params, Integer phase) throws Exception {
        AddressInfo addressInfo = addressInfoService.getByAddressAndPhase(params.getAddress(), phase);
        if(addressInfo == null) {
            addressInfo = new AddressInfo();
            addressInfo.setAddress(params.getAddress());
            addressInfo.setPhase(phase);
            addressInfo.setStatus(0);
        }
        //检查有没有邀请人, 邀请人合法性检查
        if(params.getInviterAddress() != null && !params.getInviterAddress().equals("") && !params.getInviterAddress().equals(params.getAddress())) {
            if(addressInfoService.inviterWritable(params.getAddress())) {
                addressInfo.setInviterAddress(params.getInviterAddress().toLowerCase());
            }else {
                throw new CommonException(999, "inviterNotWritable");
            }
        }
        addressInfoService.save(addressInfo);

        params.setType(params.getType().trim().toLowerCase());
        //保存交易信息
        Transaction transaction = new Transaction();
        transaction.setPhase(phase);
        BeanUtils.copyProperties(params, transaction);
        transaction.setStatus(0);
        transaction = transactionRepo.save(transaction);

        if(params.getCouponId() == null || params.getCouponId() == 0) {
            //无优惠券交易 创建一个优惠券
            // 向链上就提交交易申请
            EthSendTransaction sendTransaction = sendTransaction(params.getType(), params.getSignedString());
            if(sendTransaction.getTransactionHash() == null) {
                // 抛出链上返回错误
                throw new SendTransactionException(999, sendTransaction.getError().getMessage());
            }
            transaction.setTransactionHash(sendTransaction.getTransactionHash());
//            System.out.println(transaction.getTransactionHash());
            //根据需求的优惠券规则创造优惠券
            Coupon coupon = CouponGenerateStrategy.generateCoupon(params.getType(), params.getPayment());
            if(coupon != null) {
                coupon.setAddress(params.getAddress());
                coupon.setStatus(1);
                coupon.setTransactionId(transaction.getId());
                coupon.setLeftTime(24L * 60 * 60 * 1000);
                coupon = couponRepo.save(coupon);
            }
            transaction.setCouponId(null);
            transactionRepo.save(transaction);
            // 交易马上成功 用于测试
//            transactionSuccess(transaction.getId(), 1);

            if(coupon != null) {
                return coupon;
            }
            return null;

        }else {
            //  使用优惠券交易
            //检查优惠券
            Coupon coupon = couponRepo.findById(params.getCouponId()).get();
            if(coupon == null || coupon.getStatus() != 0) {
                throw new CommonException(999, "couponInvalid");
            }
            //将优惠券的状态更改为已使用， 记录优惠券剩余时间
            coupon.setStatus(3);
            coupon.setLeftTime(coupon.getEndTime().getTime() - System.currentTimeMillis());
            coupon.setLeftBlock(coupon.getEndBlock() - getCurrentBlockByType(params.getType()));

            EthSendTransaction sendTransaction = sendTransaction(params.getType(), params.getSignedString());
            if(sendTransaction.getTransactionHash() == null) {
                // 抛出链上返回错误
                throw new SendTransactionException(999, sendTransaction.getError().getMessage());
            }
            transaction.setTransactionHash(sendTransaction.getTransactionHash());
            transactionRepo.save(transaction);
            couponRepo.save(coupon);
            return null;
        }
    }

    // 发送共振 会检查共振是否结束
    private EthSendTransaction sendTransaction(String type, String signedString) throws Exception {
        int blockHeight;
        EthSendTransaction sendTransaction = null;
        if(type.toLowerCase().equals("etc")) {
            blockHeight = etcClient.ethBlockNumber().send().getBlockNumber().intValue();
            if(blockHeight >= getEtcEndBlock().intValue()) {
                throw new CommonException(999, "resonaceEnd");
            }else if(blockHeight < getEtcStartBlock().intValue()) {
                throw new CommonException(999, "resonanceNotBegin");
            }
            if(ContractUtils.getCurrentStage(workerAddress, etcContractAddress, etcClient).intValue() >= 2019) {
                throw new CommonException(999, "resonaceEnd");
            }
            sendTransaction = etcClient.ethSendRawTransaction(signedString).send();
        }else if("eth".equals(type.toLowerCase())){
            blockHeight = ethClient.ethBlockNumber().send().getBlockNumber().intValue();
            if(blockHeight >=  getEthEndBlock().intValue()) {
                throw new CommonException(999, "resonaceEnd");
            }else if(blockHeight < getEthStartBlock().intValue()) {
                throw new CommonException(999, "resonanceNotBegin");
            }

            if(ContractUtils.getCurrentStage(workerAddress, ethContractAddress, ethClient).intValue() >= 999) {
                throw new CommonException(999, "resonaceEnd");
            }
            sendTransaction = ethClient.ethSendRawTransaction(signedString).send();
        }
        return sendTransaction;
    }

    /**
     * 根据数据库的日期确定阶段
     * @return
     */
    @Override
    public int getPhase() {
        if(transactionRepo.getPeriodChangeTime().getTime() > new Date().getTime()){
            return 1;
        }
        return 2;
    }

    // 监听链上交易， 实现成功逻辑和失败逻辑
    // 交易成功，addressInfo status 为0，变为1 优惠券生效
    // 若转入地址填写了邀请人，转入地址不是团长，邀请人没有团，新建一个团，团长为邀请人，将转入地址加入该团 邀请人没有信息则新建一个相关信息，status 2
    // 若转入地址填写了邀请人，转入地址不是团长， 邀请人已加入团，转入地址改为加入该团
    // 若转入地址填写了邀请人，转入地址是团长，邀请人没有团，团长更换为邀请人
    // 若转入地址填写了邀请人，转入地址是团长，邀请人已加入团， 转入地址的团下的人都加入这个新的团
    // 若转入地址没有填写邀请人， 转入地址是团长
    // 若转入地址没有填写邀请人，转入地址不是团长， 新建一个团， 团长为转入地址
    // 团 人数  实时统计
    public void transactionSuccess(Transaction transaction, Integer blockNumber) {
//        Transaction transaction = transactionRepo.findById(id).get();

        String address = transaction.getAddress();
        Integer phase = transaction.getPhase();
        //网体绑定
        AddressInfo addressInfo = addressInfoService.getByAddressAndPhase(address, phase);

        //自身共振金额累加
        addPersonalFund(transaction, addressInfo);

        Team team = teamRepo.findByLeaderAddressAndPhase(address, phase);
        //2019 .7.8 邀请人可以一直加
        if(addressInfo.getStatus() != 1) {
            if(addressInfo.getInviterAddress() == null) {
                //转入地址 没有邀请人
                if(team == null){
                    //转入地址不是团长， 没有邀请人
                    team = new Team();
                    team.setLeaderAddress(address);
                    team.setPhase(phase);
//                    team.setNumber(1);
                    team = teamRepo.save(team);
                }

//                if(transaction.getType().equals("etc")) {
//                    team.setEtcFund(transaction.getPayment());
//                }else {
//                    team.setEthFund(transaction.getPayment());
//                }
                addTeamFund(transaction, team);
                addressInfo.setTeamId(team.getId());

            }else {
                //转入地址具有邀请人
                addressInfo.setStatus(1);  //邀请人绑定后无法更改
                if(team != null) {
                    //转入地址为团长
                    AddressInfo inviterAddressInfo = addressInfoService.getByAddressAndPhase(addressInfo.getInviterAddress(), phase);
                    if(inviterAddressInfo == null || inviterAddressInfo.getTeamId() == null)  {
                        //转入地址为团长, 邀请人没有加入过任何团
                        if(inviterAddressInfo == null) {
                            inviterAddressInfo = new AddressInfo();
                            inviterAddressInfo.setStatus(2);
                            inviterAddressInfo.setAddress(addressInfo.getInviterAddress());
                            inviterAddressInfo.setPhase(phase);
                        }
                        inviterAddressInfo.setTeamId(team.getId());
                        team.setLeaderAddress(inviterAddressInfo.getAddress());
                        //团队金额加钱
                        addTeamFund(transaction, team);

                    }else {
                        //转入地址为团长, 邀请人 已经加入过某个团
                        Team inviterTeam = teamRepo.findById(inviterAddressInfo.getTeamId()).get();
                        if(addressInfo.getTeamId() != inviterAddressInfo.getTeamId()) {
                            //邀请人不是本团的
                            LOG.info("address" + addressInfo.getAddress() + " add a team " + inviterAddressInfo.getTeamId());
                            List<AddressInfo> teamMembers = addressInfoRepo.findByTeamIdAndPhase(team.getId(), phase);
//                            System.out.println(teamMembers.size());
                            for(AddressInfo member: teamMembers) {
                                member.setTeamId(inviterTeam.getId());
                                addressInfoRepo.save(member);
                            }
                            //transactional 注解不能修改从其他地方获取的 addressInfo
                            addressInfo.setTeamId(inviterAddressInfo.getTeamId());
                            if(team.getEtcFund() != null) {
                                if(inviterTeam.getEtcFund() != null) {
                                    inviterTeam.setEtcFund(inviterTeam.getEtcFund().add(team.getEtcFund()));
                                }else {
                                    inviterTeam.setEtcFund(team.getEtcFund());
                                }
                            }
                            if(team.getEthFund() != null) {
                                if(inviterTeam.getEthFund() != null) {
                                    inviterTeam.setEthFund(inviterTeam.getEthFund().add(team.getEthFund()));
                                }else {
                                    inviterTeam.setEtcFund(team.getEthFund());
                                }
                            }
                            //删除旧的团
                            teamRepo.delete(team);
                            team = inviterTeam;
                            addTeamFund(transaction, team);
                        }else {
                            //邀请人是本团成员， 此时构成环  会将他们的团作废
                            team.setPhase(phase + 100);
                            addTeamFund(transaction, team);
                            teamRepo.save(team);
//                            List<AddressInfo> teamMembers = addressInfoRepo.findByTeamIdAndPhase(team.getId(), phase);
//                            for(AddressInfo member: teamMembers) {
//                                member.setStatus(4);
//                                addressInfoRepo.save(member);
//                            }
                        }
                    }
                }else {
                    //转入地址不是团长
                    AddressInfo inviterAddressInfo = addressInfoService.getByAddressAndPhase(addressInfo.getInviterAddress(), phase);
                    if(inviterAddressInfo == null || inviterAddressInfo.getTeamId() == null){
                        //邀请人没有加入过任何团
                        if(inviterAddressInfo == null) {
                            inviterAddressInfo = new AddressInfo();
                            inviterAddressInfo.setStatus(2);
                            inviterAddressInfo.setAddress(addressInfo.getInviterAddress());
                            inviterAddressInfo.setPhase(phase);
                        }
                        team = new Team();
                        team.setPhase(phase);
                        team.setLeaderAddress(inviterAddressInfo.getAddress());
                        team = teamRepo.save(team);
                        inviterAddressInfo.setTeamId(team.getId());
                        addressInfo.setTeamId(team.getId());
                        addressInfoRepo.save(inviterAddressInfo);
                    }else {
                        //邀请人已加入团
                        addressInfo.setTeamId(inviterAddressInfo.getTeamId());
                        team = teamRepo.findById(inviterAddressInfo.getTeamId()).get();
                    }
                    addTeamFund(transaction, team);
                }
            }
        }else {
            //已经绑定了邀请人的成员
//            System.out.println(addressInfo.getAddress());
            team = teamRepo.findById(addressInfo.getTeamId()).get();
            addTeamFund(transaction, team);
        }

        teamRepo.save(team);
        addressInfoService.save(addressInfo);
        transactionRepo.save(transaction);

        //优惠券生效
        couponService.startCoupon(transaction.getId(), blockNumber);
    }

    //计算个人奖金
    public void addPersonalFund(Transaction transaction, AddressInfo addressInfo) {
        if(transaction.getType().equals("etc")) {
            //个人
            if(addressInfo.getEtcPayment() == null) {
                addressInfo.setEtcPayment(transaction.getPayment());
            }else {
                addressInfo.setEtcPayment(addressInfo.getEtcPayment().add(transaction.getPayment()));
            }

        }else if(transaction.getType().equals("eth")) {
            if(addressInfo.getEthPayment() == null) {
                addressInfo.setEthPayment(transaction.getPayment());
            }else {
                addressInfo.setEthPayment(addressInfo.getEthPayment().add(transaction.getPayment()));
            }
        }
    }

    public void addTeamFund(Transaction transaction, Team team) {
        if(transaction.getType().equals("etc")) {
            if(team.getEtcFund() == null) {
                team.setEtcFund(transaction.getPayment());
            }else {
                team.setEtcFund(team.getEtcFund().add(transaction.getPayment()));
            }
        }else {
            if(team.getEthFund() == null) {
                team.setEthFund(transaction.getPayment());
            }else {
                team.setEthFund(team.getEthFund().add(transaction.getPayment()));
            }
        }
    }




    // 交易失败,若addressInfo为0， 则变为2，若有生成的优惠券，删除
    public void transactionFail(Transaction transaction, Integer blockNumber) {
        transaction.setStatus(2);
        transactionRepo.save(transaction);
        AddressInfo addressInfo = addressInfoService.getByAddressAndPhase(transaction.getAddress(), transaction.getPhase());
        if(addressInfo == null) {
            return;
        }
        if(addressInfo.getStatus() == 0) {
            addressInfo.setStatus(2);
        }
        addressInfoService.save(addressInfo);
        //返还优惠券
        if(transaction.getCouponId() != null) {
            couponService.recoverCoupon(transaction.getCouponId(), blockNumber);
        }

//        删除优惠券
        Coupon coupon = couponRepo.findByTransactionId(transaction.getId());
        if(coupon != null) {
            couponRepo.delete(coupon);
        }
    }

    @Override
    public void rewardTransactionFail(Transaction transaction) {
        AddressInfo addressInfo = addressInfoService.getByAddressAndPhase(transaction.getAddress(), transaction.getPhase());
        if("etc".equals(transaction.getType().toLowerCase())) {
            addressInfo.setEtcRewarded(false);
        }else if("eth".equals(transaction.getType().toLowerCase())) {
            addressInfo.setEthRewarded(false);
        }
        addressInfoRepo.save(addressInfo);
    }

    @Override
    public BigInteger getEtcBalance(String address) throws Exception{
        BigInteger value = etcClient.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();
        return value;
    }

    @Override
    public BigInteger getEthBalance(String address) throws Exception {
        return ethClient.ethGetBalance(address, DefaultBlockParameterName.LATEST).send().getBalance();

    }

    @Override
    public BigInteger getEtcStartBlock() throws Exception {
        if(CacheUtils.getEtcStartBlock() == null) {
            CacheUtils.setEtcStartBlock(ContractUtils.getStartBlock(workerAddress, etcContractAddress, etcClient));
        }
        return CacheUtils.getEtcStartBlock();
    }

    @Override
    public BigInteger getEtcEndBlock() throws Exception {
        if(CacheUtils.getEtcEndBlock() == null) {
            CacheUtils.setEtcEndBlock(ContractUtils.getEndBlock(workerAddress, etcContractAddress, etcClient));
        }
        return CacheUtils.getEtcEndBlock();
    }

    @Override
    public BigInteger getEthStartBlock() throws Exception {
        if(CacheUtils.getEthStartBlock() == null) {
            CacheUtils.setEthStartBlock(ContractUtils.getStartBlock(workerAddress, ethContractAddress, ethClient));
        }
//        System.out.println(ContractUtils.getStartBlock(workerAddress, ethContractAddress, ethClient));
        return CacheUtils.getEthStartBlock();
    }

    @Override
    public BigInteger getEthEndBlock() throws Exception {
        if(CacheUtils.getEthEndBlock() == null) {
            CacheUtils.setEthEndBlock(ContractUtils.getEndBlock(workerAddress, ethContractAddress, ethClient));
        }
//        System.out.println(ContractUtils.getEndBlock(workerAddress, ethContractAddress, ethClient));
        return CacheUtils.getEthEndBlock();
    }

    public Integer getCurrentBlockByType(String type) throws Exception {
        if("etc".equals(type.toLowerCase())) {
            return getEtcCurrentBlock().intValue();
        }else if("eth".equals(type.toLowerCase())) {
            return getEthCurrentBlock().intValue();
        }
        return 0;
    }

    @Override
    public BigInteger getEtcCurrentBlock() throws Exception {
        return etcClient.ethBlockNumber().send().getBlockNumber();
    }

    @Override
    public BigInteger getEthCurrentBlock() throws Exception {
        return ethClient.ethBlockNumber().send().getBlockNumber();
    }

    @Override
    public BigDecimal getEtcReward(BigDecimal payment) throws Exception {
//        return calReward(payment, ContractUtils.getRatio(workerAddress, ethContractAddress, etcClient));
        if(payment.compareTo(BigDecimal.valueOf(3100000)) > 0) {
            throw new CommonException(999, "paymentLimit");
        }
        BigInteger reward = ContractUtils.calReward(workerAddress, payment, "etc", etcContractAddress, etcClient);
        return new BigDecimal(reward, 18).setScale(2, BigDecimal.ROUND_DOWN);
    }

    @Override
    public BigDecimal getEthReward(BigDecimal payment) throws Exception {
        if(payment.compareTo(BigDecimal.valueOf(70000)) > 0) {
            throw new CommonException(999, "paymentLimit");
        }
        BigInteger reward = ContractUtils.calReward(workerAddress, payment, "eth", ethContractAddress, ethClient);
        return new BigDecimal(reward, 18).setScale(2, BigDecimal.ROUND_DOWN);
//        return calReward(payment, ContractUtils.getRatio(workerAddress, ethContractAddress, ethClient));
    }

    private BigDecimal calReward(BigDecimal payment, BigInteger ratio){

//        ratio = ratio.divide(BigInteger.valueOf((long)Math.pow(10, 18)));
        BigDecimal r = new BigDecimal(ratio, 18);
        return payment.multiply(r);
    }

    @Override
    public void save(Transaction transaction) {
        transactionRepo.save(transaction);
    }
}
