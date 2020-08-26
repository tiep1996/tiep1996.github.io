package com.iist.qlda.project.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SyPT - IIST
 * created on 7/11/2020
 */
@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class TestController {
    private final Logger log = LogManager.getLogger(TestController.class);


}
