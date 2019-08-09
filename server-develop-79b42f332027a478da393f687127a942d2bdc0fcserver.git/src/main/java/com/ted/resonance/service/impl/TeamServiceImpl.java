package com.ted.resonance.service.impl;

import com.ted.resonance.entity.Account;
import com.ted.resonance.entity.AddressInfo;
import com.ted.resonance.entity.Team;
import com.ted.resonance.entity.Transaction;
import com.ted.resonance.entity.valueobject.TeamsInfo;
import com.ted.resonance.repository.AddressInfoRepo;
import com.ted.resonance.repository.TeamRepo;
import com.ted.resonance.repository.TransactionRepo;
import com.ted.resonance.service.AccountService;
import com.ted.resonance.service.AddressInfoService;
import com.ted.resonance.service.TeamService;
import com.ted.resonance.service.TransactionService;
import com.ted.resonance.utils.CacheUtils;
import com.ted.resonance.utils.ContractUtils;
import com.ted.resonance.utils.exceptions.CommonException;
import com.ted.resonance.utils.exceptions.SendTransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private TeamRepo teamRepo;

    @Autowired
    private AddressInfoRepo addressInfoRepo;
    @Autowired
    private AddressInfoService addressInfoService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionRepo transactionRepo;
    @Autowired
    private TransactionService transactionService;

    @Autowired @Qualifier("etcClient")
    private Web3j etcClient;

    @Autowired @Qualifier("ethClient")
    private Web3j ethClient;
    @Value("${fileServer}") private String fileSever;


    @Value("${etcKnightAddr}") private String etcKnightAddr;
    @Value("${ethKnightAddr}") private String ethKnightAddr;
    @Value("${etcWorkerPrivateKey}") private String workerPrivateKey;

    @Override
    public TeamsInfo getTeamsInfo(String address, String type, Integer phase) throws Exception {
        //获取排行榜信息 并返回
        TeamsInfo teamsInfo = new TeamsInfo();
        //返回空数组
        List<Team> topTeams = new LinkedList<>();
        teamsInfo.setTopTwentyTeams(topTeams);
        //第一期没开始
        BigInteger currentBlock = transactionService.getEtcCurrentBlock();

        if(currentBlock.compareTo(transactionService.getEtcStartBlock()) < 0) {
//            System.out.println(transactionService.getEtcStartBlock());
            teamsInfo.setStatus(0);
            teamsInfo.setEtcBonusPool(BigDecimal.valueOf(0));
            teamsInfo.setEthBonusPool(BigDecimal.valueOf(0));
//            teamsInfo.setLeftBlock(transactionService.getEtcStartBlock().subtract(currentBlock).intValue());
            return teamsInfo;
        }
        currentBlock = transactionService.getEthCurrentBlock();
        if(currentBlock.compareTo(transactionService.getEthStartBlock()) < 0) {
            teamsInfo.setStatus(0);
            teamsInfo.setEtcBonusPool(BigDecimal.valueOf(0));
            teamsInfo.setEthBonusPool(BigDecimal.valueOf(0));
//            teamsInfo.setLeftBlock(transactionService.getEthStartBlock().subtract(currentBlock).intValue());
            return teamsInfo;
        }

        //进行中 和结束
        AddressInfo addressInfo = addressInfoRepo.findByAddressAndPhase(address, phase);
        if(transactionService.getPhase() == 1){
            if(phase == 2) {
                //第二期 未开始
                //计算奖池
                teamsInfo.setStatus(0);
                BigDecimal sum = null;
                if((sum = teamRepo.sumEtcByPhase(1)) != null) {
                    teamsInfo.setEtcBonusPool(sum.multiply(BigDecimal.valueOf(0.05)));
                }else {
                    teamsInfo.setEtcBonusPool(BigDecimal.ZERO);
                }
                if((sum = teamRepo.sumEthByPhase(1)) != null) {
                    teamsInfo.setEthBonusPool(sum.multiply(BigDecimal.valueOf(0.05)));
                }else {
                    teamsInfo.setEthBonusPool(BigDecimal.ZERO);
                }
                return teamsInfo;
            }
            teamsInfo.setStatus(1);
            if("etc".equals(type)) {
                BigDecimal sum = null;
                if((sum = teamRepo.sumEtcByPhase(phase)) != null) {
                    teamsInfo.setEtcBonusPool(sum.multiply(BigDecimal.valueOf(0.05)));
                }
            }else if("eth".equals(type)) {
                BigDecimal sum = null;
                if((sum = teamRepo.sumEthByPhase(phase)) != null) {
                    teamsInfo.setEthBonusPool(sum.multiply(BigDecimal.valueOf(0.05)));
                }
            }

        }else if(transactionService.getPhase() == 2){
            if(phase == 1) {
                if(teamRepo.getStatus(1) == 0) {
                    //第一期未开奖
                    teamsInfo.setStatus(5);
                }else {
                    //第一期已开奖
                    teamsInfo.setStatus(3);
                }
                if("etc".equals(type)) {
                    BigDecimal sum = null;
                    if((sum = teamRepo.sumEtcByPhase(1)) != null) {
                        teamsInfo.setEtcBonusPool(sum.multiply(BigDecimal.valueOf(0.05)));
                    }
                }else if("eth".equals(type)) {
                    BigDecimal sum = null;
                    if((sum = teamRepo.sumEthByPhase(1)) != null) {
                        teamsInfo.setEthBonusPool(sum.multiply(BigDecimal.valueOf(0.05)));
                    }
                }
            }else if(phase == 2) {
                teamsInfo.setStatus(1);
                //第二期已结束 未开奖
                if("etc".equals(type.toLowerCase())) {
                    currentBlock = transactionService.getEtcCurrentBlock();
                    if(currentBlock.compareTo(transactionService.getEtcEndBlock()) >= 0) {
                        teamsInfo.setStatus(5);
                        //第二期 开奖
                        if(teamRepo.getStatus(2) == 1) {
                            teamsInfo.setStatus(3);
                        }
                    }
                    //奖池
                    BigDecimal phase1Sum = teamRepo.sumEtcByPhase(1);
                    if(phase1Sum == null) {
                        phase1Sum = BigDecimal.ZERO;
                    }
                    BigDecimal sum = null;
                    if((sum = teamRepo.sumEtcByPhase(phase)) != null) {
                        //sum1 * 0.05 + sum2 * 0.1
                        teamsInfo.setEtcBonusPool(sum.multiply(BigDecimal.valueOf(0.1)).add(phase1Sum.multiply(BigDecimal.valueOf(0.05))));
                    }else {
                        teamsInfo.setEtcBonusPool(phase1Sum.multiply(BigDecimal.valueOf(0.05)));
                    }
                }else if("eth".equals(type.toLowerCase())) {
                    currentBlock = transactionService.getEthCurrentBlock();
                    if(currentBlock.compareTo(transactionService.getEthEndBlock()) >= 0) {
                        teamsInfo.setStatus(5);
                        //第二期 开奖
                        if(teamRepo.getStatus(2) == 1) {
                            teamsInfo.setStatus(3);
                        }
                    }
                    BigDecimal phase1Sum = teamRepo.sumEthByPhase(1);
                    if(phase1Sum == null) {
                        phase1Sum = BigDecimal.ZERO;
                    }
                    BigDecimal sum = null;
                    if((sum = teamRepo.sumEthByPhase(phase)) != null) {
                        teamsInfo.setEthBonusPool(sum.multiply(BigDecimal.valueOf(0.1)).add(phase1Sum.multiply(BigDecimal.valueOf(0.05))));
                    }else {
                        teamsInfo.setEthBonusPool(phase1Sum.multiply(BigDecimal.valueOf(0.05)));
                    }
                }
            }
        }

        teamsInfo.setPhase(phase);

        if(type.equals("etc")) {
            topTeams = teamRepo.findAllByPhaseOrderByEtcFundDesc(phase);
        }else if(type.equals("eth")) {
            topTeams = teamRepo.findAllByPhaseOrderByEthFundDesc(phase);
        }

        Integer myPosition = rankTeams(addressInfo, topTeams, type);
        if(myPosition != -1) {
            teamsInfo.setMyTeam(topTeams.get(myPosition));
            if(teamsInfo.getStatus() == 3 && teamsInfo.getMyTeam().getRank() >= 10) {
                //已中奖
                teamsInfo.setStatus(2);
                //已领奖
                if(type.equals("etc")) {
                    if(!rewardable("etc", addressInfo)) {
                        teamsInfo.setStatus(3);
                    }else if(addressInfo!=null && addressInfo.getEtcRewarded() != null && addressInfo.getEtcRewarded()){
                        teamsInfo.setStatus(4);
                    }
                }else if(type.equals("eth")) {
                    if(!rewardable("eth", addressInfo)){
                        teamsInfo.setStatus(3);
                    }else if(addressInfo != null && addressInfo.getEthRewarded() != null && addressInfo.getEthRewarded()) {
                        teamsInfo.setStatus(4);
                    }
                }
            }
        }
        if(topTeams.size() > 20) {
            teamsInfo.setTopTwentyTeams(topTeams.subList(0,20));
        }else {
            teamsInfo.setTopTwentyTeams(topTeams);
        }
        return teamsInfo;
    }

    private Integer rankTeams(AddressInfo addressInfo, List<Team> teams, String type) {
        //rank
        int rank = 0;
        //我的位置
        int myPosition = -1;
        for(Team team : teams) {
            team.setRank(++rank);
            team.setNumber(teamRepo.getTeamNumber(team.getId()));
            if(addressInfo != null && addressInfo.getTeamId() != null && addressInfo.getTeamId() == team.getId()) {
                myPosition = rank - 1;
                team.setMyTeam(true);
                //我的领队奖金
                team.setMyLeaderFund(addressInfoService.getMyLeaderFund(addressInfo.getAddress(), type, addressInfo.getPhase()));
            }else {
                team.setMyTeam(false);
            }
            //团长头像 昵称 地址，国家
            Account account = accountService.checkAccount(team.getLeaderAddress(), "Other");
            team.setNickname(account.getNickname());
            team.setHeadPicture(account.getHeadPicture());
            String nation = account.getNation().trim().toLowerCase();
            team.setNationUrl(fileSever + "nation/" + nation +".png");
        }
        return myPosition;
    }


    @Override
    public BigDecimal checkReward(String address, Integer phase, String type) {
        AddressInfo addressInfo = addressInfoRepo.findByAddressAndPhase(address, phase);
        if(addressInfo == null || addressInfo.getTeamId() == null) return BigDecimal.ZERO;
        Team myTeam = teamRepo.findById(addressInfo.getTeamId()).get();
        if(myTeam == null) {
            return BigDecimal.ZERO;
        }
        List<Team> teams = "etc".equals(type) ?
                teamRepo.findAllByPhaseOrderByEtcFundDesc(phase) :
                teamRepo.findAllByPhaseOrderByEthFundDesc(phase);

        for(int i = 0; i < Math.min(teams.size(), 10);i++){
            if(myTeam.getId() == teams.get(i).getId()) {
                myTeam.setRank(i+1);
            }
        }
        if(myTeam.getRank() == null) {
            return BigDecimal.ZERO;
        }
        HashMap<String, BigDecimal> finalRewards = calTeamReward(myTeam, type);
        BigDecimal myReward = finalRewards.get(address).setScale(8, BigDecimal.ROUND_DOWN);
        return myReward;
    }

    @Override
    public void getReward(String address, Integer phase, String type, String signedString) throws Exception {
        // 向链上提交申请 记录交易
        if(teamRepo.getStatus(phase) == 0) {
            throw new CommonException(999, "waitReward");
        }
        AddressInfo addressInfo = addressInfoRepo.findByAddressAndPhase(address, phase);
        if(addressInfo == null || addressInfo.getTeamId() == null) {
            throw new CommonException(999, "noReward");
        }
        Team team = teamRepo.findById(addressInfo.getTeamId()).get();
        if(team.getPhase() != phase) {
            throw new CommonException(999, "noReward");
        }

        BigDecimal myReward = null;
        EthSendTransaction sendTransaction = null;
        if("etc".equals(type.toLowerCase())) {
            //etc 领奖
            if(addressInfo.getEtcRewarded() != null && addressInfo.getEtcRewarded()) {
                throw new CommonException(999, "rewarded");
            }
            addressInfo.setEtcRewarded(true);
            myReward = checkReward(address, phase, type);
            if(myReward.compareTo(BigDecimal.ZERO) == 0) {
                throw new CommonException(999, "rewarded");
            }
//            hash = ContractUtils.knightWithdraw(etcClient, etcKnightAddr, credentials, phase, address, myReward.movePointRight(18).toBigInteger());
            sendTransaction = etcClient.ethSendRawTransaction(signedString).send();


        }else if("eth".equals(type.toLowerCase())) {
            //eth 领奖
            if(addressInfo.getEthRewarded() != null && addressInfo.getEthRewarded()) {
                throw new CommonException(999, "rewarded");
            }
            addressInfo.setEthRewarded(true);
            myReward = checkReward(address, phase, type);
            if(myReward.compareTo(BigDecimal.ZERO) == 0) {
                throw new CommonException(999, "rewarded");
            }
//            hash = ContractUtils.knightWithdraw(ethClient, ethKnightAddr, credentials, phase, address, myReward.movePointRight(18).toBigInteger());
            sendTransaction = ethClient.ethSendRawTransaction(signedString).send();
        }else {
            throw new CommonException(999, "coin type incorrect");
        }
        //错误
        if(sendTransaction.getTransactionHash() == null) {
            throw new SendTransactionException(999, sendTransaction.getError().getMessage());
        }
        //保存领奖的交易信息
        Transaction transaction = new Transaction();
        transaction.setTransactionHash(sendTransaction.getTransactionHash());
        transaction.setPhase(phase);
        transaction.setStatus(0);
        transaction.setType(type);
        transaction.setPayment(new BigDecimal(0));
        transaction.setReward(myReward);
        transaction.setAddress(address);
        transactionRepo.save(transaction);

        addressInfoRepo.save(addressInfo);
    }

    //计算个人的待奖励的奖金
    //团长 rank
    private HashMap<String, BigDecimal> calTeamReward(Team team, String type) {
        //sum * 50% * 20% * 50% * ratioOfRank = sum * 0.05 * ratioOfRank
        BigDecimal teamsReward = null;
        if(team.getPhase() == 1) {
            teamsReward = "etc".equals(type) ?
                    teamRepo.sumEtcByPhase(team.getPhase()).multiply(BigDecimal.valueOf(0.05 * getRatioByRank(team.getRank()))) :
                    teamRepo.sumEthByPhase(team.getPhase()).multiply(BigDecimal.valueOf(0.05 * getRatioByRank(team.getRank())));
        }

        if(team.getPhase() == 2) {
            //第二期
            BigDecimal periodOneReward = "etc".equals(type) ?
                    teamRepo.sumEtcByPhase(1).multiply(BigDecimal.valueOf(0.05)) :
                    teamRepo.sumEthByPhase(1).multiply(BigDecimal.valueOf(0.05));
            teamsReward = "etc".equals(type) ?
                    teamRepo.sumEtcByPhase(team.getPhase()).multiply(BigDecimal.valueOf(0.1 * getRatioByRank(team.getRank()))) :
                    teamRepo.sumEthByPhase(team.getPhase()).multiply(BigDecimal.valueOf(0.1 * getRatioByRank(team.getRank())));
            teamsReward = teamsReward.add(periodOneReward);
        }

        List<AddressInfo> teamMembers = addressInfoRepo.findByTeamIdAndPhase(team.getId(), team.getPhase());

        HashMap<String, BigDecimal> rewards = new HashMap<>();  //待领取的初始奖金

        for(AddressInfo teamMember : teamMembers) {
            BigDecimal myFund = BigDecimal.ZERO;
            if(!rewardable(type, teamMember)) {
                rewards.put(teamMember.getAddress(), myFund);
                continue;
            }
            //保留三位小数
            double myRatio = "etc".equals(type) ?
                    teamMember.getEtcPayment().divide(team.getEtcFund(), 3, BigDecimal.ROUND_DOWN).doubleValue() :
                    teamMember.getEthPayment().divide(team.getEthFund(), 3, BigDecimal.ROUND_DOWN).doubleValue() ;
            myFund = myFund.add(teamsReward.multiply(BigDecimal.valueOf(0.98 * myRatio)));

            rewards.put(teamMember.getAddress(), myFund);
        }

        HashMap<String, BigDecimal> finalReward = new HashMap<>(); //最终可以领取的奖金

        for(AddressInfo teamMember : teamMembers) {
            BigDecimal myFund = BigDecimal.ZERO;
            if(!rewardable(type, teamMember)) {
                finalReward.put(teamMember.getAddress(), myFund);
                continue;
            }
            if(teamMember.getAddress().equals(team.getLeaderAddress())) {
                //团长 teamsReward * 0.02
                myFund = myFund.add(teamsReward.multiply(BigDecimal.valueOf(0.02)));
            }
            //self * 0.85
            myFund = myFund.add(rewards.get(teamMember.getAddress()).multiply(BigDecimal.valueOf(0.85)));
            List<AddressInfo> nextLevels = addressInfoRepo.findByInviterAddressAndPhase(teamMember.getAddress(), team.getPhase());
            for(AddressInfo nextLevel : nextLevels) {
                //level1 0.08
                myFund = myFund.add(rewards.get(nextLevel.getAddress()).multiply(BigDecimal.valueOf(0.08)));
                List<AddressInfo> nextNextLevels = addressInfoRepo.findByInviterAddressAndPhase(nextLevel.getAddress(), team.getPhase());
                for(AddressInfo nextNextLevel : nextNextLevels) {
                    //level2 0.07
                    myFund = myFund.add(rewards.get(nextNextLevel.getAddress()).multiply(BigDecimal.valueOf(0.07)));
                }
            }

            finalReward.put(teamMember.getAddress(), myFund);
        }
        return  finalReward;
    }

    private Boolean rewardable(String type, AddressInfo teamMember) {
        if ("etc".equals(type)) {
            if (teamMember.getEtcPayment() == null || teamMember.getEtcPayment().compareTo(BigDecimal.valueOf(280)) < 0) {
                //etc 不满足领奖条件
                return false;
            }

        } else {
            if (teamMember.getEthPayment() == null || teamMember.getEthPayment().compareTo(BigDecimal.valueOf(10)) < 0) {
                //eth 不满足领奖条件
                return false;
            }
        }
        return true;
    }

    private float getRatioByRank(int rank) {
        switch (rank) {
            case 1:
                return 0.267f;
            case 2:
                return 0.18f;
            case 3:
                return 0.132f;
            case 4:
                return 0.101f;
            case 5:
                return 0.08f;
            case 6:
                return 0.065f;
            case 7:
                return 0.053f;
            case 8:
                return 0.045f;
            case 9:
                return 0.04f;
            case 10:
                return 0.037f;
            default:
                return 0;
        }
    }
}
