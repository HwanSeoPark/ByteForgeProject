package com.byteforge.post.post.repository;

import com.byteforge.account.user.domain.User;
import com.byteforge.post.post.domain.Post;
import com.byteforge.post.post.dto.PostListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomPostRepository {
	Optional<User> findRecommendationFromPost(Long postId, String userId);

	List<PostListResponse> findPostBySearch(Pageable pageable, String content);

	List<PostListResponse> findPostByTag(Pageable pageable, String tagData);

	List<PostListResponse> getPostList(Pageable pageable);

	long getTotalNumberOfPosts();

	long getTotalNumberOfTagSearchPosts(String tagData);

	long getTotalNumberOfSearchPosts(String search);

	Optional<Post> findPostByPostId(long postId);

	List<String> findTagsInPostId(long postId);

	void updatePostView(long postId);

	List<Long> findPostIdByUserId(String userId);

}
