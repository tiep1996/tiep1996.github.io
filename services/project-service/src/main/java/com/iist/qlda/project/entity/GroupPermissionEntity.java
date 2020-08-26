package com.iist.qlda.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author dangnp
 * @created 24/07/2020 - 3:23 PM
 * @project services
 **/
@Data
@Entity
@Table(name = "GROUPPERMISSION")
@AllArgsConstructor
@NoArgsConstructor
public class GroupPermissionEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "STATE")
    private int state;

}
