package c6.goblogbackend.auth.model;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static c6.goblogbackend.auth.model.ApplicationUserPermission.*;


public enum ApplicationUserRole {

    USER(Sets.newHashSet(
        USER_READ,
        USER_CREATE,
        USER_UPDATE,
        BLOG_READ,
        BLOG_CREATE,
        BLOG_UPDATE,
        BLOG_DELETE,
        COMMENT_READ,
        COMMENT_CREATE,
        COMMENT_UPDATE,
        COMMENT_DELETE));


    private final Set<ApplicationUserPermission> permissions;


    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthority() {
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+ this.name()));
        return authorities;
    }
}
