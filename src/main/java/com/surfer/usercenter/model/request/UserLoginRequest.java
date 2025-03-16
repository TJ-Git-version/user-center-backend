package com.surfer.usercenter.model.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Dev Surfer
 */
@Data
public class UserLoginRequest implements Serializable {


    @Serial
    private static final long serialVersionUID = 6518236473961615748L;

    private String userAccount;

    private String userPassword;

}
