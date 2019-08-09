package com.ted.resonance.repository;

import com.ted.resonance.entity.ExCfgActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Repository
public interface ExCfgActivityRepo extends JpaRepository<ExCfgActivity, Long> {

    ExCfgActivity findTopByDelOrderByPeriodAsc(Integer del);

    ExCfgActivity findByIdAndDel(Long id, Integer del);

    @Transactional
    @Modifying
    @Query(value = "update ex_cfg_activity set update_time=?3 ,status=?2 where id=?1", nativeQuery = true)
    int updateByActivityIdAndStatus(Long id, Integer status,Date update);
}
