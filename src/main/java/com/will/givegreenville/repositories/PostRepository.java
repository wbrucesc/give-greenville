package com.will.givegreenville.repositories;

import com.will.givegreenville.models.Post;
import com.will.givegreenville.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAllByCategory(String categoryName);
    List<Post> OrderByCreatedDesc();
    List<Post> findAllByCategoryOrderByCreatedDesc(String categoryName);
    List<Post> findAllByTitleContainsIgnoreCase(String searchString);
    List<Post> findAllByCategoryAndTitleContainsIgnoreCase(String categoryName, String searchString);
}