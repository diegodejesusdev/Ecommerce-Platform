package com.ecommerce.payment;

import java.math.BigDecimal;

/**
 * Enumeration of supported payment providers.
 */
public enum PaymentProvider {
    STRIPE("Stripe"),
    PAYPAL("PayPal"),
    CASH_ON_DELIVERY("Cash on Delivery");

    private final String displayName;

    PaymentProvider(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
