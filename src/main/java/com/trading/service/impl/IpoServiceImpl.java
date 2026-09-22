package com.trading.service.impl;

import com.trading.assembler.IpoAssembler;
import com.trading.dto.IpoApplicationDTO;
import com.trading.entity.*;
import com.trading.enums.CompanyStatus;
import com.trading.enums.IpoApplicationStatus;
import com.trading.exception.CompanyException;
import com.trading.exception.IpoException;
import com.trading.exception.UserException;
import com.trading.exception.WalletException;
import com.trading.repo.*;
import com.trading.service.IpoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IpoServiceImpl implements IpoService {

    private final UserRepo userRepo;
    private final IpoApplicationRepo ipoApplicationRepo;
    private final HoldingRepo holdingRepo;
    private final IpoAssembler ipoAssembler;
    private final CompanyRepo companyRepo;
    private final WalletRepo walletRepo;


    @Override
    @Transactional
    public IpoApplicationDTO applyForShares(Long traderId, Long companyId, Long quantity) {

        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        User trader = userRepo.findById(traderId)
                .orElseThrow(() -> new UserException("Trader not found"));

        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new CompanyException("Company not found"));

        if (company.getStatus() != CompanyStatus.IPO_OPEN) {
            throw new IpoException("IPO is not currently open for this company");
        }

        IpoApplication application = new IpoApplication(trader, company, quantity);
        application = ipoApplicationRepo.save(application);

        return ipoAssembler.assembleDetails(application);
    }

    @Override
    @Transactional
    public void closeIpoAndAllocate(Long companyId) {

        Company company = companyRepo.findById(companyId)
                .orElseThrow(() -> new CompanyException("Company not found"));

        if (company.getStatus() != CompanyStatus.IPO_OPEN) {
            throw new IpoException("IPO is not currently open for this company");
        }

        List<IpoApplication> applications = ipoApplicationRepo
                .findByCompanyIdAndStatusOrderByAppliedAtAsc(companyId, IpoApplicationStatus.PENDING);

        long remaining = company.getTotalShares();
        BigDecimal issuePrice = company.getReferencePrice();

        for (IpoApplication app : applications) {

            long totalAllotted = company.getTotalShares() - remaining;
            company.setSharesAllotted(totalAllotted);

            if (remaining <= 0) {
                app.allocate(0L, IpoApplicationStatus.REJECTED);
                continue;
            }

            long requestedGrant = Math.min(app.getRequestedQuantity(), remaining);

            Wallet wallet = walletRepo.findByUserId(app.getTrader().getId())
                    .orElseThrow(() -> new WalletException("Wallet not found for applicant"));

            BigDecimal cost = issuePrice.multiply(BigDecimal.valueOf(requestedGrant));

            long actualGrant;
            if (wallet.getBalance().compareTo(cost) >= 0) {
                actualGrant = requestedGrant;
            } else {
                actualGrant = wallet.getBalance()
                        .divideToIntegralValue(issuePrice)
                        .longValue();
                actualGrant = Math.min(actualGrant, requestedGrant);
            }

            if (actualGrant <= 0) {
                app.allocate(0L, IpoApplicationStatus.REJECTED);
                continue;
            }

            BigDecimal actualCost = issuePrice.multiply(BigDecimal.valueOf(actualGrant));
            wallet.debit(actualCost);
            walletRepo.save(wallet);

            IpoApplicationStatus status = (actualGrant == app.getRequestedQuantity())
                    ? IpoApplicationStatus.ALLOCATED
                    : IpoApplicationStatus.PARTIALLY_ALLOCATED;

            app.allocate(actualGrant, status);
            remaining -= actualGrant;

            Holding holding = holdingRepo.findByUserIdAndCompanyId(app.getTrader().getId(), companyId)
                    .orElseGet(() -> holdingRepo.save(new Holding(app.getTrader(), company)));
            holding.increase(actualGrant);
            holdingRepo.save(holding);
        }

        long totalAllotted = company.getTotalShares() - remaining;
        company.setSharesAllotted(totalAllotted);

        ipoApplicationRepo.saveAll(applications);
        company.setStatus(CompanyStatus.ACTIVE);
        companyRepo.save(company);

    }

    @Override
    @Transactional(readOnly = true)
    public IpoApplicationDTO getApplication(Long traderId, Long companyId) {

        IpoApplication application = ipoApplicationRepo
                .findByTraderIdAndCompanyId(traderId, companyId)
                .orElseThrow(() -> new IpoException(
                        "No IPO application found for this trader and company"
                ));

        return ipoAssembler.assembleDetails(application);
    }
}