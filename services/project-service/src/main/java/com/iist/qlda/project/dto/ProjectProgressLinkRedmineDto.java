package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @author nuctv
 * @since 10 ,7/25/2020 , 2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectProgressLinkRedmineDto {
    private Long projectProgressLinkId;
    private String linkBA;
    private String linkDev;
    private String linkTKCT;
    private String linkKBKT;
    private String linkTest;
    private String linkFixBug;
    private Long projectID;
    private String linkProject;
}
