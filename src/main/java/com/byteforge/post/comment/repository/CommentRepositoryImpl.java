package com.byteforge.post.comment.repository;

import com.byteforge.account.user.domain.QUser;
import com.byteforge.account.user.domain.User;
import com.byteforge.post.comment.domain.Comment;
import com.byteforge.post.comment.dto.CommentResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.byteforge.account.user.domain.QUser.user;
import static com.byteforge.post.comment.domain.QComment.comment;
import static com.byteforge.post.post.domain.QPost.post;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Comment> findCommentByCommentIdAndUserId(long commentId, String userId) {
        Comment result = queryFactory.select(comment)
                .from(comment)
                .innerJoin(comment.writer , user).on(user.id.eq(userId))
                .where(comment.commentId.eq(commentId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public List<CommentResponse> findCommentByPostId(long postId) {
        return queryFactory.select(Projections.constructor(CommentResponse.class ,
                       post.postId , comment.commentId , comment.content , comment.postDate , user.id , user.profileImage , user.isDelete))
                .from(comment)
                .innerJoin(comment.writer , user)
                .innerJoin(comment.post , post).on(post.postId.eq(postId))
                .where(comment.isDelete.eq(false))
                .fetch();
    }

    @Override
    public List<CommentResponse> findCommentByCommentPostWithoutMe(User userData) {
        QUser subUser = new QUser("subUser");

        return queryFactory.select(Projections.constructor(CommentResponse.class ,
                       post.postId , comment.commentId , comment.content , comment.postDate , user.id , user.profileImage , user.isDelete))
                .from(comment)
                .innerJoin(comment.writer , user).on(user.ne(userData))
                .innerJoin(comment.post , post)
                .on(post.eqAny(JPAExpressions.select(post)
                        .from(post)
                        .innerJoin(post.writer , subUser).on(subUser.eq(userData))
                        .where(post.isDelete.eq(false))
                ))
                .orderBy(comment.commentId.desc())
                .where(comment.isDelete.eq(false))
                .limit(10)
                .fetch();
    }
}
