package com.Ucast.auth;

import com.Ucast.models.MongoUserModel;
import com.Ucast.repositories.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
            }catch (ExpiredJwtException e){
                logger.warn("Token has expired", e);
            }catch (SignatureException e){
                logger.error("Authentication failed");
            }catch (Exception e){
                logger.error("Error", e);
            }
        }else{
            logger.warn("Cannot find bearer string");
        }
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
            Optional<MongoUserModel> user = userRepository.findById(userId);

            if(user.isPresent() && jwtTokenProvider.validateToken(authToken, user.get())){
                // TODO complete method
            }
        }
    }
}
