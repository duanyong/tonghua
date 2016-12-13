package com.tonghua.crm.action;

import com.tonghua.crm.service.ExcelService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


/**
 * Created by duanyong on 9/14/16.
 */

@Controller
@RequestMapping("/ss")
public class IndexController {
    @RequestMapping(method = RequestMethod.GET)
    public String view() {
        return "/index";
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index() {
        return view();
    }
}
