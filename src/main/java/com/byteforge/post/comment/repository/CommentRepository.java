package com.byteforge.post.comment.repository;

import com.byteforge.post.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> , CustomCommentRepository {

}
