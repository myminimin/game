package com.no3.game.repository;

import com.no3.game.entity.Item;
import com.no3.game.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    Optional<Item> findByTitle(@Param("title") String itemNm); // Review에서 사용

    // List<Item> findByTitle(String itemNm); 

    List<Item> findByTitleOrDetail(String itemNm, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    @Query("select i from Item i where i.detail like %:detail% order by i.price desc")
    List<Item> findByDetail(@Param("detail") String itemDetail);
    //@Param파라미터로 넘어온 값을 JPQL에 들어갈 변수로 지정해 줄 수 있음
    //현재는 itemDetail 변수를 like% ~ % 사이에 :itemDetail 값이 들어가도록 작성

    @Query(value = "select * from item i where i.detail like %:detail% order by i.price desc", nativeQuery = true)
    List<Item> findByDetailByNative(@Param("detail") String itemDetail);


    /*@Query("select i, ii, avg(coalesce(r.grade,0)),  count(distinct r) from Item i " +
            "left outer join ItemImg ii on ii.item = i " +
            "left outer join Review r on r.item = i group by i ")
    Page<Object[]> getListPage(Pageable pageable); // 페이징 처리

    @Query("select i, ii, avg(coalesce(r.grade,0)),  count(r)" +
            " from Item i left outer join ItemImg ii on ii.item = i " +
            " left outer join Review r on r.item = i "+
            " where i.id = :id group by ii")
    List<Object[]> getItemWithAll(Long id); // 특정 아이템 조회*/


}
