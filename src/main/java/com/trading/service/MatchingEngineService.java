package com.trading.service;

import com.trading.dto.TradeDTO;
import java.util.List;

public interface MatchingEngineService {
    List<TradeDTO> match(Long companyId);
}
