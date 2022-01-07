package com.springboot.blog.exception;

import org.springframework.http.HttpStatus;

public class BlogApiException extends RuntimeException {
  private static final long serialVersionUID = -5365118209700059011L;
  private HttpStatus httpStatus;
  private String errorMessage;

  public BlogApiException(final HttpStatus httpStatus, final String errorMessage) {
    super(errorMessage);
    this.httpStatus = httpStatus;
    this.errorMessage = errorMessage;
  }

  public HttpStatus getHttpStatus() {
    return this.httpStatus;
  }

  public String getErrorMessage() {
    return this.errorMessage;
  }
}
