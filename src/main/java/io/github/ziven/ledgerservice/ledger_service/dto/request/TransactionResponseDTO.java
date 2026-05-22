package io.github.ziven.ledgerservice.ledger_service.dto.request;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO {

    private UUID id;
    private String referenceId;
    private String description;
    private String type;
    private OffsetDateTime createdAt;
    private List<JournalEntryResponseDTO> journalEntries;
}
