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
public class PoemEntity {

    /**
     * 诗歌id
     */
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 朝代
     */
    private String dynasty;

    /**
     * 作者
     */
    private String author;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 原文id
     */
    private Integer originId;
}
