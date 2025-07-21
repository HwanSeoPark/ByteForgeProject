package com.byteforge.post.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostListResponse {
	private long numbers;
	private String title;
	private String writer;
	private String writerImage;
	private boolean writerIsDelete;
	@JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd" , timezone = "Asia/Seoul")
	private Date postDate;
	private int likes;
	private int views;
	private long count;
}
