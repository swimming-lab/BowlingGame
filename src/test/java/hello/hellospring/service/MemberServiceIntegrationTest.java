package hello.hellospring.service;
/*
import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest
{
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void join() throws Exception
    {
        // given (이것이 주어졌고)
        Member member = new Member();
        member.setName("hello");

        // when (이런 상황일 때)
        Long saveId = memberService.join(member);

        // then (어떻게 나오는가)
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void duplicate_join_exception() throws Exception
    {
        // given
        Member member1 = new Member();
        member1.setName("hello");

        Member member2 = new Member();
        member2.setName("spring");

        // when
        memberService.join(member1);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("Already exist.");


        try {
            memberService.join(member2);
            fail();
        } catch(IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("Already exist.");
        }
    }

    @Test
    void findMembers() {
    }

    @Test
    void findOne() {
    }
}*/