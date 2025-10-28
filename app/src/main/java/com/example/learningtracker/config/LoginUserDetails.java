package com.example.learningtracker.config;

import java.util.Arrays;
import java.util.Collection;

import org.hibernate.type.descriptor.java.spi.CollectionJavaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.learningtracker.entity.User;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class LoginUserDetails implements UserDetails {
  private final String userId;
  private final String password;
  private final String name;
  private final Collection <? extends GrantedAuthority> authorities;
  
  public LoginUserDetails(User user) {
    this.userId = user.getUserId();
    this.password = user.getPassword();
    this.name = user.getName();
    this.authorities = null;
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
    return userId;
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
