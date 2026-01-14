package com.example.app.security;
 
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
 
import java.io.IOException;
import java.util.List;
// import java.util.Collections;
 
@Component
public class JwtFilter extends OncePerRequestFilter {
 
    @Autowired
    private JwtUtil jwtUtil;

    // 1. Inject UserDetailsService
    @Autowired 
    private UserDetailsService userDetailsService;
 
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

            System.out.println("=== JwtFilter called ===");
 
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        boolean authenticated = false; // Add a flag for debugging
 
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
 
            try {
            
                if (jwtUtil.validateToken(token)) {
                    String userId = jwtUtil.extractUserId(token);

                    UserDetails userDetails =
                        userDetailsService.loadUserByUsername(userId);

                    // --- FIX HERE: Load UserDetails to get Authorities ---
                    //UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                            userDetails, // Use UserDetails as principal
                            userId,
                            null,
                            userDetails.getAuthorities() // Pass the actual authorities!
                        );
                    //authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    authenticated = true;
                } else {
                        System.err.println("JWT Filter: Token validation failed.");
                }
                
            } catch (Exception e) {
                // IMPORTANT: Catch any exceptions during JWT processing!
                // Unhandled exceptions here can break the filter chain and cause issues.
                System.err.println("JWT Filter: Error processing token: " + e.getMessage());
            }
        }

        // LOGGING ADDED: Check if we are proceeding without authentication
        if (!authenticated && request.getServletPath().startsWith("/api/") && 
            !request.getServletPath().equals("/api/student/login") &&
            !request.getServletPath().equals("/api/validate-otp")) {
            System.err.println("JWT Filter: API request for secured path UNATHENTICATED! Path: " + request.getServletPath());
        }
 
        filterChain.doFilter(request, response);
    }
 
    //@Override
    //protected boolean shouldNotFilter(HttpServletRequest request) {
        // PC側の画面（フォームログイン）には適用しない
        //return !request.getServletPath().startsWith("/api/");
    //}
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return
            // API 以外は除外
            !path.startsWith("/api/")
            // ログイン系
            || path.equals("/api/student/login")
            || path.equals("/api/validate-otp");
    }

}