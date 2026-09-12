package com.trading.repo;

import com.trading.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepo extends JpaRepository<Company, Long> {

    boolean existsBySymbol(String symbol);

    boolean existsByName(String name);

    Optional<Company> findBySymbol(String symbol);
}
