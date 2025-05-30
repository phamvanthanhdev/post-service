package com.demo.deploy.service;

import com.demo.deploy.mapper.PostMapper;
import com.demo.deploy.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostMapper postMapper;

    public void createPost(Post post) {
        postMapper.insertPost(post);
    }

    public List<Post> getAllPosts() {
        return postMapper.selectAllPosts();
    }

    public Post getPostById(Long id) {
        return postMapper.selectPostById(id);
    }

    public void updatePost(Post post) {
        postMapper.updatePost(post);
    }

    public void deletePost(Long id) {
        postMapper.deletePost(id);
    }
}
