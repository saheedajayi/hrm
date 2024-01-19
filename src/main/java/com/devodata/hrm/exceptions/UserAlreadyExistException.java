package com.devodata.hrm.exceptions;

import java.io.Serial;

public class UserAlreadyExistException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1;
    public UserAlreadyExistException(String message){super(message);}
}
