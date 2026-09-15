package com.trading.service.impl;

import com.trading.assembler.CompanyAssembler;
import com.trading.enums.UserRole;
import com.trading.dto.CompanyDTO;
import com.trading.entity.Company;
import com.trading.entity.Holding;
import com.trading.entity.User;
import com.trading.exception.CompanyException;
import com.trading.exception.UserException;
import com.trading.pojo.CreateCompanyPojo;
import com.trading.repo.CompanyRepo;
import com.trading.repo.HoldingRepo;
import com.trading.repo.UserRepo;
import com.trading.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepo companyRepo;
    private final UserRepo userRepo;
    private final HoldingRepo holdingRepo;

    @Transactional
    public CompanyDTO onBoardCompany(CreateCompanyPojo request) {
        if (companyRepo.existsBySymbol(request.getSymbol())) throw new CompanyException("Company symbol already exists");

        User owner = userRepo.findById(request.getInitialOwnerId())
                .orElseThrow(() ->
                        new UserException("Initial holding user doesn't exists")
                );

        if (owner.getRole() != UserRole.TRADER) throw new UserException("Initial owner must be a trader");

        Company company = CompanyAssembler.getInstance().assemble(request);

        company = companyRepo.save(company);

        Holding holding = new Holding(owner, company);
        holding.increase(request.getTotalShares());

        holdingRepo.save(holding);

        return CompanyAssembler.getInstance().assembleDetails(company);
    }
}
