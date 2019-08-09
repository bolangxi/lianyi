package com.ted.resonance.repository;

import com.ted.resonance.entity.UserAcc;
import com.ted.resonance.entity.UserAccDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccDetailRepo extends JpaRepository<UserAccDetail, Long> {

}
