package com.iist.qlda.project.controller;

import com.iist.qlda.project.config.security.JWTProvider;
import com.iist.qlda.project.dto.HumanResourcesDTO;
import com.iist.qlda.project.dto.UserLoginDTO;
import com.iist.qlda.project.entity.HumanResourcesEntity;
import com.iist.qlda.project.repository.jparepository.HumanResourcesRepository;
import com.iist.qlda.project.service.AuthenService;
import com.iist.qlda.project.service.HumanResourcesService;
import common.ErrorCode;
import common.ResultResp;
import exception.CustomExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/authen")
@CrossOrigin(origins = "*")
public class AuthenController {
    private Logger log = LogManager.getLogger(AuthenController.class);
    @Autowired
    AuthenService authenService;

    @Autowired
    HumanResourcesService humanResourcesService;

    @Autowired
    HumanResourcesRepository humanResourcesRepository;

    @Autowired
    private JWTProvider jwtProvider;

    @PostMapping("/login")
    public ResultResp login(@RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = authenService.login(userLoginDTO);
            return ResultResp.success(token);
        } catch (Exception e) {
            return ResultResp.badRequest(ErrorCode.AUTHENTICATION_FAILED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok(authenService.register(userLoginDTO));
    }


    @GetMapping("/test")
    public ResponseEntity test() {
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(userDetails.getAuthorities());
    }

    @PostMapping("/forgot-password")
    public ResultResp forgotPassword(@RequestBody HumanResourcesDTO dto) {
        if (null == dto.getEmail()) {
            return ResultResp.badRequest(ErrorCode.EMAIL_NULL);
        }
        try {
            return ResultResp.success(authenService.forgotPassword(dto.getEmail()));
        } catch (CustomExceptionHandler ex) {
            log.error("Forgot Password");
            log.error(ex);
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }
    }

    @GetMapping("/verify-email-forgot-password")
    public ResultResp verifyEmailForgotPassword(@RequestParam("email") String email,
                                                @RequestParam("key") String key, HttpServletRequest req) {
        log.info("Start verify ");
        if (null == email) {
            return ResultResp.badRequest(ErrorCode.EMAIL_NULL);
        }

        HumanResourcesEntity entity = humanResourcesRepository.findByEmail2(email);

        if(key.equals(entity.getVerifyKey())){
            String header = req.getHeader("Authorization");
            String token = header.substring(7);
            String username = jwtProvider.getUsernameFromToken(token);
            ResultResp rs = humanResourcesService.resetPassword(entity.getHumanResourceId(),username);
            if(rs.getStatusCode() == HttpStatus.OK){
                entity.setVerifyKey("");
                humanResourcesRepository.save(entity);
            }
            return rs;
        }
        return ResultResp.badRequest(ErrorCode.VERIFY_KEY_NOT_FOUND);
    }
}
