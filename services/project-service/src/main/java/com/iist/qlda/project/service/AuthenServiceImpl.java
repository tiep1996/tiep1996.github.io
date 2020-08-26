package com.iist.qlda.project.service;

import com.iist.qlda.project.config.security.JWTProvider;
import com.iist.qlda.project.dto.UserLoginDTO;
import com.iist.qlda.project.entity.HumanResourcesEntity;
import com.iist.qlda.project.repository.jparepository.HumanResourcesRepository;
import common.Constants;
import common.ErrorCode;
import common.ObjectError;
import exception.CustomExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.UUID;

//ANHTT_IIST
@Service
public class AuthenServiceImpl implements AuthenService {
    private Logger log = LogManager.getLogger(AuthenServiceImpl.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private HumanResourcesRepository resourcesRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private HumanResourcesRepository humanResourcesRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${urlForgotPassword}")
    private String urlForgotPassword;

    //ANHTT_IIST dang nhap
    @Override
    public String login(UserLoginDTO userLoginDTO) {
        String token = null;
        try {
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(upToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            HumanResourcesEntity humanResourcesEntity = resourcesRepository.findByUsername(userLoginDTO.getUsername());

            return token = jwtProvider.generateToken(humanResourcesEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public String register(UserLoginDTO userLoginDTO) {
        HumanResourcesEntity resourcesEntity = new HumanResourcesEntity();
        resourcesEntity.setUsername(userLoginDTO.getUsername());
        resourcesEntity.setPassword(passwordEncoder.encode(userLoginDTO.getPassword()));
        resourcesEntity.setLastName("test");

        resourcesRepository.save(resourcesEntity);

        return jwtProvider.generateToken(resourcesEntity);
    }

    @Override
    public ObjectError forgotPassword(String email) {
        HumanResourcesEntity en = humanResourcesRepository.findByEmail2(email);
        if (null == en) {
            throw new CustomExceptionHandler(Constants.EMAIL_NOT_FOUND, HttpStatus.BAD_REQUEST, ErrorCode.EMAIL_NOT_FOUND);
        }
        try {
            String key = UUID.randomUUID().toString();
            en.setVerifyKey(key);
            humanResourcesRepository.save(en);
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            message.setSubject("THÔNG TIN TÀI KHOẢN HỆ THỐNG QUẢN LÝ DỰ ÁN", "UTF-8");
            String subject = "Kính gửi anh/chị,\n\n" +
                    "Hệ thống Quản lý dự án của CÔNG TY TNHH GIẢI PHÁP VÀ CÔNG NGHỆ TÍCH HỢP ĐÔNG DƯƠNG gửi đến anh chị thông tin như sau:\n" +
                    "Anh/chị click link phía dưới để nhận mật khẩu mới:\n" +
                    "Link truy cập hệ thống:" + urlForgotPassword + email + "/" + key + "\n" +
                    "Họ và tên: " + en.getFirstName() + " " + en.getLastName() + "\n" +
                    "Tên đăng nhập: " + en.getUsername() + "\n" +
                    "Trân trọng!";
            message.setText(subject, "UTF-8");
            javaMailSender.send(message);
            log.info("<--- Send email success!");
            return ErrorCode.RESET_PASSWORD_OK;
        } catch (MessagingException | MailException ex) {
            log.error("Send email notification fail by Error ", ex.getMessage());
            throw new CustomExceptionHandler(Constants.RESET_PASSWORD_FAIL, HttpStatus.BAD_REQUEST, ErrorCode.RESET_PASSWORD_FAIL);
        }
    }

}
