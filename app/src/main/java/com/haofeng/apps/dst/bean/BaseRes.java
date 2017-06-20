package com.haofeng.apps.dst.bean;

/**
 * @author  hubble
 */

public class BaseRes {
    public final static int RESULT_OK = 0;
    public final static int RESULT_PARSE_ERROR = -1;
    public final static int RESULT_EMPTY_CONTENT = -2;
    public int mCode = RESULT_OK;
    public String mMessage = "";
}
