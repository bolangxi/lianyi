package com.ted.resonance.repository;

import com.ted.resonance.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {
    Account findByAddress(String address);
    Account findByNickname(String nickname);
    Account findByAddressNotAndNickname(String address, String nickname);
}
