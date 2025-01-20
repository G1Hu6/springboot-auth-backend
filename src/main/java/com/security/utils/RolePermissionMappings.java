package com.security.utils;

import com.security.entities.enums.Permission;
import com.security.entities.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.security.entities.enums.Permission.*;
import static com.security.entities.enums.Role.*;

// Mapping for roles and permissions
public class RolePermissionMappings {

    private static final Map<Role, Set<Permission>> mappings = Map.of(
            USER, Set.of(USER_VIEW, POST_VIEW),
            CREATOR,Set.of(POST_CREATE, POST_UPDATE, USER_CREATE, USER_UPDATE),
            ADMIN, Set.of(USER_CREATE, USER_DELETE, POST_DELETE, POST_VIEW, POST_UPDATE)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role){
        return mappings.get(role).stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toSet());
    }
}
