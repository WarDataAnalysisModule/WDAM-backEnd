package com.back.wdam.webcontroller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

//리액트 연동 시 모든 view 맵핑을 index.html로
@CrossOrigin
@Controller
public class WebController implements ErrorController {
    @GetMapping({"/", "/error"})
    public String index() {
        return "forward:/index.html";
    }
}