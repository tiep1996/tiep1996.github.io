package com.iist.qlda.project.entity;

import com.iist.qlda.project.dto.HumanResourcesDTO;
import com.iist.qlda.project.dto.ProjectProgressDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="PROJECT_PROGRESS")
public class ProjectProgressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROGRESS_ID")
    private Long progressId;

    @Column(name = "BA_PROGRESS")
    private Long baProgress;

    @Column(name = "DEV_PROGRESS")
    private Long devProgress;

    @Column(name = "TEST_PROGRESS")
    private Long testProgress;

    @Column(name = "DOC_PROGRESS")
    private Long docProgress;

    @Column(name = "KBKT_PROGRESS")
    private Long kbktProgress;

    @Column(name = "RF_PROGRESS")
    private Long rfProgress; //tien do retest/fixbug

    @Column(name = "PROJECT_ID")
    private Long projectId; //tien do retest/fixbug

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

    @Transient
    public List<GrantedAuthority> authorities ;

    public ProjectProgressDTO toDto(ProjectProgressEntity entity){
        ProjectProgressDTO dto = new ProjectProgressDTO();
        dto.setProjectId(entity.getProjectId());
        dto.setBaProgress(entity.getBaProgress());
        dto.setDevProgress(entity.getDevProgress());
        dto.setTestProgress(entity.getTestProgress());
        dto.setDocProgress(entity.getDocProgress());
        dto.setKbktProgress(entity.getKbktProgress());
        dto.setRfProgress(entity.getRfProgress());
        return dto;
    }
}
