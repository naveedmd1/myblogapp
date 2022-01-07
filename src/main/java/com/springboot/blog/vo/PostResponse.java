package com.springboot.blog.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

  private List<PostDto> content;

  private int pageNo;

  private int pageSize;

  private int totalPages;

  private long totalElements;

  private boolean last;

}
