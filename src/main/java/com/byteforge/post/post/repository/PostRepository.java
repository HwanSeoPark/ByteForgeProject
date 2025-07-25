package com.byteforge.post.post.repository;

import com.byteforge.post.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> , CustomPostRepository {
	void deletePostByPostId(Long postId);

}