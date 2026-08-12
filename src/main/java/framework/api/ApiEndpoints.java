package framework.api;

public class ApiEndpoints {
    public static final String LOGIN_ENDPOINT = "/auth/login";
    public static final String BOOKINGS_ENDPOINT = "/bookings";
    public static final String LOGOUT_ENDPOINT = "/auth/logout";
    public static final String USER_PROFILE_ENDPOINT = "/users/profile";
    public static final String USERS_ENDPOINT = "/users";
    public static final String USER_BY_ID_ENDPOINT = "/users/{id}";
    public static final String POSTS_ENDPOINT = "/posts";
    public static final String POSTS_BY_ID_ENDPOINT = "/posts/{id}";

    private ApiEndpoints() {
    }
}
