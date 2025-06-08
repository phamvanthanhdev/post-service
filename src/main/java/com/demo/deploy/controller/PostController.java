package com.demo.deploy.controller;

import com.demo.deploy.model.Post;
import com.demo.deploy.service.AuthService;
import com.demo.deploy.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    @GetMapping("/posts")
    public String listPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("username", authService.getUserLogin());
        return "post-list";
    }

    @GetMapping("/my-posts")
    public String listMyPosts(Model model) {
        List<Post> posts = postService.getAllPostsUser();
        model.addAttribute("posts", posts);
        model.addAttribute("username", authService.getUserLogin());
        return "post-list";
    }

    @GetMapping("/posts/create")
    public String showCreateForm(Model model) {
        model.addAttribute("post", new Post());
        return "create-post";
    }

    @PostMapping("/posts")
    public String createPost(@ModelAttribute Post post, RedirectAttributes redirectAttributes) {
        try {
            postService.createPost(post);
            redirectAttributes.addFlashAttribute("success", "Bài viết đã được tạo thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tạo bài viết!");
        }
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            int result = postService.deletePost(id);
            if (result <= 0) {
                redirectAttributes.addFlashAttribute("error", "Không thể xóa bài viết!");
                return "redirect:/posts";
            }
            redirectAttributes.addFlashAttribute("success", "Bài viết đã được xóa thành công!");
        } catch (Exception e) {
            System.out.println("Error deleting post: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa bài viết!");
        }
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Post post = postService.getPostById(id);
            if (Objects.isNull(post)) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bài viết!");
                return "redirect:/posts";
            }
            if (!authService.getUserLogin().equals(post.getCreatedBy())) {
                redirectAttributes.addFlashAttribute("error", "Không thể chỉnh sửa bài viết!");
                return "redirect:/posts";
            }
            model.addAttribute("post", post);
            return "edit-post";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi tải bài viết!");
            return "redirect:/posts";
        }
    }

    @PostMapping("/posts/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post, RedirectAttributes redirectAttributes) {
        try {
            post.setId(id);
            int result = postService.updatePost(post);
            if (result <= 0) {
                redirectAttributes.addFlashAttribute("error", "Không thể chỉnh sửa bài viết!");
                return "redirect:/posts";
            }
            redirectAttributes.addFlashAttribute("success", "Bài viết đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật bài viết!");
        }
        return "redirect:/posts";
    }


}
