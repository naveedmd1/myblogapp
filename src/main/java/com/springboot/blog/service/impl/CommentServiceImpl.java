package com.springboot.blog.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Comment;
import com.springboot.blog.model.Post;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.vo.CommentDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CommentServiceImpl implements CommentService {
  @Autowired
  private CommentRepository commentRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public CommentDto createComment(long postId, CommentDto commentDto) {

    log.debug("Postid {} and commentdto is {}", postId, commentDto);
    Comment comment = this.mapToEntity(commentDto);
    Post post = getPost(postId);
    comment.setPost(post);
    Comment savedComment = commentRepository.save(comment);
    CommentDto savedCommentDto = mapToDto(savedComment);
    log.debug("Returning Comment Dto {}", commentDto);
    return savedCommentDto;
  }

  public CommentDto mapToDto(Comment comment) {

    log.debug("Comment  is {}", comment);
    CommentDto commentDto = modelMapper.map(comment, CommentDto.class);
    log.debug("Returning commentdto is {}", commentDto);
    return commentDto;
  }

  public Comment mapToEntity(CommentDto commentDto) {

    log.debug("Commentdto is {}", commentDto);
    Comment comment = modelMapper.map(commentDto, Comment.class);
    log.debug("Returning comment object {}", comment);
    return comment;
  }

  @Override
  public List<CommentDto> getCommentsByPostId(long postId) {
    // retrieve comments by postId
    List<Comment> comments = commentRepository.findByPostId(postId);

    // convert list of comment entities to list of comment dto's
    List<CommentDto> commentDtos = comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    return commentDtos;
  }

  @Override
  public CommentDto getCommentById(Long postId, Long commentId) {

    log.debug("Postid and commentid is {} {}", postId, commentId);
    Post post = this.getPost(postId);
    Comment comment = getComment(commentId);
    isCommentBelongsToPost(comment, post);
    return this.mapToDto(comment);
  }

  @Override
  public CommentDto updateComment(Long postId, long commentId, CommentDto commentRequest) {

    log.debug("Postid {},commentid {} and commentdto is {}",postId,commentId,commentRequest);
    final Post post = this.getPost(postId);
    final Comment comment = this.getComment(commentId);
    this.isCommentBelongsToPost(comment, post);
    comment.setName(commentRequest.getName());
    comment.setEmail(commentRequest.getEmail());
    comment.setBody(commentRequest.getBody());
    Comment updatedComment = commentRepository.save(comment);
    return mapToDto(updatedComment);
  }

  @Override
  public void deleteCommentById(Long postId, Long commentId) {

    log.debug("Postid and commentid is {} {}", postId, commentId);
    Post post = this.postRepository.findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));
    Comment comment = this.getComment(commentId);
    isCommentBelongsToPost(comment, post);
    commentRepository.delete(comment);
  }

  public Post getPost(Long postId) {

    Post post = this.postRepository.findById(postId)
        .orElseThrow(() -> new ResourceNotFoundException("post", "id", postId));
    return post;
  }

  public Comment getComment(Long commentId) {
    Comment comment = this.commentRepository.findById(commentId)
        .orElseThrow(() -> new ResourceNotFoundException("Comment", "Commentid", commentId));
    return comment;
  }

  public boolean isCommentBelongsToPost(Comment comment, Post post) {
    if (!comment.getPost().getId().equals(post.getId())) {
      throw new BlogApiException(HttpStatus.BAD_REQUEST, "comment does not belong to post");
    }
    return true;
  }
}
