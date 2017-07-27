package com.taqooo.crawler;

import com.taqooo.crawler.crawler.gushiwen.AnthologyBean;
import com.taqooo.crawler.crawler.gushiwen.PoemConsts;
import com.taqooo.crawler.crawler.gushiwen.PoemCrawler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/6/29                          <br/>
 * Time    : 上午3:19                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
@Slf4j
public class Main {
    public static final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000));

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        PoemCrawler poemCrawler = applicationContext.getBean(PoemCrawler.class);
        for (int idx = 0; idx < PoemConsts.anthologys.size(); idx++) {
            AnthologyBean anthologyBean = PoemConsts.anthologys.get(idx);
            poemCrawler.crawSingleAnthology(anthologyBean.getAnthology(), anthologyBean.getUrl());
        }
    }
}
