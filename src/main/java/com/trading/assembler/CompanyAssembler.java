package com.trading.assembler;

import com.trading.dto.CompanyDTO;
import com.trading.entity.Company;
import com.trading.enums.CompanyStatus;
import com.trading.pojo.CreateCompanyPojo;

public class CompanyAssembler {

    private static CompanyAssembler instance;
    private CompanyAssembler() {}

    public static CompanyAssembler getInstance() {
        if(instance == null) {
            synchronized (CompanyAssembler.class) {
                if(instance == null) {
                    instance = new CompanyAssembler();
                }
            }
        }
        return instance;
    }
    public Company assemble(CreateCompanyPojo request) {

        Company company = new Company();

        company.setName(request.getName());
        company.setSymbol(request.getSymbol());
        company.setTotalShares(request.getTotalShares());
        company.setReferencePrice(request.getReferencePrice());
        company.setStatus(CompanyStatus.ACTIVE);

        return company;
    }

    public CompanyDTO assembleDetails(Company company) {

        return new CompanyDTO(
                company.getId(),
                company.getName(),
                company.getSymbol(),
                company.getTotalShares(),
                company.getReferencePrice(),
                company.getStatus().name()
        );
    }
}
