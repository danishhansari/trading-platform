package com.exchange.service;

import com.exchange.dto.CompanyDTO;
import com.exchange.pojo.CreateCompanyPojo;

public interface CompanyService {
    public CompanyDTO onBoardCompany(CreateCompanyPojo request);
}
