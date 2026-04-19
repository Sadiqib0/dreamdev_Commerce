package org.dreamcommerce.dreamCommerce.service;

import lombok.RequiredArgsConstructor;
import org.dreamcommerce.dreamCommerce.dtos.requests.CreatePaymentRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.PaymentResponse;
import org.dreamcommerce.dreamCommerce.enums.PaymentStatus;
import org.dreamcommerce.dreamCommerce.models.Payment;
import org.dreamcommerce.dreamCommerce.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        ModelMapper mapper = new ModelMapper();
        Payment payment = mapper.map(request, Payment.class);
        payment.setStatus(PaymentStatus.COMPLETED);
        return mapper.map(paymentRepository.save(payment), PaymentResponse.class);
    }

    @Override
    public PaymentResponse getPayment(Long id) {
        ModelMapper mapper = new ModelMapper();
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return mapper.map(payment, PaymentResponse.class);
    }

    @Override
    public PaymentResponse getPaymentByOrder(Long orderId) {
        ModelMapper mapper = new ModelMapper();
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order"));
        return mapper.map(payment, PaymentResponse.class);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {
        ModelMapper mapper = new ModelMapper();
        return paymentRepository.findAll().stream()
                .map(payment -> mapper.map(payment, PaymentResponse.class))
                .toList();
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
