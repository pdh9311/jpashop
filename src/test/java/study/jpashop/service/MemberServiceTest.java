package study.jpashop.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.jpashop.domain.Member;
import study.jpashop.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    void 회원가입() {
        // given
        Member member = new Member();
        member.setName("park");

        // when
        Long memberId = memberService.join(member);

        // then
        assertThat(memberRepository.findOne(memberId)).isEqualTo(member);
    }

    @DisplayName("중복 회원 가입 확인")
    @Test
    void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        // when
        memberService.join(member1);

        // then
        assertThatThrownBy(() -> {
            memberService.join(member2);
        }).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 가입한 회원입니다.");
    }

}