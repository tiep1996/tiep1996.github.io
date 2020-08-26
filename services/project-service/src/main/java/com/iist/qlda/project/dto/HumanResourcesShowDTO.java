package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HumanResourcesShowDTO {

    private BigInteger humanResourceId;

    private String code;

    private String Name;

    private String email;

    private String phone;

    private String username;

    private String parcode;

    private Long centerId;

    private Integer active;

    private Integer page;

    private Integer pageSize;

    private Long totalRecord;
    private Long departmentId;
}
