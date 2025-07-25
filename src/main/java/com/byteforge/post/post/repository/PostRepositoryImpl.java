package com.byteforge.post.post.repository;

import com.byteforge.account.user.domain.User;
import com.byteforge.post.post.domain.Post;
import com.byteforge.post.post.dto.PostListResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.byteforge.account.user.domain.QUser.user;
import static com.byteforge.post.comment.domain.QComment.comment;
import static com.byteforge.post.post.domain.QPost.post;
import static com.byteforge.post.tag.domain.QTag.tag;


@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository {

	private final JPAQueryFactory queryFactory;
	
	@Override
	public Optional<User> findRecommendationFromPost(Long postId , String userId) {
		User result = queryFactory.select(user)
				.from(post)
				.innerJoin(post.recommendation , user).on(user.id.eq(userId))
				.where(post.postId.eq(postId))
				.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public List<PostListResponse> findPostBySearch(Pageable pageable , String content) {
		return queryFactory.select(Projections.constructor(PostListResponse.class , post.postId , post.title
				, user.id , user.profileImage , user.isDelete , post.postDate , post.likes , post.views , comment.count()))
				.from(post)
				.innerJoin(post.writer , user).on(post.writer.eq(user))
				.leftJoin(post.comment , comment).on(comment.post.eq(post))
				.where(post.content.contains(content).or(post.title.contains(content)).and(post.isDelete.eq(false)))
				.groupBy(post.postId)
				.orderBy(post.postId.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
	}

	@Override
	public List<PostListResponse> findPostByTag(Pageable pageable , String tagData) {
		return queryFactory.select(Projections.constructor(PostListResponse.class , post.postId , post.title ,
						user.id , user.profileImage , user.isDelete , post.postDate , post.likes , post.views , comment.count()))
				.from(post)
				.innerJoin(post.tag , tag).on(tag.tagData.eq(tagData))
				.innerJoin(post.writer , user).on(post.writer.eq(user))
				.leftJoin(post.comment , comment).on(comment.post.eq(post))
				.groupBy(post.postId)
				.orderBy(post.postId.desc())
				.where(post.isDelete.eq(false))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
	}

	@Override
	public Optional<Post> findPostByPostId(long postId) {
		Post result = queryFactory.select(post)
				.from(post)
				.join(post.writer , user).fetchJoin()
				.where(post.postId.eq(postId))
				.fetchOne();

		return Optional.ofNullable(result);
	}

	@Override
	public List<PostListResponse> getPostList(Pageable pageable) {
		return queryFactory.select(Projections.constructor(PostListResponse.class , post.postId
						, post.title , user.id , user.profileImage , user.isDelete , post.postDate , post.likes
						, post.views , comment.count()))
				.from(post)
				.innerJoin(post.writer , user).on(post.writer.eq(user))
				.leftJoin(post.comment , comment).on(comment.post.eq(post))
				.groupBy(post.postId)
				.where(post.isDelete.eq(false))
				.orderBy(post.postId.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();
	}

	@Override
	public long getTotalNumberOfPosts() {
		return queryFactory.select(post.count())
				.from(post)
				.where(post.isDelete.eq(false))
				.fetchOne();
	}

	@Override
	public long getTotalNumberOfTagSearchPosts(String tagData) {
		return queryFactory.select(post.count())
				.from(post)
				.innerJoin(post.tag , tag).on(tag.tagData.eq(tagData))
				.where(post.isDelete.eq(false))
				.fetchOne();
	}

	@Override
	public long getTotalNumberOfSearchPosts(String search) {
		return queryFactory.select(post.count())
				.from(post)
				.where(post.title.contains(search).or(post.content.contains(search)).and(post.isDelete.eq(false)))
				.fetchOne();
	}

	@Override
	public List<String> findTagsInPostId(long postId) {
		return queryFactory.select(tag.tagData)
				.from(tag)
				.leftJoin(post).on(post.tag.contains(tag))
				.where(post.postId.eq(postId).and(post.isDelete.eq(false)))
				.fetch();
	}

	@Override
	public void updatePostView(long postId) {
		queryFactory.update(post)
				.set(post.views , post.views.add(1))
				.where(post.postId.eq(postId))
				.execute();
	}

	@Override
	public List<Long> findPostIdByUserId(String userId) {
		return queryFactory.select(post.postId)
				.from(post)
				.innerJoin(post.writer , user).on(user.id.eq(userId))
				.where(post.isDelete.eq(false))
				.fetch();
	}

}
