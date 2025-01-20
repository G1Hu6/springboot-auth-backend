package com.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;

    private String name;

    private String description;

    @JsonIgnore
    private UserEntity author;
}
