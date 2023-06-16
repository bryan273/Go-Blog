package c6.goblogbackend.blogs.model;

import com.google.common.collect.Sets;
import java.util.Set;

import static c6.goblogbackend.blogs.model.ApplicationUserPermission.*;


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
}
