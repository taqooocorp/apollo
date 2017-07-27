package com.taqooo.crawler.crawler.gushiwen;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/27                          <br/>
 * Time    : 上午8:07                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
@Data
@AllArgsConstructor
public class AnthologyBean {
    /**
     * 诗词选集名
     */
    private String anthology;

    /**
     * 跳转链接
     */
    private String url;
}
