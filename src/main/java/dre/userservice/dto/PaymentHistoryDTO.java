package dre.userservice.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record PaymentHistoryDTO(
    Long id,
    Long invoiceId,
    Long userId,
    BigDecimal amountPaid,
    LocalDateTime paymentDate,
    PaymentMethod paymentMethod,
    PaymentStatus status
) {
}
