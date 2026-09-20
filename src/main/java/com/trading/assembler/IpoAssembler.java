package com.trading.assembler;

import com.trading.dto.IpoApplicationDTO;
import com.trading.entity.IpoApplication;
import org.springframework.stereotype.Component;

@Component
public class IpoAssembler {
    public IpoApplicationDTO assembleDetails(IpoApplication app) {
        return new IpoApplicationDTO(
                app.getId(), app.getTrader().getId(), app.getCompany().getId(),
                app.getRequestedQuantity(), app.getAllocatedQuantity(),
                app.getStatus(), app.getAppliedAt()
        );
    }
}
