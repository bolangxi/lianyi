package com.ted.resonance.repository;

import com.ted.resonance.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TeamRepo extends JpaRepository<Team, Integer> {
    Team findByLeaderAddressAndPhase(String leaderAddress, Integer phase);
    List<Team> findAllByPhaseOrderByEtcFundDesc(Integer phase);
    List<Team> findAllByPhaseOrderByEthFundDesc(Integer phase);

    @Query(value = "select status from period where phase=?1", nativeQuery = true)
    Integer getStatus(Integer pahse);

    @Query(value = "select sum(eth_fund) from team where phase=?1", nativeQuery = true)
    BigDecimal sumEthByPhase(Integer phase);

    @Query(value = "select sum(etc_fund) from team where phase=?1", nativeQuery = true)
    BigDecimal sumEtcByPhase(Integer phase);

    @Query(value = "select count(id) from address_info where team_id = ?1", nativeQuery = true)
    Integer getTeamNumber(Integer teamId);
}
