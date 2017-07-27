package com.taqooo.crawler.crawler.gushiwen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/27                          <br/>
 * Time    : 上午8:12                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :  爬虫结果
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawerResult {
    private List<Integer> successedIds;
    private List<Integer> failedIds;
}
