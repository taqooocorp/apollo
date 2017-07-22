package com.taqooo.mms.cfg;

import com.taqooo.mms.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/6/15                          <br/>
 * Time    : 上午2:36                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
public class Config {

    public static String get(String key){
        return System.getProperty(key);
    }
    public static String get(String key,String defaultValue){
        return System.getProperty(key,defaultValue);
    }
    public static void set(String key,String value){
        System.setProperty(key, value);
    }
    private static final String splitor="[\\s,;，；]+";
    public static String[] getArray(String key) {
        String value = get(key);
        return value==null?null:value.split(splitor);
    }
    public static String getAppkey() {
        return get("jetty.appkey");
    }
    public static String getApptoken() {
        return get("jetty.apptoken");
    }
    public static int getInt(String key,int defaultValue) {
        String value=get(key);
        return value==null?defaultValue:Integer.parseInt(value);
    }
    public static void reload() throws IOException {
        InputStream input=Config.class.getResourceAsStream("/config.properties");
        if(input!=null){
            System.getProperties().load(input);
            Log.info("Config Loaded...");
        }else{
            Log.info("No /config.properties found to load...");
        }
    }
}
