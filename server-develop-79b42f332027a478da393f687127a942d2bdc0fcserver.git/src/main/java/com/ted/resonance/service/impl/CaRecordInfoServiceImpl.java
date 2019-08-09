package com.ted.resonance.service.impl;

import com.ted.resonance.config.DServiceConstant;
import com.ted.resonance.entity.Account;
import com.ted.resonance.entity.CaRecordInfoBean;
import com.ted.resonance.mapper.CaRecordInfoBeanMapper;
import com.ted.resonance.repository.AccountRepo;
import com.ted.resonance.service.CaRecordInfoService;
import com.ted.resonance.utils.DateUtil;
import com.ted.resonance.utils.exceptions.CommonException;
import com.ted.resonance.utils.web.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CaRecordInfoServiceImpl implements CaRecordInfoService {
@Autowired
private CaRecordInfoBeanMapper caRecordInfoBeanMapper;

@Autowired
private AccountRepo accountRepo;

    @Override
    public ResponseEntity<CaRecordInfoBean> addCaRecordInfo(String address) {
        //现根据地址查询用户的账户信息
        Account account = accountRepo.findByAddress(address);
        if(account != null){
            //封装加入炼金师记录信息
            CaRecordInfoBean caRecordInfoBean = new CaRecordInfoBean();
            caRecordInfoBean.setAddr(address);
            caRecordInfoBean.setUserId(account.getId());
            caRecordInfoBean.setDayTime(DateUtil.getBeforeDay(new Date(), 0));
            caRecordInfoBeanMapper.insertSelective(caRecordInfoBean);
            return new ResponseEntity<>();
        }else {
            throw new CommonException(DServiceConstant.USER_ERROR.getCode(), DServiceConstant.USER_ERROR.getMsg());
        }

    }
}
