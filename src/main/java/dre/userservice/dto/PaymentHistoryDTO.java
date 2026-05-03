package dre.userservice.dto;

import dre.membership.model.PaymentMethod;
import dre.membership.model.PaymentStatus;
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
