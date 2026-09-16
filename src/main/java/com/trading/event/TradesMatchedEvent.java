package com.trading.event;

import java.util.List;

public record TradesMatchedEvent(Long companyId, List<Long> tradeIds) {}