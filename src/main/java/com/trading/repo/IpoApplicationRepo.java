package com.trading.repo;

import com.trading.entity.IpoApplication;
import com.trading.enums.IpoApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IpoApplicationRepo extends JpaRepository<IpoApplication, Long> {
    List<IpoApplication> findByCompanyIdAndStatusOrderByAppliedAtAsc(Long companyId, IpoApplicationStatus status);
    Optional<IpoApplication> findByTraderIdAndCompanyId(Long traderId, Long companyId);
}