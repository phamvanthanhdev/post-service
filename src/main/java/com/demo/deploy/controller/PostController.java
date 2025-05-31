package com.demo.deploy.controller;

import com.demo.deploy.model.Post;
import com.demo.deploy.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String listPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
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
            postService.deletePost(id);
            redirectAttributes.addFlashAttribute("success", "Bài viết đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa bài viết!");
        }
        return "redirect:/posts";
    }

    @GetMapping("/posts/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Post post = postService.getPostById(id);
            if (post == null) {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bài viết!");
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
            postService.updatePost(post);
            redirectAttributes.addFlashAttribute("success", "Bài viết đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật bài viết!");
        }
        return "redirect:/posts";
    }


}
