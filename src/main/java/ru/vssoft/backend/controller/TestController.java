package ru.vssoft.backend.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ApiOperation(value = "Тестовое описание", authorizations = {@Authorization("JWT")})
    public List<String> list() {
        List<String> _l = new ArrayList<>();
        _l.add("Honey");
        _l.add("Almond");
        return _l;
    }

    @RequestMapping(value = "list", method = RequestMethod.POST)
    public String list_post() {
        return "Product is saved successfully";
    }
}
