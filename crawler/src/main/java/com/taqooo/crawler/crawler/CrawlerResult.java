package com.taqooo.crawler.crawler;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/3                          <br/>
 * Time    : 上午12:07                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
@Data
@Builder
public class CrawlerResult {
    private List<Integer> successedIds;
    private List<Integer> storeFailedIds;
    private List<Integer> loadFailedIds;
}
