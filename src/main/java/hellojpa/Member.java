package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Member {
    @Id @GeneratedValue
    @Column(name = "MEMEBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String name;

    /* (임베디드 타입)
        @Embedded
        private Period period;

        @Embedded
        private Address address;

        @Embedded
        @AttributeOverrides({
                @AttributeOverride(name="city",
                        column=@Column(name = "office_city")),
                @AttributeOverride(name="street",
                        column=@Column(name = "office_street")),
                @AttributeOverride(name="zipcode",
                        column=@Column(name = "office_zipcode")),
        })
        private Address officeAddress;
    */

    // (값 타입 컬렉션)
    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns =
        @JoinColumn(name="MEMBER_ID")
    )
    @Column(name="FOOD_NAME")
    private Set<String> favoriteFood = new HashSet<>();

    //    @ElementCollection
    //    @CollectionTable(name = "ADDRESS", joinColumns =
    //        @JoinColumn(name="MEMBER_ID")
    //    )
    //    private List<Address> addressHistory = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="MEMBER_ID")
    List<AddressEntity> addressHistory = new ArrayList<>();

    // (fetch = FetchType.EAGER): team 객체, 즉시 로딩
    // (fetch = FetchType.LAZY): team은 proxy 객체, 지연 로딩
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="TEAM_ID")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*  (값 타입 컬렉션)
        public Period getPeriod() {
            return period;
        }
        public void setPeriod(Period period) {
            this.period = period;
        }
        public Address getAddress() {
            return address;
        }
        public void setAddress(Address address) {
            this.address = address;
        }
    */

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Set<String> getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(Set<String> favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public List<AddressEntity> getAddressHistory() {
        return addressHistory;
    }

    public void setAddressHistory(List<AddressEntity> addressHistory) {
        this.addressHistory = addressHistory;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }
}
