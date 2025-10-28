package com.example.learningtracker.config;

import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.learningtracker.entity.User;
import com.example.learningtracker.repository.UserRepository;

@Service
public class LoginUserDetailService implements UserDetailsService {
  private final UserRepository userRepository;

  public LoginUserDetailService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
    Optional<User> _user = userRepository.findByUserId(userId);
    return _user.map(user -> new LoginUserDetails(user))
        .orElseThrow(() -> new UsernameNotFoundException("not found user account name =" + userId));
  }
}