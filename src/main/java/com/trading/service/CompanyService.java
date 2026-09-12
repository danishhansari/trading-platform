package com.trading.service;

import com.trading.dto.CompanyDTO;
import com.trading.pojo.CreateCompanyPojo;

public interface CompanyService {
    public CompanyDTO onBoardCompany(CreateCompanyPojo request);
}
