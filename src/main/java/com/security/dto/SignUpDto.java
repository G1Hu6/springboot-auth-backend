package com.security.dto;


import com.security.entities.enums.Permission;
import com.security.entities.enums.Role;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    private String email;
    private String password;
    private String name;
    // Not good practice
    private Set<Role> roles;
    private Set<Permission> permissions;
}
