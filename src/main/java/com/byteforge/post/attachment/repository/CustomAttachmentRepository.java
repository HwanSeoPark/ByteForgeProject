package com.byteforge.post.attachment.repository;

import com.byteforge.post.attachment.dto.AttachmentResponse;

import java.util.List;

public interface CustomAttachmentRepository {
    List<AttachmentResponse> findAttachmentsByPostId(long postId);

}
