package dre.userservice.dto;

import dre.membership.model.InvoiceStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record InvoiceDTO(
    Long id,
    Long membershipId,
    Long userId,
    BigDecimal discount,
    BigDecimal subTotal,
    BigDecimal totalAmount,
    LocalDateTime dueDate,
    InvoiceStatus status,
    LocalDateTime createdDate,
    LocalDateTime paidDate,
    String description
) {}
