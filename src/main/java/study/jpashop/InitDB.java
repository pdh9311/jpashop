package study.jpashop;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import study.jpashop.domain.*;
import study.jpashop.domain.item.Book;
import study.jpashop.service.ItemService;
import study.jpashop.service.MemberService;
import study.jpashop.service.OrderService;


@Component
@RequiredArgsConstructor
public class InitDB {

    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderService orderService;

    private final InitService initService;

    @PostConstruct
    public void init() {
//        Member member = new Member();
//        member.setName("박동현");
//        member.setAddress(new Address("경상남도", "양산", "50631"));
//        memberService.join(member);
//
//        Book book = new Book();
//        book.setName("JPA");
//        book.setPrice(10000);
//        book.setStockQuantity(1000);
//        book.setAuthor("김영한");
//        book.setIsbn("123-123");
//        itemService.saveItem(book);
//
//        Member joonpark = new Member();
//        joonpark.setName("joonpark");
//        joonpark.setAddress(new Address("경기도", "정자역", "123123"));
//        memberService.join(joonpark);
//
//        Member junyopar = new Member();
//        junyopar.setName("junyopar");
//        junyopar.setAddress(new Address("전라남도", "광주", "98734"));
//        memberService.join(junyopar);


        initService.initDb();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final MemberService memberService;
        private final ItemService itemService;

        public void initDb() {
            // 고급 //////////////////////////////////////////////////////////////////////////////////
            Member userA = new Member();
            userA.setName("userA");
            userA.setAddress(new Address("서울", "금천구" ,"111-111"));
            memberService.join(userA);

            Book jpa1 = new Book();
            jpa1.setName("JPA1 Book");
            jpa1.setPrice(10000);
            jpa1.setStockQuantity(100);
            itemService.saveItem(jpa1);

            Book jpa2 = new Book();
            jpa2.setName("JPA2 Book");
            jpa2.setPrice(20000);
            jpa2.setStockQuantity(100);
            itemService.saveItem(jpa2);

            OrderItem orderItem1 = OrderItem.createOrderItem(jpa1, jpa1.getPrice(), 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(jpa2, jpa2.getPrice(), 2);

            Order order1 = Order.createOrder(userA, createDelivery(userA), orderItem1, orderItem2);
            em.persist(order1);

            // 고급 //////////////////////////////////////////////////////////////////////////////////
            Member userB = new Member();
            userB.setName("userB");
            userB.setAddress(new Address("부산", "사하구" ,"123-123"));
            memberService.join(userB);

            Book spring1 = new Book();
            spring1.setName("Spring1 Book");
            spring1.setPrice(20000);
            spring1.setStockQuantity(200);
            itemService.saveItem(spring1);

            Book spring2 = new Book();
            spring2.setName("Spring2 Book");
            spring2.setPrice(40000);
            spring2.setStockQuantity(300);
            itemService.saveItem(spring2);

            OrderItem orderItem3 = OrderItem.createOrderItem(spring1, spring1.getPrice(), 3);
            OrderItem orderItem4 = OrderItem.createOrderItem(spring2, spring2.getPrice(), 4);

            Order order2 = Order.createOrder(userB, createDelivery(userB), orderItem3, orderItem4);
            em.persist(order2);
        }
        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }


    }
}
