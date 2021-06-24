package com.example.digitalshop.Model;

public class Result
{
    public boolean hasError () {
        return hasError;
    }

    public void setHasError (boolean hasError) {
        this.hasError = hasError;
    }

    public String getError_message () {
        return error_message;
    }

    public void setError_message (String error_message) {
        this.error_message = error_message;
    }

    public boolean hasError;
    public  String error_message;

    public String getSuccess_message () {
        return success_message;
    }

    public void setSuccess_message (String success_message) {
        this.success_message = success_message;
    }

    public  String success_message;
}
