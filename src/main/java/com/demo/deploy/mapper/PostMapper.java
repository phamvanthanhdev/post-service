package com.demo.deploy.mapper;

import com.demo.deploy.model.Post;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {
    void insertPost(Post post);
    List<Post> selectAllPosts();
    Post selectPostById(Long id);
    int updatePost(Post post);
    int deletePost(Map<String, Object> params);
    List<Post> selectAllPostsUser(String username);
    int updatePostAdmin(Post post);

}
