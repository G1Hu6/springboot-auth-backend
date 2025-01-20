package com.security.services.impl;

import com.security.dto.PostDto;
import com.security.entities.PostEntity;
import com.security.entities.UserEntity;
import com.security.exceptions.ResourceNotFoundException;
import com.security.repositories.PostRepository;
import com.security.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PostDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postEntity -> modelMapper.map(postEntity, PostDto.class) )
                .toList();
    }

    @Override
    public PostDto getPostById(Long id) {
        // Logging current user with JWT
//        UserEntity user = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info("Current User : {}", user);
        return modelMapper.map(postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id : " + id)), PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto toUpdateDto, Long id) {
        PostEntity oldPost = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post not found with id : " + id));
        toUpdateDto.setId(id);
        modelMapper.map(toUpdateDto, oldPost);
        return modelMapper.map(postRepository.save(oldPost), PostDto.class);
    }

    @Override
    public PostDto insertNewPostByID(PostDto newPostDto) {

        // Get the current logged in user...
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        PostEntity newPostEntity = modelMapper.map(newPostDto, PostEntity.class);
        // Save the current author
        newPostEntity.setAuthor(currentUser);
        return modelMapper.map(postRepository.save(newPostEntity), PostDto.class);
    }
}
