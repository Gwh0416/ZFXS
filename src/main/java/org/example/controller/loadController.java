package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.common.R;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/load")
public class loadController {

    @PostMapping()
    public R<String> load(HttpServletRequest request){
        return R.success("loading...");
    }

}
