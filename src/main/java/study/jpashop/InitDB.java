package study.jpashop;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import study.jpashop.domain.Address;
import study.jpashop.domain.Member;
import study.jpashop.domain.item.Book;
import study.jpashop.service.ItemService;
import study.jpashop.service.MemberService;

@Component
@RequiredArgsConstructor
public class InitDB {
    private final MemberService memberService;
    private final ItemService itemService;

    @PostConstruct
    public void init() {
        Member member = new Member();
        member.setName("박동현");
        member.setAddress(new Address("양산", "양주로16", "50631"));
        memberService.join(member);

        Book book = new Book();
        book.setName("JPA");
        book.setPrice(10000);
        book.setStockQuantity(1000);
        book.setAuthor("김영한");
        book.setIsbn("123-123");
        itemService.saveItem(book);
    }
}
