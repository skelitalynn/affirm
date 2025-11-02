package com.affirm.service;

import com.affirm.domain.AffirmCounter;
import com.affirm.domain.User;
import com.affirm.mapper.AffirmCounterMapper;
import com.affirm.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final UserMapper userMapper;
    private final AffirmCounterMapper counterMapper;

    public long getTotalByUsername(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            return 0L;
        }
        AffirmCounter counter = counterMapper.selectById(user.getId());
        return counter == null ? 0L : (counter.getTotalCount() == null ? 0L : counter.getTotalCount());
    }
}





