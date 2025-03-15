package com.surfer.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surfer.usercenter.mapper.UserMapper;
import com.surfer.usercenter.model.User;
import com.surfer.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * @author key point
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (!userAccount.matches("^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$")) {
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        long isExistAccount = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getLoginAccount, userAccount));
        if (isExistAccount > 0) {
            return -1;
        }
        // 2.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex(("surfer" + userPassword).getBytes());
        User user = new User();
        user.setLoginAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        return saveResult ? user.getId() : -1;
    }
}




