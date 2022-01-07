package com.springboot.blog.vo;

import java.util.Set;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "Post model information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

  @ApiModelProperty("Blog post id")
  private Long id;

  @ApiModelProperty("Blog post title")
  @NotEmpty
  @Size(min = 5, message = "post title should have atleast 5 characters")
  private String title;

  @ApiModelProperty("Blog post description")
  @NotEmpty
  @Size(min = 10, message = "post description should have atleast 10 characters")
  private String description;

  @ApiModelProperty("Blog post description")
  @NotEmpty
  private String content;

  @ApiModelProperty("Blog post comments")
  private Set<CommentDto> comments;

}
