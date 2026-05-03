package dre.userservice.client;

import dre.userservice.dto.InvoiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@FeignClient(name = "billing-service", url = "${billingservice.url:http://localhost:8082}", configuration = FeignConfig.class)
public interface BillingServiceClient {

    Page<InvoiceDTO> getUserInvoices(Long userId, Pageable pageable);

}
