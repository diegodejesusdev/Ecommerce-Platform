package com.ecommerce.payment;

import java.time.LocalDateTime;

/**
 * Response object from payment processing.
 */
public class PaymentResponse {
    private final boolean success;
    private final String transactionId;
    private final String status;
    private final String errorMessage;
    private final LocalDateTime processedAt;
    private final PaymentProvider provider;

    private PaymentResponse(Builder builder) {
        this.success = builder.success;
        this.transactionId = builder.transactionId;
        this.status = builder.status;
        this.errorMessage = builder.errorMessage;
        this.processedAt = builder.processedAt;
        this.provider = builder.provider;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public PaymentProvider getProvider() {
        return provider;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean success;
        private String transactionId;
        private String status;
        private String errorMessage;
        private LocalDateTime processedAt = LocalDateTime.now();
        private PaymentProvider provider;

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        public Builder processedAt(LocalDateTime processedAt) {
            this.processedAt = processedAt;
            return this;
        }

        public Builder provider(PaymentProvider provider) {
            this.provider = provider;
            return this;
        }

        public PaymentResponse build() {
            return new PaymentResponse(this);
        }
    }
}
