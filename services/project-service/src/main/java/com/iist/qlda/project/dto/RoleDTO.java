package com.iist.qlda.project.dto;

import lombok.Data;

/**
 * @author dangnp
 * @created 19/08/2020 - 9:44 AM
 * @project services
 **/
@Data
public class RoleDTO {
    private Long id;
    private String code;
    private String name;

    public RoleDTO() {

    }

    public RoleDTO(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
}
