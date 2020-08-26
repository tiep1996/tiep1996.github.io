package com.iist.qlda.project.entity;

import javax.persistence.*;

import lombok.*;

import java.util.Date;

@Data
@Entity
@Table(name = "PRODUCT_HANDOVER")
@AllArgsConstructor
@NoArgsConstructor
public class ProductHandoverEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_HANDOVER_ID")
    private Long productHandoverId;

    @Column(name = "PROJECT_ID")
    private Long projectId;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "STATUS_HANDOVER")
    private Integer statusHandover;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "CREATE_BY")
    private Long createBy;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Column(name = "UPDATE_BY")
    private Long updateBy;

}
