package study.data_jpa.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.repository.MemberRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testEntity() {
        Team t1 = new Team("team1");
        Team t2 = new Team("team2");
        em.persist(t1);
        em.persist(t2);

        Member member1 = new Member("Jedong1", 10, t1);
        Member member2 = new Member("Jedong2", 11, t1);
        Member member3 = new Member("Jedong3", 12, t2);
        Member member4 = new Member("Jedong4", 13, t2);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member : " + member);
            System.out.println("member.team : " + member.getTeam());
        }

    }

    @Test
    public void JpaEventBaseEntity()  throws Exception{
        Member member1 = new Member("member1");
        memberRepository.save(member1);

        Thread.sleep(100);
        member1.setUsername("member2");

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member1.getId()).get();
        System.out.println("findMember.getCreatedDate() = " + findMember.getCreatedDate());
        System.out.println("findMember.getUpdatedDate() = " + findMember.getUpdatedDate());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getUpdatedBy() = " + findMember.getUpdatedBy());


    }


}