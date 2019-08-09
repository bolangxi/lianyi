package com.ted.resonance.service.impl;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.dto.OperateUserAccDto;
import com.ted.resonance.entity.UserAccBean;
import com.ted.resonance.entity.UserAccDetailBean;
import com.ted.resonance.enums.OperateType;
import com.ted.resonance.mapper.UserAccBeanMapper;
import com.ted.resonance.mapper.UserAccDetailBeanMapper;
import com.ted.resonance.service.UserAccService;
import com.ted.resonance.utils.exceptions.CommonException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Auther: zzm
 * @Date: 2019-07-17 16:58
 * @Description:
 */
@Service
public class UserAccServiceImpl implements UserAccService {

    @Autowired
    private UserAccBeanMapper userAccMapper;

    @Autowired
    private UserAccDetailBeanMapper accDetailBeanMapper;

    @Override
    public Long operateUserAccByCoin(OperateUserAccDto operateUserAccDto) {
        Boolean check = checkParam(operateUserAccDto);
        if(check){
            String  addr = operateUserAccDto.getAddr();
            String coinType = operateUserAccDto.getCoinType();
            UserAccBean userAcc = userAccMapper.selectByAddrAndCoinType(addr, coinType);
            if (userAcc == null) {
                userAcc = saveUserAccInfo(operateUserAccDto);
            }
            //账户余额
            BigDecimal balance = userAcc.getBalance() == null ? BigDecimal.ZERO : userAcc.getBalance();
            //交易金额
            BigDecimal tradeAmount = operateUserAccDto.getTradeAmount();
            //交易后金额
            BigDecimal aferAmount = BigDecimal.ZERO;

            if (operateUserAccDto.getOperateType() == OperateType.ADD) {
                aferAmount = balance.add(tradeAmount);
            }
            UserAccDetailBean accDetail = saveUserAccDetail(operateUserAccDto, userAcc, aferAmount);

            if (operateUserAccDto.getOperateType() == OperateType.ADD) {

                userAccMapper.updateAddBalanceById(tradeAmount, userAcc.getId());
            }
            return accDetail.getId();
        }
        return null;

    }

    @Override
    public List<UserAccDetailBean> selectDetailByAddrAndCoinType(String addr, String coinType) {
        return accDetailBeanMapper.selectDetailByAddrAndCoinType(addr,coinType);
    }


    public boolean checkParam(OperateUserAccDto operateUserAccDto) {
        if(!StringUtils.isEmpty(operateUserAccDto.getCoinType()) && operateUserAccDto.getOperateType()!=null
                && operateUserAccDto.getTradeType()!=null && operateUserAccDto.getTradeAmount()!=null && !StringUtils.isEmpty(operateUserAccDto.getAddr())){
            return true;
        }
        return false;
    }



    //创建币种账户
    private UserAccBean saveUserAccInfo(OperateUserAccDto operateUserAccDto) {
        UserAccBean userAcc = new UserAccBean();
        Long userId = operateUserAccDto.getUserId();
        userAcc.setUserId(userId);
        userAcc.setAddr(operateUserAccDto.getAddr());
        userAcc.setCoinType(operateUserAccDto.getCoinType());
        userAcc.setCreateTime(new Date());
        userAcc.setUpdateTime(new Date());
        userAcc.setBalance(BigDecimal.ZERO);
        userAcc.setFrozenAmount(BigDecimal.ZERO);
        userAcc.setIsDeleted(false);
        userAccMapper.insert(userAcc);
        return userAcc;

    }


    //创建账户交易明细
    private UserAccDetailBean saveUserAccDetail(OperateUserAccDto operateUserAccDto, UserAccBean userAcc, BigDecimal
            aferAmount) {
        UserAccDetailBean accDetail = new UserAccDetailBean();
        accDetail.setUserId(operateUserAccDto.getUserId());
        accDetail.setAccId(userAcc.getId());
        accDetail.setAddr(operateUserAccDto.getAddr());
        accDetail.setCoinType(operateUserAccDto.getCoinType());
        accDetail.setAmount(operateUserAccDto.getTradeAmount());
        accDetail.setPreAmount(userAcc.getBalance());
        accDetail.setAfterAmount(aferAmount);
        accDetail.setTradeType(operateUserAccDto.getTradeType().name());
        accDetail.setOperateType(operateUserAccDto.getOperateType().name());
        accDetail.setCreateTime(new Date());
        accDetail.setUpdateTime(new Date());
        accDetail.setRemark(operateUserAccDto.getRemark());
        accDetail.setIsDeleted(false);
        accDetailBeanMapper.insert(accDetail);
        return accDetail;

    }

}
