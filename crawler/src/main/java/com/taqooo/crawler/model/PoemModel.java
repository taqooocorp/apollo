package com.taqooo.crawler.model;

import lombok.Data;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/2                          <br/>
 * Time    : 下午8:42                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
@Data
public class PoemModel {

    /**
     * 诗歌id
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 朝代
     */
    private String dynasty;

    /**
     * 作者
     */
    private String author;

    /**
     * 诗词选集
     */
    private String anthology;

    /**
     * 古诗文网文章id
     */
    private Integer originId;

    /**
     * 来源地址
     */
    private String originUrl;
}
