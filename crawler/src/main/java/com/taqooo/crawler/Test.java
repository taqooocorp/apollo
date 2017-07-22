package com.taqooo.crawler;

import lombok.extern.slf4j.Slf4j;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasSiblingFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.util.NodeList;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/7/2                          <br/>
 * Time    : 下午8:27                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
@Slf4j
public class Test {
    public static void main(String[] args) throws Exception {
        String url = "http://so.gushiwen.org/view_71137.aspx";
        Parser parser = new Parser(url);
        parser.setEncoding("utf-8");
        NodeFilter divFilter = new NodeClassFilter(Div.class);
        HasSiblingFilter hasSiblingFilter = new HasSiblingFilter(divFilter);
        TagNameFilter h1TagFilter = new TagNameFilter("h1");
        AndFilter andFilter = new AndFilter(hasSiblingFilter, h1TagFilter);
        NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);
        String title = nodeList.elementAt(0).toPlainTextString();
        System.out.println(title);

    }
}
