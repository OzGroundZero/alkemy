package com.groundzero.alkemy.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by lwdthe1 on 9/18/2015.
 */
public abstract class AHandler extends Handler {
    public static long id = 0;
    private String name;
    private int totalMessagesAndPostsPassed;
    private Context context;



    protected AHandler() {
    }

    public AHandler(Context context, String name){
        this.context = context;
        this.name = name;
        id++;
    }

    public static long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalMessagesAndPostsPassed() {
        return totalMessagesAndPostsPassed;
    }

    public Context getContext() {
        return context;
    }


    public abstract void handleTheMessage(Message msg);

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        handleTheMessage(msg);
    }

    public class Messages {
        public static final int ACTION_STARTED = 100;
        public static final int ACTION_SUCCESS = 200;
        public static final int ACTION_FAIL = 400;

        public class Seen {

            public static final int FINISH_ACTIVITY = -1;
        }

        public class Scanner {
            public static final int FINISH_SCANNING_OBJECT = 1;
            public static final int GOT_IMAGE = 2;
        }
    }
}
