package com.no3.game.repository;

import com.no3.game.entity.Item;
import com.no3.game.entity.Member;
import com.no3.game.entity.Review;
import com.no3.game.repository.search.SearchReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, SearchReviewRepository {

    @EntityGraph(attributePaths = {"member"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Review> findByItem(Item item);
    // Review 클래스의 Member에 대한 Fetch 방식이 LAZY이기 때문에 한 번에 Review와 Member 객체를 조회할 수 없어서
    // @EntityGraph를 이용해 Review 객체를 가져올 때 Member 객체를 로딩하게 함

    @Modifying
    @Query("delete from Review mr where mr.member = :member")
    void deleteByMember(Member member);

    @Query(value ="SELECT r, m, i FROM Review r LEFT JOIN r.member m LEFT JOIN r.item i ")
    Page<Object[]> getReviewWithAll(Pageable pageable);

    @Query(value ="SELECT r, m, i FROM Review r LEFT JOIN r.member m LEFT JOIN r.item i WHERE r.id = :id")
    Object getReviewById(@Param("id") Long id);
}
