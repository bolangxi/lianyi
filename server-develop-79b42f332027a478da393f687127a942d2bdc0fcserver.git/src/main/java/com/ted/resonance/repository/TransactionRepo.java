package com.ted.resonance.repository;

import com.ted.resonance.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
    Transaction findByTransactionHash(String transactionHash);
    List<Transaction> findAllByStatusAndType(Integer status, String type);

    @Query(value = "select period_end_time from period where phase=1", nativeQuery = true)
    Date getPeriodChangeTime();

    @Transactional
    @Modifying
    @Query(value = "update period set status=?2 where phase =?1 and id<>0", nativeQuery = true)
    void updateRewardStatus(Integer phase, Integer status);

    @Transactional
    @Modifying
    @Query(value = "update period set period_end_time=?2 where phase =?1 and id<>0", nativeQuery = true)
    void setPeriodEndTime(Integer phase, Date date);
}
