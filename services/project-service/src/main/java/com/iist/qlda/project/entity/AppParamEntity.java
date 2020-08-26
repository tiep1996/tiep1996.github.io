package com.iist.qlda.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name ="APP_PARAMS")
@AllArgsConstructor
@NoArgsConstructor
public class AppParamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APP_PARAMS_ID")
    private Long id;

    @Column(name="PAR_CODE")
    private String code;

    @Column(name="PAR_NAME")
    private String name;

    @Column(name="PAR_TYPE")
    private String type;

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
}
