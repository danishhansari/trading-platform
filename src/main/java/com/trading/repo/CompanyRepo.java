package com.trading.repo;

import com.trading.entity.Company;
import com.trading.enums.CompanyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompanyRepo extends JpaRepository<Company, Long> {
    boolean existsBySymbol(String symbol);
    List<Company> findByStatusAndIpoClosesAtBefore(CompanyStatus companyStatus, LocalDateTime time);
}
