package com.iist.qlda.project.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerDTO {
    private Long id;

    private Long partnerID;

    private String customerPmName;

    private String customerEmail;

    private String amName;

    private String amEmail;

    private Date createDate;

    private Long createBy;

    private Date updateDate;

    private Long updateBy;

    private Integer isActive;

    private String amPhone;

    private String customerPmPhone;

}
