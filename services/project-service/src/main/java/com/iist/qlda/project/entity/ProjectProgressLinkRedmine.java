package com.iist.qlda.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author nuctv
 * @since 10 ,7/25/2020 , 2020
 */
@Data
@Entity
@Table(name ="PROJECT_PROGRESS_LINK_REDMINE")
@AllArgsConstructor
@NoArgsConstructor
public class ProjectProgressLinkRedmine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROJECT_PROGRESS_LINK_ID")
    private Long projectProgressLinkId;

    @Column(name = "LINK_BA")
    private String linkBA;

    @Column(name = "LINK_DEV")
    private String linkDev;

    @Column(name = "LINK_TKCT")
    private String linkTKCT;

    @Column(name = "LINK_KBKT")
    private String linkKBKT;

    @Column(name = "LINK_TEST")
    private String linkTest;

    @Column(name = "LINK_FIXBUG")
    private String linkFixBug;

    @Column(name = "PROJECT_ID")
    private Long projectID;

    @Column(name = "PROJECT_LINK")
    private String linkProject;

}
