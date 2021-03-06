package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            /* (프록시)
                Team team = new Team();
                team.setName("teamA");
                em.persist(team);

                Member member = new Member();
                member.setName("hello");
                member.changeTeam(team);
                em.persist(member);

                em.flush();
                em.clear();
            */

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

            /*  (LAZY 로딩 & EAGER 로딩)
                Member m = em.find(Member.class, member.getId());

                System.out.println("m = "+m.getTeam().getClass());
                System.out.println("teamName = "+m.getTeam().getName());
            */

            /*  (영속성 전이 & 고아 객체 )
                Child child1 = new Child();
                Child child2 = new Child();

                Parent parent = new Parent();
                parent.addChild(child1);
                parent.addChild(child2);


                em.persist(parent);
                // em.persist(child1);
                // em.persist(child2);

                em.flush();
                em.clear();

                Parent findParent = em.find(Parent.class, parent.getId());
                findParent.getChildList().remove(0);
            */

            /*  (임베디드 타입)
                Member member = new Member();
                member.setName("Hello");
                member.setAddress(new Address("city", "street", "100"));
                member.setPeriod(new Period());

                em.persist(member);
            */

            /*  (값타입 비교, 동등성 비교)
                Address address1 = new Address("city", "street", "100");
                Address address2 = new Address("city", "street", "100");
                System.out.println(address1.equals(address2));
            */

            /*  (값 타입 컬렉션)
                // (저장)
                Member member = new Member();
                member.setName("member1");
                member.setHomeAddress(new Address("city1", "street", "10000"));

                member.getFavoriteFood().add("치킨");
                member.getFavoriteFood().add("족발");
                member.getFavoriteFood().add("피자");

                // member.getAddressHistory().add(new Address("old1", "street", "10001"));
                // member.getAddressHistory().add(new Address("old2", "street", "10002"));
                member.getAddressHistory().add(new AddressEntity("old1", "street", "10001"));
                member.getAddressHistory().add(new AddressEntity("old2", "street", "10002"));

                em.persist(member);

                em.flush();
                em.clear();

                // (조회)
                Member findMember = em.find(Member.class, member.getId()); // 컬렉션들은 모두 지연 로딩이다

                // (수정)
                Address a = findMember.getHomeAddress();
                // 값 타입 수정은 setter 사용하지 마세요!, 무조건 통으로 교체할 것
                findMember.setHomeAddress(new Address("new City", a.getStreet(), a.getZipcode()));

                // JPA에서 알아서 해당 정보 삭제한다.
                findMember.getFavoriteFood().remove("치킨");
                findMember.getFavoriteFood().add("한식");

                // remove 할 때 equals를 사용한다
                // findMember.getAddressHistory().remove(new Address("old1", "street", "10001"));
                // findMember.getAddressHistory().add(new Address("new1", "street", "10001"));
            */

            // jpql
            List<Member> result = em.createQuery(
                    "select m from Member m where m.name like '%kim%'",
                    Member.class
            ).getResultList();

            System.out.println("========================================");

            // criteria
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);

            Root<Member> m = query.from(Member.class);

            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("name"), "kim"));
            List<Member> resultList = em.createQuery(cq).getResultList();



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
