package com.demo.deploy.mapper;

import com.demo.deploy.model.Post;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PostMapper {
    void insertPost(Post post);
    List<Post> selectAllPosts();
    Post selectPostById(Long id);
    void updatePost(Post post);
    void deletePost(Long id);
}
