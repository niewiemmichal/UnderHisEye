package pl.niewiemmichal.underhiseye.entities;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@RequiredArgsConstructor
public class User implements UserDetails {

    @Id
    @NonNull
    private final String username;

    @NonNull
    private final String password;

    @ElementCollection
    @NonNull
    private final Set<Role> role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.stream().map(r -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
