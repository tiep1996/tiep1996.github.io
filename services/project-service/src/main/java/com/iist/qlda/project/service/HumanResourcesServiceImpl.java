package com.iist.qlda.project.service;

import com.iist.qlda.project.common.Constants;
import com.iist.qlda.project.dto.*;
import com.iist.qlda.project.entity.HumanResourcesEntity;
import com.iist.qlda.project.repository.customreporsitory.HumanResourcesCustomRepository;
import com.iist.qlda.project.repository.customreporsitory.ProjectCustomRepository;
import com.iist.qlda.project.repository.customreporsitory.ProjectMemberCustomRepository;
import com.iist.qlda.project.repository.jparepository.CentersRepository;
import com.iist.qlda.project.repository.jparepository.GroupUserRepository;
import com.iist.qlda.project.repository.jparepository.HumanResourcesRepository;
import com.iist.qlda.project.service.mapper.HumanResourcesMapper;
import common.ErrorCode;
import common.ResultResp;
import exception.CustomExceptionHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service(value = "humanResourcesService")
public class HumanResourcesServiceImpl implements HumanResourcesService, UserDetailsService {

    private final Logger log = LogManager.getLogger(HumanResourcesServiceImpl.class);

    @Value("${urlUser}")
    private String urlUser;
    @Value("${keyAdmin}")
    private String keyAdmin;
    @Autowired
    private HumanResourcesCustomRepository customRepository;

    @Autowired
    private HumanResourcesRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HumanResourcesMapper humanResourcesMapper;

    @Autowired
    private GroupUserRepository groupUserRepository;

    @Autowired
    private CentersRepository centersRepository;


    @Autowired
    private BCryptPasswordEncoder passwordEncode;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ProjectCustomRepository projectCustomRepository;

    @Autowired
    private ProjectMemberCustomRepository projectMemberCustomRepository;
    //AnhTT_IIST
    @Override
    public List<HumanResourcesDTO> getListHumanResourceByNameOrCode(DTOSearch dto) {
        List<HumanResourcesDTO> lst = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(customRepository.getListHumanResourceByNameOrCode(dto))) {
            lst = customRepository.getListHumanResourceByNameOrCode(dto);
            for (HumanResourcesDTO humanResourcesDTO : lst) {
                humanResourcesDTO.setResourcesUsed((projectMemberCustomRepository.countResourceOfMember(humanResourcesDTO.getHumanResourceId())).doubleValue());
            }
        }
        return lst;
    }

    @Override
    public HumanResourcesDTO getUserInfo(String username) {
        log.info("--------------------get user info by username-----------------");
        HumanResourcesEntity humanResourcesEntity = repository.findByUsername(username);
        HumanResourcesDTO humanResourcesDTO = new HumanResourcesDTO();
        BeanUtils.copyProperties(humanResourcesEntity, humanResourcesDTO);

        log.info("--------------------set list permission to user-----------------");
        List<String> lstPermission = customRepository.getListPermissionByUserId(humanResourcesEntity.getHumanResourceId());
        if (CollectionUtils.isNotEmpty(lstPermission)) {
            humanResourcesDTO.setLstPermission(lstPermission);
        }
        humanResourcesDTO.setRole(projectCustomRepository.getGroupPermissionCodeByUserId(humanResourcesDTO.getHumanResourceId()));
        return humanResourcesDTO;
    }

    @Override
    public List<HumanResourcesDTO> getListHumanResourceByProjectID(long i) {
        return customRepository.getListHumanResourceByProjectID(i);
    }

    //hungnv
    @Override
    public Long changePassword(HumanResourcesDTO humanResourcesDTO) {

        String password = humanResourcesDTO.getPassword();
        String newPassword = humanResourcesDTO.getNewPassword();
        String newPasswordConfirm = humanResourcesDTO.getNewPasswordConfirm();
        if (!newPassword.equals(newPasswordConfirm)) {
            throw new CustomExceptionHandler("khong_trung", HttpStatus.BAD_REQUEST);
        }
        try {
            HumanResourcesEntity entity = repository.findByUsername(humanResourcesDTO.getUsername());
            if (!BCrypt.checkpw(password, entity.getPassword())) {
                throw new CustomExceptionHandler("sai_password", HttpStatus.BAD_REQUEST);
            }
            entity.setPassword(passwordEncoder.encode(newPassword));
            entity.setIsNew(common.Constants.IS_NOT_NEW);
            log.info("update password");
            HumanResourcesEntity result = repository.save(entity);
            log.info("doi pass thanh cong");
            return result.getHumanResourceId();
        } catch (Exception ex) {
            log.error("doi khong duoc", ex);
            throw ex;
        }


    }
    @Override
    public Long checkPassword(HumanResourcesDTO humanResourcesDTO) {
        HumanResourcesEntity entity = repository.findByUsername(humanResourcesDTO.getUsername()) ;
        if (!BCrypt.checkpw(humanResourcesDTO.getPassword(), entity.getPassword())) {
            throw new CustomExceptionHandler("sai_password", HttpStatus.BAD_REQUEST);
        }
        return entity.getHumanResourceId();
    }

    //end hungnv

    //AnhTT_IIST
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HumanResourcesEntity humanResourcesEntity = repository.findByUsername(username);

        List<GrantedAuthority> roleList = new ArrayList<>();

        List<String> lstPermission = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(customRepository.getListPermissionByUserId(humanResourcesEntity.getHumanResourceId()))) {

            //luu danh sach cac quyen cua user dang nhap
            lstPermission = customRepository.getListPermissionByUserId(humanResourcesEntity.getHumanResourceId());
            for (String str : lstPermission) {
                roleList.add(new SimpleGrantedAuthority(str));
            }
        }

        humanResourcesEntity.setAuthorities(roleList);

        if (humanResourcesEntity == null) {
            throw new CustomExceptionHandler(ErrorCode.USERNAME_NOT_FOUND.getCode(), HttpStatus.UNAUTHORIZED);
        }

        return new org.springframework.security.core.userdetails.User(humanResourcesEntity.getUsername(), humanResourcesEntity.getPassword(), humanResourcesEntity.getAuthorities());
    }

    // nuctv 30/07
    @Override
    public Map<String,Object> updateDataHumanResources(Long id) {
        log.info("<---- service: updateDataHumanResources - start");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = null;
        JSONObject body = null;
        int total_record;
        int limit;
        int total_page;
        List<HumanResourcesEntity> usersObject = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        int total_add = 0;
        int total_update = 0;
        // lấy user theo từng loại
        for (int i = 1; i <= 3; i++) {
            response = restTemplate.exchange(urlUser + "?status=" + i + "&key=" + keyAdmin, HttpMethod.GET, entity, String.class);
            body = new JSONObject(response.getBody());
            total_record = body.getInt("total_count");
            if (total_record == 0 || i == 2) {
                continue;
            }
            limit = body.getInt("limit");
            total_page = (int) Math.ceil((double) total_record / limit);
            // lấy các user của từng trang
            for (int j = 1; j <= total_page; j++) {
                response = restTemplate.exchange(urlUser + "?status=" + i + "&key=" + keyAdmin + "&page=" + j, HttpMethod.GET, entity, String.class);
                body = new JSONObject(response.getBody());
                JSONArray usersJson = body.getJSONArray("users");
                for (int t = 0; t < usersJson.length(); t++) {
                    JSONObject jObj = usersJson.getJSONObject(t);
                    String username = jObj.getString("login");
                    try {
                        HumanResourcesEntity user = repository.findByUsername(username);
                        if (null == user) {
                            user = new HumanResourcesEntity();
                            user.setUsername(username);
                            user.setIsNew(0);
                            total_add++;
                            if(id!=null){
                                user.setCreateBy(id);
                            }
                            user.setCreateDate(new Date());
                        } else {
                            if(!user.getFirstName().equalsIgnoreCase(jObj.getString("firstname"))||
                                    !user.getLastName().equalsIgnoreCase(jObj.getString("lastname"))||
                                    !user.getEmail().equalsIgnoreCase(jObj.getString("mail"))||
                                    user.getIsActive()!=i){
                                total_update++;
                            }
                            if(id!=null){
                                user.setUpdateBy(id);
                            }
                            user.setUpdateDate(new Date());
                        }

                        user.setFirstName(jObj.getString("firstname"));
                        user.setLastName(jObj.getString("lastname"));
                        user.setEmail(jObj.getString("mail"));
                        user.setIsActive(i);
                        usersObject.add(user);
                    } catch (Exception ex) {
                        log.error("<--- error find by username");
                        log.error(ex);
                    }
                }
            }
        }
        repository.saveAll(usersObject);
        map.put("total_add", total_add);
        map.put("total_update", total_update);
        log.info("<--- Done--->");
        return map;
    }

    @Scheduled(cron = "${autoSynchronized}")
    public void autoSynchronized(){
        try{
            log.info("----------Start Auto Synchronized User From Redmine----------");
        updateDataHumanResources(null);
            log.info("----------Synchronized Done----------");
        } catch (Exception e){
            log.error("----------Fail Auto Synchronized User From Redmine----------");
        }
    }
//nuctv end

    /*duc impl*/
    @Override
    public HumanResourcesDTO create(HumanResourcesDTO humanResourcesDTO) {
        HumanResourcesEntity humanResourcesEntity = repository.findByUsername(humanResourcesDTO.getUsername());

        if (null!=humanResourcesEntity && humanResourcesDTO.getHumanResourceId() == null) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_FALSE.getCode(), HttpStatus.BAD_REQUEST);
        }
        else if(null != humanResourcesEntity){
            humanResourcesEntity.setIsActive(humanResourcesDTO.getIsActive());
            humanResourcesEntity.setFirstName(humanResourcesDTO.getFirstName());
            humanResourcesEntity.setLastName(humanResourcesDTO.getLastName());
            humanResourcesEntity.setCenterId(humanResourcesDTO.getCenterId());
            humanResourcesEntity.setDepartmentId(humanResourcesDTO.getDepartmentId());
            humanResourcesEntity.setPositionId(humanResourcesDTO.getPositionId());
            humanResourcesEntity.setEmail(humanResourcesDTO.getEmail());
            humanResourcesEntity.setStartDate(humanResourcesDTO.getStartDate());
            humanResourcesEntity.setEndDate(humanResourcesDTO.getEndDate());
            humanResourcesEntity.setPhone(humanResourcesDTO.getPhone());
            humanResourcesEntity.setDescription(humanResourcesDTO.getDescription());


        }
        else if (humanResourcesDTO.getHumanResourceId()== null){
            humanResourcesEntity = humanResourcesMapper.toEntity(humanResourcesDTO);
        }
        humanResourcesEntity=repository.save(humanResourcesEntity);
        return humanResourcesMapper.toDto(humanResourcesEntity);
    }

    @Override
    public HumanResourcesDTO update(HumanResourcesDTO humanResourcesDTO) {
        HumanResourcesEntity hrResourcesEntity = humanResourcesMapper.toEntity(humanResourcesDTO);
        hrResourcesEntity = repository.save(hrResourcesEntity);
        return humanResourcesMapper.toDto(hrResourcesEntity);
    }

    @Override
    public List<IGroupUserDTO> getAllGroupUser() {
        return groupUserRepository.getAllGroupuser();
    }

    @Override
    public List<ICentersDTO> getCenters() {
        return centersRepository.getCenter();
    }

    @Override
    public HumanResourcesDTO findById(Long Id) {
        if (!repository.findById(Id).isPresent()) {
            throw new CustomExceptionHandler(ErrorCode.USERNAME_NOT_FOUND.getCode(), HttpStatus.BAD_REQUEST);
        }
        return humanResourcesMapper.toDto(repository.findById(Id).get());
    }

    @Override
    public HumanResourcesDTO findByCode(String code) {
        if (null == repository.findByCode(code)) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_EXIST.getCode(),HttpStatus.BAD_REQUEST);
        }
        return humanResourcesMapper.toDto(repository.findByCode(code));
    }

    @Override
    public HumanResourcesDTO checkUsername(String username) {
        if (null != repository.findByUsername(username)) {
            throw new CustomExceptionHandler(ErrorCode.CREATED_HR_EXIST.getCode(),HttpStatus.BAD_REQUEST);
        }
        return humanResourcesMapper.toDto(repository.findByUsername(username));
    }

    @Override
    public List<HumanResourcesDTO> findByEmail(String email) {

        if(!repository.findByEmail(email).isEmpty()){
            throw new CustomExceptionHandler(ErrorCode.EMAIL_IS_EXIST.getCode(),HttpStatus.BAD_REQUEST);
        }

        return humanResourcesMapper.toDto(repository.findByEmail(email));
    }

    @Override
    public HumanResourcesDTO getByEmail(String email) {
        HumanResourcesEntity entity = repository.findByEmail2(email);
        if (null == entity) {
            throw new CustomExceptionHandler(common.Constants.EMAIL_NOT_FOUND, HttpStatus.BAD_REQUEST, ErrorCode.EMAIL_NOT_FOUND);
        }
        return humanResourcesMapper.toDto(entity);
    }

    /*
     *@author ThaoLC - IIST
     *created on 7/30/2020
     */
    @Override
    public ResultResp resetPassword(Long humanResourceID,String usernameAdmin) {
         log.info("---> RESET PASSWORD: UserID " + humanResourceID + " confirm reset password START");
        HumanResourcesEntity humanResource = repository.findById(humanResourceID).get();
        // generate random password
        String SALTCHARS = "ABCDEefgh!@ijklFGH123IJKL!@#$MNOPQRS012345TUVWXYZabcdmnopqrstuvwxyz6789%^&*";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 15) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        try {
            humanResource.setPassword(passwordEncode.encode(saltStr));
        } catch (Exception ex) {
            log.error("<--- Reset Password Fail by Error: ", ex.getMessage());
            ex.printStackTrace();
        }
//        Long idAdmin = repository.findByUsername(usernameAdmin).getHumanResourceId();
//        humanResource.setUpdateBy(idAdmin);
        humanResource.setUpdateDate(new Date());
        humanResource = repository.save(humanResource);
        log.info("<--- Reset Password Complete");
        log.info("<--- Send email notification to user start!");
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(humanResource.getEmail()));
            message.setSubject("THÔNG TIN TÀI KHOẢN HỆ THỐNG QUẢN LÝ DỰ ÁN", "UTF-8");
            String subject = "Kính gửi anh/chị,\n\n" +
                    "Hệ thống Quản lý dự án của CÔNG TY TNHH GIẢI PHÁP VÀ CÔNG NGHỆ TÍCH HỢP ĐÔNG DƯƠNG gửi đến anh chị thông tin như sau:\n" +
                    "Anh/chị đã được reset mậu khẩu:\n" +
                    "Link truy cập hệ thống:" + Constants.URL_WEBAPP + "\n" +
                    "Họ và tên: " + humanResource.getFirstName() + " " + humanResource.getLastName() + "\n" +
                    "Tên đăng nhập: " + humanResource.getUsername() + "\n" +
                    "Mật khẩu mới: " + saltStr + "\n\n" +
                    "Trân trọng!";
            message.setText(subject, "UTF-8");
            javaMailSender.send(message);
            log.info("<--- Send email success!");
            return ResultResp.success(humanResource);
        } catch (MessagingException | MailException ex) {
            log.error("Send email notification fail by Error ", ex.getMessage());
            return ResultResp.badRequest(ErrorCode.RESET_PASSWORD_FAIL);
        }
    }


    /*end duc*/
    // TanNV
    @Override
    public DataPage<HumanResourcesShowDTO> getPageHumanResourcesSeach(HumanResourcesShowDTO dto) {
        log.info("-----------------Danh sach nhan su--------------");

        DataPage<HumanResourcesShowDTO> data = new DataPage<>();
        dto.setPage(null != dto.getPage() ? dto.getPage().intValue() : 1);
        dto.setPageSize(null != dto.getPageSize() ? dto.getPageSize().intValue() : 10);
//        if (dto.getActive() == 2) {
//            dto.setActive(null);
//        }

        List<HumanResourcesShowDTO> listProject = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(customRepository.getlistHumanResources(dto))) {
            listProject = customRepository.getlistHumanResources(dto);
            data.setData(listProject);
        }

        data.setPageIndex(dto.getPage());
        data.setPageSize(dto.getPageSize());
        data.setDataCount(dto.getTotalRecord());
        data.setPageCount(dto.getTotalRecord() / dto.getPageSize());
        if (data.getDataCount() % data.getPageSize() != 0) {
            data.setPageCount(data.getPageCount() + 1);
        }
        return data;
    }

    @Override
    public Boolean deleteHumanResources(Long id) {
        log.info("-----------------Xoa nhan su---------------");
        if (id != null) {
            HumanResourcesEntity humanResourcesEntity = repository.findByHumanResourceId(id);
            if (humanResourcesEntity.getIsActive() == 3) {
                humanResourcesEntity.setIsActive(1);
                repository.save(humanResourcesEntity);
                log.info("<--- Unlock Human Resources with id = " + id);
                return true;
            } else if (humanResourcesEntity.getIsActive() == 1) {
                if (customRepository.checkAssociationBeforeDelete(id)) {
                    humanResourcesEntity.setIsActive(3);
                    repository.save(humanResourcesEntity);
                    log.info("<--- DELETE HUMAN_RESOURCES COMPLETE");
                    return true;
                } else {
                    log.error("<--- CAN'T DELETE HUMAN RESOURCES");
                    return false;
                }
            }
//            Integer cout = customRepository.deleteHumanGroupUser(id);
//            if (humanResourcesEntity != null && cout <= 0) {
//                humanResourcesEntity.setIsActive(3);
//                repository.save(humanResourcesEntity);
//
        }
        return false;
    }

    @Override
    public Integer getActiveFromHumanResourceId(Long id) {
        return repository.findById(id).get().getIsActive();
    }
    // End TanNV
}
