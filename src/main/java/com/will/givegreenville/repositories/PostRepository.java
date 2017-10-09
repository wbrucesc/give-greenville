package com.will.givegreenville.repositories;

import com.will.givegreenville.models.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAllByCategory(String categoryName);
}
