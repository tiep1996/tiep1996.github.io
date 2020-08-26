package com.iist.qlda.project.entity;

import com.iist.qlda.project.dto.HumanResourcesDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "HUMAN_RESOURCES")
@AllArgsConstructor
@NoArgsConstructor
public class HumanResourcesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HUMAN_RESOURCES_ID")
    private Long humanResourceId;

//    @Column(name = "USER_ID")
//    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "FIRSTNAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;

    @Column(name = "POSITION_ID")
    private Long positionId;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "CREATE_BY")
    private Long createBy;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Column(name = "UPDATE_BY")
    private Long updateBy;

    @Column(name = "IS_ACTIVE")
    private Integer isActive;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "CENTER")
    private Long centerId;

    @Column(name = "IS_NEW")
    private Integer isNew;

    @Column(name = "VERIFY_KEY")
    private String verifyKey;
    @Transient
    public List<GrantedAuthority> authorities;

    public HumanResourcesDTO toDto(HumanResourcesEntity entity) {
        HumanResourcesDTO dto = new HumanResourcesDTO();
        dto.setHumanResourceId(entity.getHumanResourceId());
        dto.setCode(entity.getCode());
        dto.setLastName(entity.getLastName());
        dto.setFirstName(entity.getFirstName());
        dto.setEmail(entity.getEmail());
        dto.setUsername(entity.getUsername());

        return dto;
    }
}
