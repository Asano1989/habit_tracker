package com.example.learningtracker.config;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public boolean isExistUserWithId(String accountId, Integer userId) {
        String sql = "SELECT COUNT(*) FROM users WHERE account_id = ? AND id != ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[] { accountId, userId });
        if (count == 0) {
            return false;
        }
        return true;
    }
}