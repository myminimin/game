package com.no3.game.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="item_img")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "item")
public class ItemImg extends BaseEntity{

    @Id
    @Column(name="item_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uuid;

    private String imgName;

    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


}