package com.byteforge.post.comment.repository;

import com.byteforge.account.user.domain.User;
import com.byteforge.post.comment.domain.Comment;
import com.byteforge.post.comment.dto.CommentResponse;

import java.util.List;
import java.util.Optional;

public interface CustomCommentRepository {

    Optional<Comment> findCommentByCommentIdAndUserId(long commentId , String userId);

    List<CommentResponse> findCommentByPostId(long postId);

    List<CommentResponse> findCommentByCommentPostWithoutMe(User user);
}
