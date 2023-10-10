package com.no3.game.repository.search;

import com.no3.game.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchReviewRepository {

    Review search1();

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
