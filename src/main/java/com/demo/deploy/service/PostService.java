package com.demo.deploy.service;

import com.demo.deploy.mapper.PostMapper;
import com.demo.deploy.model.Post;
import io.micrometer.common.util.StringUtils;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private AuthService authService;

    public void createPost(Post post) {
        String username = authService.getUserLogin();
        if (StringUtils.isEmpty(username)) {
            throw new IllegalStateException("User is not authenticated");
        }
        post.setCreatedBy(username);
        postMapper.insertPost(post);
    }

    public List<Post> getAllPosts() {
        return postMapper.selectAllPosts();
    }

    public Post getPostById(Long id) {
        return postMapper.selectPostById(id);
    }

    public int updatePost(Post post) {
        String username = authService.getUserLogin();
        if (StringUtils.isEmpty(username)) {
            throw new IllegalStateException("User is not authenticated");
        }
        post.setCreatedBy(username);
        return postMapper.updatePost(post);
    }

    public int deletePost(Long id) {
        String username = authService.getUserLogin();
        if (StringUtils.isEmpty(username)) {
            throw new IllegalStateException("User is not authenticated");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("username", username);
        return postMapper.deletePost(params);
    }

    public List<Post> getAllPostsUser() {
        String username = authService.getUserLogin();
        if (StringUtils.isEmpty(username)) {
            return Collections.emptyList();
        }
        return postMapper.selectAllPostsUser(username);
    }

    public int updatePostAdmin(Post post) {
        return postMapper.updatePostAdmin(post);
    }

    public int deletePostAdmin(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return postMapper.deletePost(params);
    }
}
