package com.surfer.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.surfer.usercenter.model.domain.User;
import com.surfer.usercenter.model.request.UserRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

/**
* @author key point
*/
public interface UserService extends IService<User> {



    /**
     * 用户注册接口
     *
     * @param userAccount   账号
     * @param userPassword  密码
     * @param checkPassword 校验密码
     * @param planetCode
     * @return 返回用户id
     */
    long userRegister(UserRegisterRequest userRegisterRequest);


    /**
     * 用户登录接口
     * @param userAccount           账户
     * @param userPassword          密码
     * @return                      登录成功的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);


    /**
     * 用户脱敏
     * @param originUser            原始用户信息
     * @return                      脱敏用户信息
     */
    User getSafetyUser(User originUser);

    /**
     * 用户列表接口
     * @param username              用户名模糊查询
     * @return                      用户列表
     */
    List<User> searchUsers(String username);

    /**
     * 用户注销
     *
     * @param session session域
     * @return
     */
    Integer userLogout(HttpSession session);

}
