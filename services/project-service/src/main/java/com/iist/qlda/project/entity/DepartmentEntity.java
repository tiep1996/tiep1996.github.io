package com.iist.qlda.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author dangnp
 * @created 24/07/2020 - 3:28 PM
 * @project services
 **/
@Data
@Entity
@Table(name = "DEPARTMENT")
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private String status;

}
