package c6.goblogbackend.blogs.service.post;

import c6.goblogbackend.blogs.dto.CreatePostRequest;
import c6.goblogbackend.blogs.dto.GetPostResponse;
import c6.goblogbackend.blogs.dto.SearchPostRequest;
import c6.goblogbackend.blogs.dto.UpdatePostRequest;
import c6.goblogbackend.blogs.model.Post;
import c6.goblogbackend.blogs.repository.CommentRepository;
import c6.goblogbackend.blogs.repository.PostRepository;
import c6.goblogbackend.blogs.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Override
    public List<Post> findAll() {
        var sort = Sort.by(Sort.Direction.DESC, "timeCreated");
        return postRepository.findAll(sort);
    }

    @Override
    public List<Post> findByTitleIgnoreCase(SearchPostRequest request ) {
        return postRepository.findByTitleContainingIgnoreCase(request.getQueryTitle());
    }

    @Override
    public GetPostResponse findById(Integer id) {
        GetPostResponse res = new GetPostResponse();
        Optional<Post> post = postRepository.findById(id);

        res.setComments(commentRepository.findByPostId(id));
        if(post.isPresent()) res.setPost(post.get());
        return res;
    }

    @Override
    public CompletableFuture<Post> create(CreatePostRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            var user = userRepository.findById(request.getCreatorId()).orElseThrow();

            var post = Post.builder()
                    .creator(user)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .likes(0)
                    .timeCreated(new Date())
                    .build();

            postRepository.save(post);

            return post;
        }, executor);
    }

    @Override
    public CompletableFuture<Post> update(UpdatePostRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Post> post = postRepository.findById(request.getPostId());

            var res = post.get();
            res.setTitle(request.getTitle());
            res.setContent(request.getContent());
            res.setLikes(0);
            res.setTimeCreated(request.getTimeCreated());

            postRepository.save(res);

            return res;
        }, executor);
    }

    @Override
    public void delete(Integer id) {
        postRepository.deleteById(id);
    }

}
