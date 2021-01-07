package com.sp.notesapp;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

public class ValidateInput {

    Context context;

    ValidateInput(Context context)
    {
        this.context=context;
    }

    //Method 1 : validate the email
    boolean checkIfEmailIsValid(String email)
    {
        if(email.length()==0)
        {
            Toast.makeText(context, "Please enter your email ID!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) //validate the email format
        {
            Toast.makeText(context, "Please enter a valid email ID!", Toast.LENGTH_SHORT).show();
            return  false;

        }
        else
        {
            return true; //email is valid
        }
    }


    //Method 2 : validate the password(must be >6)
    boolean checkIfPasswordIsValid(String password)
    {
        if(password.length()==0)
        {
            Toast.makeText(context, "Please enter a password!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(password.length()<6)
        {
            Toast.makeText(context, "Please enter a password of at least 6 characters.", Toast.LENGTH_SHORT).show();
            return  false;
        }
        else
        {
            //password is valid;
            return  true;
        }
    }

}
