package com.taqooo.crawler.crawler;

import com.taqooo.crawler.model.PoemEntity;
import com.taqooo.crawler.service.PoemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasSiblingFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/2                          <br/>
 * Time    : 下午9:00                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
@Slf4j
@Service
public class PoemCrawler {
    @Resource
    private PoemService poemService;

    /**
     * 解析
     *
     * @param originId
     * @return
     * @throws Exception
     */
    private PoemEntity parsePoem(int originId) {
        try {
            String urlFormat = "http://so.gushiwen.org/view_%05d.aspx";
            String url = String.format(urlFormat, originId);
            Parser parser = new Parser(url);
            parser.setEncoding("utf-8");

            NodeFilter divFilter = new NodeClassFilter(Div.class);
            HasSiblingFilter hasSiblingFilter = new HasSiblingFilter(divFilter);
            TagNameFilter h1TagFilter = new TagNameFilter("h1");
            AndFilter titleFilter = new AndFilter(hasSiblingFilter, h1TagFilter);

            PoemEntity poemEntity = new PoemEntity();
            //标题
            NodeList nodeList = parser.extractAllNodesThatMatch(titleFilter);
            Node titleNode = nodeList.elementAt(0);
            String title = titleNode.toPlainTextString();

            Node dynastyPNode = titleNode.getNextSibling().getNextSibling();
            NodeList baseInfos = dynastyPNode.getChildren();
            String dynasty = baseInfos.elementAt(0).toPlainTextString();
            String author = baseInfos.elementAt(2).toPlainTextString();

            String content = dynastyPNode.getNextSibling().getNextSibling().toPlainTextString();

            poemEntity.setTitle(title);
            poemEntity.setDynasty(dynasty);
            poemEntity.setAuthor(author);
            poemEntity.setContent(content);
            poemEntity.setOriginId(originId);
            return poemEntity;
        } catch (Exception e) {
            log.error("解析诗歌url出错,originId={}.", originId, e);
        }
        return null;
    }

    public CrawlerResult startCrawler(int from, int size, Set<Integer> ignoreIds) {

        List<Integer> loadFailedIds = new ArrayList<>();
        List<Integer> storeFailedIds = new ArrayList<>();
        List<Integer> successedIds = new ArrayList<>();
        int count = 0;
        for (int i = from; i < from + size; i++) {
            if (CollectionUtils.isNotEmpty(ignoreIds) && ignoreIds.contains(i)) {
                continue;
            }
            //打印信息
            System.out.println("-------------------------------");
            System.out.println(i);

            PoemEntity poemEntity = null;
            if (i >= 1 && i < 74000) {
                poemEntity = this.parsePoem(i);
                //重试一次
                if (poemEntity == null) {
                    poemEntity = this.parsePoem(i);
                }
            }
            System.out.println(poemEntity);
            if (poemEntity == null) {
                loadFailedIds.add(i);
                continue;
            }

            //添加到mysql
            int r = poemService.addPoem(poemEntity);
            if (r == 1) {
                successedIds.add(i);
            } else {
                storeFailedIds.add(i);
            }
        }
        System.out.println("-------------------------------");
        System.out.println("爬虫结束，总共" + successedIds.size() + "篇古文.");
        System.out.println("成功id:" + successedIds);
        System.out.println("加载失败id:" + loadFailedIds);
        System.out.println("存储失败id:" + storeFailedIds);
        return CrawlerResult.builder().loadFailedIds(loadFailedIds).storeFailedIds(storeFailedIds).successedIds(successedIds).build();
    }

    public CrawlerResult crawPoems(List<Integer> ids) {
        List<Integer> loadFailedIds = new ArrayList<>();
        List<Integer> storeFailedIds = new ArrayList<>();
        List<Integer> successedIds = new ArrayList<>();
        for (Integer id : ids) {
            //打印信息
            System.out.println("-------------------------------");
            System.out.println(id);

            PoemEntity poemEntity = this.parsePoem(id);
            //重试一次
            if (poemEntity == null) {
                poemEntity = this.parsePoem(id);
            }

            System.out.println(poemEntity);
            if (poemEntity == null) {
                loadFailedIds.add(id);
                continue;
            }

            //添加到mysql
            int r = poemService.addPoem(poemEntity);
            if (r == 1) {
                successedIds.add(id);
            } else {
                storeFailedIds.add(id);
            }
        }
        System.out.println("-------------------------------");
        System.out.println("爬虫结束，总共" + successedIds.size() + "篇古文.");
        System.out.println("成功id:" + successedIds);
        System.out.println("加载失败id:" + loadFailedIds);
        System.out.println("存储失败id:" + storeFailedIds);
        return CrawlerResult.builder().loadFailedIds(loadFailedIds).storeFailedIds(storeFailedIds).successedIds(successedIds).build();
    }
}
