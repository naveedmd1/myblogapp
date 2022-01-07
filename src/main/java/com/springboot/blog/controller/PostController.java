package com.springboot.blog.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.blog.service.PostService;
import com.springboot.blog.vo.PostDto;
import com.springboot.blog.vo.PostResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Api("CRUD Rest APIs for Post resources")
@RestController
@RequestMapping
public class PostController {

  @Autowired
  private PostService postService;

  @ApiOperation("Create Post REST API")
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping({ "/api/v1/posts" })
  public ResponseEntity<PostDto> createNewPost(@Valid @RequestBody final PostDto postDto) {

    log.info("Post Dto is {}", postDto);
    final PostDto postDtoResponse = this.postService.createPost(postDto);
    log.info("Postdto Response is {}", postDtoResponse);
    return new ResponseEntity<PostDto>(postDtoResponse, HttpStatus.CREATED);
  }

  @ApiOperation("Get All Posts REST API")
  @GetMapping({ "/api/v1/posts" })
  public ResponseEntity<PostResponse> getAllposts(
      @RequestParam(required = false, value = "pageNo", defaultValue = "0") final int pageNo,
      @RequestParam(required = false, value = "pageSize", defaultValue = "10") final int pageSize,
      @RequestParam(required = false, value = "sortBy", defaultValue = "id") final String sortBy,
      @RequestParam(required = false, value = "sortDir", defaultValue = "asc") final String sortDir) {

    final PostResponse postResponse = this.postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
    log.info("Returning list of post dto {}", postResponse);
    return new ResponseEntity<PostResponse>(postResponse, HttpStatus.OK);
  }

  @ApiOperation("Get Post By Id REST API")
  @GetMapping({ "/api/v1/posts/{id}" })
  public ResponseEntity<PostDto> getPostById(@PathVariable(name = "id") final Long id) {

    log.info("Id is {}", id);
    PostDto postDto = this.postService.getPostById(id);
    log.info("Returning post dto {}", postDto);
    return new ResponseEntity<PostDto>(postDto, HttpStatus.OK);
  }

  @ApiOperation("Update Post By Id REST API")
  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping({ "/api/v1/posts/{id}" })
  public ResponseEntity<PostDto> updatePostById(@Valid @RequestBody final PostDto postDto,
      @PathVariable(name = "id") final Long id) {

    log.info("Post dto {}and id is {}", postDto, id);
    PostDto updatedPostDto = this.postService.updatePost(postDto, id);
    log.info("Returning updated post dto {}", updatedPostDto);
    return new ResponseEntity<PostDto>(updatedPostDto, HttpStatus.OK);
  }

  @ApiOperation("Delete Post By Id REST API")
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping({ "/api/v1/posts/{id}" })
  public ResponseEntity<String> deleteById(@PathVariable(name = "id") final Long id) {

    PostController.log.info("Id is {}", id);
    this.postService.deletePostById(id);
    return new ResponseEntity<String>("Post deleted Successfully", HttpStatus.OK);
  }
}
