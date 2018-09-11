package com.weguard.controller;

import com.ioc.annotation.Controller;
import com.ioc.annotation.RequestMapping;
import com.ioc.annotation.RequestParam;
import com.ioc.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/go")
public class RBuildingZoneAppController {
    @RequestMapping("/test")
    @ResponseBody
    public String test(@RequestParam("age") int age, @RequestParam("name") String name, @RequestParam("sex") String d) {
       return age+","+name+","+d;
    }

    public String test1() {
        return "123";
    }
}
