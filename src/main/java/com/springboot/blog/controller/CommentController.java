package com.springboot.blog.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.service.CommentService;
import com.springboot.blog.vo.CommentDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Api("CRUD REST APIs for Comment Respource")
@RestController
@RequestMapping({ "/api/v1" })
public class CommentController {

  @Autowired
  private CommentService commentService;

  @ApiOperation("Create Comment REST API")
  @PostMapping({ "/posts/{postId}/comments" })
  public ResponseEntity<CommentDto> createComment(@PathVariable("postId") final Long postId,
      @Valid @RequestBody final CommentDto commentDto) {

    log.info("Postid and comment dto is {} {}", postId, commentDto);
    CommentDto savedCommentDto = commentService.createComment(postId, commentDto);
    log.info("Returning commentDto {}", savedCommentDto);
    return new ResponseEntity<CommentDto>(savedCommentDto, HttpStatus.CREATED);
  }

  @ApiOperation("Get All Comments By Post ID REST API")
  @GetMapping({ "/posts/{postId}/comments" })
  public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable("postId") final Long postId) {

    log.info("Post id is {}", postId);
    final List<CommentDto> commentDtos = commentService.getCommentsByPostId(postId);
    log.info("Returning list of comments by postid{}", commentDtos);
    return new ResponseEntity<List<CommentDto>>(commentDtos, HttpStatus.OK);
  }

  @ApiOperation("Get Single Comment By ID REST API")
  @GetMapping({ "/posts/{postId}/comments/{commentId}" })
  public ResponseEntity<CommentDto> getCommentByID(@PathVariable("postId") final Long postId,
      @PathVariable("commentId") final Long commentId) {

    log.info("postid and comment id is {} {} ", postId, commentId);
    final CommentDto commentDto = commentService.getCommentById(postId, commentId);
    log.info("Returning commentdto {}", commentDto);
    return new ResponseEntity<CommentDto>(commentDto, HttpStatus.OK);
  }

  @ApiOperation("Update Comment By ID REST API")
  @PutMapping({ "/posts/{postId}/comments/{commentId}" })
  public ResponseEntity<CommentDto> updateCommentById(@PathVariable("postId") final Long postId,
      @PathVariable("commentId") final Long commentId, @Valid @RequestBody final CommentDto commentDto) {

    log.info("Postid {},comment id {} and commentDto is {}", postId, commentId, commentDto);
    CommentDto updatedComment = commentService.updateComment(postId, commentId, commentDto);
    log.info("returning updated comment {}", updatedComment);
    return new ResponseEntity<CommentDto>(updatedComment, HttpStatus.OK);
  }

  @ApiOperation("Delete Comment By ID REST API")
  @DeleteMapping({ "/posts/{postId}/comments/{commentId}" })
  public ResponseEntity<String> deleteComment(@PathVariable("postId") final Long postId,
      @PathVariable("commentId") final Long commentId) {

    log.info("postid {} and commentid is {} ", postId, commentId);
    commentService.deleteCommentById(postId, commentId);
    log.info("comment deleted successfully");
    return new ResponseEntity<String>("Comment deleted Successfully", HttpStatus.OK);
  }

}
