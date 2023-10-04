package no3.game.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


}