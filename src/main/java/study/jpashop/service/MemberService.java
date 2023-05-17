package study.jpashop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.jpashop.domain.Member;
import study.jpashop.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // spring이 제공하는 어노테이션을 사용하세요.   // 읽기 전용은 스프링이 좀 더 최적화해준다.
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());       // 실무에서는 동시 접근으로 인한 문제가 발생할 수 있기 때문에, name을 유니크 제약조건으로 해결할 수 있습니다.
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 가입한 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long memberId, String name) {
        Member member = memberRepository.findOne(memberId);
        member.setName(name);
    }

}
