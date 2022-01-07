package com.springboot.blog.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class JWTAuthResponse {

  private String accessToken;
  private String tokenType = "Bearer";

  public JWTAuthResponse(String accessToken) {
    this.accessToken = accessToken;
  }
}
