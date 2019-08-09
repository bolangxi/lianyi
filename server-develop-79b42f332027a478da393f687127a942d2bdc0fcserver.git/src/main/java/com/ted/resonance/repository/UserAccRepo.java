package com.ted.resonance.repository;

import com.ted.resonance.entity.Transaction;
import com.ted.resonance.entity.UserAcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface UserAccRepo extends JpaRepository<UserAcc, Long> {

    UserAcc findByAddrAndCoinTypeAndIsDeleted(String addr, String coinType,Integer isDeleted);

    @Transactional
    @Modifying
    @Query(value = "update user_acc set update_time=?2 ,balance= balance + ?3 where addr=?1 and coin_type=?4", nativeQuery = true)
    void update(String addr, Date date, BigDecimal balance,String coinType);

}
