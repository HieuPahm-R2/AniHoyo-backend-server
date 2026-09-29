package com.HieuPahm.AniHoyo.config;

/**
 * Single source of truth for the paths that are reachable without a permission
 * row in the `permissions` table.
 *
 * <p>
 * Two layers used to keep their own copy of this list:
 * {@link SecurityConfiguration} (Spring Security filter chain) and
 * {@link InterceptorConfiguration} (the {@link AuthorityIntercepter} ACL). The two
 * copies drifted: {@code POST /api/v1/{id}/view} and the HLS playlists are
 * {@code permitAll} in the filter chain but were missing from the interceptor
 * whitelist, so a <em>logged-in</em> visitor got 403 where an anonymous one was
 * served — the API answered "you don't have permission" for a public endpoint.
 * Keeping one list prevents the drift from coming back.
 */
public final class PublicPaths {

    private PublicPaths() {
    }

    /**
     * Public whatever the HTTP method is: authentication endpoints, the WebSocket
     * handshake, notifications, locally served media and the API documentation.
     */
    public static final String[] PUBLIC = {
            "/",
            "/api/v1/",
            "/ws/**",
            "/api/v1/auth/**",
            "/api/v1/stream/range/**",
            "/api/v1/notifications/**",
            "/storage/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    /** View counter: {@code POST /api/v1/{seasonId}/view}. */
    public static final String[] PUBLIC_VIEW_TRACKING = {
            "/api/v1/*/view/**"
    };

    /** HLS playback (master playlist, quality playlists, segments). */
    public static final String[] PUBLIC_PLAYBACK = {
            "/api/v1/*/master.m3u8",
            "/api/v1/*/*.ts",
            "/api/v1/*/*/index.m3u8",
            "/api/v1/*/*/*.ts"
    };

    /** {@link #PUBLIC} + view tracking + playback, for the interceptor whitelist. */
    public static String[] all() {
        String[] all = new String[PUBLIC.length + PUBLIC_VIEW_TRACKING.length + PUBLIC_PLAYBACK.length];
        int at = 0;
        for (String[] group : new String[][] { PUBLIC, PUBLIC_VIEW_TRACKING, PUBLIC_PLAYBACK }) {
            System.arraycopy(group, 0, all, at, group.length);
            at += group.length;
        }
        return all;
    }
}
