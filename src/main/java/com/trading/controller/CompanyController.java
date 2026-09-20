package com.trading.controller;

import com.trading.dto.CompanyDTO;
import com.trading.pojo.CreateCompanyPojo;
import com.trading.service.CompanyService;
import com.trading.service.IpoService;
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
    private final IpoService ipoService;

    @PostMapping
    @PreAuthorize("hasRole('EXCHANGE_ADMIN')")
    public ResponseEntity<CompanyDTO> onboardCompany(@RequestBody CreateCompanyPojo request) {
        CompanyDTO companyDTO = companyService.onBoardCompany(request);
        return ResponseEntity.status(HttpStatus.CREATED).
                body(companyDTO);
    }


}
