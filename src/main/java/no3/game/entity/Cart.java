package no3.game.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    private int count; // 카트에 담긴 총 상품 개수

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setCount(0);
        cart.setMember(member);
        return cart;
    }

}