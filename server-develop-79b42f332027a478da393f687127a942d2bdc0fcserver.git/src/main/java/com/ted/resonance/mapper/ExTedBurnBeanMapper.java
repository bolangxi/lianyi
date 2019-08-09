package com.ted.resonance.mapper;

import com.ted.resonance.entity.ExTedBurnBean;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface ExTedBurnBeanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ex_ted_burn
     *
     * @mbggenerated
     */
    int insert(ExTedBurnBean record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ex_ted_burn
     *
     * @mbggenerated
     */
    int insertSelective(ExTedBurnBean record);

    ExTedBurnBean selectById(@Param("id") Long id);

    List<ExTedBurnBean> queryListByStatus(@Param("calStatus") Integer calStatus);


    int updateStatus(@Param("id")Long id, @Param("status") Integer status);

    int  updateBlockAndStatus(@Param("id") Long id, @Param("blockHash") String blockHash,
                              @Param("blockHeight") Integer blockHeight,  @Param("status") Integer status);

    int updateContributionAndCalStatus(@Param("id") Long id, @Param("contribution") BigDecimal contribution, @Param("calStatus") Integer calStatus);

    void updateIsDelete(@Param("id")Long id);
}