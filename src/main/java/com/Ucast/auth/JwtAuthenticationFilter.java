package com.Ucast.auth;

import com.Ucast.models.MongoAdminModel;
import com.Ucast.models.MongoAuthorModel;
import com.Ucast.models.MongoUserModel;
import com.Ucast.repositories.AdminRepository;
import com.Ucast.repositories.AuthorRepository;
import com.Ucast.repositories.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.header.string}")
    public String HEADER_STRING;

    @Value("${jwt.token.prefix}")
    public String TOKEN_PREFIX;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;  // TODO create bean

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HEADER_STRING);
        String userId = null;
        String authToken = null;
        if(header != null && header.startsWith(TOKEN_PREFIX)){
            authToken = header.replace(TOKEN_PREFIX, "");
            try{
                userId = jwtTokenProvider.getUserIdFromToken(authToken);
            } catch (ExpiredJwtException e){
                logger.warn("Token has expired", e);
            } catch (SignatureException e){
                logger.error("Authentication failed");
            } catch (Exception e){
                logger.error("Error", e);
            }
        } else {
            logger.warn("Cannot find bearer string");
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
            Optional<MongoUserModel> user = userRepository.findById(userId);
            Optional<MongoAuthorModel> author = Optional.ofNullable(authorRepository.findByUserId(new ObjectId(userId)));
            Optional<MongoAdminModel> admin = Optional.ofNullable(adminRepository.findByUserId(new ObjectId(userId)));

            if (user.isPresent() && jwtTokenProvider.validateToken(authToken, user.get())) {
                String role = "USER";

                if(author.isPresent() && author.get().isConfirmed()){
                    role = "AUTHOR";
                }
                if(admin.isPresent()){
                    role = "ADMIN";
                }
                var userDetails = CustomUserDetails.fromUserEntityToCustomUserDetails(user.get(), role);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}
