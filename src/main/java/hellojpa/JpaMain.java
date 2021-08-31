package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.*;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setName("hello");
            member.changeTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            /*  (em.find)
                // findMember: 진짜 객체
                Member findMember = em.find(Member.class, member.getId());
            */

            /*  (em.getReference)
                // findMember: 가짜 객체, 프록시 클래스
                Member findMember = em.getReference(Member.class, member.getId());

                // em.getReference(): 레퍼런스를 찾을 때 member.getId()(파라미터)를 사용하기 때문에 쿼리가 날라가지 않는다
                System.out.println("findMember.id = "+findMember.getId());
                // em.getReference(): findMember의 name을 사용할 떄 쿼리를 날린다. 프록시 초기화
                System.out.println("findMember.username = "+findMember.getName());
                // 프록시 인스턴스의 초기화 여부 확인
                System.out.println("isLoaded = " + emf.getPersistenceUnitUtil().isLoaded(findMember));
            */

            Member m = em.find(Member.class, member.getId());

            System.out.println("m = "+m.getTeam().getClass());
            System.out.println("teamName = "+m.getTeam().getName());

            tx.commit();
        }catch(Exception e) {
            System.out.println(e.getMessage());
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
