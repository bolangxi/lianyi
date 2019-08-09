package com.ted.update.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

@Mapper
public interface CaDrawChanceDao {
    String TABLE_NAME = "ca_draw_chance";
    @Update("update " + TABLE_NAME + " set chance_times = 0 where expires_time < current_timestamp")
    Integer updateChanceTimes();
}
