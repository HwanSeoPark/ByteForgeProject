package com.byteforge.post.attachment.domain;

import com.byteforge.post.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long attachmentId;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Post post;
	
	@Column(nullable = false , length = 50)
	private String realFileName;

	@Column(nullable = false , length = 50)
	private String uuidFileName;

	@Column(nullable = false , length = 100)
	private String s3Url;

	@Column(nullable = false)
	private long fileSize;

	public static Attachment createAttachment(String uuid, String s3Url , MultipartFile multipartFile) {
		return Attachment.builder()
				.realFileName(multipartFile.getOriginalFilename())
				.uuidFileName(uuid)
				.s3Url(s3Url)
				.fileSize(multipartFile.getSize())
				.build();
	}
}
