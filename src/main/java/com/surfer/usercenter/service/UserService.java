package com.surfer.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.surfer.usercenter.model.User;

/**
* @author key point
* @description 针对表【user(用户)】的数据库操作Service
* @createDate 2025-03-15 20:08:10
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册接口
     * @param userAccount           账号
     * @param userPassword          密码
     * @param checkPassword         校验密码
     * @return                      返回用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

}
