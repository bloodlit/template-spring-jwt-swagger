package ru.vssoft.backend.model.types;

import org.springframework.security.core.GrantedAuthority;

public enum AuthorityType implements GrantedAuthority {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_MANAGER;

    @Override
    public String getAuthority() {
        return name();
    }
}
