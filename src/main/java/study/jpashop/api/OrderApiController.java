package study.jpashop.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.jpashop.domain.Address;
import study.jpashop.domain.Order;
import study.jpashop.domain.OrderStatus;
import study.jpashop.repository.OrderSearch;
import study.jpashop.repository.order.simplequery.OrderQueryDto;
import study.jpashop.service.OrderService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    /**
     * 양방향 참조로 인해 무한루프에 빠집니다.
     */
//    @GetMapping("/api/v1/simple-orders")
//    public List<Order> ordersV1() {
//        List<Order> orders = orderService.findOrders(new OrderSearch());
//        for (Order order : orders) {
//            order.getMember().getName();        // LAZY 강제 초기화
//            order.getDelivery().getAddress();   // LAZY 강제 초기화
//        }
//        return orders;
//    }

    @GetMapping("/api/v2/simple-orders")
    public Result<List<SimpleOrderDto>> ordersV2() {
        List<Order> orders = orderService.findOrders(new OrderSearch());
        List<SimpleOrderDto> all = orders.stream()
                .map(SimpleOrderDto::new)
                .toList();
        return new Result<>(all);
    }

    // join fetch 적용 (GOOD!)
    @GetMapping("/api/v3/simple-orders")
    public Result<List<SimpleOrderDto>> ordersV3() {
        List<Order> orders = orderService.findAllWithMemberDelivery();
        List<SimpleOrderDto> all = orders.stream()
                .map(SimpleOrderDto::new)
                .toList();
        return new Result<>(all);
    }

    // 바로 dto로 조회
    @GetMapping("/api/v4/simple-orders")
    public Result<List<OrderQueryDto>> ordersV4() {
        log.info("ordersV4 start");
        List<OrderQueryDto> orderDtos = orderService.findOrderDtos();
        log.info("ordersV4 end");
        return new Result<>(orderDtos);
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();    // LAZY 초기화
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();    // LAZY 초기화
        }
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }



}
