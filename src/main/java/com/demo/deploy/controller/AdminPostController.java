package com.demo.deploy.controller;

import com.demo.deploy.model.Post;
import com.demo.deploy.service.AuthService;
import com.demo.deploy.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin/posts")
public class AdminPostController {
    private final static String REDIRECT_POSTS = "redirect:/admin/posts";
    
    @Autowired
    private PostService postService;
    @Autowired
    private AuthService authService;
    @GetMapping("")
    public String adminPostList(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("username", authService.getUserLogin());
        return "admin/admin-post-list";
    }

    @GetMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            postService.deletePostAdmin(id);
            redirectAttributes.addFlashAttribute("success", "Bài viết đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa bài viết!");
        }
        return REDIRECT_POSTS;
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Post post = postService.getPostById(id);
            if (Objects.isNull(post)) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bài viết!");
                return REDIRECT_POSTS;
            }
            model.addAttribute("post", post);
            return "admin/admin-edit-post";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tải bài viết!");
            return REDIRECT_POSTS;
        }
    }

    @PostMapping("/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post, RedirectAttributes redirectAttributes) {
        try {
            postService.updatePostAdmin(post);
            redirectAttributes.addFlashAttribute("success", "Bài viết đã được cập nhật thành công!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật bài viết!");
        }
        return REDIRECT_POSTS;
    }
}
