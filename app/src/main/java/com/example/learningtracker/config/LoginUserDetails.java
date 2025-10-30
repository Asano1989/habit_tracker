package com.example.learningtracker.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.learningtracker.entity.User;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class LoginUserDetails implements UserDetails {

    private User user;
    private final String accountId;
    private final String password;
    private final String name;
    private final Collection <? extends GrantedAuthority> authorities;
    
    public LoginUserDetails(User user) {
        this.accountId = user.getAccountId();
        this.password = user.getPassword();
        this.name = user.getName();
        this.authorities = null;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ロールのコレクションを返す
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public String getPassword() {
        // パスワードを返す
        return password;
    }

    @Override
    public String getUsername() {
        // ログイン名を返す
        return accountId;
    }

    public String getName() {
        // ユーザー名を返す
        return name;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        //  ユーザーが期限切れでなければtrueを返す
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //  ユーザーがロックされていなければtrueを返す
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //  パスワードが期限切れでなければtrueを返す
        return true;
    }

    @Override
    public boolean isEnabled() {
        //  ユーザーが有効ならtrueを返す
        return true;
    }
}
