package com.ecommerce.payment;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Cash on Delivery (COD) payment gateway implementation.
 * This gateway doesn't process actual payments but marks orders for COD processing.
 */
@Component
public class CashOnDeliveryGateway implements PaymentGateway {

    @Override
    public PaymentProvider getProvider() {
        return PaymentProvider.CASH_ON_DELIVERY;
    }

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        // COD doesn't require actual payment processing
        // Just generate a reference number for tracking
        String transactionId = "cod_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);

        return PaymentResponse.builder()
                .success(true)
                .transactionId(transactionId)
                .status("PENDING_COLLECTION")
                .provider(PaymentProvider.CASH_ON_DELIVERY)
                .build();
    }

    @Override
    public PaymentResponse refund(String transactionId, BigDecimal amount) {
        // COD refunds would be handled manually
        return PaymentResponse.builder()
                .success(false)
                .status("NOT_SUPPORTED")
                .errorMessage("Refunds for COD orders must be processed manually")
                .provider(PaymentProvider.CASH_ON_DELIVERY)
                .build();
    }

    @Override
    public PaymentResponse checkStatus(String transactionId) {
        if (transactionId == null || !transactionId.startsWith("cod_")) {
            return PaymentResponse.builder()
                    .success(false)
                    .status("INVALID")
                    .errorMessage("Invalid COD transaction ID")
                    .provider(PaymentProvider.CASH_ON_DELIVERY)
                    .build();
        }

        return PaymentResponse.builder()
                .success(true)
                .transactionId(transactionId)
                .status("PENDING_COLLECTION")
                .provider(PaymentProvider.CASH_ON_DELIVERY)
                .build();
    }

    @Override
    public boolean isConfigured() {
        // COD is always available as it doesn't require external configuration
        return true;
    }
}
