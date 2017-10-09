package com.will.givegreenville.repositories;

import com.will.givegreenville.models.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    Post findAllByCategoryEquals(String categoryName);
}
