package com.ted.resonance.repository;

import com.ted.resonance.entity.ExActivityAddition;
import com.ted.resonance.entity.ExCfgActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExActivityAdditionRepo extends JpaRepository<ExActivityAddition, Long> {

    @Query(value = "select * from ex_activity_addition where activity_id=?1 and begin_block>=?2 and  end_block>=?2 and is_deleted=0 limit 1", nativeQuery = true)
    ExActivityAddition findActivityAddByActivityIdAndHeight(Long activityId,Integer height);
}
