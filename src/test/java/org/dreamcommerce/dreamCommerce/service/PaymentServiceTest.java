package org.dreamcommerce.dreamCommerce.service;

import org.dreamcommerce.dreamCommerce.dtos.requests.CreatePaymentRequest;
import org.dreamcommerce.dreamCommerce.dtos.responses.PaymentResponse;
import org.dreamcommerce.dreamCommerce.enums.PaymentMethod;
import org.dreamcommerce.dreamCommerce.enums.PaymentStatus;
import org.dreamcommerce.dreamCommerce.models.Payment;
import org.dreamcommerce.dreamCommerce.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void testCanCreatePayment() {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setOrderId(1L);
        request.setAmount(BigDecimal.valueOf(500.00));
        request.setMethod(PaymentMethod.CARD);

        Payment savedPayment = buildPayment(1L, 1L, BigDecimal.valueOf(500.00), PaymentStatus.COMPLETED);
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponse response = paymentService.createPayment(request);

        assertNotNull(response);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    void testCanGetPayment() {
        Payment payment = buildPayment(1L, 1L, BigDecimal.valueOf(300.00), PaymentStatus.COMPLETED);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        PaymentResponse response = paymentService.getPayment(1L);

        assertNotNull(response);
        assertThat(response.getOrderId()).isEqualTo(1L);
    }

    @Test
    void testGetPaymentThrowsWhenNotFound() {
        when(paymentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.getPayment(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Payment not found");
    }

    @Test
    void testCanGetPaymentByOrder() {
        Payment payment = buildPayment(1L, 5L, BigDecimal.valueOf(200.00), PaymentStatus.COMPLETED);
        when(paymentRepository.findByOrderId(5L)).thenReturn(Optional.of(payment));

        PaymentResponse response = paymentService.getPaymentByOrder(5L);

        assertNotNull(response);
        assertThat(response.getOrderId()).isEqualTo(5L);
    }

    @Test
    void testGetPaymentByOrderThrowsWhenNotFound() {
        when(paymentRepository.findByOrderId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.getPaymentByOrder(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Payment not found for order");
    }

    @Test
    void testCanGetAllPayments() {
        Payment p1 = buildPayment(1L, 1L, BigDecimal.valueOf(100.00), PaymentStatus.COMPLETED);
        Payment p2 = buildPayment(2L, 2L, BigDecimal.valueOf(200.00), PaymentStatus.PENDING);
        when(paymentRepository.findAll()).thenReturn(List.of(p1, p2));

        List<PaymentResponse> responses = paymentService.getAllPayments();

        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void testCanDeletePayment() {
        doNothing().when(paymentRepository).deleteById(1L);

        paymentService.deletePayment(1L);

        verify(paymentRepository).deleteById(1L);
    }

    private Payment buildPayment(Long id, Long orderId, BigDecimal amount, PaymentStatus status) {
        Payment payment = new Payment();
        payment.setId(id);
        payment.setOrderId(orderId);
        payment.setAmount(amount);
        payment.setStatus(status);
        payment.setMethod(PaymentMethod.CARD);
        return payment;
    }
}
