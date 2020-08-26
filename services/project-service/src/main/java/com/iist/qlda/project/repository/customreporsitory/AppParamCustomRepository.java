package com.iist.qlda.project.repository.customreporsitory;

import com.iist.qlda.project.dto.AppParamDTO;
import com.iist.qlda.project.dto.DTOSearch;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AppParamCustomRepository {
    private final Logger log = LogManager.getLogger(AppParamCustomRepository.class);

    @Autowired
    private EntityManager em;

    //ANHTT_IIST lay danh sach param theo partype
    public List<AppParamDTO> getAppParamBytype(DTOSearch dto){
        log.info("---------------------sql get thong tin doi tac------------------");
        StringBuilder sql = new StringBuilder();
        sql.append("    select ");
        sql.append("    app.APP_PARAMS_ID as id, ");
        sql.append("    app.PAR_CODE as code, ");
        sql.append("    app.PAR_NAME as name ");
        sql.append("    from APP_PARAMS app ");
        sql.append("    where 1=1 and app.IS_ACTIVE=1 ");
        if(StringUtils.isNotBlank(dto.getKeySearch())){
            sql.append("    and (( upper(app.PAR_NAME) LIKE (:keySearch)) or (upper(app.PAR_CODE) LIKE (:keySearch))) ");
        }
        if(StringUtils.isNotBlank(dto.getType())){
            sql.append("    and upper(app.PAR_TYPE) LIKE upper(:parType)");
        }

        sql.append(" ORDER BY app.PAR_CODE ASC ");

        Query query = em.createNativeQuery(sql.toString());

        if(StringUtils.isNotBlank(dto.getKeySearch())){
            query.setParameter("keySearch", "%" + dto.getKeySearch() + "%");
        }
        if(StringUtils.isNotBlank(dto.getType())){
            query.setParameter("parType", dto.getType());
        }
        List<Object[]> lstObject = query.getResultList();
        return convertToDto(lstObject);
    }

    //anhtt_iist convert object to dto
    public List<AppParamDTO> convertToDto(List<Object[]> lstObject){
        List<AppParamDTO> listAppDto = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(lstObject)){
            for(Object[] obj : lstObject){
                AppParamDTO paramDTO = new AppParamDTO();
                paramDTO.setId(((BigInteger)obj[0]).longValue());
                paramDTO.setCode((String) obj[1]);
                paramDTO.setName((String) obj[2]);
                listAppDto.add(paramDTO);
            }
        }
        return listAppDto;
    }
}
