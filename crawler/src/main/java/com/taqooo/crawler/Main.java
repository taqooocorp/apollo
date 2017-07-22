package com.taqooo.crawler;

import com.taqooo.crawler.crawler.CrawlerResult;
import com.taqooo.crawler.crawler.PoemCrawler;
import com.taqooo.crawler.service.PoemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
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
    public static final int SIZE = 800;

    public static void parallCraw(PoemCrawler poemCrawler) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000));
        List<Future<CrawlerResult>> crawlerResultFutures = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int from = i * SIZE;
            Future<CrawlerResult> crawlerResultFuture = threadPoolExecutor.submit(() -> poemCrawler.startCrawler(from, SIZE, null));
            crawlerResultFutures.add(crawlerResultFuture);
        }

        Set<Integer> successedIds = new TreeSet<>();
        Set<Integer> loadFailedIds = new TreeSet<>();
        Set<Integer> storeFailedIds = new TreeSet<>();
        for (Future<CrawlerResult> crawlerResultFuture : crawlerResultFutures) {
            try {
                CrawlerResult crawlerResult = crawlerResultFuture.get();
                successedIds.addAll(crawlerResult.getSuccessedIds());
                loadFailedIds.addAll(crawlerResult.getLoadFailedIds());
                storeFailedIds.addAll(crawlerResult.getStoreFailedIds());
            } catch (Exception e) {
                log.error("线程结束错误,crawlerFuture={}.", crawlerResultFuture);
            }
        }
        System.out.println("-----------------------------------------");
        System.out.println("总共成功id个数:" + successedIds.size());
        System.out.println("总共成功id:" + successedIds);
        System.out.println("总共加载失败id个数:" + loadFailedIds.size());
        System.out.println("总共加载失败id:" + loadFailedIds);
        System.out.println("总共存储失败id个数:" + storeFailedIds.size());
        System.out.println("总共存储失败id:" + storeFailedIds);
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        PoemCrawler poemCrawler = applicationContext.getBean(PoemCrawler.class);
        PoemService poemService = applicationContext.getBean(PoemService.class);
//        Set<Integer> allOriginIds = poemService.listAllPoemOriginIds();
//        List<Integer> avaIds = new ArrayList<>();
//        for (int id = 72915; id < 74000; id++) {
//            if (!allOriginIds.contains(id)) {
//                avaIds.add(id);
//            }
//        }
        List<Integer> avaIds = new ArrayList<>();
        avaIds.add(72914);
        avaIds.add(72915);
        System.out.println("备选id个数:" + avaIds.size());
        System.out.println("备选id:" + avaIds);
        CrawlerResult crawlerResult = poemCrawler.crawPoems(avaIds);
        System.out.println("-----------------------------------------");
        System.out.println("总共成功id个数:" + crawlerResult.getSuccessedIds().size());
        System.out.println("总共成功id:" + crawlerResult.getSuccessedIds());
        System.out.println("总共加载失败id个数:" + crawlerResult.getLoadFailedIds().size());
        System.out.println("总共加载失败id:" + crawlerResult.getLoadFailedIds());
        System.out.println("总共存储失败id个数:" + crawlerResult.getStoreFailedIds().size());
        System.out.println("总共存储失败id:" + crawlerResult.getStoreFailedIds());
    }
}
