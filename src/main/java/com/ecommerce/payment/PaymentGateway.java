package com.ecommerce.payment;

/**
 * Interface for payment gateway operations.
 * Implement this interface to add support for different payment providers.
 */
public interface PaymentGateway {

    /**
     * Get the payment provider this gateway handles.
     */
    PaymentProvider getProvider();

    /**
     * Process a payment request.
     *
     * @param request The payment request containing order and customer details
     * @return PaymentResponse with transaction details
     */
    PaymentResponse processPayment(PaymentRequest request);

    /**
     * Refund a previously processed payment.
     *
     * @param transactionId The original transaction ID
     * @param amount Amount to refund (null for full refund)
     * @return PaymentResponse with refund details
     */
    PaymentResponse refund(String transactionId, java.math.BigDecimal amount);

    /**
     * Check the status of a transaction.
     *
     * @param transactionId The transaction ID to check
     * @return PaymentResponse with current status
     */
    PaymentResponse checkStatus(String transactionId);

    /**
     * Validate if this gateway is properly configured.
     *
     * @return true if the gateway is configured and ready to process payments
     */
    boolean isConfigured();
}
