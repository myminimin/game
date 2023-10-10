package com.no3.game.repository;

import com.no3.game.entity.Item;
import com.no3.game.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i, ii, avg(coalesce(r.grade,0)),  count(distinct r) from Item i " +
            "left outer join ItemImg ii on ii.item = i " +
            "left outer join Review r on r.item = i group by i ")
    Page<Object[]> getListPage(Pageable pageable); // 페이징 처리

    @Query("select i, ii, avg(coalesce(r.grade,0)),  count(r)" +
            " from Item i left outer join ItemImg ii on ii.item = i " +
            " left outer join Review r on r.item = i "+
            " where i.id = :id group by ii")
    List<Object[]> getItemWithAll(Long id); // 특정 아이템 조회

    Optional<Item> findByTitle(@Param("title") String title);

}
