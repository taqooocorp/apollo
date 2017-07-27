package com.taqooo.crawler.service;

import com.taqooo.crawler.model.PoemModel;

import java.util.Set;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/2                          <br/>
 * Time    : 下午8:54                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
public interface PoemService {
    /**
     * 存储诗词
     * @param poemModel
     * @return
     */
    Integer addPoem(PoemModel poemModel);

    /**
     * 统计数据库中某个选集中的诗词总数
     * @param anthology
     * @return
     */
    Integer countPoem(String anthology);

    /**
     * 列出某个选集中的所有原始id
     * @param anthology
     * @return
     */
    Set<Integer> listAllPoemOriginIds(String anthology);
}
