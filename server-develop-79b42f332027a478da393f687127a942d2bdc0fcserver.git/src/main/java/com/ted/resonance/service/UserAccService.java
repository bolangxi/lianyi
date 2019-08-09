package com.ted.resonance.service;

import com.ted.resonance.dto.OperateUserAccDto;
import com.ted.resonance.entity.UserAccDetailBean;

import java.util.List;

/**
 * @Auther: zzm
 * @Date: 2019-07-17 16:57
 * @Description:
 */
public interface UserAccService {

    /**
     * 根据币种操作用户账户
     * //     * @param userId 用户ID
     * //     * @param coinType 币种
     * //     * @param tradeAmount 交易金额
     * //     * @param tradeType  交易类型枚举
     * //     * @param operateType 操作类型枚举
     * //     * @param remark 操作备注
     *
     * @return
     */
    Long operateUserAccByCoin(OperateUserAccDto operateUserAccDto);

    /**
     * 根据地址和币种类型查询详细的信息
     * @param addr
     * @param coinType
     * @return
     */
    List<UserAccDetailBean> selectDetailByAddrAndCoinType(String addr, String coinType);


}
