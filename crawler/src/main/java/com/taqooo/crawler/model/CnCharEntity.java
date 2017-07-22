package com.taqooo.crawler.model;

import lombok.Data;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/6/29                          <br/>
 * Time    : 上午4:43                          <br/>
 * ---------------------------------------    <br/>
 * Desc    : 汉字model
 */
@Data
public class CnCharEntity {

    /**
     * id
     */
    private Integer id;

    /**
     * 汉字
     */
    private String name;

    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 基本解释
     */
    private String simdesc;
}
