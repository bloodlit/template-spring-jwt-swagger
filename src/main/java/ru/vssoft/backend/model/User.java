package ru.vssoft.backend.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.vssoft.backend.model.types.AuthorityType;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Data
@Table(name = "usr")
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private boolean enabled;

    @ElementCollection(targetClass = AuthorityType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_authority",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "AUTHORITY_USER_FK"))
    )
    @Enumerated(EnumType.STRING)
    private Set<AuthorityType> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
