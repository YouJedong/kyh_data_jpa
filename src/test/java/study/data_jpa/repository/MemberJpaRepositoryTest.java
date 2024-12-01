package study.data_jpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.entity.Member;

import java.util.List;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberRepository;

    @Test
    public void testMember() {
        Member member = new Member("you jedong");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.find(savedMember.getId());

        Assertions.assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());


    }

    @Test
    public void findByPage() {
        Member member1 = new Member("you jedong");
        member1.setAge(11);
        Member member2 = new Member("you jedong2");
        member2.setAge(11);
        Member savedMember1 = memberRepository.save(member1);
        Member savedMember2 = memberRepository.save(member2);

        List<Member> members = memberRepository.findByPage(11, 0, 1);
        long cnt = memberRepository.findByPageCnt(11);
        for (Member member : members) {
            System.out.println("member = " + member);
        }
        System.out.println("cnt = " + cnt);

    }

    @Test
    public void bulkAgePlus() {
        memberRepository.save(new Member("you jedong0", 9));
        memberRepository.save(new Member("you jedong1", 10));
        memberRepository.save(new Member("you jedong2", 20));
        memberRepository.save(new Member("you jedong3", 30));
        memberRepository.save(new Member("you jedong4", 40));
        memberRepository.save(new Member("you jedong5", 50));

        int resultCnt = memberRepository.bulkAgePlus(10);
        System.out.println("resultCnt = " + resultCnt);



    }


}