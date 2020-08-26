package com.iist.qlda.project.entity;


import javax.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Table(name ="MASTERPLAN")
@AllArgsConstructor
@NoArgsConstructor
public class MasterPlanEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MASTERPLAN_ID")
    private Long masterPlanId;
	
    @Column(name = "PROJECT_ID")
    private Long projectId;
	
	@Column(name = "NAME")
    private String name;
	
	@Column(name = "DATE_SENDING_PLAN")
    private Date dateSendingPlan;
	
	@Column(name = "DATE_DELIVERY_KBKT")
    private Date dateDeliveryKBKT;
	
	@Column(name = "ACTUAL_DATE_DELIVERY")
    private Date actualDateDelivery;
	
	@Column(name = "DATE_DEMO")
    private Date dateDemo;
	
	@Column(name = "END_DATE")
    private Date endDate;
	
	@Column(name = "STATUS_BA_MANAGER")
    private int statusBA;
	
	@Column(name = "NOTE_BA_MANAGER")
    private String noteBA;
	
	@Column(name = "REASON_BA_MANAGER")
    private String reasonBA;

    @Column(name = "STATUS_TEST")
    private int statusTest;

    @Column(name = "NOTE_TEST")
    private String noteTest;

    @Column(name = "REASON_TEST")
    private String reasonTest;

    @Column(name = "STATUS_PM")
    private int statusPM;

    @Column(name = "NOTE_PM")
    private String notePM;

    @Column(name = "REASON_PM")
    private String reasonPM;

    @Column(name = "STATUS_QA")
    private int statusQA;

    @Column(name = "NOTE_QA")
    private String noteQA;

    @Column(name = "REASON_QA")
    private String reasonQA;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "CREATE_BY")
    private Long createBy;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Column(name = "UPDATE_BY")
    private Long updateBy;
}
