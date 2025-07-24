package com.byteforge.post.post.domain;

import com.byteforge.account.user.domain.User;
import com.byteforge.common.config.BooleanConverter;
import com.byteforge.post.attachment.domain.Attachment;
import com.byteforge.post.comment.domain.Comment;
import com.byteforge.post.post.dto.PostRequest;
import com.byteforge.post.tag.domain.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "NUMBERS_SEQUENCE", sequenceName = "ID_numbers", initialValue = 1, allocationSize = 1)
@JsonIdentityInfo(generator = IntSequenceGenerator.class, property = "id")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private int views;

    @Column(nullable = false)
    private int likes;

    @Column(nullable = false)
    @Convert(converter = BooleanConverter.class)
    private boolean isPrivate;

    @Column(nullable = false)
    @Convert(converter = BooleanConverter.class)
    private boolean isBlockComment;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date postDate;

    @Column(nullable = false , length = 1100)
    private String content;

    @Column(nullable = false)
    @Convert(converter = BooleanConverter.class)
    private boolean isDelete;

    @Builder.Default
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer = new User();

    @Builder.Default
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<User> recommendation = new ArrayList<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Tag> tag = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comment = new ArrayList<>();

    public static Post createPost(PostRequest postRequest) {
        return Post.builder()
                .content(postRequest.getContent())
                .title(postRequest.getTitle())
                .isPrivate(postRequest.isPrivatePost())
                .isBlockComment(postRequest.isBlockComment())
                .isDelete(false)
                .build();
    }

    public void updatePost(PostRequest postRequest) {
        this.content = postRequest.getContent();
        this.title = postRequest.getTitle();
        this.isPrivate = postRequest.isPrivatePost();
        this.isBlockComment = postRequest.isBlockComment();

        this.tag.clear();
        addTagFromTagList(postRequest.getTags());
    }

    public void updateLike() {
        this.likes += 1;
    }

    public void deletePost() {
        this.isDelete = true;
    }

    public void addFreeAttach(Attachment attach) {
        attachments.add(attach);
        attach.setPost(this);
    }

    public void addFreeCommit(Comment commit) {
        comment.add(commit);
        commit.setPost(this);
    }

    public void addTagFromTagList(String[] tagList) {
        for(String tag : tagList) {
            Tag tagEntity = Tag.createFreeTag(tag);
            this.tag.add(tagEntity);
        }
    }

    public void addRecommendation(User user) {
        this.recommendation.add(user);
        updateLike();
    }
}
