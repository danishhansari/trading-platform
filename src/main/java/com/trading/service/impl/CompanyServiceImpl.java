package com.trading.service.impl;

import com.trading.assembler.CompanyAssembler;
import com.trading.dto.CompanyDTO;
import com.trading.entity.Company;
import com.trading.exception.CompanyException;
import com.trading.pojo.CreateCompanyPojo;
import com.trading.repo.CompanyRepo;
import com.trading.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepo companyRepo;
    private final CompanyAssembler companyAssembler;

    @Transactional
    public CompanyDTO onBoardCompany(CreateCompanyPojo request) {
        if (companyRepo.existsBySymbol(request.getSymbol())) throw new CompanyException("Company symbol already exists");

        if (!request.getIpoClosesAt().isAfter(request.getIpoOpensAt())) {
            throw new CompanyException("IPO close time must be after open time");
        }
        Company company = companyAssembler.assemble(request);

        company = companyRepo.save(company);

        return companyAssembler.assembleDetails(company);
    }
}
