package com.taqooo.mms.boot;

import com.taqooo.mms.cfg.Config;
import com.taqooo.mms.util.Log;
import org.eclipse.jetty.xml.XmlConfiguration;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/6/15                          <br/>
 * Time    : 上午2:34                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
public class Bootstrap {

    public static void main(String[] args) throws Exception{
        Log.info("\n");

        initClasspath();
        loadConfig();

        String logs=Config.get("jetty.logs");
        if(logs==null){
            if(new File("/opt/logs/mobile").exists()){
                logs="/opt/logs/mobile";
            }else{
                logs="./logs";
            }
        }
        if(!new File(logs).exists()){
            new File(logs).mkdirs();
        }

        String webroot=Config.get("jetty.webroot");
        if(webroot==null){
            if(new File("webroot").exists()){
                webroot="./webroot";
            }else if(new File("src/main/webapp").exists()){
                webroot="./src/main/webapp";
            }else{
                webroot=".";
            }
        }
        Config.set("jetty.webroot", webroot);

        String context=Config.get("jetty.context");
        if(context==null||context.isEmpty()){
            context="/";
        }else if(context.charAt(0)!='/'){
            context="/"+context;
        }
        Config.set("jetty.context", context);

        Log.info("user.dir=" + System.getProperty("user.dir"));
        Log.info("jetty.webroot="+webroot);
        Log.info("jetty.context="+context);

        InputStream input=Bootstrap.class.getResourceAsStream("/jetty/jetty.xml");
        if(input==null){
            input=Bootstrap.class.getResourceAsStream("/jetty9.xml");
            Log.info("Booting with /jetty9.xml");
        }else{
            Log.info("Booting with /jetty/jetty.xml");
        }


        final XmlConfiguration jettyConfig = new XmlConfiguration(input);

        final Object server = jettyConfig.configure();

        final Pattern p=Pattern.compile("jetty-[\\w]+.xml");
        File[] paths=new File[]{
                new File("src/main/resources/jetty")
                ,new File("src/test/resources/jetty"),new File(webroot,"WEB-INF/classes/jetty")};

        for(File path:paths){
            if(path.exists()&&path.isDirectory()){
                File[] jettys=path.listFiles(new FileFilter(){
                    @Override
                    public boolean accept(File file) {
                        return file.isFile()&&p.matcher(file.getName()).matches();
                    }
                });
                if(jettys!=null){
                    for(File jf:jettys){
                        Log.info("Plusing "+jf.getName());
                        XmlConfiguration cfg = new XmlConfiguration(new FileInputStream(jf));
                        cfg.configure(server);
                    }
                }

                break;
            }
        }

        Log.info("\n\n");
        server.getClass().getMethod("start").invoke(server);
        //自运行--WebApp
//        org.eclipse.jetty.server.nio.NetworkTrafficSelectChannelConnector p;
//默认jetty8.xml jetty9.xml if出现jetty.xml，使用jetty.xml
//jetty-mms.xml, 作为片段文件插入使用
    }

    private static void loadConfig() throws IOException {
        InputStream input=Bootstrap.class.getResourceAsStream("/jetty/boot.properties");
        if(input!=null){
            System.getProperties().load(input);
            Log.info("Boot Loaded...");
        }else{
            Log.info("No /jetty/boot.properties found to load...");
        }
        Config.reload();
    }

    private static void initClasspath() {
        String projectClassPath=System.getProperty("java.class.path");
        String javaHome=System.getProperty("java.home");
        if(javaHome.endsWith("/jre")){
            javaHome=javaHome.substring(0,javaHome.length()-4);
        }
        Log.info("JAVA_HOME="+javaHome);
        Log.info("CLASSPATH="+projectClassPath);
        List<String> classpaths=ProjectClassLoader.getClasspaths();
        boolean logger=true;
        if(projectClassPath!=null){
            StringBuffer excludedString = new StringBuffer();
            String[] tokens = projectClassPath.split(String.valueOf(File.pathSeparatorChar));
            for(String entry:tokens){
                String path = entry;
                if(path.startsWith("-y-")|| path.startsWith("-n-")){ //backard compatiable.
                    path = path.substring(3);
                }
                if(entry.startsWith("-n-")||entry.startsWith(javaHome)){
                    if (logger) excludedString.append((excludedString.length()>0?"\n":"")+"Excluded entry="+ path);
                }else{
                    if (logger) Log.info("ProjectClassLoader: entry="+ path);
                    classpaths.add(path);
                    //super.addClassPath( path);
                }
            }
            Log.info(excludedString.toString());
        }
    }
}
