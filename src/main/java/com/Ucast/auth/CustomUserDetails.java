package com.Ucast.auth;

import com.Ucast.models.MongoAuthorModel;
import com.Ucast.models.MongoUserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private String login;
    private String password;
    private Collection<GrantedAuthority> grantedAuthorities;

    public static CustomUserDetails fromUserEntityToCustomUserDetails(MongoUserModel userEntity, String role) {
        CustomUserDetails c = new CustomUserDetails();
        c.login = userEntity.getEmail();
        c.password = userEntity.getPassword();
        switch(role){
            case "USER":
                c.grantedAuthorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")); // TODO change role
                break;
            case "AUTHOR":
                c.grantedAuthorities = new ArrayList<>();
                c.grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                c.grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                break;
        }
        return c;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
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