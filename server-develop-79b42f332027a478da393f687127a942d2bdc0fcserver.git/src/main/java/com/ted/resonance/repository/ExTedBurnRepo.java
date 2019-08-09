package com.ted.resonance.repository;

import com.ted.resonance.entity.ExCfgActivity;
import com.ted.resonance.entity.ExTedBurn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExTedBurnRepo extends JpaRepository<ExTedBurn, Long> {

    @Query(value = "select sum(contribution_degree) from ex_ted_burn where activity_id=?1 and is_deleted=0", nativeQuery = true)
    Double findSumContributionDegreeByActivityId(Long activityId);

    @Query(value = "select sum(contribution_degree) from ex_ted_burn where activity_id=?1 and addr=?2 and is_deleted=0", nativeQuery = true)
    Double findSumContributionDegreeByActivityIdAndAddr(Long activityId,String addr);

    List<ExTedBurn> findByActivityIdAndIsDeleted(Long activityId, String isDelete);

    List<ExTedBurn> findByActivityIdAndCalStatusAndIsDeleted(Long activityId, Integer calStatus, String isDelete);
}
