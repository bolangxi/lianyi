package com.ted.resonance.mapper;

import com.ted.resonance.entity.ExActivityAdditionBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ExActivityAdditionBeanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ex_activity_addition
     *
     * @mbggenerated
     */
    int insert(ExActivityAdditionBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ex_activity_addition
     *
     * @mbggenerated
     */
    int insertSelective(ExActivityAdditionBean record);


    List<ExActivityAdditionBean> selectAdditionByActivityId(@Param("activityId") Long activityId);
}