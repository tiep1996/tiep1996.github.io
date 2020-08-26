package com.iist.qlda.project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name ="PARTNER")
@AllArgsConstructor
@NoArgsConstructor
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PARTNER_ID", nullable = true)
    private Long partnerID;

    @Column(name = "CUSTOMER_PM_NAME")
    private String customerPmName;

    @Column(name = "CUSTOMER_EMAIL")
    private String customerEmail;

    @Column(name = "AM_NAME")
    private String amName;

    @Column(name = "AM_EMAIL")
    private String amEmail;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "CREATE_BY")
    private Long createBy;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Column(name = "UPDATE_BY")
    private Long updateBy;

    @Column(name = "AM_PHONE")
    private String amPhone;

    @Column(name = "CUSTOMER_PM_PHONE")
    private String customerPmPhone;

    @Column(name = "IS_ACTIVE")
    private Integer isActive;
}
