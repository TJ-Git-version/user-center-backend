package com.surfer.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Dev Surfer
 */
@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 5334697626563849514L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;

    private String planetCode;

}
