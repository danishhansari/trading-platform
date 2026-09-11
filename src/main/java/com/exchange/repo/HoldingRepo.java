package com.exchange.repo;

import com.exchange.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoldingRepo extends JpaRepository<Holding, Long> {
}
