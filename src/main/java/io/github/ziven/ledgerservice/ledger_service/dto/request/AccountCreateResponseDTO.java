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
public class AccountCreateResponseDTO {

    private UUID id;
    private String name;
    private String type;
    private String currency;
    private BigDecimal balance;
    private OffsetDateTime createdAt;
}
