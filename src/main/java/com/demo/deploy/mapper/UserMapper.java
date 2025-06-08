package com.demo.deploy.mapper;

import com.demo.deploy.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "email", column = "email"),
            @Result(property = "password", column = "password"),
            @Result(property = "fullName", column = "full_name"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at"),
            @Result(property = "roles", column = "id",
                    many = @Many(select = "com.demo.deploy.mapper.RoleMapper.findByUserId"))
    })
    Optional<User> findByUsername(String username);

    @Select("SELECT * FROM users WHERE email = #{email}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "fullName", column = "full_name"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    Optional<User> findByEmail(String email);

    @Insert("INSERT INTO users (username, email, password, full_name, enabled) " +
            "VALUES (#{username}, #{email}, #{password}, #{fullName}, #{enabled})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Update("UPDATE users SET email = #{email}, full_name = #{fullName}, " +
            "enabled = #{enabled}, updated_at = CURRENT_TIMESTAMP WHERE id = #{id}")
    void update(User user);

    @Delete("DELETE FROM users WHERE id = #{id}")
    void delete(Long id);

    @Select("SELECT * FROM users")
    @Results({
            @Result(property = "fullName", column = "full_name"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    List<User> findAll();
}
