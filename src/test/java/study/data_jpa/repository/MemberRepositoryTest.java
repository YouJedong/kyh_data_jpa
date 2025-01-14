package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.data_jpa.dto.MemberDto;
import study.data_jpa.entity.Member;
import study.data_jpa.entity.MemberProjection;
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

    @PersistenceContext
    EntityManager em;

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
    
    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        List<Member> content = page.getContent();
        Long total = page.getTotalElements();
        for (Member member : content) {
            System.out.println("member = " + member);
        }
        System.out.println("total = " + total);
        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(6);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));
        for (MemberDto memberDto : toMap) {
            System.out.println("memberDto = " + memberDto);
        }

    }

    @Test
    public void slice() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        memberRepository.save(new Member("member6", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);
        List<Member> content = page.getContent();
        for (Member member : content) {
            System.out.println("member = " + member);
        }

        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

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
        //em.flush();
        // em.clear();

        List<Member> Members = memberRepository.findAll();
        for (Member member : Members) {
            System.out.println("member = " + member);
        }

    }

    @Test
    public void findMemberLazy() {
        Team team1 = new Team("Team1");
        Team team2 = new Team("Team2");
        teamRepository.save(team1);
        teamRepository.save(team2);

        Member member1 = new Member("Member1", 10, team1);
        Member member2 = new Member("Member2", 20, team2);
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println("member = " + member);
        }

    }

    @Test
    public void queryHint() {
        Member member = new Member("member1", 10);
        memberRepository.save(member);
        em.flush();
        em.clear();

        List<Member> members = memberRepository.findReadOnlyByUsername("member1");
        Member findMember = members.get(0);
        findMember.setUsername("hahahaha");


    }

    @Test
    public void callCustom() {

        List<Member> memberCustom = memberRepository.findMemberCustom();
    }

    @Test
    public void projections() {
        Team temaA = new Team("teamA");
        em.persist(temaA);

        Member m1 = new Member("m1", 0, temaA);
        Member m2 = new Member("m2", 0, temaA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<NestedClosedProjections> result = memberRepository.findProjectionsByUsername("m1");

        for (NestedClosedProjections nestedClosedProjections : result) {
            System.out.println("nestedClosedProjections = " + nestedClosedProjections.getUsername());
            System.out.println("nestedClosedProjections.getTeam().getName() = " + nestedClosedProjections.getTeam().getName());
        }
    }

    @Test
    public void nativeQuery() {
        Team temaA = new Team("teamA");
        em.persist(temaA);

        Member m1 = new Member("m1", 0, temaA);
        Member m2 = new Member("m2", 0, temaA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0,10));

        List<MemberProjection> resultList = result.getContent();

        for (MemberProjection memberProjection : resultList) {
            System.out.println("memberProjection.getUsername() = " + memberProjection.getUsername());
            System.out.println("memberProjection.getTeamName() = " + memberProjection.getTeamName());
        }

    }

}