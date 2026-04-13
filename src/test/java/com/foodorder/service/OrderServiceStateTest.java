package com.foodorder.service;

import com.foodorder.entity.OrderRecordEntity;
import com.foodorder.model.enums.OrderStatus;
import com.foodorder.repository.OrderJpaRepository;
import com.foodorder.state.order.CancelledState;
import com.foodorder.state.order.DeliveryState;
import com.foodorder.state.order.FinishedState;
import com.foodorder.state.order.InvalidOrderTransitionException;
import com.foodorder.state.order.OrderStateFactory;
import com.foodorder.state.order.PreparingState;
import com.foodorder.state.order.ReadyForPickupState;
import com.foodorder.state.order.ReceivedState;
import com.foodorder.strategy.payment.PaymentStrategyResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceStateTest {

    @Mock
    OrderJpaRepository orderRepository;
    @Mock
    PaymentStrategyResolver paymentStrategyResolver;

    OrderService orderService;

    @BeforeEach
    void setUp() {
        OrderStateFactory factory = new OrderStateFactory(List.of(
                new ReceivedState(),
                new PreparingState(),
                new DeliveryState(),
                new ReadyForPickupState(),
                new FinishedState(),
                new CancelledState()
        ));
        orderService = new OrderService(orderRepository, paymentStrategyResolver, factory);
    }

    @Test
    void updateOrderStatus_valid_savesNewStatus() {
        OrderRecordEntity record = new OrderRecordEntity();
        record.setOrderId("test-order-1");
        record.setOrderStatus(OrderStatus.RECEIVED);

        when(orderRepository.findById("test-order-1")).thenReturn(Optional.of(record));
        when(orderRepository.save(any(OrderRecordEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        orderService.updateOrderStatus("test-order-1", OrderStatus.PREPARING);

        ArgumentCaptor<OrderRecordEntity> captor = ArgumentCaptor.forClass(OrderRecordEntity.class);
        verify(orderRepository).save(captor.capture());
        assertThat(captor.getValue().getOrderStatus()).isEqualTo(OrderStatus.PREPARING);
    }

    @Test
    void updateOrderStatus_invalid_throwsAndDoesNotMutatePrematurely() {
        OrderRecordEntity record = new OrderRecordEntity();
        record.setOrderId("test-order-2");
        record.setOrderStatus(OrderStatus.RECEIVED);

        when(orderRepository.findById("test-order-2")).thenReturn(Optional.of(record));

        assertThrows(InvalidOrderTransitionException.class,
                () -> orderService.updateOrderStatus("test-order-2", OrderStatus.FINISHED));

        assertThat(record.getOrderStatus()).isEqualTo(OrderStatus.RECEIVED);
    }
}
