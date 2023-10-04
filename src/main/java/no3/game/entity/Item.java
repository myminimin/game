package no3.game.entity;

import lombok.*;

import javax.persistence.*;
import no3.game.constant.ItemSellStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity //클래스를 엔티티로 선언
@Table(name="item") //엔티티와 매핑할 테이블을 지정(테이블 명)
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item extends BaseEntity {

    @Id //테이블에 기본키로 사용(pk)
    @Column(name="item_id") //필드와 컬럼 매핑
    @GeneratedValue(strategy = GenerationType.AUTO)  //자동번호 생성
    private Long id;      // 상품 코드

    @Column(nullable = false, length = 50) // not null 설정 및 길이 지정, nullable = false : not null
    private String title; //상품명

    @Column(name="price", nullable = false)
    private int price;    //가격

    private String genre;      // 장르
    private String developer;  // 개발사

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate relaseyear; // 발매일

    @Lob //BLOB, CLOB 타입 매핑
    // CLOB : 사이즈가 큰 테이터를 외부 파일로 저장하기 위한 데이터 타입 (문자형 대용량 파일 저장)
    // BLOB : 바이너리 데이터를 DB외부에 저장하기 위한 타입 (이미지, 사운드, 비디오 : 멀티미디어)
    @Column(nullable = false)
    private String detail; // 상세 설명

    @Enumerated(EnumType.STRING)  // enum 타입 매핑
    private ItemSellStatus itemSellStatus; //상품 판매 상태

/*    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }*/

}