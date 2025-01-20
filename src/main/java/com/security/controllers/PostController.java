package com.security.controllers;

import com.security.dto.PostDto;
import com.security.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    //@Secured("ROLE_ADMIN")
    public List<PostDto> getAllPost(){
        return postService.getAllPosts();
    }

    //@PreAuthorize("hasAnyRole('ADMIN','USER') OR hasAuthority(POST_VIEW)")
    //@PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(path = "/{postId}")
    @PreAuthorize("@postSecurity.isOwnerOfPost(#postId)")
    public PostDto getPostById(@PathVariable Long postId){
        return postService.getPostById(postId);
    }

    @PostMapping
    public PostDto insertNewPost(@RequestBody PostDto postDto){
        return postService.insertNewPostByID(postDto);
    }

    @PutMapping(path = "/{postId}")
    public PostDto updatePostById(@RequestBody PostDto postDto, @PathVariable Long postId){
        return postService.updatePost(postDto, postId);
    }


}
