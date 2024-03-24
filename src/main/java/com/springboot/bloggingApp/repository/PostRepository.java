package com.springboot.bloggingApp.repository;

import com.springboot.bloggingApp.entities.Category;
import com.springboot.bloggingApp.entities.Post;
import com.springboot.bloggingApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUser(User user);

    List<Post> findByCategory(Category category);

    List<Post> findByTitleContaining(String title);
}
