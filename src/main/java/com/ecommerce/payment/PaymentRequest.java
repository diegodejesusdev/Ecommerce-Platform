package com.ecommerce.payment;

import java.math.BigDecimal;

/**
 * Request object for payment processing.
 */
public class PaymentRequest {
    private final String orderNumber;
    private final BigDecimal amount;
    private final String currency;
    private final String customerEmail;
    private final String customerName;
    private final String description;

    public PaymentRequest(String orderNumber, BigDecimal amount, String currency,
                          String customerEmail, String customerName, String description) {
        this.orderNumber = orderNumber;
        this.amount = amount;
        this.currency = currency;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.description = description;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentRequestBuilder builder() {
        return new PaymentRequestBuilder();
    }

    public static class PaymentRequestBuilder {
        private String orderNumber;
        private BigDecimal amount;
        private String currency = "USD";
        private String customerEmail;
        private String customerName;
        private String description;

        public PaymentRequestBuilder orderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public PaymentRequestBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentRequestBuilder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public PaymentRequestBuilder customerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
            return this;
        }

        public PaymentRequestBuilder customerName(String customerName) {
            this.customerName = customerName;
            return this;
        }

        public PaymentRequestBuilder description(String description) {
            this.description = description;
            return this;
        }

        public PaymentRequest build() {
            return new PaymentRequest(orderNumber, amount, currency, customerEmail, customerName, description);
        }
    }
}
