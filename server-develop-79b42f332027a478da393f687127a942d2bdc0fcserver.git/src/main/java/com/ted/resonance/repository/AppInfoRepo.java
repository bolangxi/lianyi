package com.ted.resonance.repository;

import com.ted.resonance.entity.AppInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppInfoRepo extends JpaRepository<AppInfo, Integer> {
    AppInfo findTopBySysOrderByCreatedAtDesc(Integer sys);
}
