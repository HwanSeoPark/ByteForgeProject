package com.byteforge.post.post.dto;

import com.byteforge.post.attachment.dto.AttachmentResponse;
import com.byteforge.post.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder
@Getter
public class PostResponse {
    private long postId;
    private String title;
    private String content;
    private int views;
    private int likes;
    private boolean isPrivate;
    private boolean isBlockComment;
    private String writer;
    private String writerImage;
    private boolean writerIsDelete;
    @JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:MM:SS" , timezone = "Asia/Seoul")
    private Date postDate;

    @Builder.Default
    List<String> tags = new ArrayList<>();

    @Setter
    @Builder.Default
    List<AttachmentResponse> attachment = new ArrayList<>();

    public static PostResponse createPostResponse(Post post) {
        return PostResponse.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .views(post.getViews())
                .likes(post.getLikes())
                .isPrivate(post.isPrivate())
                .isBlockComment(post.isBlockComment())
                .writer(post.getWriter().getId())
                .writerImage(post.getWriter().getProfileImage())
                .writerIsDelete(post.getWriter().isDelete())
                .postDate(post.getPostDate())
                .build();
    }

    public void addTagData(List<String> tags) {
        this.tags = tags;
    }

}
