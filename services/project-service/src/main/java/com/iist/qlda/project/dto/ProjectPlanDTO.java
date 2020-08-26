package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPlanDTO {
    private Long projectPlanId;

    private String planCode;

    private String planName;

    private Long masterPlanId;

    private Date startDate;

    private Date endDate;

    private Date deadline;

    private String module;

    private String mileStone;

    private Long humanResourceId;
    
    private String description;

    private String humanName;

    private String deadLineString;

    private int stt;

    private String startDateString;

    private String endDateString;
    

}
