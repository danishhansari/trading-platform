package com.exchange.service.impl;

import com.exchange.assembler.CompanyAssembler;
import com.exchange.constants.UserRole;
import com.exchange.dto.CompanyDTO;
import com.exchange.entity.Company;
import com.exchange.entity.Holding;
import com.exchange.entity.User;
import com.exchange.exception.CompanyAlreadyExists;
import com.exchange.exception.InitialOwnerTraderException;
import com.exchange.exception.UserNotFoundException;
import com.exchange.pojo.CreateCompanyPojo;
import com.exchange.repo.CompanyRepo;
import com.exchange.repo.HoldingRepo;
import com.exchange.repo.UserRepo;
import com.exchange.service.CompanyService;
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
        if (companyRepo.existsBySymbol(request.getSymbol())) throw new CompanyAlreadyExists("Company symbol already exists");

        User owner = userRepo.findById(request.getInitialOwnerId())
                .orElseThrow(() ->
                        new UserNotFoundException("Initial holding user doesn't exists")
                );

        if (owner.getRole() != UserRole.TRADER) throw new InitialOwnerTraderException("Initial owner must be a trader");

        Company company = CompanyAssembler.getInstance().assemble(request);

        company = companyRepo.save(company);

        Holding holding = new Holding();

        holding.setUser(owner);
        holding.setCompany(company);
        holding.setQuantity(request.getTotalShares());

        holdingRepo.save(holding);

        return CompanyAssembler.getInstance().assembleDetails(company);
    }
}
