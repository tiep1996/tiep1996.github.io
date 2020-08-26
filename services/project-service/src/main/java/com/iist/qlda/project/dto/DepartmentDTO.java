package com.iist.qlda.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author dangnp
 * @created 24/07/2020 - 3:28 PM
 * @project services
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
    private Long id;

    private String name;

    private String description;

    private String status;
}
