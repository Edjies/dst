package com.haofeng.apps.dst;

/**
 * Created by WIN10 on 2017/6/12.
 */

public class ServerManager {

    private static ServerManager manager = new ServerManager();

    private ServerManager() {}

    public static ServerManager getInstance() {
        return manager;
    }

    public String getMainServer() {
        //return "http://192.168.1.107/dstzc_new/version2.2.0"; // 测试环境
        return "http://app.dstcar.com/v2/index.php?";
    }

    public String getImageServer() {
        return "http://120.25.209.72/";
    }
}
