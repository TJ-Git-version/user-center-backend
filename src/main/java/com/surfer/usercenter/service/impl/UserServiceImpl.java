package com.surfer.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surfer.usercenter.mapper.UserMapper;
import com.surfer.usercenter.model.domain.User;
import com.surfer.usercenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;

import static com.surfer.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author key point
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 盐值, 混淆密码
     */
    private static final String SALT = "surfer";

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
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
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        User user = new User();
        user.setLoginAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        return saveResult ? user.getId() : -1;
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4) {
            return null;
        }
        if (!userAccount.matches("^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$")) {
            return null;
        }
        if (userPassword.length() < 8) {
            return null;
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getLoginAccount, userAccount));
        // 用户不存在
        if (user == null) {
            log.info("user login failed, account cannot match");
            return null;
        }
        // 账号是否封禁
        if (user.getUserStatus() != 0) {
            log.info("user login failed, account is forbidden");
            return null;
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 密码错误
        if (!user.getUserPassword().equals(encryptPassword)) {
            log.info("user login failed, password error");
            return null;
        }
        // 设置登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        // 用户脱敏
        return getSafetyUser(user);
    }

    /**
     * 用户脱敏
     * @param originUser                原始用户信息
     * @return 安全用户信息
     */
    @Override
    public User getSafetyUser(User originUser) {
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setLoginAccount(originUser.getLoginAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setUpdateTime(originUser.getUpdateTime());
        return safetyUser;
    }

    @Override
    public List<User> searchUsers(String username) {
        return this.list(new LambdaQueryWrapper<User>()
                .like(StringUtils.isNotBlank(username), User::getUsername, username)
                .or()
                .like(StringUtils.isNotBlank(username), User::getLoginAccount, username)
        );
    }
}




