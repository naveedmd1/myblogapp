package com.springboot.blog.repository;

import java.util.List;
import com.springboot.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>
{
    List<Comment> findByPostId(final Long PostId);
}
