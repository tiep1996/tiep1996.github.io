package com.iist.qlda.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name ="PROJECT")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_ID")
    private Long projectId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "STATUS_OVERVIEW")
    private Integer statusOverview;

    @Column(name = "STATUS_DETAIL")
    private Integer statusDetail;

    @Column(name = "STATUS_PAYMENT")
    private Integer statusPayment;

    @Column(name = "ESTIMATE_PRELIMIINARY_EFFORTS")
    private Double estimatePrelimiinary;

    @Column(name = "ESTIMATE_INTERNAL_EFFORTS")
    private Double estimateInternal;

    @Column(name = "ESTIMATE_ACTUAL_EFFORTS")
    private Double estimateActual;

    @Column(name = "ESTIMATE_OFFER_EFFORTS")
    private Double estimateOffer;

    @Column(name = "ESTIMATE_LATCH_EFFORTS")
    private Double estimateLatch;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "PARTNER_ID")
    private Long partnerId;

    @Column(name = "DESCREPTION")
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
    
    @Column(name="MONTH")
    private String month;
    
    @Column(name="DATE_EXPECTED")
    private String dateExpected;
    
    @Column(name="BA")
    private Integer Ba;
    
    @Column(name="DEV")
    private Integer Dev;
    
    @Column(name="TEST")
    private Integer Test;
    
    @Column(name="BA_MAN")
    private Integer BA_MAN;

    @Column(name="STATUS_PRELIMIINARY_EFORTS")
    private Integer statusPreliinary;

    @Column(name ="NOTE_PRELIMIINARY_EFORTS")
    private String notePreliinary;

    @Column(name="REASON_PRELIMIINARY_EFORTS")
    private String reasonPrelimiinary;

    @Column(name="STATUS_INTERNAL_EFFORTS")
    private Integer statusInternal;

    @Column(name="NOTE_INTERNAL_EFFORTS")
    private String noteInternal;

    @Column(name="REASON_INTERNAL_EFFORTS")
    private String reasonInternal;

    @Column(name="STATUS_OFFER_EFFORTS")
    private Integer statusOffer;

    @Column(name="NOTE_OFFER_EFFORTS")
    private String noteOffer;

    @Column(name="REASON_OFFER_EFFORTS")
    private  String reasonOffer;

    @Column(name="STATUS_LATCH_EFFORTS")
    private Integer statusLatch;

    @Column(name="NOTE_LATCH_EFFORTS")
    private String noteLatch;

    @Column(name="REASON_LATCH_EFFORTS")
    private String reasonLatch;
    
//    @Column(name="PM_MAN")
//    private Integer PM_MAN;

}
