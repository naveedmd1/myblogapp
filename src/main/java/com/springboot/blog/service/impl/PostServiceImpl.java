package com.springboot.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Post;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import com.springboot.blog.vo.PostDto;
import com.springboot.blog.vo.PostResponse;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PostServiceImpl implements PostService {

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private ModelMapper modelMapper;

  public PostDto createPost(PostDto postDto) {

    log.debug("The value of Postdto is {}", (Object) postDto);
    Post post = this.mapToEntity(postDto);
    log.debug("Post value is {}", post);
    Post newPost = postRepository.save(post);
    log.debug("The value of new post is {}", newPost);
    PostDto postResponse = mapToDto(newPost);
    log.debug("Returning postresponse {}", postResponse);
    return postResponse;
  }

  public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

    log.debug("Returning List of posts");

    Pageable pageable = PageRequest.of(pageNo, pageSize,Sort.by(Direction.fromString(sortDir),sortBy));

    Page<Post> pageposts = (Page<Post>) this.postRepository.findAll(pageable);

    List<Post> posts = (List<Post>) pageposts.getContent();

    List<PostDto> postDtos = posts.stream().map(post -> this.mapToDto(post)).collect((Collectors.toList()));
    log.debug((Object) postDtos);

    PostResponse postResponse = PostResponse.builder().content(postDtos).pageNo(pageposts.getNumber())
        .pageSize(pageposts.getSize()).totalElements(pageposts.getTotalElements()).totalPages(pageposts.getTotalPages())
        .last(pageposts.isLast()).build();

    log.debug("Post response is {}", postResponse);
    return postResponse;
  }

  private PostDto mapToDto(Post post) {

    log.debug("Post value is {}", post);
    PostDto postDto = modelMapper.map(post, PostDto.class);
    log.debug("Returning post dto {}", postDto);
    return postDto;
  }

  private Post mapToEntity(PostDto postDto) {

    log.debug("The value of Postdto is {}", postDto);
    Post post = modelMapper.map(postDto, Post.class);
    log.debug("Post value is {}", post);
    return post;
  }

  @Override
  public PostDto getPostById(long id) {
    log.debug("Id is {}", id);
    Post post = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
    log.debug("Post value is {}", post);
    PostDto postDto = this.mapToDto(post);
    log.debug("Returning post dto {}", postDto);
    return postDto;
  }

  @Override
  public PostDto updatePost(PostDto postDto, long id) {

    log.debug("Post dto and id is {} {}", postDto, id);
    return postRepository.findById(id).map(record -> {
      record.setTitle(postDto.getTitle());
      record.setDescription(postDto.getDescription());
      record.setContent(postDto.getContent());
      Post updatedPost = postRepository.save(record);
      log.debug("Updated post is {}", updatedPost);
      return this.mapToDto(updatedPost);
    }).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
  }

  @Override
  public void deletePostById(long id) {

    log.debug("Id is {}", id);
    Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post", "id", id));
    this.postRepository.delete(post);
  }

}
