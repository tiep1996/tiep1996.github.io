//package com.iist.qlda.project.controller;
//
//import com.iist.qlda.project.dao.HumanResouceDao;
//import com.iist.qlda.project.dao.ProjectProgressDAO;
//import com.iist.qlda.project.entity.HumanResourcesEntity;
//import com.iist.qlda.project.entity.ProjectProgressEntity;
//import com.iist.qlda.project.service.HumanResourcesService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/")
//@CrossOrigin("*")
//public class RedmineController {
//    @Autowired
//    HumanResouceDao userDAO;
//    @Autowired
//    ProjectProgressDAO prgDAO;
//    @Autowired
//    HumanResourcesService humanService;
//
//    @GetMapping("/redmineUser")
//    public ResponseEntity<List<HumanResourcesEntity>> insert() {
//        return new ResponseEntity<List<HumanResourcesEntity>>( humanService.updateDataHumanResources(), HttpStatus.OK);
//    }
//
//}
