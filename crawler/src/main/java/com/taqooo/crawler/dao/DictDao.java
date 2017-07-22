package com.taqooo.crawler.dao;

import com.taqooo.crawler.model.CnCharEntity;
import org.springframework.stereotype.Repository;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/6/29                          <br/>
 * Time    : 上午4:50                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
@Repository
public interface DictDao {
    int addCnchar(CnCharEntity cnCharEntity);
}
