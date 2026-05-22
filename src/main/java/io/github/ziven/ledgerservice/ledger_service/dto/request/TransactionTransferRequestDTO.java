package io.github.ziven.ledgerservice.ledger_service.dto.request;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionTransferRequestDTO {

    private UUID fromAccountId;
    private UUID toAccountId;
    private BigDecimal amount;
    private String referenceId;
    private String description;
}
