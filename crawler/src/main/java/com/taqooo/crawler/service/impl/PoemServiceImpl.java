package com.taqooo.crawler.service.impl;

import com.taqooo.crawler.dao.PoemDao;
import com.taqooo.crawler.model.PoemEntity;
import com.taqooo.crawler.service.PoemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/2                          <br/>
 * Time    : 下午8:55                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
@Slf4j
@Service
public class PoemServiceImpl implements PoemService {

    @Resource
    private PoemDao poemDao;

    @Override
    public Integer addPoem(PoemEntity poemEntity) {
        if (poemEntity == null) {
            return 0;
        }
        return poemDao.addPoem(poemEntity);
    }

    @Override
    public Integer countPoem() {
        return poemDao.countPoems();
    }

    @Override
    public List<Integer> listPoemIds(Integer startId, Integer limit) {
        if (startId == null || limit == null || limit <= 0) {
            return new ArrayList<>();
        }

        return poemDao.listPoemIds(startId, limit);
    }

    @Override
    public List<Integer> listAllPoemIds() {
        return null;
    }

    @Override
    public Set<Integer> listAllPoemOriginIds() {
        Set<Integer> allOriginIds = new HashSet<>();

        int count = poemDao.countPoems();
        int size = 100;
        for (int offset = 0; offset < count; offset += size) {
            List<Integer> originIds = poemDao.listPoemOriginIds(offset, size);
            if (CollectionUtils.isNotEmpty(originIds)) {
                allOriginIds.addAll(originIds);
            }
        }

        return allOriginIds;
    }
}
