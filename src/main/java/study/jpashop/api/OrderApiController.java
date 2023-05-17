package study.jpashop.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.jpashop.domain.Address;
import study.jpashop.domain.Order;
import study.jpashop.domain.OrderItem;
import study.jpashop.domain.OrderStatus;
import study.jpashop.repository.OrderRepository;
import study.jpashop.repository.OrderSearch;
import study.jpashop.repository.order.query.OrderFlatDto;
import study.jpashop.repository.order.query.OrderItemQueryDto;
import study.jpashop.repository.order.query.OrderQueryDto;
import study.jpashop.repository.order.query.OrderQueryRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName();    // LAZY 초기화
            order.getDelivery().getAddress();   // LAZY 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName();  // LAZY 초기화
            }
        }
        return orders;
    }

    @GetMapping("/api/v2/orders")
    public Result<List<OrderDto>> ordersV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .toList();
        return new Result<>(result);
    }

    @GetMapping("/api/v2_1/orders")
    public Result<List<OrderDto>> ordersV2_1() {
        List<Order> orders = orderRepository.findOrders();
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .toList();
        return new Result<>(result);
    }


    /**
     *  SQL로 조회했을때, 중복된 데이터가 발생한다.
     *  중복된 데이터를 애플리케이션에서 제거한다.
     */
    @GetMapping("/api/v3/orders")
    public Result<List<OrderDto>> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .toList();
        return new Result<>(result);
    }

    /**
     *  SQL로 조회했을때, 중복된 데이터가 발생하지 않는다.
     */
    @GetMapping("/api/v3_1/orders")
    public Result<List<OrderDto>> ordersV3_1(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .toList();
        return new Result<>(result);
    }

    @GetMapping("/api/v4/orders")
    public Result<List<OrderQueryDto>> ordersV4() {
        List<OrderQueryDto> result = orderQueryRepository.findOrderQueryDtos();
        return new Result<>(result);
    }

    @GetMapping("/api/v5/orders")
    public Result<List<OrderQueryDto>> ordersV5() {
        List<OrderQueryDto> result = orderQueryRepository.findAllByDto_optimization();
        return new Result<>(result);
    }

    @GetMapping("/api/v6/orders")
    public Result<List<OrderQueryDto>> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        List<OrderQueryDto> result = flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                )).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());

        return new Result<>(result);
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;
        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .toList();
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;
        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }


}
