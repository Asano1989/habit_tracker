package com.example.learningtracker.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.learningtracker.entity.User;
import com.example.learningtracker.repository.UserRepository;

@Service
public class LoginUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public LoginUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
        Optional<User> _user = userRepository.findByAccountId(accountId);
        return _user.map(user -> new LoginUserDetails(user))
            .orElseThrow(() -> new UsernameNotFoundException("not found user account name =" + accountId));
    }

    public boolean isExistUser(String accountId) {
        String sql = "SELECT COUNT(*) FROM users WHERE account_id = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { accountId });
        if (count == 0) {
            return false;
        }
        return true;
    }
}