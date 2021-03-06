package com.taqooo.crawler.dao;

import com.taqooo.crawler.model.PoemModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/2                          <br/>
 * Time    : 下午8:46                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
@Repository
public interface PoemDao {
    Integer addPoem(PoemModel poem);

    Integer countPoems(String anthology);

    List<Integer> listPoemIds(@Param("anthology") String anthology, @Param("id") int startId, @Param("limit") int limit);

    List<Integer> listPoemOriginIds(@Param("anthology") String anthology, @Param("offset") int offset, @Param("limit") int limit);
}
