package com.trading.repo;

import com.trading.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HoldingRepo extends JpaRepository<Holding, Long> {
    Optional<Holding> findByUserIdAndCompanyId(Long userId, Long companyId);
}
