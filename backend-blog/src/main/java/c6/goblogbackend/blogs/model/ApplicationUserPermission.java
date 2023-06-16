package c6.goblogbackend.blogs.model;



public enum ApplicationUserPermission {
    USER_READ("user:read"),
    USER_CREATE("user:create"),
    USER_UPDATE("user:update"),
    BLOG_READ("blog:read"),
    BLOG_CREATE("blog:create"),
    BLOG_UPDATE("blog:update"),
    BLOG_DELETE("blog:delete"),
    COMMENT_READ("comment:read"),
    COMMENT_CREATE("comment:create"),
    COMMENT_UPDATE("comment:update"),
    COMMENT_DELETE("comment:delete");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
