package com.surfer.usercenter.service;


import com.surfer.usercenter.mapper.UserMapper;
import com.surfer.usercenter.model.domain.User;
import com.surfer.usercenter.model.request.UserRegisterRequest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * 用户服务测试
 *
 * @author surfer
 */
@SpringBootTest
class UserServiceTest {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUsername("testSurfer");
        user.setLoginAccount("123");
        user.setAvatarUrl("");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("345");
        int result = userMapper.insert(user);
        System.out.println(user.getId());
        Assertions.assertEquals(result, 1);
    }

    @Test
    void userRegister() {
        UserRegisterRequest userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setUserAccount("surfer");
        userRegisterRequest.setUserPassword("123123");
        userRegisterRequest.setCheckPassword("12345678");
        userRegisterRequest.setPlanetCode("1");
        long result = userService.userRegister(userRegisterRequest);
        Assertions.assertEquals(-1, result);
        userRegisterRequest.setUserAccount("");
        result = userService.userRegister(userRegisterRequest);
        Assertions.assertEquals(-1, result);
        userRegisterRequest.setUserAccount("sur fer");
        result = userService.userRegister(userRegisterRequest);
        Assertions.assertEquals(-1, result);
        userRegisterRequest.setUserAccount("su");
        result = userService.userRegister(userRegisterRequest);
        Assertions.assertEquals(-1, result);
        userRegisterRequest.setUserAccount("surfer12");
        userRegisterRequest.setUserPassword("1233");
        result = userService.userRegister(userRegisterRequest);
        Assertions.assertEquals(-1, result);
        userRegisterRequest.setUserAccount("surfer123");
        userRegisterRequest.setUserPassword("123");
        result = userService.userRegister(userRegisterRequest);
        Assertions.assertEquals(-1, result);
    }
}
