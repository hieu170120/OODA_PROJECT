package com.foodorder.state.order;

import com.foodorder.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static com.foodorder.enums.OrderStatus.CANCELLED;
import static com.foodorder.enums.OrderStatus.DELIVERY;
import static com.foodorder.enums.OrderStatus.FINISHED;
import static com.foodorder.enums.OrderStatus.PREPARING;
import static com.foodorder.enums.OrderStatus.READY_FOR_PICKUP;
import static com.foodorder.enums.OrderStatus.RECEIVED;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class OrderStateTransitionTest {

    private OrderStateFactory factory;

    @BeforeEach
    void setUp() {
        factory = new OrderStateFactory(List.of(
                new ReceivedState(),
                new PreparingState(),
                new DeliveryState(),
                new ReadyForPickupState(),
                new FinishedState(),
                new CancelledState()
        ));
    }

    static Stream<Arguments> allowedTransitions() {
        return Stream.of(
                arguments(RECEIVED, RECEIVED),
                arguments(RECEIVED, PREPARING),
                arguments(RECEIVED, CANCELLED),
                arguments(PREPARING, PREPARING),
                arguments(PREPARING, DELIVERY),
                arguments(PREPARING, READY_FOR_PICKUP),
                arguments(PREPARING, CANCELLED),
                arguments(DELIVERY, DELIVERY),
                arguments(DELIVERY, FINISHED),
                arguments(READY_FOR_PICKUP, READY_FOR_PICKUP),
                arguments(READY_FOR_PICKUP, FINISHED),
                arguments(FINISHED, FINISHED),
                arguments(CANCELLED, CANCELLED)
        );
    }

    static Stream<Arguments> deniedTransitions() {
        return Stream.of(
                arguments(RECEIVED, FINISHED),
                arguments(RECEIVED, DELIVERY),
                arguments(RECEIVED, READY_FOR_PICKUP),
                arguments(PREPARING, RECEIVED),
                arguments(PREPARING, FINISHED),
                arguments(DELIVERY, RECEIVED),
                arguments(DELIVERY, PREPARING),
                arguments(DELIVERY, CANCELLED),
                arguments(DELIVERY, READY_FOR_PICKUP),
                arguments(READY_FOR_PICKUP, RECEIVED),
                arguments(READY_FOR_PICKUP, PREPARING),
                arguments(READY_FOR_PICKUP, CANCELLED),
                arguments(READY_FOR_PICKUP, DELIVERY),
                arguments(FINISHED, RECEIVED),
                arguments(FINISHED, PREPARING),
                arguments(FINISHED, CANCELLED),
                arguments(CANCELLED, RECEIVED),
                arguments(CANCELLED, PREPARING),
                arguments(CANCELLED, FINISHED)
        );
    }

    @ParameterizedTest
    @MethodSource("allowedTransitions")
    void validateTransition_allowed(OrderStatus from, OrderStatus to) {
        assertDoesNotThrow(() -> factory.forStatus(from).validateTransition(to));
    }

    @ParameterizedTest
    @MethodSource("deniedTransitions")
    void validateTransition_denied(OrderStatus from, OrderStatus to) {
        assertThrows(InvalidOrderTransitionException.class,
                () -> factory.forStatus(from).validateTransition(to));
    }
}
