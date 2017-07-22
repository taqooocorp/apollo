package com.taqooo.moon.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author  : fnn@meituan.com              <br/>
 * Date    : 2017/6/15                          <br/>
 * Time    : 上午3:04                          <br/>
 * ---------------------------------------    <br/>
 * Desc    :
 */
@Controller
public class ExampleController {
    @RequestMapping(path = {"/", "/api/v1/name/gen"}, method = RequestMethod.GET)
    @ResponseBody
    public JSONObject test() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "taqooo");
        return jsonObject;
    }

}
