package study.jpashop.repository.order.simplequery;

import lombok.Data;
import study.jpashop.domain.Address;
import study.jpashop.domain.Order;
import study.jpashop.domain.OrderStatus;

import java.time.LocalDateTime;

@Data
public class OrderQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderQueryDto() {
    }

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

    public OrderQueryDto(Order order) {
        this.orderId = order.getId();
        this.name = order.getMember().getName();    // LAZY 초기화
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getStatus();
        this.address = order.getDelivery().getAddress();    // LAZY 초기화
    }
}
