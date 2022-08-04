package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.dao.SchooleCodeMapper;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.param.SchooleCodeParam;
import com.saving.metadata.pojo.SchooleCode;
import com.saving.metadata.service.SchooleCodeService;
import com.saving.metadata.utils.BeanValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 * APi授权服务实现类
 * </p>
 *
 * @author 陈志强
 * @since 2020-02-24
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class SchooleCodeServiceImpl extends ServiceImpl<SchooleCodeMapper, SchooleCode> implements SchooleCodeService {

    private final SchooleCodeMapper schooleCodeMapper;

    @Autowired
    public SchooleCodeServiceImpl(SchooleCodeMapper schooleCodeMapper) {
        this.schooleCodeMapper = schooleCodeMapper;
    }

    @Override
    public void save(SchooleCodeParam param) throws ParseException {
        SchooleCode schooleCode = checkExist(param);
        update(schooleCode, new UpdateWrapper<SchooleCode>().lambda().eq(SchooleCode::getSchoolcode, schooleCode.getSchoolcode()));
    }

    @Override
    public void update(SchooleCodeParam param) {
        try {
            SchooleCode schooleCode = checkExist(param);
            if (StringUtils.isNotEmpty(param.getId())) {
                schooleCode.setId(param.getId());
                schooleCode.setSchoolcode(null);
                schooleCodeMapper.updateById(schooleCode);
            } else {
                throw new ParamException(ResponseCode.ERRORPARAM);
            }
        } catch (ParseException e) {
            log.error("时间转换错误!参数1:{},参数2:{}", param.getStarttime(), param.getEndtime(), e);
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
    }

    @Override
    public List<SchooleCode> opiton() {
        return list(new QueryWrapper<SchooleCode>().lambda()
                .select(SchooleCode::getSchoolcode, SchooleCode::getSchoolname)
                .and(obj -> obj.isNull(SchooleCode::getStarttime).isNull(SchooleCode::getEndtime))
                .orderByDesc(SchooleCode::getStatus)
                .orderByAsc(SchooleCode::getSchoolcode));
    }

    @Override
    public void updateByIdsIsDel(List<String> ids) {
        schooleCodeMapper.updateByIdsIsDel(ids);
    }


    private SchooleCode checkExist(SchooleCodeParam param) throws ParseException {
        BeanValidator.check(param);
        return SchooleCode.builder()
                .schoolcode(param.getSchoolcode())
                .apikey(param.getApikey())
                .starttime(DateUtils.parseDate(param.getStarttime(), Locale.CHINA, "yyyy-MM-dd HH:mm:ss"))
                .endtime(DateUtils.parseDate(param.getEndtime(), Locale.CHINA, "yyyy-MM-dd HH:mm:ss"))
                .build();

    }
}
