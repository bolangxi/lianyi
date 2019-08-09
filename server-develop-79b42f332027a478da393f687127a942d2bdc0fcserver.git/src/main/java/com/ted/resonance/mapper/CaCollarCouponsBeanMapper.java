package com.ted.resonance.mapper;

import com.ted.resonance.entity.CaCollarCouponsBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Mapper
public interface CaCollarCouponsBeanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ca_collar_coupons
     *
     * @mbggenerated
     */
    int insert(CaCollarCouponsBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ca_collar_coupons
     *
     * @mbggenerated
     */
    int insertSelective(CaCollarCouponsBean record);


    int updateRewardStatus(@Param("id") Long id, @Param("rewardStatus") String status,  @Param("rewardTime") Date rewardTime,@Param("expiresTime") Date expiresTime);

    CaCollarCouponsBean findCaCollarCouponsByUserId(@Param("addr") String addr);

    List<CaCollarCouponsBean> couponsList(@Param("addr") String addr);

    CaCollarCouponsBean findCouponsById(@Param("id") Long id);

    /**
     * 定时跑批处理过期数据
     * @return
     */
    int updateRewardStatusJob();

    /**
     * 批量保存or插入
     * @param couponsList
     */
    void batchSaveOrUpdate(List<CaCollarCouponsBean> couponsList);


    List<CaCollarCouponsBean> selectCouponsByRewardStatus(@Param("rewardStatus") String rewardStatus);

    CaCollarCouponsBean selectById(@Param("id") Long id);

    int updateDelStatusById(@Param("id") Long id);

    int updateRewardStatusById(@Param("id")Long id, @Param("rewardStatus") String rewardStatus);
}