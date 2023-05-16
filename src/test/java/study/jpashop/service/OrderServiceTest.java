package study.jpashop.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.jpashop.domain.Address;
import study.jpashop.domain.Member;
import study.jpashop.domain.Order;
import study.jpashop.domain.OrderStatus;
import study.jpashop.domain.item.Book;
import study.jpashop.exception.NotEnoughStockException;
import study.jpashop.repository.OrderRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    EntityManager em;

    @Test
    void 상품주문() {
        // given
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertThat(getOrder.getStatus())
                .as("상품 주문시 상태는 ORDER")
                .isEqualTo(OrderStatus.ORDER);
        assertThat(getOrder.getOrderItems().size())
                .as("주문한 상품 종류 수가 정확해야 한다")
                .isEqualTo(1);
        assertThat(getOrder.getTotalPrice())
                .as("주문 가격은 가격 * 수량이다.")
                .isEqualTo(book.getPrice() * orderCount);
        assertThat(book.getStockQuantity())
                .as("주문 수량만큼 재고가 줄어야 한다.")
                .isEqualTo(8);
    }

    @Test
    void 주문취소() {
        // given
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order getOrder = orderRepository.findOne(orderId);
        assertThat(getOrder.getStatus())
                .as("주문 취소시 상태는 CANCEL이다.")
                .isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity())
                .as("주문 취소된 상품은 그만큼 재고가 증가해야 한다.")
                .isEqualTo(10);
    }

    @Test
    void 상품주문_재고수량초과() {
        // given
        Member member = createMember();
        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 11;
        // when
        // then
        assertThatThrownBy(() -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        }).isInstanceOf(NotEnoughStockException.class)
                .hasMessage("need more stock");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

}