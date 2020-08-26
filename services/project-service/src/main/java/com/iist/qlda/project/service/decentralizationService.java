package com.iist.qlda.project.service;

import com.iist.qlda.project.dto.PermissionDTO;

import java.util.List;

public interface decentralizationService {
    List<PermissionDTO> findByParams(PermissionDTO dto);
    void saveDecentralization(PermissionDTO dto);
}
