package com.surfer.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.surfer.usercenter.common.ErrorCode;
import com.surfer.usercenter.exception.BusinessException;
import com.surfer.usercenter.mapper.UserMapper;
import com.surfer.usercenter.model.domain.User;
import com.surfer.usercenter.model.request.UserRegisterRequest;
import com.surfer.usercenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号长度不能小于4位");
        }
        if (!userAccount.matches("^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能带特殊字符");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能小于8位");
        }
        if (planetCode.length() > 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号不能大于5位");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }
        // 账号不能重复
        long isExistAccount = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getLoginAccount, userAccount));
        if (isExistAccount > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该账号已被占用");
        }
        // 星球编号不能重复
        long isExistCode = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getPlanetCode, planetCode));
        if (isExistCode > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "星球编号已被占用");
        }
        // 密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 插入数据
        User user = new User();
        user.setLoginAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        return saveResult ? user.getId() : -1;
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能小于4位");
        }
        if (!userAccount.matches("^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能带特殊字符");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能小于8位");
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getLoginAccount, userAccount));
        // 用户不存在
        if (user == null) {
            log.info("user login failed, account cannot match");
            throw new BusinessException(ErrorCode.NULL_ERROR, "该用户不存在");
        }
        // 账号是否封禁
        if (user.getUserStatus() != 0) {
            log.info("user login failed, account cannot match");
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "用户已被封禁");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 密码错误
        if (!user.getUserPassword().equals(encryptPassword)) {
            log.info("user login failed, password error");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
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
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setLoginAccount(originUser.getLoginAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
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

    @Override
    public Integer userLogout(HttpSession session) {
        session.removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




