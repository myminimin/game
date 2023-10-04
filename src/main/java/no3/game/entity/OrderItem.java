package no3.game.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문가격


    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(item.getPrice());
        return orderItem;
    }

    public int getTotalPrice(){

        return orderPrice;
    }

    public void cancel() {

        this.getItem();
    }

}