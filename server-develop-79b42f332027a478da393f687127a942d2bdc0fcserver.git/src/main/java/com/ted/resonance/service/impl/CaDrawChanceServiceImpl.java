package com.ted.resonance.service.impl;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.config.DataDictCache;
import com.ted.resonance.config.DataDictConstant;
import com.ted.resonance.entity.CaCollarCouponsBean;
import com.ted.resonance.entity.CaDrawChanceBean;
import com.ted.resonance.entity.TokenBalance;
import com.ted.resonance.mapper.CaCollarCouponsBeanMapper;
import com.ted.resonance.mapper.CaDrawChanceBeanMapper;
import com.ted.resonance.mapper.TokenBalanceBeanMapper;
import com.ted.resonance.service.CaCollarCouponsService;
import com.ted.resonance.service.CaDrawChanceService;
import com.ted.resonance.utils.exceptions.CommonException;
import com.ted.resonance.utils.web.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
public class CaDrawChanceServiceImpl implements CaDrawChanceService {

    @Autowired
    private CaDrawChanceBeanMapper caDrawChanceBeanMapper;

    @Autowired
    private TokenBalanceBeanMapper tokenBalanceRepo;

    @Autowired
    private CaCollarCouponsBeanMapper caCollarCouponsRepo;

    @Autowired
    private CaCollarCouponsService caCollarCouponsService;

    @Override
    public CaDrawChanceBean findCaDrawChanceByAddr(String addr) {
        if(StringUtils.isEmpty(addr)){
            throw new CommonException(DServiceConstant.PARAMS_ERROR.getCode(), DServiceConstant.PARAMS_ERROR.getMsg());
        }

        CaDrawChanceBean caDrawChanceBean = caDrawChanceBeanMapper.findCaCollarCouponsByUserId(addr);
        if(caDrawChanceBean == null){
            caDrawChanceBean = new CaDrawChanceBean();
        }
        //TODO 查询当前用户的 账户TED余额   根据余额确认第二天可获得的抽奖次数
        BigDecimal tokenBalance =  tokenBalanceRepo.findAllByAddr(addr);
        //用户余额
        caDrawChanceBean.setBalance(tokenBalance==null?new BigDecimal(0):tokenBalance);
        caDrawChanceBean.setRewardTimes(0);
        if(tokenBalance != null && tokenBalance.compareTo(BigDecimal.ZERO)> 0) {
            BigDecimal balance = new BigDecimal(0);
            //获取用户的余额信息存在
            if (!StringUtils.isEmpty(DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD4))) {
                balance = new BigDecimal(DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD4));
                int num = tokenBalance.subtract(balance).compareTo(BigDecimal.ZERO);
                if (num >= 0) {
                    //符合第三阶段
                    caDrawChanceBean.setRewardTimes(4);
                    return caDrawChanceBean;
                }
            }
            if (!StringUtils.isEmpty(DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD3))) {
                balance = new BigDecimal(DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD3));
                int num = tokenBalance.subtract(balance).compareTo(BigDecimal.ZERO);
                if (num >= 0) {
                    //符合第三阶段
                    caDrawChanceBean.setRewardTimes(3);
                    return caDrawChanceBean;
                }
            }
            if (!StringUtils.isEmpty(DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD2))) {
                balance = new BigDecimal(DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD2));
                int num = tokenBalance.subtract(balance).compareTo(BigDecimal.ZERO);
                if (num >= 0) {
                    //符合第三阶段
                    caDrawChanceBean.setRewardTimes(2);
                    return caDrawChanceBean;
                }
            }
            if (!StringUtils.isEmpty(DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD1))) {
                balance = new BigDecimal(DataDictCache.getDataValue(DataDictConstant.TEDTHRESHOLD1));
                int num = tokenBalance.subtract(balance).compareTo(BigDecimal.ZERO);
                if (num >= 0) {
                    //符合第三阶段
                    caDrawChanceBean.setRewardTimes(1);
                    return caDrawChanceBean;
                }
            } else {
                //符合第三阶段
                caDrawChanceBean.setRewardTimes(0);
                return caDrawChanceBean;
            }
        }
        return caDrawChanceBean;
    }

    @Transactional
    @Override
    public ResponseEntity<CaCollarCouponsBean> updateByCouponId(Long id, String addr) {
        ResponseEntity<CaCollarCouponsBean> responseEntity = new ResponseEntity<>();
        CaCollarCouponsBean caCollarCouponsBean = null;
        //开奖时，根据主键ID 更新抽奖信息表中的用的抽奖次数
        int num  = caDrawChanceBeanMapper.updateRewardStatus(id); //
        if(num >0){
            //数据更新成功
            //随机获取一张领金券显示在前端页面  并将该领金券状态改为 已领取
            caCollarCouponsBean = caCollarCouponsRepo.findCaCollarCouponsByUserId(addr);
            if(caCollarCouponsBean == null ){
                throw new CommonException(DServiceConstant.REWARD_FAIL.getCode(), DServiceConstant.REWARD_FAIL.getMsg());
            }
            //从数据字典中获取用户的矿工费用
            BigDecimal rate = new BigDecimal(DataDictCache.getDataValue(DataDictConstant.ETHPOUNDAGE));
            caCollarCouponsBean.setEthRate(rate);
            int result = caCollarCouponsService.updateCollarCoupons(caCollarCouponsBean);
            if(result>0){
                responseEntity.setContent(caCollarCouponsBean);
                return  responseEntity;
            }else{
                throw new CommonException(DServiceConstant.REWARD_FAIL.getCode(), DServiceConstant.REWARD_FAIL.getMsg());
            }

        }else{
            throw new CommonException(DServiceConstant.REWARD_FAIL.getCode(), DServiceConstant.REWARD_FAIL.getMsg());
        }
    }
}
