package com.no3.game.repository.search;

import com.no3.game.entity.QItem;
import com.no3.game.entity.QMember;
import com.no3.game.entity.QReview;
import com.no3.game.entity.Review;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchReviewRepositoryImpl extends QuerydslRepositorySupport implements SearchReviewRepository {

    public SearchReviewRepositoryImpl() {
        super(Review.class);
    }

    @Override
    public Review search1() {
        log.info("search1........................");

        QReview review = QReview.review;
        QItem item = QItem.item;
        QMember member = QMember.member;

        JPQLQuery<Review> jpqlQuery = from(review);
        jpqlQuery.leftJoin(member).on(review.member.eq(member));
        jpqlQuery.leftJoin(item).on(review.item.eq(item));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(review, member.email, item.title);
        tuple.groupBy(review);

        log.info("---------------------------");
        log.info(tuple);
        log.info("---------------------------");

        List<Tuple> result = tuple.fetch();

        log.info(result);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage.............................");

        QReview review = QReview.review;
        QItem item = QItem.item;
        QMember member = QMember.member;

        JPQLQuery<Review> jpqlQuery = from(review);
        jpqlQuery.leftJoin(member).on(review.member.eq(member));
        jpqlQuery.leftJoin(item).on(review.item.eq(item));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(review, member, item);

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = review.id.gt(0L);

        booleanBuilder.and(expression);

        if(type != null){
            String[] typeArr = type.split("");
            //검색 조건을 작성하기
            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t:typeArr) {
                switch (t){
                    case "t":
                        conditionBuilder.or(item.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(review.text.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }

        tuple.where(booleanBuilder);

        //order by
        Sort sort = pageable.getSort();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending()? Order.ASC: Order.DESC;
            String prop = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Review.class, "review");
            tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));

        });
        tuple.groupBy(review);

        //page 처리
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        List<Tuple> result = tuple.fetch();

        log.info(result);

        long count = tuple.fetchCount();

        log.info("COUNT: " +count);

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,
                count);
    }
}
