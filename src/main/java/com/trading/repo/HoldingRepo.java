package com.trading.repo;

import com.trading.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HoldingRepo extends JpaRepository<Holding, Long> {
    Optional<Holding> findByUserIdAndCompanyId(Long userId, Long companyId);
}
