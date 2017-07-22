package com.taqooo.mms.boot;

import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/6/15                          <br/>
 * Time    : 上午2:38                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
public class ProjectClassLoader extends WebAppClassLoader {
    private boolean initialized = false;
    private static List<String> classpaths = new ArrayList<String>();

    public ProjectClassLoader(WebAppContext context, String projectClassPath,
                              String excluded) throws IOException {
        this(context, projectClassPath, excluded, true);
    }

    public ProjectClassLoader(WebAppContext context, String projectClassPath,
                              String excluded, boolean logger) throws IOException {
        super(context);

                /*
                 * As reported in these bugs:
                 *
                 * http://code.google.com/p/run-jetty-run/issues/detail?id=25
                 * http://code.google.com/p/run-jetty-run/issues/detail?id=26
                 *
                 * the path separator defined by Java (java.io.File.pathSeparator) (and
                 * used by the run-jetty-run plug-in) may not match the one used by
                 * Jetty (which is expects it to be either a comma or a semi-colon).
                 * Rather than move away from the standard path separator, I'm choosing
                 * to split the projectClassPath, and hand each entry to the super
                 * class, one at a time.
                 */
        if (projectClassPath != null) {
            StringBuffer excludedString = new StringBuffer();
            String[] tokens = projectClassPath.split(String.valueOf(File.pathSeparatorChar));
            for (String entry : tokens) {
                String path = entry;
                if (path.startsWith("-y-") || path.startsWith("-n-")) { //backard compatiable.
                    path = path.substring(3);
                }
                if (entry.startsWith("-n-")) {
                    if (logger) excludedString.append("Excluded entry=" + path + " \n");
                } else {
                    if (logger) System.err.println("ProjectClassLoader: entry=" + path);
                    classpaths.add(path);
                    super.addClassPath(path);
                }
            }
            System.err.print(excludedString.toString());
        }

        initialized = true;
    }

    /**
     * code fix for a strange case with Beanshell suuport , see Issue #53 for
     * more detail http://code.google.com/p/run-jetty-run/issues/detail?id=53
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Class loadClass(String name) throws ClassNotFoundException {
        try {
            return loadClass(name, false);
        } catch (NoClassDefFoundError e) {
            throw new ClassNotFoundException(name);
        }
    }

    public void addClassPath(String classPath) throws IOException {

        if (initialized) {
                        /*
                         * Disable the adding of directories to the class path after
                         * initialization with the project class path. XXX Except for the
                         * addition of the WEB-INF/classes
                         */
            if (!classPath.endsWith("WEB-INF/classes/"))
                return;
        }
        super.addClassPath(classPath);
        return;
    }

    public void addJars(Resource lib) {
        if (initialized) {
                        /*
                         * Disable the adding of jars (or folders of jars) to the class path
                         * after initialization with the project class path.
                         */
            return;
        }
        super.addJars(lib);
        return;
    }

    /**
     * TODO review this later , it's just a temp workaround. :(
     *
     * @return
     */
    public static List<String> getClasspaths() {
        return classpaths;
    }
}
