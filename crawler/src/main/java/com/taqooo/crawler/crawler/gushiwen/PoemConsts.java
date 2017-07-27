package com.taqooo.crawler.crawler.gushiwen;

import java.util.ArrayList;
import java.util.List;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/27                          <br/>
 * Time    : 上午8:07                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
public class PoemConsts {
    private PoemConsts() {
    }

    /**
     * 诗词选集列表
     */
    public static final List<AnthologyBean> anthologys = new ArrayList<>();

    static {
        anthologys.add(new AnthologyBean("唐诗三百首", "http://so.gushiwen.org/gushi/tangshi.aspx"));
        anthologys.add(new AnthologyBean("宋词三百首", "http://so.gushiwen.org/gushi/songsan.aspx"));
        anthologys.add(new AnthologyBean("古诗十九首", "http://so.gushiwen.org/gushi/shijiu.aspx"));
        anthologys.add(new AnthologyBean("诗经", "http://so.gushiwen.org/gushi/shijing.aspx"));
        anthologys.add(new AnthologyBean("楚辞", "http://so.gushiwen.org/gushi/chuci.aspx"));
        anthologys.add(new AnthologyBean("乐府", "http://so.gushiwen.org/gushi/yuefu.aspx"));
    }

}
