package com.springboot.blog.vo;

import javax.validation.constraints.Size;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "Comment model information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

  @ApiModelProperty("Comment id")
  private Long id;

  @ApiModelProperty("Comment name")
  @NotEmpty(message = "name should not be null or empty")
  private String name;

  @ApiModelProperty("Comment email")
  @NotEmpty(message = "Email should not be null or empty")
  @Email
  private String email;

  @ApiModelProperty("Comment body")
  @NotEmpty
  @Size(min = 10, message = "email body should have atleast 10 characters")
  private String body;

}
