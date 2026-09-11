package com.exchange.controller;

import com.exchange.dto.CompanyDTO;
import com.exchange.pojo.CreateCompanyPojo;
import com.exchange.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @PreAuthorize("hasRole('EXCHANGE_ADMIN')")
    public ResponseEntity<CompanyDTO> onboardCompany(@RequestBody CreateCompanyPojo request) {
        CompanyDTO companyDTO = companyService.onBoardCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(companyDTO);
    }
}
