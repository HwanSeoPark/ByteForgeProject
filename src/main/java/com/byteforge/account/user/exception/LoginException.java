package com.byteforge.account.user.exception;

import com.byteforge.common.response.message.AccountMessage;

public class LoginException extends RuntimeException {

    public LoginException(AccountMessage message) {
        super(message.getMessage());
    }

    public LoginException(String message) {
        super(message);
    }

}
