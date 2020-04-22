package com.king2.longyingqin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ================================================================
 * 说明：当前类说说明
 * <p>
 * 作者          时间                    注释
 * 刘梓江	2020/4/22  18:20            创建
 * =================================================================
 **/

@Controller
public class HtmlController {
    @RequestMapping("/HtmlController")
    public String showHtml(){
        return "index";
    }
}
    
    
    