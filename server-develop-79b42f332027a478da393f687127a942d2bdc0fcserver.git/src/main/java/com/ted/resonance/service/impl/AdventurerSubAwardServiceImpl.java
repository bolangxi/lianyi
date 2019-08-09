package com.ted.resonance.service.impl;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.config.DataDictCache;
import com.ted.resonance.config.DataDictConstant;
import com.ted.resonance.entity.*;
import com.ted.resonance.entity.response.AdventurerPageVO;
import com.ted.resonance.entity.response.ExpectContributionPageVO;
import com.ted.resonance.enums.ActivityStatusEnum;
import com.ted.resonance.enums.OperateType;
import com.ted.resonance.enums.TradeType;
import com.ted.resonance.repository.*;
import com.ted.resonance.service.AdventurerPageService;
import com.ted.resonance.service.AdventurerSubAwardService;
import com.ted.resonance.utils.DateUtil;
import com.ted.resonance.utils.ParamValidUtil;
import com.ted.resonance.utils.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

@Service
public class AdventurerSubAwardServiceImpl implements AdventurerSubAwardService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserAccRepo userAccRepo;

    @Autowired
    private UserAccDetailRepo userAccDetailRepo;

    @Autowired
    private ExCfgActivityRepo exCfgActivityRepo;

    @Autowired
    private ExTedBurnRepo exTedBurnRepo;

    @Transactional
    public void subAward(Long activityId){
        List<ExTedBurn> unCalExTedBurns = exTedBurnRepo.findByActivityIdAndCalStatusAndIsDeleted(activityId,0,"0");
        if(!unCalExTedBurns.isEmpty()){
            throw new CommonException(DServiceConstant.SUBAWARD_UNCAL_ERROR.getCode(), DServiceConstant.SUBAWARD_UNCAL_ERROR.getMsg());
        }
        ExCfgActivity exCfgActivity = exCfgActivityRepo.findByIdAndDel(activityId,0);
        if(null == exCfgActivity){
            throw new CommonException(DServiceConstant.NO_ACTIVITY_INFO.getCode(), DServiceConstant.NO_ACTIVITY_INFO.getMsg());
        }
        if(ActivityStatusEnum.NOT_REWARD.getCode() != exCfgActivity.getStatus()){
            throw new CommonException(DServiceConstant.SUBAWARD_ERROR.getCode(), DServiceConstant.SUBAWARD_ERROR.getMsg());
        }
        logger.info("分奖活动信息:{}", exCfgActivity.toString());
//        //查询条件对象
//        ExTedBurn xTedBurn = new ExTedBurn();
//        //设置需要查询的条件
//        xTedBurn.setActivityId(activityId);
//        xTedBurn.setIsDeleted("0");
//        Example<ExTedBurn> example = Example.of(xTedBurn);
        List<ExTedBurn> exTedBurns = exTedBurnRepo.findByActivityIdAndIsDeleted(activityId, "0");
        Double sumContributionDegree = exTedBurnRepo.findSumContributionDegreeByActivityId(activityId);
        Map<String, List<ExTedBurn>> exTedBurnCollect = exTedBurns.stream().collect(groupingBy(ExTedBurn::getAddr));
        for(String key : exTedBurnCollect.keySet()){
            Long userId = exTedBurnCollect.get(key).get(0).getUserId();
            Double userSumContributionDegree = exTedBurnRepo.findSumContributionDegreeByActivityIdAndAddr(activityId, key);
            //占比
            BigDecimal myMix = new BigDecimal(userSumContributionDegree/ sumContributionDegree);
            //需要分ETC
            if(1 == exCfgActivity.getEtc().compareTo(BigDecimal.ZERO)){
                BigDecimal amount = exCfgActivity.getEtc().multiply(myMix).setScale(4,BigDecimal.ROUND_DOWN);
                insertAccInfo("ETC", userId,amount,key);
            }
            //需要分ETH
            if(1 == exCfgActivity.getEth().compareTo(BigDecimal.ZERO)){
                BigDecimal amount = exCfgActivity.getEth().multiply(myMix).setScale(4,BigDecimal.ROUND_DOWN);
                insertAccInfo("ETH", userId,amount,key);
            }
        }
        exCfgActivityRepo.updateByActivityIdAndStatus(activityId,ActivityStatusEnum.HAS_REWARD.getCode(),new Date());
        logger.info("分奖结束:{}", exCfgActivity.toString());
    }

    private void insertAccInfo(String coinType, Long userId,BigDecimal amount,String addr){
        BigDecimal afterAmount = BigDecimal.ZERO;
        BigDecimal beforeAmount = BigDecimal.ZERO;
        Long accId = 0l;
        Date now = new Date();
        //查询条件对象
        UserAcc userAccBefore = userAccRepo.findByAddrAndCoinTypeAndIsDeleted(addr,coinType,0);
        if (null == userAccBefore){
            UserAcc userAccNew = new UserAcc();
            userAccNew.setIsDeleted(0);
            userAccNew.setCoinType(coinType);
            userAccNew.setUserId(userId);
            userAccNew.setCreateTime(now);
            userAccNew.setBalance(amount);
            userAccNew.setAddr(addr);
            userAccNew.setUpdateTime(now);
            userAccNew = userAccRepo.save(userAccNew);
            accId = userAccNew.getId();
            afterAmount = amount;
        } else {
            afterAmount = userAccBefore.getBalance().add(amount);
            beforeAmount = userAccBefore.getBalance();
            accId = userAccBefore.getId();
            userAccRepo.update(addr,now,amount,coinType);
        }
        UserAccDetail userAccDetail =  new UserAccDetail();
        userAccDetail.setUserId(userId);
        userAccDetail.setCreateTime(now);
        userAccDetail.setCoinType(coinType);
        userAccDetail.setAmount(amount);
        userAccDetail.setPreAmount(beforeAmount);
        userAccDetail.setAfterAmount(afterAmount);
        userAccDetail.setAccId(accId);
        userAccDetail.setOperateType(OperateType.ADD.name());
        userAccDetail.setIsDeleted(0);
        userAccDetail.setRemark("冒险家分奖");
        userAccDetail.setTradeType(TradeType.EX_REWARD.name());
        userAccDetail.setAddr(addr);
        userAccDetailRepo.save(userAccDetail);
    }
}
