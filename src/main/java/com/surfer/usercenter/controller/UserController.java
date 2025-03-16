package com.surfer.usercenter.controller;

import com.surfer.usercenter.common.BaseResponse;
import com.surfer.usercenter.common.ErrorCode;
import com.surfer.usercenter.common.ResultUtils;
import com.surfer.usercenter.exception.BusinessException;
import com.surfer.usercenter.model.domain.User;
import com.surfer.usercenter.model.request.UserLoginRequest;
import com.surfer.usercenter.model.request.UserRegisterRequest;
import com.surfer.usercenter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.surfer.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.surfer.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口
 *
 * @author Dev Surfer
 */
@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        return ResultUtils.success(userService.userRegister(userRegisterRequest));
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        return ResultUtils.success(userService.userLogin(userAccount, userPassword, request));
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpSession session) {
        if (session == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.userLogout(session));
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // TODO 校验用户是否合法
        User user = userService.getById(currentUser.getId());
        return ResultUtils.success(userService.getSafetyUser(user));
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(@RequestParam(required = false) String username, HttpServletRequest request) {
        // 仅管理员查询
        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(userService.searchUsers(username));
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody Long id, HttpServletRequest request) {
        if (isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userService.removeById(id));
    }

    /**
     * 是否为管理员
     *
     * @param request               请求体
     * @return                      true: 管理员 | false: 非管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        return request.getSession().getAttribute(USER_LOGIN_STATE) instanceof User user && user.getUserRole() != ADMIN_ROLE;
    }

}
