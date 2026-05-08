package io.github.ziven.ledgerservice.ledger_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateRequestDTO {

    private String name;
    private String type;
    private String currency;
}
