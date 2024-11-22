package study.data_jpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.entity.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember() {
        Member member = new Member("you jedong2");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        Assertions.assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);


    }

    @Test
    public void findUser() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 21);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMember = memberRepository.findUser("member1", 20);
        Assertions.assertThat(findMember.get(0)).isEqualTo(member1);
    }

    @Test
    public void findUsernameList() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 21);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = : " + s);
        }

    }

    @Test
    public void findMemberDto() {
        Team team = new Team("team1");
        teamRepository.save(team);

        Member member1 = new Member("member1", 20);
        member1.setTeam(team);
        memberRepository.save(member1);



        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto : " + dto);
        }

    }

    @Test
    public void findNames() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 21);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> names = new ArrayList<>();
        names.add("member1");
        names.add("member2");
        List<Member> findMembers = memberRepository.findByNames(names);
        for (Member findMember : findMembers) {
            System.out.println("findMember : " + findMember);
        }

    }

    @Test
    public void returnType() {
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 21);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> list = memberRepository.findListByUsername("member1");
        System.out.println("list = " + list.get(0));
        Member member = memberRepository.findMemberByUsername("member2");
        System.out.println("member = " + member);
        Optional<Member> oMember = memberRepository.findOptionalByUsername("member1");
        System.out.println("oMember.get() = " + oMember.get());
        
    }
    


}