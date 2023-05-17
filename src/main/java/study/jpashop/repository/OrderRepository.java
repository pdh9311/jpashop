package study.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import study.jpashop.domain.Order;
import study.jpashop.domain.OrderStatus;
import study.jpashop.domain.QMember;
import study.jpashop.domain.QOrder;

import java.util.List;

import static study.jpashop.domain.QMember.member;
import static study.jpashop.domain.QOrder.order;

@Repository
public class OrderRepository {

    @Autowired
    private EntityManager em;

    private JPAQueryFactory query;

    public OrderRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }


    public List<Order> findAll(OrderSearch orderSearch) {
        QOrder order = QOrder.order;
        QMember member = QMember.member;

        return query
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()),
                        nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    public List<Order> findOrders() {
        return em.createQuery("select o from Order o join o.member", Order.class)
                .getResultList();
    }


    private BooleanExpression statusEq(OrderStatus orderStatus) {
        if (orderStatus == null) {
            return null;
        }
        return order.status.eq(orderStatus);
    }

    private BooleanExpression nameLike(String memberName) {
        if (!StringUtils.hasText(memberName)) {
            return null;
        }
        return member.name.eq(memberName);
    }

    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }


    /**
     * distinct : [DB쿼리 결과에 대한 중복 제거] + [애플리케이션에서 ID값이 같은 값을 중복 제거] 해줍니다.
     * hibernate6 부터는 distinct를 사용하지 않더라도 기본적으로 distinct 가 동작합니다.
     * 일대다 조인의 경우 페이징처리를 메모리에서 하게 됩니다. (위험)
     */
    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
                .getResultList();
    }
}
