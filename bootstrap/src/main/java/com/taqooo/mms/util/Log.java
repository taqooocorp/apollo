package com.taqooo.mms.util;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/6/15                          <br/>
 * Time    : 上午2:35                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */

/**
 * 控制台打印
 */
public class Log {
    private Log() {
    }

    public static void info(String msg) {
        System.out.println(msg);
    }

    public static void error(String msg) {
        System.err.println(msg);
    }
}
