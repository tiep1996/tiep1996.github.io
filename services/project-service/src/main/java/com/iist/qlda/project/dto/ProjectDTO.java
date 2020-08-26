package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO extends PartnerDTO{

    private Long projectId;

    private String code;

    private String name;

    private String fullName;

    private Integer statusOverview;

    private String statusOverviewName;

    private Integer statusDetail;

    private Integer statusPayment;

    private Double estimatePrelimiinary;

    private Double estimateInternal;

    private Double estimateActual;

    private Double estimateOffer;

    private Double estimateLatch;

    private String startDate;

    private String endDate;

    private String startDate1;

    private String endDate1;

    private String description;

    private Date createDate;

    private Long createBy;

    private Date updateDate;

    private Long updateBy;

    private Integer isActive;

    private List<AttachDocumentDTO> lstAttachDocument;
    
    private List<Long> lstId; 
    
    private String month;

    private String dateExpected;

    private List<Long> lstHumanId;

    private Long pmId;

    private Long baId;

    private Long testLeadId;

    private Long qaId;

   private Integer ba;
   
   private Integer dev;
   
   private Integer test;
   
   private Integer baMan;
   
  // private Integer pmMan;

   private Double estimateLatchTo;

    private Double estimateLatchFrom;

    private String partnerName;

    private String pmName;

    private String testLeaderName;

    private String qmName;

    private String bmName;

    private String partnerCode;

    private Date dateDeliveryKbkt;

    private Date dateActualDelivery;

    private Date dateActualComplete;

    private Date dateDemo;

    private Double baProgress;

    private Double devProgress;

    private Double testProgress;

    private Double docProgress;

    private Double kbktProgress;

    private Double retestOrFixbugProgress;

    private Integer page;

    private Integer pageSize;

    private Long totalRecord;

    private List<Integer> lstStatusDetail;

    private Long countProjectMember;

    private List<ProjectMemberDTO> lstProjectMember;

    private MultipartFile[] lstAttachFile;

    private List<ProjectMemberDTO> lstProjectMemberDelete;

    private String statusDetailName;

    private String statusPaymentName;

    private Long humanResourcesId;

    private Date dateSendingPlan;

    private String requireRange; //pham vi yeu cau

    private String masterPlan;

    private Integer statusPreliinary;

    private String notePreliinary;

    private String reasonPrelimiinary;

    private Integer statusInternal;

    private String noteInternal;

    private String reasonInternal;

    private Integer statusOffer;

    private String noteOffer;

    private  String reasonOffer;

    private Integer statusLatch;

    private String noteLatch;

    private String reasonLatch;
}
