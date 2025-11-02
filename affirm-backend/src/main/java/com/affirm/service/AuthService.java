package com.affirm.service;

import com.affirm.domain.AffirmCounter;
import com.affirm.domain.User;
import com.affirm.mapper.AffirmCounterMapper;
import com.affirm.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final AffirmCounterMapper counterMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User register(String username, String rawPassword) {
        // 唯一校验
        Long exists = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (exists != null && exists > 0) {
            throw new IllegalArgumentException("用户名已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        userMapper.insert(user);

        // 初始化计数
        AffirmCounter counter = new AffirmCounter();
        counter.setUserId(user.getId());
        counter.setTotalCount(0L);
        counterMapper.insert(counter);
        return user;
    }

    public User authenticate(String username, String rawPassword) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null || user.getPasswordHash() == null || !passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        return user;
    }
}





