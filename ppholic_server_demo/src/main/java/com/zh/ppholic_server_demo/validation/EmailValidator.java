package com.zh.ppholic_server_demo.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<ValidEmail, String>{
    
    private Pattern thePattern;
    
    private Matcher theMatcher;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context){
        
        thePattern = Pattern.compile(EMAIL_PATTERN);
        
        if (email == null){
            
            return false;
        }
        
        theMatcher = thePattern.matcher(email);

        return theMatcher.matches();
    }
}
