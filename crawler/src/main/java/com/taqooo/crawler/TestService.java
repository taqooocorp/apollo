package com.taqooo.crawler;

import com.taqooo.crawler.dao.DictDao;
import com.taqooo.crawler.model.CnCharEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/6/29                          <br/>
 * Time    : 上午3:18                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
@Slf4j
@Service
public class TestService {
    @Value("${a}")
    private String a;
    @Autowired
    private DictDao dictDao;

    public String hello() {
        return a;
    }

    public int addCnchar(CnCharEntity charEntity) {
        if (charEntity == null) {
            return -1;
        }
        return dictDao.addCnchar(charEntity);
    }
}
