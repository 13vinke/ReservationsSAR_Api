package com.SAR.ReservationsSAR.context.payment.infrastructure.adapters.stripe;

import com.SAR.ReservationsSAR.context.payment.application.exceptions.PaymentException;
import com.SAR.ReservationsSAR.context.payment.domain.*;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripeExternalPaymentServiceAdapter implements ExternalPaymentService {

    public StripeExternalPaymentServiceAdapter(
            @Value("${application.payment.services.stripe.api-key}") String stripeApiKey
    ) {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public PaymentResponse cardPay(
            Customer customer,
            Long amount,
            PaymentCurrency currency
    ) {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCustomer(customer.getId())
                .setCurrency(currency.name())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        try {
            PaymentIntent paymentResponse = PaymentIntent.create(params);
            return new PaymentResponse(paymentResponse.getId(), paymentResponse.getClientSecret());
        } catch (StripeException e) {
            throw new PaymentException(e.getStripeError().getMessage());
        }
    }

    @Override
    public void cancelPayment(String payId) {
        try {
            PaymentIntent payment = PaymentIntent.retrieve(payId);
            payment.cancel(PaymentIntentCancelParams.builder().build());
        } catch (StripeException e) {
            throw new PaymentException(e.getStripeError().getMessage());
        }
    }
}