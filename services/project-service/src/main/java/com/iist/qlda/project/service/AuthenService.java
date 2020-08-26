package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.UserLoginDTO;
import common.ErrorCode;
import common.ObjectError;

import javax.servlet.http.HttpServletRequest;

public interface AuthenService {
    String login(UserLoginDTO userLoginDTO);

    String register(UserLoginDTO userLoginDTO);

    ObjectError forgotPassword(String email);
}
