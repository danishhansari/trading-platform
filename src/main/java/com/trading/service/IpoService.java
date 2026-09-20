package com.trading.service;

import com.trading.dto.IpoApplicationDTO;

public interface IpoService {
    IpoApplicationDTO applyForShares(Long traderId, Long companyId, Long quantity);
    void closeIpoAndAllocate(Long companyId);
    IpoApplicationDTO getApplication(Long traderId, Long companyId);
}