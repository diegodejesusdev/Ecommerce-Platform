package com.ecommerce.service;

import com.ecommerce.payment.PaymentGateway;
import com.ecommerce.payment.PaymentProvider;
import com.ecommerce.payment.PaymentRequest;
import com.ecommerce.payment.PaymentResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service for managing payment processing across multiple gateways.
 */
@Service
public class PaymentService {

    private final List<PaymentGateway> paymentGateways;
    private final Map<PaymentProvider, PaymentGateway> gatewayMap;

    public PaymentService(List<PaymentGateway> paymentGateways) {
        this.paymentGateways = paymentGateways;
        this.gatewayMap = paymentGateways.stream()
                .collect(Collectors.toMap(PaymentGateway::getProvider, gateway -> gateway));
    }

    /**
     * Get all available payment providers.
     */
    public List<PaymentProvider> getAvailableProviders() {
        return paymentGateways.stream()
                .filter(PaymentGateway::isConfigured)
                .map(PaymentGateway::getProvider)
                .collect(Collectors.toList());
    }

    /**
     * Get all configured payment providers including COD.
     */
    public List<PaymentProvider> getAllProviders() {
        return paymentGateways.stream()
                .map(PaymentGateway::getProvider)
                .collect(Collectors.toList());
    }

    /**
     * Process a payment using the specified provider.
     */
    public PaymentResponse processPayment(PaymentProvider provider, PaymentRequest request) {
        PaymentGateway gateway = gatewayMap.get(provider);
        if (gateway == null) {
            return PaymentResponse.builder()
                    .success(false)
                    .status("ERROR")
                    .errorMessage("Payment provider not found: " + provider)
                    .build();
        }

        if (!gateway.isConfigured() && provider != PaymentProvider.CASH_ON_DELIVERY) {
            return PaymentResponse.builder()
                    .success(false)
                    .status("NOT_CONFIGURED")
                    .errorMessage("Payment provider is not configured: " + provider.getDisplayName())
                    .provider(provider)
                    .build();
        }

        return gateway.processPayment(request);
    }

    /**
     * Refund a payment.
     */
    public PaymentResponse refund(PaymentProvider provider, String transactionId, BigDecimal amount) {
        PaymentGateway gateway = gatewayMap.get(provider);
        if (gateway == null) {
            return PaymentResponse.builder()
                    .success(false)
                    .status("ERROR")
                    .errorMessage("Payment provider not found: " + provider)
                    .build();
        }

        return gateway.refund(transactionId, amount);
    }

    /**
     * Check the status of a transaction.
     */
    public PaymentResponse checkTransactionStatus(PaymentProvider provider, String transactionId) {
        PaymentGateway gateway = gatewayMap.get(provider);
        if (gateway == null) {
            return PaymentResponse.builder()
                    .success(false)
                    .status("ERROR")
                    .errorMessage("Payment provider not found: " + provider)
                    .build();
        }

        return gateway.checkStatus(transactionId);
    }

    /**
     * Check if a specific payment provider is configured.
     */
    public boolean isProviderConfigured(PaymentProvider provider) {
        PaymentGateway gateway = gatewayMap.get(provider);
        return gateway != null && gateway.isConfigured();
    }

    /**
     * Get a specific payment gateway.
     */
    public Optional<PaymentGateway> getGateway(PaymentProvider provider) {
        return Optional.ofNullable(gatewayMap.get(provider));
    }
}
