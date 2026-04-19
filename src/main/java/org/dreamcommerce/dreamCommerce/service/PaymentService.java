package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.CreatePaymentRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse createPayment(CreatePaymentRequest request);
    PaymentResponse getPayment(Long id);
    PaymentResponse getPaymentByOrder(Long orderId);
    List<PaymentResponse> getAllPayments();
    void deletePayment(Long id);
}
