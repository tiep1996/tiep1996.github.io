package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectProgressDTO {
    private Long progressId;

    private Long baProgress;

    private Long devProgress;

    private Long testProgress;

    private Long docProgress;

    private Long kbktProgress;

    private Long rfProgress; //tien do retest/fixbug

    private Long projectId;

    private Date createDate;

    private Long createBy;

    private Date updateDate;

    private Long updateBy;

    private Integer isActive;
}
