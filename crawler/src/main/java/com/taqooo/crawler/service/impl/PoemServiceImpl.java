package com.taqooo.crawler.service.impl;

import com.taqooo.crawler.dao.PoemDao;
import com.taqooo.crawler.model.PoemModel;
import com.taqooo.crawler.service.PoemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    public Integer addPoem(PoemModel poemModel) {
        if (poemModel == null) {
            return 0;
        }

        return poemDao.addPoem(poemModel);
    }

    @Override
    public Integer countPoem(String anthology) {
        if (StringUtils.isBlank(anthology)) {
            return 0;
        }

        return poemDao.countPoems(anthology);
    }

    @Override
    public Set<Integer> listAllPoemOriginIds(String anthology) {
        if (StringUtils.isBlank(anthology)) {
            return null;
        }
        Set<Integer> allOriginIds = new HashSet<>();
        int count = poemDao.countPoems(anthology);
        int size = 100;
        for (int offset = 0; offset < count; offset += size) {
            List<Integer> originIds = poemDao.listPoemOriginIds(anthology, offset, size);
            if (CollectionUtils.isNotEmpty(originIds)) {
                allOriginIds.addAll(originIds);
            }
        }
        return allOriginIds;
    }
}
