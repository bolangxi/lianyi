package com.ted.resonance.repository;

import com.ted.resonance.entity.AddressInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressInfoRepo extends JpaRepository<AddressInfo, Integer> {
    AddressInfo findByAddressAndPhase(String address, Integer phase);
    List<AddressInfo> findByTeamIdAndPhase(Integer teamId, Integer phase);
    List<AddressInfo> findByInviterAddressAndPhase(String inviterAddress, Integer phase);
}
