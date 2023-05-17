package study.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderDtos() {
        return em.createQuery(
                        "select new study.jpashop.repository.order.simplequery.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                                " from Order o" +
                                " join fetch o.member m" +
                                " join fetch o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

}
