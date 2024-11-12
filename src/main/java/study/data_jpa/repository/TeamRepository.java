package study.data_jpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import study.data_jpa.entity.Member;
import study.data_jpa.entity.Team;

import java.util.List;
import java.util.Optional;

@Component
public class TeamRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Team tema) {
        em.persist(tema);
    }

    public void delete(Team team) {
        em.remove(team);
    }

    public List<Team> findAll() {
        return em.createQuery("select t from Team t", Team.class)
                .getResultList();
    }

    public Optional<Team> findById(Long id) {
        Team team = em.find(Team.class, id);
        return Optional.ofNullable(team);
    }

    public long count() {
        return em.createQuery("select count(t) from Team m", Long.class)
                .getSingleResult();
    }

    public Team find(Long id) {
        return em.find(Team.class, id);

    }
}


