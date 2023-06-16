package c6.goblogbackend.blogs.repository;

import c6.goblogbackend.blogs.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @NonNull
    Optional<Comment> findById(Integer id);
    @Query(value="SELECT * FROM comments WHERE post=?1", nativeQuery = true)
    List<Comment> findByPostId(@NonNull Integer postId);

    void deleteById(@NonNull Integer id);
}


