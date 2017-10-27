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
    List<Post> findAllByActiveIsTrueAndCategoryOrderByCreatedDesc(String categoryName);
    List<Post> findAllByActiveIsTrueAndTitleContainsIgnoreCase(String searchString);
    List<Post> findAllByCategoryAndTitleContainsIgnoreCase(String categoryName, String searchString);
    List<Post> findAllByAuthorOrderByCreatedDesc(User username);
    List<Post> findAllByActiveIsTrueOrderByCreatedDesc();
    List<Post> findAllByActiveIsFalseOrderByCreatedDesc();
    List<Post> findAllByCompletedIsTrueOrderByCreatedDesc();
    List<Post> findAllByAuthorAndActiveIsTrueOrderByConsiderationsDesc(User username);
    List<Post> findAllByAuthorAndCompletedIsTrueOrderByCreatedDesc(User username);
    List<Post> findAllByRecipientOrderByCreatedDesc(User username);
    List<Post> findAllByFlaggedTrueOrderByCreated();
}