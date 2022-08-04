package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.dao.ApiTableMapper;
import com.saving.metadata.dao.MetaDataFiledsMapper;
import com.saving.metadata.dao.MetaDataTablesMapper;
import com.saving.metadata.dto.ApiParamDto;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.param.ApiTableParam;
import com.saving.metadata.pojo.*;
import com.saving.metadata.service.ApiTableLogService;
import com.saving.metadata.service.ApiTableService;
import com.saving.metadata.service.OptionService;
import com.saving.metadata.utils.BeanValidator;
import com.saving.metadata.utils.IpUtils;
import com.saving.metadata.utils.JsonMapperUtil;
import com.saving.metadata.utils.UUID64Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 陈志强
 * @since 2020-04-09
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ApiTableServiceImpl extends ServiceImpl<ApiTableMapper, ApiTable> implements ApiTableService {


    public final static Map<String, String> LYMAPS = Maps.newLinkedHashMap();
    public final static Map<String, String> LYVALUEMAPS = Maps.newLinkedHashMap();

    static {
        LYMAPS.put("1", "购置");
        LYMAPS.put("2", "自研");
        LYMAPS.put("3", "社会捐赠");
        LYMAPS.put("4", "社会准捐");
        LYMAPS.put("5", "赠");
        LYMAPS.put("6", "其他");

        LYVALUEMAPS.put("购置", "1");
        LYVALUEMAPS.put("自研", "2");
        LYVALUEMAPS.put("社会捐赠", "3");
        LYVALUEMAPS.put("社会准捐", "4");
        LYVALUEMAPS.put("赠", "5");
        LYVALUEMAPS.put("其他", "6");
    }

    private final ApiTableMapper apiTableMapper;
    private final MetaDataTablesMapper metaDataTablesMapper;
    private final MetaDataFiledsMapper metaDataFiledsMapper;
    private final String errorMsg = "存在相同的软件编号";
    private final String YYYYMMMDD = "^([\\d]{4}((((0[13578]|1[02])((0[1-9])|([12][0-9])|(3[01])))|(((0[469])|11)((0[1-9])|([12][0-9])|30))|(02((0[1-9])|(1[0-9])|(2[0-8])))))|((((([02468][048])|([13579][26]))00)|([0-9]{2}(([02468][048])|([13579][26]))))(((0[13578]|1[02])((0[1-9])|([12][0-9])|(3[01])))|(((0[469])|11)((0[1-9])|([12][0-9])|30))|(02((0[1-9])|(1[0-9])|(2[0-9]))))){4})$";
    private final String YQXZ = "YQXZ";
    private final String SJGGHDQMC = "SJGGHDQMC";
    private final OptionService optionService;

    @Resource
    private ApiTableLogService apiTableLogService;

    @Autowired
    public ApiTableServiceImpl(ApiTableMapper apiTableMapper, MetaDataTablesMapper metaDataTablesMapper, OptionService optionService, MetaDataFiledsMapper metaDataFiledsMapper) {
        this.apiTableMapper = apiTableMapper;
        this.metaDataTablesMapper = metaDataTablesMapper;
        this.metaDataFiledsMapper = metaDataFiledsMapper;
        this.optionService = optionService;
    }

    @Override
    public void updateCheck(ApiTableParam param) {
        BeanValidator.check(param);
        if (StringUtils.isEmpty(param.getId())) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        ApiTable byId = getById(param.getId());
        if (byId == null) {
            throw new ParamException(ResponseCode.UPDATEERRORMSG);
        }
        if (checkExist(param.getId(), param.getSysName())) {
            throw new ParamException(errorMsg);
        }

        ApiTable apiTable = getApiTable(param);
        String warnMsg = checkObj(apiTable);
        if (StringUtils.isNotEmpty(warnMsg)) {
            throw new ParamException(warnMsg);
        }
        updateById(apiTable);
    }

    @Override
    public void saveCheck(ApiTableParam param) {
        BeanValidator.check(param);
        if (checkExist(null, param.getRjbh())) {
            throw new ParamException(errorMsg);
        }
        ApiTable apiTable = ApiTable.builder().build();
        BeanUtils.copyProperties(param, apiTable);
        apiTable.setId(UUID64Utils.get64UUIDString());
        apiTable.setSort(String.valueOf(count() + 1));
        String warnMsg = checkObj(apiTable);
        if (StringUtils.isNotEmpty(warnMsg)) {
            throw new ParamException(warnMsg);
        }
        save(apiTable);
    }


    @Override
    public String checkObj(ApiTable apiTable) {
        StringBuilder errs = new StringBuilder();
        if (StringUtils.isEmpty(LYMAPS.get(apiTable.getLy()))) {
            if (StringUtils.isEmpty(LYVALUEMAPS.get(apiTable.getLy()))) {
                errs.append(apiTable.getLy() + "不在规定范围的值空间内，请输入以下范围" + LYMAPS.keySet());
            }
        }
        if (!apiTable.getGzrq().matches(YYYYMMMDD)) {
            errs.append(apiTable.getGzrq() + "购置日期的格式不正确，应该为YYYYMMDD格式");
        }
        if (StringUtils.isNotEmpty(apiTable.getXzm())) {
            Option xzm = optionService.checkOptionValue(YQXZ, apiTable.getXzm());
            if (xzm == null) {
                errs.append(apiTable.getXzm() + "现状码的数据不规范,应该为3.2.7.10YQXZ仪器现状代码表中的代码");
            }
        }
//        Option gbm = optionService.checkOptionValue(SJGGHDQMC, apiTable.getScgbm());
//        if (gbm == null) {
//            errs.append(apiTable.getXzm() + "生产国别码的数据不规范,应该为3.1.5世界各国和地区名称代码表中的代码");
//        }
        return errs.toString();
    }

    @Override
    public List<ApiTable> transformOption(List<ApiTable> list) {
        Map<String, String> yqdmMaps = Maps.newLinkedHashMap();
        List<Option> yqdm = optionService.list(new QueryWrapper<Option>()
                .inSql("Hierarchy_ID_", "SELECT ID_ FROM METADATA_B_HIERARCHY  WHERE  TypeKey_='YQXZ'  and IsDelete_ !=1")
                .orderByAsc("Sort_"));
        for (Option option : yqdm) {
            yqdmMaps.put(option.getName(), option.getKey());
        }
        for (int i = 0; i < list.size(); i++) {
            if (StringUtils.isNotEmpty(LYVALUEMAPS.get(list.get(i).getLy()))) {
                list.get(i).setLy(LYVALUEMAPS.get(list.get(i).getLy()));
            }
            if (StringUtils.isNotEmpty(yqdmMaps.get(list.get(i).getXzm()))) {
                list.get(i).setXzm(yqdmMaps.get(list.get(i).getXzm()));
            }
        }
        return list;

    }

    @Override
    public void rjzcSync() {
        checkTable();
        apiTableMapper.syncRjzc(IpUtils.getIpAddr(RequestHolder.getCurrenRequest()));
    }

    @Override
    public void syncRjzc() {

        checkTable();
        User user = RequestHolder.getCurrenSysUser();
        apiTableMapper.rjzcSync(user.getUsername());
        int count = count();
        List<ApiTable> rjzcData = apiTableMapper.getRjzcData();
        for (int i = 0; i < rjzcData.size(); i++) {
            rjzcData.get(i).setId(UUID64Utils.get64UUIDString());
            rjzcData.get(i).setKey(rjzcData.get(i).getRjbh());
            rjzcData.get(i).setSort(String.valueOf(count + i + 1));
        }
        saveBatch(rjzcData, 200);
    }

    @Override
    public ApiTable findByRjbh(String rjbh) {
        return getOne(new QueryWrapper<ApiTable>().lambda().eq(ApiTable::getRjbh, rjbh));
    }

    @Override
    public ServerResponse apiFindData(ApiParamDto apiParamDto) {
        try {
            List<ApiParamDto.FilterField> filterField = apiParamDto.getFilterField();
            ApiTable apiTable = findByRjbh(apiParamDto.getSoftwareNumber());
            checkTableName(apiParamDto.getTableName());
            //判断查询字段
            checkTableField(apiParamDto.getTableName(), apiParamDto.getSelectField(), "selectField");
            if (CollectionUtils.isNotEmpty(filterField)) {
                //判断过滤字段是否存在
                checkFilterFieldName(apiParamDto.getTableName(), filterField, "filterField");
                //判断过滤方式的字符是否符合规范
                checkFilterType(filterField);
            }
            ApiTableLog apiTableLog = geiApiTableByRjbh(apiParamDto, "查询");
            if (apiParamDto.getIsPage()) {
                Page page = new Page(apiParamDto.getPageNum(), apiParamDto.getPageSize());
                List<Map<String, String>> list = metaDataTablesMapper.findApiParamDto(page, apiParamDto.getTableName(), apiParamDto.getSelectField(), apiParamDto.getFilterField());
                Page records = page.setRecords(list);
                apiTableLog.setDataCount(list.size());
                apiTableLogService.save(apiTableLog);
                return ServerResponse.createBySuccess("查询成功!数据以分页格式返回", records);
            } else {
                List<Map<String, String>> apiParamDtoNoPage = metaDataTablesMapper.findApiParamDtoNoPage(apiParamDto.getTableName(), apiParamDto.getSelectField(), apiParamDto.getFilterField());
                apiTableLog.setDataCount(apiParamDtoNoPage.size());
                apiTableLogService.save(apiTableLog);
                return ServerResponse.createBySuccess("查询成功!数据以未分页格式返回", apiParamDtoNoPage);
            }
        } catch (Exception e) {
            log.error(ResponseCode.ERRORPARAM, e);
        }
        return ServerResponse.createBySuccess("查无数据");
    }

    @Override
    public int apiUpdateData(ApiParamDto apiParamDto) {

        checkTableName(apiParamDto.getTableName());
        //判断更新字段是否存在
        checkFilterFieldName(apiParamDto.getTableName(), apiParamDto.getUpdateField(), "updateField");
        //判断过滤字段是否存在
        checkFilterFieldName(apiParamDto.getTableName(), apiParamDto.getFilterField(), "filterField");
        //判断过滤方式的字符是否符合规范
        checkFilterType(apiParamDto.getFilterField());
        //判断值是否正确
        checkFieldValue(apiParamDto.getFilterField(), apiParamDto.getTableName(), false);
        if (CollectionUtils.isEmpty(apiParamDto.getUpdateField())) {
            throw new ParamException("updateField字段不能为空!");
        }
        ApiTableLog apiTableLog = geiApiTableByRjbh(apiParamDto, "更新");
        int count = metaDataTablesMapper.apiUpdateData(apiParamDto.getUpdateField(), apiParamDto.getFilterField(), apiParamDto.getTableName());
        apiTableLog.setDataCount(count);
        apiTableLogService.save(apiTableLog);
        return count;
    }

    @Override
    public int apiDeleteData(ApiParamDto apiParamDto) {
        checkTableName(apiParamDto.getTableName());
        //判断过滤字段是否存在
        checkFilterFieldName(apiParamDto.getTableName(), apiParamDto.getFilterField(), "filterField");
        //判断过滤方式的字符是否符合规范
        checkFilterType(apiParamDto.getFilterField());
        ApiTableLog apiTableLog = geiApiTableByRjbh(apiParamDto, "删除");
        int count = metaDataTablesMapper.apiDeleteData(apiParamDto.getTableName(), apiParamDto.getFilterField());
        apiTableLog.setDataCount(count);
        apiTableLogService.save(apiTableLog);
        return count;
    }

    private ApiTableLog geiApiTableByRjbh(ApiParamDto apiParamDto, String type) {
        ApiTable apiTable = findByRjbh(apiParamDto.getSoftwareNumber());
        ApiTableLog apiTableLog = ApiTableLog.builder()
                .id(UUID64Utils.get64UUIDString())
                .operationIp(IpUtils.getIpAddr(RequestHolder.getCurrenRequest()))
                .operationSysId(apiTable.getId())
                .operationSysName(apiTable.getSysName())
                .jsonData(JsonMapperUtil.object2String(apiParamDto))
                .type(type)
                .tableId(metaDataTablesMapper.selectOne(new QueryWrapper<MetaDataTables>().lambda().eq(MetaDataTables::getTableName, apiParamDto.getTableName())).getId())
                .tableName(apiParamDto.getTableName())
                .build();
        return apiTableLog;
    }

    @Override
    public int apiInsertData(ApiParamDto apiParamDto) {
        checkTableName(apiParamDto.getTableName());
        //判断更新字段是否存在
        checkFilterFieldName(apiParamDto.getTableName(), apiParamDto.getUpdateField(), "updateField");
        //判断值是否正确
        checkFieldValue(apiParamDto.getFilterField(), apiParamDto.getTableName(), false);
        if (CollectionUtils.isEmpty(apiParamDto.getUpdateField())) {
            throw new ParamException("updateField字段不能为空!");
        }
        ApiTableLog apiTableLog = geiApiTableByRjbh(apiParamDto, "新增");
        int count = metaDataTablesMapper.apiInsertData(apiParamDto.getTableName(), apiParamDto.getUpdateField());
        apiTableLog.setDataCount(count);
        apiTableLogService.save(apiTableLog);
        return count;
    }

    /**
     * 判断过滤字段是否存在
     */
    public void checkFilterFieldName(String tableName, List<ApiParamDto.FilterField> filterField, String typeName) {
        List<String> filterFieldName = filterField.stream().map(ApiParamDto.FilterField::getFieldName).collect(Collectors.toList());
        checkTableField(tableName, filterFieldName, typeName);
    }


    /**
     * 效验表名是否存在  不单单包含元数据表名，也得包含状态数据平台表名
     *
     * @param tableName 表名
     */
    private void checkTableName(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            throw new ParamException("tableName参数错误，请检查后尝试！");
        }
        int count = apiTableMapper.checkTableName(tableName);
        if (count != 1) {
            throw new ParamException("tableName参数错误；找不到对应的表，请检查后尝试！");
        }
    }


    /**
     * 效验字段名是否存在
     *
     * @param tableName 表名
     * @param lists     字段列表
     * @param typeName  类型名称
     */
    private void checkTableField(String tableName, List<String> lists, String typeName) {
        List<String> list = apiTableMapper.findByTableName(tableName);
        if (list.size() < 1) {
            throw new ParamException("tableName参数错误；找不到对应的表，请检查后尝试！");
        }
        StringBuilder noneFields = new StringBuilder();
        lists.forEach(obj -> {
            if (!list.contains(obj)) {
                noneFields.append(obj).append(",");
            }
        });
        if (noneFields.toString().length() > 0) {
            throw new ParamException(typeName + "参数中" + noneFields.substring(0, noneFields.toString().length() - 1) + "字段不存在，请检查后尝试！");
        }
    }

    /**
     * 效验过滤判断方式是否合法 filterType
     *
     * @param filterFields 判断参数组
     */
    private void checkFilterType(List<ApiParamDto.FilterField> filterFields) {
        StringBuilder errMsg = new StringBuilder();
        List<String> lists = Arrays.asList("=", "!=", ">", "<", "<=", ">=", "like", "LIKE");
        filterFields.forEach(obj -> {
            if (!lists.contains(obj.getFilterType())) {
                errMsg.append(obj.getFieldName()).append("字段判断方式错误!</br>");
            }
        });
        if (errMsg.length() > 0) {
            throw new ParamException(errMsg.toString());
        }
    }

    //todo 效验值长度，类型，默认格式

    /**
     * 效验值长度，类型，默认格式
     *
     * @param filterFields 准备增加的字段组
     * @param tableName    表名
     * @param isInsert     是否新增
     */
    private void checkFieldValue(List<ApiParamDto.FilterField> filterFields, String tableName, Boolean isInsert) {
        List<MetaDataFileds> fieldsList = metaDataFiledsMapper.selectList(new QueryWrapper<MetaDataFileds>()
                .lambda().eq(MetaDataFileds::getTableId, metaDataTablesMapper.selectOne(new QueryWrapper<MetaDataTables>().lambda().eq(MetaDataTables::getTableName, tableName)).getId()));
        Map<String, MetaDataFileds> maps = Maps.newLinkedHashMap();
        Map<String, MetaDataFileds> keyMaps = Maps.newLinkedHashMap();
        fieldsList.forEach(obj -> {
            maps.put(obj.getFiledName(), obj);
            if (obj.getIsNotNull() == 1 && isInsert) {
                keyMaps.put(obj.getFiledName(), obj);
            }
        });
        StringBuilder finalErrMsg = new StringBuilder();
        filterFields.forEach(obj -> {
            MetaDataFileds dataFields = maps.get(obj.getFieldName());
            //判断值类型
            if (!("T".equals(dataFields.getStorageType()) || "B".equals(dataFields.getStorageType()))) {
                if ("C".equals(dataFields.getStorageType())) {
                    //判断长度
                    if (obj.getValues().length() > maps.get(obj.getFieldName()).getFiledLength()) {
                        finalErrMsg.append(obj.getFieldName()).append("字段值长度大于限制长度！</br>");
                    }
                }
            }
            if (isInsert) {
                keyMaps.remove(obj.getFieldName());
            }
        });
        if (finalErrMsg.length() > 0) {
            throw new ParamException(finalErrMsg.toString());
        }
        if (isInsert) {
            StringBuilder errMsg = new StringBuilder();
            if (keyMaps.size() > 0) {
                keyMaps.forEach((key, value) -> {
                    errMsg.append(value.getFiledName()).append(",");
                });
                errMsg.substring(0, errMsg.length() - 1);
                errMsg.append("缺少，缺少的字段不能为空!");
                throw new ParamException(errMsg.toString());
            }
        }
    }


    private Boolean checkExist(String id, String rjbh) {
        return count(new QueryWrapper<ApiTable>().lambda().eq(ApiTable::getRjbh, rjbh)
                .ne(StringUtils.isNotEmpty(id), ApiTable::getId, id)) > 0;
    }

    private ApiTable getApiTable(ApiTableParam param) {
        ApiTable apiTable = getById(param.getId());
        if (apiTable == null) {
            throw new ParamException("找不到需要修改的业务系统,请检查软件系统是否存在!");
        }
        ApiTable newApiTable = ApiTable.builder().build();
        BeanUtils.copyProperties(param, newApiTable);
        return newApiTable;
    }

    private void checkTable() {
        if (metaDataTablesMapper.getCheckTableCount("XXSJ_RJZC") != 1) {
            throw new ParamException("软件资产同步表不存在请在元数据中生成后重试！");
        }
    }
}
