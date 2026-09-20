package com.trading.dto;

import com.trading.enums.IpoApplicationStatus;

import java.time.LocalDateTime;

public record IpoApplicationDTO(
        Long id,
        Long traderId,
        Long companyId,
        Long requestedQuantity,
        Long allocatedQuantity,
        IpoApplicationStatus status,
        LocalDateTime appliedAt
) {}