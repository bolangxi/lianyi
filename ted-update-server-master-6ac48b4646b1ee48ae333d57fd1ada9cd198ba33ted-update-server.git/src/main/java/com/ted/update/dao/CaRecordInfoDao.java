package com.ted.update.dao;

import com.ted.update.entity.CaRecordInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface CaRecordInfoDao {
    String TABLE_NAME = "ca_record_info";
    String TABLE_COLUMN = "id, user_id userId, addr , day_time dayTime, income_time incomeTime, update_time updateTime, del, remark";

    @Select("select " + TABLE_COLUMN + " from " + TABLE_NAME + " where del = 0 and day_time < #{dayTime}")
    List<CaRecordInfo> selectCaRecordInfo(Date dayTime);

    @Update("update " + TABLE_NAME + " set day_time=#{dayTime} where id=#{id}")
    Integer updateDayTime(@Param("dayTime") Date dayTime, @Param("id") Long id);
}
