package com.no3.game.repository;

import com.no3.game.entity.Item;
import com.no3.game.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByItem(Item item); // 아이템 먼저 한 다음에 수정할 예정
}
