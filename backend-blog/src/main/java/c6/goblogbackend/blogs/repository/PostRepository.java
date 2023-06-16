package c6.goblogbackend.blogs.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import c6.goblogbackend.blogs.model.Post;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query(value="SELECT * FROM posts WHERE title ILIKE %?% ORDER BY time_created DESC", nativeQuery = true)
    List<Post> findByTitleContainingIgnoreCase(@NonNull String title);

    @NonNull
    Optional<Post> findById(Integer id);
    void deleteById(@NonNull Integer id);
}
