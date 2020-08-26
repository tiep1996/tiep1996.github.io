package com.iist.qlda.project.service.impl;

import com.iist.qlda.project.dto.ProjectProgressLinkRedmineDto;
import com.iist.qlda.project.entity.ProjectProgressEntity;
import com.iist.qlda.project.entity.ProjectProgressLinkRedmine;
import com.iist.qlda.project.repository.jparepository.ProjectProgressLinkRedmineRepository;
import com.iist.qlda.project.repository.jparepository.ProjectProgressRepository;
import com.iist.qlda.project.repository.jparepository.ProjectRepository;
import com.iist.qlda.project.service.ProjectProgressService;
import com.iist.qlda.project.service.mapper.ProjectProgressLinkRedmineMapper;
import exception.CustomExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author nuctv
 * @since 11 ,7/24/2020 , 2020
 */
@Service
public class ProjectProgressServiceImpl implements ProjectProgressService {
    private final Logger log = LogManager.getLogger(ProjectProgressServiceImpl.class);
    @Value("${keyAdmin}")
    private String keyAdmin;
    @Autowired
    private ProjectProgressRepository projectProgressRepository;
    @Autowired
    private ProjectProgressLinkRedmineRepository projectProgressLinkRedmineRepository;
    @Autowired
    private ProjectProgressLinkRedmineMapper projectProgressLinkRedmineMapper;
    @Autowired
    private ProjectRepository projectRepository;

    // lưu lại link liên kết với redmine của tiến độ Ba, TEST,DEV, TKCT, KBKT, fixBug
    @Override
    public ProjectProgressLinkRedmineDto saveLinkRedmine(ProjectProgressLinkRedmineDto dto) {
        try {
            ProjectProgressLinkRedmine entity = projectProgressLinkRedmineRepository.findByProjectID(dto.getProjectID());
            ProjectProgressEntity progressEntity = projectProgressRepository.findByProjectId(dto.getProjectID());
            if (null == progressEntity) {
                progressEntity = new ProjectProgressEntity();
                progressEntity.setProjectId(dto.getProjectID());
                projectProgressRepository.save(progressEntity);
            }
            if (null == entity) {
                entity = new ProjectProgressLinkRedmine();
                entity.setProjectID(dto.getProjectID());
            }
            entity.setLinkBA(dto.getLinkBA());
            entity.setLinkDev(dto.getLinkDev());
            entity.setLinkFixBug(dto.getLinkFixBug());
            entity.setLinkKBKT(dto.getLinkKBKT());
            entity.setLinkTKCT(dto.getLinkTKCT());
            entity.setLinkTest(dto.getLinkTest());
            entity.setLinkProject(dto.getLinkProject());
            return projectProgressLinkRedmineMapper.toDto(projectProgressLinkRedmineRepository.save(entity));
        } catch (Exception e) {
            throw new CustomExceptionHandler("khong tìm thấy thể lưu link", HttpStatus.BAD_REQUEST);
        }

    }

    // thực hiện đồng bộ với redmine
    @Override
    @Scheduled(cron = "${autoSynchronized}")
    public List<String> synchronizedWithRedmine() {
        List<String> errorProject = new ArrayList<>();
        List<ProjectProgressEntity> list = projectProgressRepository.getAllWithProjectActive();
        List<ProjectProgressEntity> result = new ArrayList<>();
        int length = list.size();
        for (int i = 0; i < length; i++) {
            ProjectProgressEntity entity = new ProjectProgressEntity();
            entity = callApiRedmine(list.get(i));
            if (null == entity) {
                errorProject.add(projectRepository.findById(list.get(i).getProjectId()).get().getCode());
            } else {
                result.add(entity);
            }
        }
        if (!result.isEmpty()) {
            projectProgressRepository.saveAll(result);
        }
        return errorProject;
    }

    @Override
    public Map<String, Object> getLInkRedmineByProjectID(Long id) {
        String codeProject = projectRepository.findById(id).get().getCode();
        Map<String, Object> map = new HashMap<>();
        map.put("code", codeProject);
        map.put("link", projectProgressLinkRedmineMapper.toDto(projectProgressLinkRedmineRepository.findByProjectID(id)));
        return map;
    }

    @Override
    public List<String> checkLinkExist(ProjectProgressLinkRedmineDto dto) {
        HttpHeaders headers = new HttpHeaders();
        List<String> errorLink = new ArrayList<>();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        JSONObject jsonObject = null;
        if (null != dto.getLinkProject() && !"".equals(dto.getLinkProject())) {
            try {
                response = restTemplate.exchange(dto.getLinkProject() + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                jsonObject.getJSONObject("project");
            } catch (Exception e) {
                errorLink.add("pro");
            }
        }
        if (null != dto.getLinkBA() && !"".equals(dto.getLinkBA())) {
            try {
                response = restTemplate.exchange(dto.getLinkBA() + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                jsonObject.getJSONObject("issue").getLong("done_ratio");
            } catch (Exception e) {
                errorLink.add("ba");
            }
        }

        if (null != dto.getLinkDev() && !"".equals(dto.getLinkDev())) {
            try {
                response = restTemplate.exchange(dto.getLinkDev() + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                jsonObject.getJSONObject("issue").getLong("done_ratio");
            } catch (Exception e) {
                errorLink.add("dev");
            }
        }

        if (null != dto.getLinkKBKT() && !"".equals(dto.getLinkKBKT())) {
            try {
                response = restTemplate.exchange(dto.getLinkKBKT() + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                jsonObject.getJSONObject("issue").getLong("done_ratio");
            } catch (Exception e) {
                errorLink.add("kbkt");
            }
        }
        if (null != dto.getLinkTest() && !"".equals(dto.getLinkTest())) {
            try {
                response = restTemplate.exchange(dto.getLinkTest() + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                jsonObject.getJSONObject("issue").getLong("done_ratio");
            } catch (Exception e) {
                errorLink.add("test");
            }
        }
        if (null != dto.getLinkFixBug() && !"".equals(dto.getLinkFixBug())) {
            try {
                response = restTemplate.exchange(dto.getLinkFixBug() + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                jsonObject.getJSONObject("issue").getLong("done_ratio");
            } catch (Exception e) {
                errorLink.add("fixbug");
            }
        }
        if (null != dto.getLinkTKCT() && !"".equals(dto.getLinkTKCT())) {
            try {
                response = restTemplate.exchange(dto.getLinkTKCT() + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                jsonObject.getJSONObject("issue").getLong("done_ratio");
            } catch (Exception e) {
                errorLink.add("tkct");
            }
        }
        return errorLink;
    }

    public ProjectProgressEntity callApiRedmine(ProjectProgressEntity entity) {

        log.info("----->call api from redmine");
        Long id = entity.getProjectId();
        ProjectProgressLinkRedmine projectProgressLinkRedmine = projectProgressLinkRedmineRepository.findByProjectID(id);
        String baURL = projectProgressLinkRedmine.getLinkBA();
        String devURL = projectProgressLinkRedmine.getLinkDev();
        String testURL = projectProgressLinkRedmine.getLinkTest();
        String tkctURL = projectProgressLinkRedmine.getLinkTKCT();
        String kbctURL = projectProgressLinkRedmine.getLinkKBKT();
        String fixbugURL = projectProgressLinkRedmine.getLinkFixBug();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        JSONObject jsonObject = null;
        try {
            if (null == baURL || "".equals(baURL.trim())) {
                entity.setBaProgress(null);
            } else {
                response = restTemplate.exchange(baURL + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                entity.setBaProgress(jsonObject.getJSONObject("issue").getLong("done_ratio"));
            }

            if (null == devURL || "".equals(devURL.trim())) {
                entity.setDevProgress(null);
            } else {
                response = restTemplate.exchange(devURL + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                entity.setDevProgress(jsonObject.getJSONObject("issue").getLong("done_ratio"));
            }

            if (null == testURL || "".equals(testURL.trim())) {
                entity.setTestProgress(null);
            } else {
                response = restTemplate.exchange(testURL + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                entity.setTestProgress(jsonObject.getJSONObject("issue").getLong("done_ratio"));
            }
            if (null == tkctURL || "".equals(tkctURL.trim())) {
                entity.setDocProgress(null);
            } else {
                response = restTemplate.exchange(tkctURL + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                entity.setDocProgress(jsonObject.getJSONObject("issue").getLong("done_ratio"));
            }
            if (null == kbctURL || "".equals(kbctURL.trim())) {
                entity.setKbktProgress(null);
            } else {
                response = restTemplate.exchange(kbctURL + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                entity.setKbktProgress(jsonObject.getJSONObject("issue").getLong("done_ratio"));
            }
            if (null == fixbugURL || "".equals(fixbugURL.trim())) {
                entity.setRfProgress(null);
            } else {
                response = restTemplate.exchange(fixbugURL + ".json?key=" + keyAdmin, HttpMethod.GET, httpEntity, String.class);
                jsonObject = new JSONObject(response.getBody());
                entity.setRfProgress(jsonObject.getJSONObject("issue").getLong("done_ratio"));
            }
        } catch (Exception e) {
            return null;
        }
        return entity;
    }
}
