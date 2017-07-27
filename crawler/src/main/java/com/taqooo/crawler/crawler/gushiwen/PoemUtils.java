package com.taqooo.crawler.crawler.gushiwen;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/27                          <br/>
 * Time    : 上午7:33                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
public class PoemUtils {
    private PoemUtils() {
    }

    /**
     * 古诗文网的诗词url转id
     *
     * @param url
     * @return
     */
    public static Integer parseId(String url) {
        int a = url.indexOf("_");
        int b = url.indexOf(".", a + 1);
        String idstr = url.substring(a + 1, b);
        return NumberUtils.isDigits(idstr) ? Integer.parseInt(idstr) : null;
    }

    /**
     * 古诗文网的诗词id转url
     *
     * @param id
     * @return
     */
    public static String buildUrl(Integer id) {
        if (id == null || id < 0) {
            return null;
        }
        return String.format("http://so.gushiwen.org/view_%05d.aspx", id);
    }


}
