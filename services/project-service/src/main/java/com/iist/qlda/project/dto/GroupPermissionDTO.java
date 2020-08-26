package com.iist.qlda.project.dto;

import common.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author dangnp
 * @created 24/07/2020 - 3:27 PM
 * @project services
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupPermissionDTO {
    private Long id;

    @NotNull(message = Constants.NAME_NULL)
    @NotBlank(message = Constants.NAME_NULL)
    private String code;

    @NotNull(message = Constants.NAME_NULL)
    @NotBlank(message = Constants.NAME_NULL)
    private String name;

    private String note;

    @NotNull(message = Constants.STATUS_NULL)
    @NotBlank(message = Constants.STATUS_NULL)
    private String status;

    private List<HumanResourcesDTO> humanResourcesList;
    @NotEmpty(message = Constants.PERMISSIONS_EMPTY)
    private List<PermissionDTO> permissionList;

    private int state;
}
