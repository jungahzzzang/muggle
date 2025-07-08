package com.curty.muggle.post.repository;

import com.curty.muggle.member.entity.Member;
import com.curty.muggle.post.dto.response.PostResponse;
import com.curty.muggle.post.entity.Post;
import com.curty.muggle.post.entity.PostCategory;
import com.curty.muggle.post.entity.PostSortCondition;
import com.curty.muggle.post.entity.PostState;
import com.curty.muggle.postcomment.entity.PostComment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.hibernate.Session;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PostCustomRepositoryImpl implements PostCustomRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<PostResponse> getAll(
            Pageable pageable,
            PostSortCondition sort,
            PostCategory category,
            String keyword
    ) {
        HibernateCriteriaBuilder cb = em.unwrap(Session.class).getCriteriaBuilder(); // Criteria 쿼리 작성을 위한 빌더 객체

        // 메인 쿼리
        JpaCriteriaQuery<PostResponse> query = cb.createQuery(PostResponse.class);

        Root<Post> post = query.from(Post.class); // 루트 엔티티
        Join<Post, Member> member = post.join("member", JoinType.INNER); // INNER 조인 엔티티

        // 댓글 개수 카운팅을 위한 서브쿼리
        Subquery<Long> commentCountSubquery = query.subquery(Long.class);
        Root<PostComment> comment = commentCountSubquery.from(PostComment.class); // 서브쿼리의 루트 엔티티

        commentCountSubquery.select(cb.count(comment));
        commentCountSubquery.where(cb.equal(comment.get("post").get("postId"), post.get("postId")));

        // 메인 쿼리문 작성 시작
        query.select(
                cb.construct(
                        PostResponse.class,
                        post.get("postId"),
                        member.get("memberId"),
                        post.get("state"),
                        post.get("category"),
                        post.get("title"),
                        post.get("content"),
                        member.get("nickname"),
                        post.get("likeCount"),
                        post.get("viewCount"),
                        post.get("createdAt"),
                        cb.coalesce(commentCountSubquery, 0L)
                )
        );

        // where
        List<Predicate> predicates = new ArrayList<>();
        if (category != null) {
            predicates.add(cb.equal(post.get("category"), category));
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            predicates.add(
                    cb.and(
                            cb.or(
                                    cb.like(post.get("title"), "%" + keyword.trim() + "%"),
                                    cb.like(post.get("content"), "%" + keyword.trim() + "%")
                            ),
                            cb.notEqual(post.get("state"), PostState.DELETED)
                    )
            );
        }

        if (!predicates.isEmpty()) {
            query.where(predicates.toArray(new Predicate[0])); // 모든 Predicate를 AND 조건으로 묶어 쿼리에 적용
        }

        // order-by
        Order orderCondition = cb.desc(post.get("createdAt"));

        switch (sort) {
            case LATEST -> orderCondition = cb.desc(post.get("postId"));
            case OLDEST -> orderCondition = cb.asc(post.get("postId"));
            case MOST_VIEWED -> orderCondition = cb.desc(post.get("viewCount"));
            case MOST_LIKED -> orderCondition = cb.desc(post.get("likeCount"));
        }

        query.orderBy(orderCondition, cb.desc(post.get("postId")));

        // 쿼리 실행
        TypedQuery<PostResponse> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<PostResponse> contents = typedQuery.getResultList();

        // 총 개수 조회
        Long total = em.createQuery(query.createCountQuery()).getSingleResult();

        // 페이징 처리
        return new PageImpl<>(contents, pageable, (total != null) ? total : 0L);
    }

}
