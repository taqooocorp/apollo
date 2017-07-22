package com.taqooo.crawler.service;

import com.taqooo.crawler.model.PoemEntity;

import java.util.List;
import java.util.Set;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/2                          <br/>
 * Time    : 下午8:54                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
public interface PoemService {
    Integer addPoem(PoemEntity poemEntity);

    Integer countPoem();

    List<Integer> listPoemIds(Integer startId, Integer limit);

    List<Integer> listAllPoemIds();

    Set<Integer> listAllPoemOriginIds();
}
