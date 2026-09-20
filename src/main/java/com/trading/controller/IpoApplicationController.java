package com.trading.controller;

import com.trading.dto.IpoApplicationDTO;
import com.trading.pojo.IpoApplicationPojo;
import com.trading.service.IpoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies/{companyId}/ipo")
@RequiredArgsConstructor
public class IpoApplicationController {

    private final IpoService ipoService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<IpoApplicationDTO> apply(
            @PathVariable Long companyId,
            @RequestBody IpoApplicationPojo pojo,
            HttpServletRequest request) {

        Long traderId = (Long) request.getAttribute("x-user-id");
        IpoApplicationDTO dto = ipoService.applyForShares(traderId, companyId, pojo.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping("/my-application")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<IpoApplicationDTO> myApplication(
            @PathVariable Long companyId,
            HttpServletRequest request) {

        Long traderId = (Long) request.getAttribute("x-user-id");
        return ResponseEntity.ok(ipoService.getApplication(traderId, companyId));
    }
}
