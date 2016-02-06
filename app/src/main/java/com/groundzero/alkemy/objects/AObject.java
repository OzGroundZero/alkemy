package com.groundzero.alkemy.objects;

import java.util.HashMap;

/**
 * Created by lwdthe1 on 9/20/2015.
 */
public class AObject {
    public static final String KEY_ID = "object_id";
    protected static final int CODE_BAD_OBJECT = -1;
    private HashMap<String, Object> vars;//dynamic list of variables
    protected int id;//the id of this object

    public AObject(){
        vars = new HashMap<>();
    }

    public AObject set(String key, Object val){ vars.put(key, val); return this;}
    public Object get(String key){ return vars.get(key); }

    public int getId() {
        return id;
    }

    public static String filterIntsOnly(String s) {
        return s.replaceAll("\\D+","");
    }
}
