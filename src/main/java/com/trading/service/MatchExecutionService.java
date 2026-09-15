package com.trading.service;

import com.trading.dto.TradeDTO;

import java.util.List;

public interface MatchExecutionService {
    List<TradeDTO> doMatch(Long companyId);
}