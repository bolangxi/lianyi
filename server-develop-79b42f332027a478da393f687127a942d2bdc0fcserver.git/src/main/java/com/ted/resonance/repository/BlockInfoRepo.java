package com.ted.resonance.repository;

import com.ted.resonance.entity.BlockInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockInfoRepo extends JpaRepository<BlockInfo, Integer> {

    BlockInfo findTopByTypeOrderByHeightDesc(String type);

}
