package com.iist.qlda.project.dto;


import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterPlanDTO {

	private Long masterPlanId;
	private Long projectId;
	private String name;
	private Date dateSendingPlan;
	private Date dateDeliveryKBKT;
	private Date actualDateDelivery;
	private Date dateDemo;
	private Date endDate;
	private List<ProjectPlanDTO> lstprojectPlanDTO;
	private int statusBA;
	private String noteBA;
	private String reasonBA;
	private int statusTest;
	private String noteTest;
	private String reasonTest;
	private int statusPM;
	private String notePM;
	private String reasonPM;
	private int statusQA;
	private String noteQA;
	private String reasonQA;
	private String code;

	private Date createDate;

	private Long createBy;

	private Date updateDate;

	private Long updateBy;

	private Long humanId;

	private int isBA;
}
