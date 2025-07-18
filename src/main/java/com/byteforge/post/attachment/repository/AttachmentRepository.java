package com.byteforge.post.attachment.repository;

import com.byteforge.post.attachment.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> , CustomAttachmentRepository {

}
