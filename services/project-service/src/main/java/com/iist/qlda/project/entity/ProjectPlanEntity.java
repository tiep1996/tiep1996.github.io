package com.iist.qlda.project.entity;

import javax.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Entity
@Table(name ="PROJECT_PLAN")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_PLAN_ID")
    private Long projectPlanId;

    @Column(name = "PLAN_CODE")
    private String planCode;

    @Column(name = "PLAN_NAME")
    private String planName;

    @Column(name = "MASTERPLAN_ID")
    private Long masterPlanId;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "DEADLINE")
    private Date deadline;

    @Column(name = "MODULE")
    private String module;

    @Column(name = "MILESTONE")
    private String mileStone;

    @Column(name = "HUMAN_RESOURCE_ID")
    private Long humanResourceId;

    
    @Column(name = "DESCRIPTION")
    private String description;

}
