package com.trading.scheduler;

import com.trading.entity.Company;
import com.trading.enums.CompanyStatus;
import com.trading.repo.CompanyRepo;
import com.trading.service.IpoService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class IpoClosingSchedular {

    private final CompanyRepo companyRepo;
    private final IpoService ipoService;


    @Scheduled(cron = "${schedular.ipo.cron}")
    public void closeExpiredIpos() {

        List<Company> expired = companyRepo
                .findByStatusAndIpoClosesAtBefore(CompanyStatus.IPO_OPEN, LocalDateTime.now());

        for (Company company : expired) {
                ipoService.closeIpoAndAllocate(company.getId());
        }

    }
}