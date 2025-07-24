package com.byteforge.post.comment.service;

import com.byteforge.account.user.domain.User;
import com.byteforge.account.user.service.LoginService;
import com.byteforge.common.response.ResponseCode;
import com.byteforge.common.response.ResponseMessage;
import com.byteforge.common.response.message.CommentMessage;
import com.byteforge.post.comment.domain.Comment;
import com.byteforge.post.comment.dto.CommentRequest;
import com.byteforge.post.comment.dto.CommentResponse;
import com.byteforge.post.comment.exception.CommentException;
import com.byteforge.post.comment.repository.CommentRepository;
import com.byteforge.post.post.domain.Post;
import com.byteforge.post.post.service.PostService;
import com.byteforge.security.jwt.support.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostService postService;
    private final CommentRepository commentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final LoginService loginService;

    public ResponseMessage<List<Comment>> getCommit(Long postId) {
        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS , commentRepository.findCommentByPostId(postId));
    }

    @Transactional
    public ResponseMessage addComment(CommentRequest commentRequest , String token) {
        User user = loginService.findUserByAccessToken(token);
        Post post = postService.findPostById(commentRequest.getPostId());

        Comment comment = Comment.createComment(commentRequest , user);
        post.addFreeCommit(comment);

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    @Transactional
    public ResponseMessage deleteCommentByCommentId(long commentId , String token) {
        String userId = jwtTokenProvider.getUserPk(token);

        Comment comment = commentRepository.findCommentByCommentIdAndUserId(commentId , userId)
                .orElseThrow(() -> new CommentException(CommentMessage.ONLY_OWNER_CAN_DELETE));

        comment.deleteComment();

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS);
    }

    public ResponseMessage<List<CommentResponse>> getNotificationFromUser(String token) {
        User user = loginService.findUserByAccessToken(token);

        return ResponseMessage.of(ResponseCode.REQUEST_SUCCESS, commentRepository.findCommentByCommentPostWithoutMe(user));
    }
}