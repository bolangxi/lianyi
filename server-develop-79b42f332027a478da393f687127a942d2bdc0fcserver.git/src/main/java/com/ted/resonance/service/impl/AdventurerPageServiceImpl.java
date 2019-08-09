package com.ted.resonance.service.impl;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.config.DataDictCache;
import com.ted.resonance.config.DataDictConstant;
import com.ted.resonance.entity.ExActivityAddition;
import com.ted.resonance.entity.ExCfgActivity;
import com.ted.resonance.entity.response.AdventurerPageVO;
import com.ted.resonance.entity.response.ExpectContributionPageVO;
import com.ted.resonance.enums.ActivityStatusEnum;
import com.ted.resonance.mapper.TokenBalanceBeanMapper;
import com.ted.resonance.repository.ExActivityAdditionRepo;
import com.ted.resonance.repository.ExCfgActivityRepo;
import com.ted.resonance.repository.ExTedBurnRepo;
import com.ted.resonance.service.AdventurerPageService;
import com.ted.resonance.utils.DateUtil;
import com.ted.resonance.utils.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@Service
public class AdventurerPageServiceImpl implements AdventurerPageService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExCfgActivityRepo exCfgActivityRepo;
    @Autowired
    private ExActivityAdditionRepo exActivityAdditionRepo;
    @Autowired
    private ExTedBurnRepo exTedBurnRepo;
    @Autowired
    private DataDictCache dataDictCache;
    @Autowired
    private TokenBalanceBeanMapper tokenBalanceMapper;
    @Value("${ted-contract-address}")
    private String tedContractAddr;
    @Value("${ex-contract-address}")
    private String exContractAddr;

    public AdventurerPageVO ActivityPage(String address) {
        AdventurerPageVO vo = new AdventurerPageVO();
        //活动信息
        ExCfgActivity exCfgActivity = exCfgActivityRepo.findTopByDelOrderByPeriodAsc(0);
        if (null == exCfgActivity) {
            throw new CommonException(DServiceConstant.NO_ACTIVITY_INFO.getCode(), DServiceConstant.NO_ACTIVITY_INFO.getMsg());
        }
        logger.info("获取当前活动信息:{}", exCfgActivity.toString());
        BigDecimal ted = tokenBalanceMapper.findAllByAddrAndContractAddr(address, tedContractAddr);
        vo.setTedContractAddr(tedContractAddr);
        vo.setActivityId(exCfgActivity.getId());
        vo.setExContractAddr(exContractAddr);
        vo.setTed(ted == null ? BigDecimal.ZERO :ted);
        vo.setEtc(exCfgActivity.getEtc());
        vo.setEth(exCfgActivity.getEth());
        vo.setEndBlock(exCfgActivity.getEndBlock());
        vo.setStatus(exCfgActivity.getStatus());
        String gasLimit = dataDictCache.getDataValue(DataDictConstant.ETHGASLIMIT);
        logger.info("获取当前gaslimit:{}", gasLimit);
        String gasPrice = dataDictCache.getDataValue(DataDictConstant.ETHGASPRICE);
        logger.info("获取当前gasPrice:{}", gasPrice);
        vo.setGasLimit(Integer.valueOf(gasLimit == null ? "0" : gasLimit));
        vo.setGasPrice(Integer.valueOf(gasPrice == null ? "0" : gasPrice));
        if (ActivityStatusEnum.STARTING.getCode() == exCfgActivity.getStatus()) {
            //获取当前ETH最高区块
            String height = dataDictCache.getDataValue(DataDictConstant.ETHHEIGHT);
            logger.info("获取当前最高区块高度:{}", height);
            if (Integer.valueOf(height == null ? "0" : height) < exCfgActivity.getBeginBlock()) {
                throw new CommonException(DServiceConstant.BLOCK_ERROR.getCode(), DServiceConstant.BLOCK_ERROR.getMsg());
            }
            //活动进度
            Integer sumHeight = exCfgActivity.getEndBlock() - exCfgActivity.getBeginBlock();
            Integer nowHeight = Integer.valueOf(height) - exCfgActivity.getBeginBlock();
            if(sumHeight !=0 && nowHeight !=0){
                double rate = new BigDecimal((float) nowHeight / sumHeight).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                vo.setActivitySchedule(rate * 100);
            }
            //加成剩余信息
            ExActivityAddition addsInfo = exActivityAdditionRepo.findActivityAddByActivityIdAndHeight(exCfgActivity.getId(), Integer.valueOf(height));
            if (null != addsInfo) {
                logger.info("获取当前活动加成信息:{}", addsInfo.toString());
                Long surplus = addsInfo.getEndBlock() - Long.valueOf(height);
                vo.setSurplus("~" + surplus.toString() + "区块");
                vo.setAddRate(addsInfo.getAdditionRate());
            }
            //矿工费
            String minersFees = dataDictCache.getDataValue(DataDictConstant.ETHPOUNDAGE);
            logger.info("获取矿工费:{}", minersFees);
            vo.setMinersFees(minersFees);
            vo = getEntity(vo, exCfgActivity,address);
            return vo;
        } else if (ActivityStatusEnum.HAS_REWARD.getCode() == exCfgActivity.getStatus() || ActivityStatusEnum.NOT_REWARD.getCode() == exCfgActivity.getStatus()) {
            vo = getEntity(vo, exCfgActivity,address);
            return vo;
        } else {
            vo.setMsgTxt("活动即将开始,敬请期待!");
            return vo;
        }
    }


    public ExpectContributionPageVO getContribution(Integer ted) {
        BigDecimal expectContribution = BigDecimal.ZERO;
        //活动信息
        ExCfgActivity exCfgActivity = exCfgActivityRepo.findTopByDelOrderByPeriodAsc(0);
        if (null == exCfgActivity) {
            throw new CommonException(DServiceConstant.NO_ACTIVITY_INFO.getCode(), DServiceConstant.NO_ACTIVITY_INFO.getMsg());
        }
        logger.info("获取当前活动信息:{}", exCfgActivity.toString());
        Date update = exCfgActivity.getUpdateTime();
        Date after24 = DateUtil.getAfterHour(update, 24);
        Date after48 = DateUtil.getAfterHour(update, 48);
        Date now = Calendar.getInstance().getTime();
        if (now.getTime() < after24.getTime()) {
            expectContribution = new BigDecimal(ted ).multiply(new BigDecimal("1.1")).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else if (now.getTime() >= after24.getTime() && now.getTime() < after48.getTime()) {
            expectContribution = new BigDecimal(ted ).multiply(new BigDecimal("1.05")).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else if (now.getTime() >= after48.getTime()) {
            expectContribution = new BigDecimal(ted);
        }
        ExpectContributionPageVO vo = new ExpectContributionPageVO();
        vo.setExpectContribution(expectContribution);
        return vo;
    }


    private AdventurerPageVO getEntity(AdventurerPageVO vo,ExCfgActivity exCfgActivity,String address) {
        //当前活动总的贡献值
        Double sumContributionDegree = exTedBurnRepo.findSumContributionDegreeByActivityId(exCfgActivity.getId());
        if (null == sumContributionDegree) {
            sumContributionDegree = 0D;
        }
        //我的贡献值
        Double mySumContributionDegree = exTedBurnRepo.findSumContributionDegreeByActivityIdAndAddr(exCfgActivity.getId(), address);
        if (null == mySumContributionDegree) {
            mySumContributionDegree = 0D;
        }
        vo.setMyContributionDegree(mySumContributionDegree);
        if (mySumContributionDegree != 0D && sumContributionDegree != 0D) {
            //占比
            BigDecimal myMix = new BigDecimal(mySumContributionDegree).divide(new BigDecimal(sumContributionDegree)).setScale(4, BigDecimal.ROUND_HALF_UP);
            //需要分ETC
            if (1 == exCfgActivity.getEtc().compareTo(BigDecimal.ZERO)) {
                BigDecimal amount = exCfgActivity.getEtc().multiply(myMix).setScale(4, BigDecimal.ROUND_HALF_UP);
                vo.setExpectEtc(amount);
            }
            //需要分ETH
            if (1 == exCfgActivity.getEth().compareTo(BigDecimal.ZERO)) {
                BigDecimal amount = exCfgActivity.getEth().multiply(myMix).setScale(4, BigDecimal.ROUND_HALF_UP);
                vo.setExpectEth(amount);
            }
        } else{
            vo.setExpectEtc(BigDecimal.ZERO);
            vo.setExpectEth(BigDecimal.ZERO);
        }
        return vo;
    }
}
