package com.iist.qlda.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name ="HUMAN_GROUP_USER")
@AllArgsConstructor
@NoArgsConstructor
public class HumanGroupUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "HUMAN_RESOURCE_ID")
    private Long humanResourceId;

    @Column(name = "GROUP_USER_ID")
    private Long groupUserId;

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
}
