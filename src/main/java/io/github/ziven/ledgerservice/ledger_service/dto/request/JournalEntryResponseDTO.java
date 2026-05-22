package io.github.ziven.ledgerservice.ledger_service.dto.request;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JournalEntryResponseDTO {

    private UUID id;
    private UUID accountId;
    private BigDecimal amount;
    private OffsetDateTime createdAt;
}
