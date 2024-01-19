package com.devodata.hrm.exceptions;

import java.io.Serial;

public class HealthRecordNotFoundException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1;
    public HealthRecordNotFoundException(String message){super(message);}
}
