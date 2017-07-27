package com.taqooo.crawler.crawler.gushiwen;

import com.taqooo.crawler.model.PoemModel;
import com.taqooo.crawler.service.PoemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.*;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
     * 解析诗词内容页面
     *
     * @param url
     * @return
     * @throws Exception
     */
    private PoemModel parsePoem(String url) {
        try {
            Parser parser = new Parser(url);
            parser.setEncoding("utf-8");

            NodeFilter divFilter = new NodeClassFilter(Div.class);
            HasSiblingFilter hasSiblingFilter = new HasSiblingFilter(divFilter);
            TagNameFilter h1TagFilter = new TagNameFilter("h1");
            AndFilter titleFilter = new AndFilter(hasSiblingFilter, h1TagFilter);

            PoemModel poemModel = new PoemModel();
            //标题
            NodeList nodeList = parser.extractAllNodesThatMatch(titleFilter);
            Node titleNode = nodeList.elementAt(0);
            String title = titleNode.toPlainTextString();

            Node dynastyPNode = titleNode.getNextSibling().getNextSibling();
            NodeList baseInfos = dynastyPNode.getChildren();
            //朝代
            String dynasty = baseInfos.elementAt(0).toPlainTextString();
            //作者
            String author = baseInfos.elementAt(2).toPlainTextString();
            //内容
            String content = dynastyPNode.getNextSibling().getNextSibling().toPlainTextString();

            poemModel.setTitle(title);
            poemModel.setContent(content);
            poemModel.setDynasty(dynasty);
            poemModel.setAuthor(author);
            poemModel.setOriginId(PoemUtils.parseId(url));
            poemModel.setOriginUrl(url);
            return poemModel;
        } catch (Exception e) {
            log.error("解析诗词内容页面出错,url={}.", url, e);
        }
        return null;
    }

    /**
     * 从诗词选集页面解析出其中的诗词页面列表
     *
     * @param url
     * @return
     */
    private List<String> parsePoemLinksFromAnthology(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        try {
            Parser parser = new Parser(url);
            parser.setEncoding("utf-8");

            NodeFilter spanFilter = new NodeClassFilter(Span.class);
            HasParentFilter hasParentFilter = new HasParentFilter(spanFilter);
            NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
            HasAttributeFilter hasAttributeFilter = new HasAttributeFilter("target", "_blank");
            AndFilter poemLinkFilter = new AndFilter(new NodeFilter[]{hasParentFilter, linkFilter, hasAttributeFilter});
            NodeList nodeList = parser.extractAllNodesThatMatch(poemLinkFilter);

            List<String> links = new ArrayList<>();
            for (int i = 0; i < nodeList.size(); i++) {
                Node node = nodeList.elementAt(i);
                LinkTag linkTag = (LinkTag) node;
                String link = linkTag.getLink();
                links.add(link);
            }
            return links;
        } catch (Exception e) {
            log.error("解析诗词选集页面出错,url={}.", url, e);
            return null;
        }
    }

    /**
     * 爬取诗词选集
     *
     * @param anthology
     * @param url
     * @return
     */
    public CrawerResult crawSingleAnthology(String anthology, String url) {

        List<Integer> successedIds = new ArrayList<>();
        List<Integer> failedIds = new ArrayList<>();

        List<String> poemUrls = this.parsePoemLinksFromAnthology(url);
        if (CollectionUtils.isEmpty(poemUrls)) {
            log.error("");
            return null;
        }
        //逐个处理
        for (int i = 0; i < poemUrls.size(); i++) {
            String poemUrl = poemUrls.get(i);
            if (StringUtils.isBlank(poemUrl)) {
                continue;
            }
            System.out.println("==============================");
            System.out.println(i);
            System.out.println("url=" + poemUrl);
            //解析
            PoemModel poemModel = this.parsePoem(poemUrl);
            if (poemModel != null) {
                poemModel.setAnthology(anthology);
            }
            System.out.println(poemModel);
            Integer originId = PoemUtils.parseId(poemUrl);
            if (poemModel == null) {
                failedIds.add(originId);
                continue;
            }
            //存储
            Integer result = poemService.addPoem(poemModel);
            if (result != null && result > 0) {
                successedIds.add(originId);
            } else {
                failedIds.add(originId);
            }
        }
        System.out.println("==========================");
        System.out.println("成功个数:" + successedIds.size());
        System.out.println(successedIds);
        System.out.println("失败个数:" + failedIds.size());
        System.out.println(failedIds);
        return new CrawerResult(successedIds, failedIds);
    }

    /**
     * 爬取配置的所有诗词选集
     */
    public void startCrawer() {
        PoemConsts.anthologys.stream().forEach(e -> {
            this.crawSingleAnthology(e.getAnthology(), e.getUrl());
        });
    }
}
