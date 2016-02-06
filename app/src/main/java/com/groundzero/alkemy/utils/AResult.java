package com.groundzero.alkemy.utils;


import com.groundzero.alkemy.objects.AObject;

/**
 * Created by lwdthe1 on 8/2/2015.
 */
public class AResult extends AObject {
    Codes code;
    String message;

    public void addExtra(String key, String value) {
        set(key, value);
    }

    public Object getExtra(String key){
        return get(key);
    }

    public enum Codes {
        SUCCESS, FAIL, ERROR, UNKNOWN
    };

    public AResult setCode(Codes code){
        this.code = code;
        return this;
    }

    public Codes getCode(){
        return code;
    }

    public String getMessage() {
        return message;
    }

    public AResult setMessage(String message) {
        this.message = message;
        return this;
    }
}
