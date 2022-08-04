package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.dao.MetaDataFiledsMapper;
import com.saving.metadata.dao.MetaDataTablesMapper;
import com.saving.metadata.dao.UserMapper;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.exception.PermissionException;
import com.saving.metadata.pojo.*;
import com.saving.metadata.service.*;
import com.saving.metadata.utils.*;
import com.saving.metadata.vo.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 陈志强
 * @since 2019-12-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MetaDataTablesServiceImpl extends ServiceImpl<MetaDataTablesMapper, MetaDataTables> implements MetaDataTablesService {
    @Autowired
    private MetaDataTablesMapper metaDataTablesMapper;
    @Autowired
    private MetaDataFiledsMapper metaDataFiledsMapper;
    @Autowired
    private OperationTableLogService operationTableLogService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OptionService optionService;
    @Autowired
    private HierarchyService hierarchyService;
    @Autowired
    private DwdxxbjzService dwdxxbjzService;
    @Autowired
    private DwdxxbzdmysService dwdxxbzdmysService;
    @Autowired
    private MetaDataFiledsService metaDataFiledsService;


    @Value("${application.syncUrl}")
    private String syncUrl;

    @Value("${application.apiKey}")
    private String apiKey;

    @Value("${application.isCloud}")
    private Boolean isCloud;

    @Override
    public void importExcel(List<Map<Integer, String>> listMap, String metadataTypeId, Integer isStandard) {
        if (StringUtils.isEmpty(metadataTypeId)) {
            throw new ParamException("元数据类型尚未选择!");
        }
        Map<String, String> maps = Maps.newHashMap();
        maps.put("扩展", "2");
        maps.put("标准", "1");
        maps.put("自建", "0");
        ArrayList<MetaDataTables> hierarchies = Lists.newArrayList();
        ArrayList<String> keys = Lists.newArrayList();
        StringBuilder tableName = new StringBuilder();
        Map<String, String> keyMaps = Maps.newHashMap();
        StringBuffer errMsg = new StringBuffer();
        int count = count(new QueryWrapper<MetaDataTables>().lambda().eq(MetaDataTables::getIsDelete, 0));
        AtomicInteger num = new AtomicInteger(count);
        listMap.forEach(LambdaUtils.consumerWithIndex((obj, index) -> {
            if (obj.size() < 6 && isCloud) {
                errMsg.append("第" + (index + 1) + "行记录中的列数缺少，应为目录、表名、中文表名、字段数量、备注、性质</br>");
            } else if (StringUtils.isEmpty(obj.get(0))) {
                errMsg.append("第" + (index + 1) + "行记录中的目录不能为空;</br>");
            } else if (StringUtils.isEmpty(obj.get(1))) {
                errMsg.append("第" + (index + 1) + "行记录中的表名不能为空;</br>");
            } else if (StringUtils.isEmpty(obj.get(2))) {
                errMsg.append("第").append((index + 1)).append("行记录中的中文表名不能为空;</br>");
            } else if (StringUtils.isNotEmpty(keyMaps.get(obj.get(1))) || StringUtils.isNotEmpty(keyMaps.get(obj.get(2)))) {
                errMsg.append("第").append((index + 1)).append("行记录中的表名必须唯一,导入文件中存在重复;</br>");
            } else if (StringUtils.isEmpty(obj.get(5)) && isCloud) {
                errMsg.append("第" + (index + 1) + "行记录中的列数缺少性质</br>");
            } else if (StringUtils.isEmpty(maps.get(obj.get(5))) && isCloud) {
                errMsg.append("第" + (index + 1) + "行记录中的性质值应该为自建、标准、扩展</br>");
            } else {
                num.getAndIncrement();
            }
            keyMaps.put(obj.get(1), obj.get(2));
            MetaDataTables build = MetaDataTables.builder()
                    .tableName(obj.get(1) != null ? obj.get(1).replaceAll(" ", "") : null)
                    .id(UUID64Utils.get64UUIDString())
                    .chineseTableName(obj.get(2) != null ? obj.get(2).trim() : null)
                    .catalog(obj.get(0) != null ? obj.get(0).trim() : null)
                    .remark(obj.size() < 4 ? null : obj.get(3).trim())
                    .isStandard(isStandard)
                    .metadataTypeId(metadataTypeId)
                    .nature(Integer.valueOf(maps.get(obj.get(5))))
                    .sort(String.valueOf(num.get()))
                    .cdmpVersion(DateUtil.getYyyyMmDd())
                    .build();
            keys.add(obj.get(1) != null ? obj.get(1).trim() : null);
            tableName.append(build.getTableName()).append("(").append(build.getChineseTableName()).append("),");
            hierarchies.add(build);
        }));
        if (errMsg.length() > 0) {
            throw new ParamException(errMsg.toString());
        }
        List<MetaDataTables> list = list(new QueryWrapper<MetaDataTables>().lambda().and(obj -> obj.in(MetaDataTables::getTableName, keys)).eq(MetaDataTables::getMetadataTypeId, metadataTypeId)
                .eq(MetaDataTables::getSchoolCode, RequestHolder.getCurrenSysUser().getSchoolCode()));
        list.forEach(obj -> errMsg.append("记录中的").append(obj.getTableName()).append("(").append(obj.getChineseTableName()).append("）存在重复记录</br>"));
        if (errMsg.length() > 0) {
            throw new ParamException(errMsg.toString());
        }
        saveBatch(hierarchies, 220);
        if (tableName.length() > 0) {
            operationTableLogService.saveLog(tableName.substring(0, tableName.toString().length() - 1), "批量导入表记录", "批量导入表记录成功!");
        }
    }

    @Override
    public Page<MetaDataTablesVo> getMetaDataTablesVo(Page<MetaDataTablesVo> page, String name, Integer isStandard, Integer tableStatus, String startTime, String endTime, String metadataTypeId, Integer nature) {
        return page.setRecords(metaDataTablesMapper.getMetaDataTablesVo(page, name, startTime, endTime, RequestHolder.getCurrenSysUser().getSchoolCode(), isStandard, metadataTypeId, tableStatus, nature));
    }

    @Override
    public void createDataBase(List<String> ids) {
        List<MetaDataTables> metaDataTables = listByIds(ids);
        Map<String, List<MetaDataFileds>> maps = Maps.newLinkedHashMap();
        StringBuilder tableName = new StringBuilder();
        metaDataTables.forEach(obj -> {
            List<MetaDataFileds> metaDataFileds = getMetaDataFileds(obj.getId());
            if (CollectionUtils.isEmpty(metaDataFileds)) {
                throw new ParamException(obj.getTableName() + "表不存在字段记录请检查后重试!");
            }
            metaDataTablesMapper.createDataTable(obj.getTableName(), metaDataFileds);
            MetaDataTables tables = MetaDataTables.builder().id(obj.getId()).tableStatus(1).build();

            metaDataTablesMapper.updateById(tables);
            maps.put(obj.getId(), metaDataFileds);
            tableName.append(obj.getTableName()).append("(").append(obj.getChineseTableName()).append("),");
        });
        if (tableName.length() > 0) {
            operationTableLogService.saveLog(tableName.substring(0, tableName.toString().length() - 1), "批量根据记录生成表", "批量根据记录生成" + metaDataTables.size() + "张表成功!");
        }
    }

    @Override
    public void updateTableFiledNumber(String tableId) {
        MetaDataTables metaDataTables = metaDataTablesMapper.selectById(tableId);
        if (metaDataTables == null) {
            throw new ParamException(ResponseCode.UPDATEERRORMSG);
        }
        Integer filedSize = metaDataFiledsMapper.selectCount(new QueryWrapper<MetaDataFileds>().lambda().eq(MetaDataFileds::getTableId, tableId));
        metaDataTables.setFieldSize(filedSize);
        metaDataTables.setSortId(null);
        updateById(metaDataTables);
    }

    @Override
    public void delDataBase(List<String> ids) {
        List<MetaDataTables> metaDataTables = listByIds(ids);
        StringBuilder tableName = new StringBuilder();
        metaDataTables.forEach(obj -> {
            if (obj.getTableStatus() == 1) {
                metaDataTablesMapper.delDataTable(obj.getTableName());
                MetaDataTables tables = MetaDataTables.builder().id(obj.getId()).tableStatus(0).cdmpVersion(DateUtil.getYyyyMmDd()).build();
                metaDataTablesMapper.updateById(tables);
                tableName.append(obj.getTableName()).append("(").append(obj.getChineseTableName()).append("),");
            }
        });
        if (tableName.length() > 0) {
            operationTableLogService.saveLog(tableName.substring(0, tableName.toString().length() - 1), "批量删除实际表", "批量删除" + metaDataTables.size() + "张表成功!");
        }

    }

    @Override
    public void syncDataBase() {
        metaDataTablesMapper.syncDataBase();
        operationTableLogService.saveLog("全部表更新表状态", "更新表状态", "批量更新表状态成功!");
    }

    @Override
    public Page<AicTablesVo> getZtptListPage(Page<AicTablesVo> page) {

        return page.setRecords(userMapper.findZtpt(page));
    }

    @Override
    public ServerResponse syncAllData() {
        List<List> lists = Lists.newArrayList();
        List<MetaDataTables> metaDataTables = metaDataTablesMapper.selectList(new QueryWrapper<>());
        List<MetaDataFileds> metaDataFileds = metaDataFiledsMapper.selectList(new QueryWrapper<>());
        List<Option> optionList = optionService.list();
        List<Hierarchy> hierarchyList = hierarchyService.list();
        List<Dwdxxbjz> dwdxxbjzList = dwdxxbjzService.list();
        List<Dwdxxbzdmys> dwdxxbzdmysList = dwdxxbzdmysService.list();
        lists.add(metaDataTables);
        lists.add(metaDataFileds);
        lists.add(optionList);
        lists.add(hierarchyList);
        lists.add(dwdxxbjzList);
        lists.add(dwdxxbzdmysList);
        return ServerResponse.createBySuccess(lists);
    }

    @Override
    public ServerResponse syncMaxVersion() {
        return ServerResponse.createBySuccess(metaDataTablesMapper.syncMaxVersion());
    }

    @Override
    public String updateSync(String str) {
        Map<String, Object> maps = Maps.newHashMap();
        String userName = RequestHolder.getCurrenSysUser().getUsername();
        maps.put("apiKey", apiKey);
        maps.put("schoolCode", Objects.requireNonNull(RequestHolder.getCurrenSysUser()).getSchoolCode());
        String urlPathData = UrlUtil.getHttpInterface(syncUrl + "metadata/metaDataTables/syncAllData.json", maps);
        ProgressSingleton.put(str + userName, 20);
        Map<Object, Object> map = (Map) JSONObject.fromObject(urlPathData);
        List list;
        if (map.size() > 0 && !(map.get("data") instanceof JSONNull)) {
            list = (List) map.get("data");
        } else {
            throw new PermissionException((String) map.get("msg"));
        }
        if (list.size() > 0) {
            List<MetaDataTables> metaDataTablesList = Lists.newArrayList();
            List<MetaDataFileds> metaDataFiledsList = Lists.newArrayList();
            List<Option> optionList = Lists.newArrayList();
            List<Hierarchy> hierarchyList = Lists.newArrayList();
            List<Dwdxxbjz> dwdxxbjzList = Lists.newArrayList();
            List<Dwdxxbzdmys> dwdxxbzdmysList = Lists.newArrayList();
            if (StringUtils.isNotEmpty(list.get(0).toString())) {
                metaDataTablesList = JSONArray.toList(JSONArray.fromObject(list.get(0)), new MetaDataTables(), new JsonConfig());
            }
            if (StringUtils.isNotEmpty(list.get(1).toString())) {
                metaDataFiledsList = JSONArray.toList(JSONArray.fromObject(list.get(1)), new MetaDataFileds(), new JsonConfig());

            }
            if (StringUtils.isNotEmpty(list.get(2).toString())) {
                optionList = JSONArray.toList(JSONArray.fromObject(list.get(2)), new Option(), new JsonConfig());
            }
            if (StringUtils.isNotEmpty(list.get(3).toString())) {
                hierarchyList = JSONArray.toList(JSONArray.fromObject(list.get(3)), new Hierarchy(), new JsonConfig());
            }
            if (StringUtils.isNotEmpty(list.get(4).toString())) {
                dwdxxbjzList = JSONArray.toList(JSONArray.fromObject(list.get(4)), new Dwdxxbjz(), new JsonConfig());
            }
            if (StringUtils.isNotEmpty(list.get(5).toString())) {
                dwdxxbzdmysList = JSONArray.toList(JSONArray.fromObject(list.get(5)), new Dwdxxbzdmys(), new JsonConfig());
            }
            metaDataTablesMapper.createTempTable();
            ProgressSingleton.put(str + userName, 25);
            if (CollectionUtils.isNotEmpty(metaDataTablesList)) {
                List<List<MetaDataTables>> subMetaDataTables = MySubTUtil.subList(metaDataTablesList, 100);
                for (List<MetaDataTables> subList : subMetaDataTables) {
                    metaDataTablesMapper.insertTempMetaDataTables(subList);
                }
            }
            ProgressSingleton.put(str + userName, 30);
            if (CollectionUtils.isNotEmpty(metaDataFiledsList)) {
                List<List<MetaDataFileds>> subMetaDataFileds = MySubTUtil.subList(metaDataFiledsList, 50);
                for (List<MetaDataFileds> subList : subMetaDataFileds) {
                    metaDataTablesMapper.insertTempMetaDataFileds(subList);
                }
            }
            ProgressSingleton.put(str + userName, 35);
            if (CollectionUtils.isNotEmpty(optionList)) {
                List<List<Option>> subOption = MySubTUtil.subList(optionList, 50);
                for (List<Option> subList : subOption) {
                    metaDataTablesMapper.insertTempOption(subList);
                }
            }
            ProgressSingleton.put(str + userName, 40);
            if (CollectionUtils.isNotEmpty(hierarchyList)) {
                List<List<Hierarchy>> subHierarchy = MySubTUtil.subList(hierarchyList, 50);
                for (List<Hierarchy> subList : subHierarchy) {
                    metaDataTablesMapper.insertTempHierarchy(subList);
                }
            }
            if (CollectionUtils.isNotEmpty(dwdxxbjzList)) {
                List<List<Dwdxxbjz>> subDwdxxbjz = MySubTUtil.subList(dwdxxbjzList, 50);
                for (List<Dwdxxbjz> subList : subDwdxxbjz) {
                    metaDataTablesMapper.insertTempDwdxxbjz(subList);
                }
            }

            if (CollectionUtils.isNotEmpty(dwdxxbzdmysList)) {
                List<List<Dwdxxbzdmys>> subDwdxxbzdmys = MySubTUtil.subList(dwdxxbzdmysList, 50);
                for (List<Dwdxxbzdmys> subList : subDwdxxbzdmys) {
                    metaDataTablesMapper.insertTempDwdxxbzdmys(subList);
                }
            }
            ProgressSingleton.put(str + userName, 45);
            //进行增量插入操作返回更新数目
            int insertMetaTableSize = metaDataTablesMapper.insertSyncDataMetaTable(userName);
            int insertMetaFiledSize = metaDataTablesMapper.insertSyncDataMetaFiled(userName);
            int insertOptionSize = metaDataTablesMapper.insertSyncDataOption(userName);
            int insertHierarchyListSize = metaDataTablesMapper.insertSyncDataHierarchyList(userName);
            int insertSyncDwdxxbjz = metaDataTablesMapper.insertSyncDwdxxbjz(userName);
            int insertSyncDwdxxbzdmys = metaDataTablesMapper.insertSyncDwdxxbzdmys(userName);
            ProgressSingleton.put(str + userName, 60);
            // 进行增量更新操作返回更新数目
            int updateMetaTableSize = metaDataTablesMapper.updateSyncDataMetaTable(userName);
            int updateMetaFiledSize = metaDataTablesMapper.updateSyncDataMetaFiled(userName);
            int updateOptionSize = metaDataTablesMapper.updateSyncDataOption(userName);
            int updateHierarchyListSize = metaDataTablesMapper.updateSyncDataHierarchyList(userName);
            int updateSyncDwdxxbjz = metaDataTablesMapper.updateSyncDwdxxbjz(userName);
            int updateSyncDwdxxbzdmys = metaDataTablesMapper.updateSyncDwdxxbzdmys(userName);
            ProgressSingleton.put(str + userName, 70);
            //进行删除操作返回更新数目
            int delMetaTableSize = metaDataTablesMapper.delSyncDataMetaTable(userName);
            int delMetaFiledSize = metaDataTablesMapper.delSyncDataMetaFiled(userName);
            int delOptionSize = metaDataTablesMapper.delSyncDataOption(userName);
            int delHierarchyListSize = metaDataTablesMapper.delSyncDataHierarchyList(userName);
            int delSyncDwdxxbjz = metaDataTablesMapper.delSyncDwdxxbjz(userName);
            int delSyncDwdxxbzdmys = metaDataTablesMapper.delSyncDwdxxbzdmys(userName);
            ProgressSingleton.put(str + userName, 80);
            metaDataTablesMapper.deleteTempTable();
            ProgressSingleton.put(str + userName, 100);
            String resultMsg = "表信息中新增" + insertMetaTableSize + "张表,更新" + updateMetaTableSize + "张表,删除" + delMetaTableSize + "张表;" +
                    "</br>字段信息中新增" + insertMetaFiledSize + "张表,更新" + updateMetaFiledSize + "张表,删除" + delMetaFiledSize + "张表;" +
                    "</br>类型信息中新增" + insertHierarchyListSize + "张表,更新" + updateHierarchyListSize + "张表,删除" + delHierarchyListSize + "张表;" +
                    "</br>下拉信息中新增" + insertOptionSize + "张表,更新" + updateOptionSize + "张表,删除" + delOptionSize + "张表;";
            //增加日志记录
            operationTableLogService.saveLog("同步云端表", "同步云端表", "同步云端记录", resultMsg);

            //执行效验代码
            List ids = listObjs(new QueryWrapper<MetaDataTables>().lambda()
                    .select(MetaDataTables::getId).eq(MetaDataTables::getIsStandard, 1).eq(MetaDataTables::getTableStatus, 1));
            String uuidKey = UUID64Utils.get64UUIDString();
            metaDataFiledsService.setUpdatedFiledCodeCache(uuidKey, ids);
            metaDataFiledsService.executeUpdatedFiledCodeAll(uuidKey);
            return resultMsg;
        } else {
            throw new ParamException("获取更新服务器无数据,请检查更新服务器地址是否正确!");
        }


    }

    @Override
    public String getMaxVersion() {
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("apiKey", apiKey);
        maps.put("schoolCode", Objects.requireNonNull(RequestHolder.getCurrenSysUser()).getSchoolCode());
        String urlPathData = UrlUtil.getHttpInterface(syncUrl + "metadata/metaDataTables/syncMaxVersion.json", maps);
        Map map = JSONObject.fromObject(urlPathData);
        return (String) (map.get("data") instanceof JSONNull ? map.get("msg") : map.get("data"));
    }

    @Override
    public Page<DataLabelVo> getDataLabelVo(Page<DataLabelVo> objectPage, String bqm) {
        return objectPage.setRecords(metaDataTablesMapper.getDataLabelVo(objectPage, bqm));
    }

    @Override
    public List<IndexListVo> getIndexListVo() {
        List<MetaDataTables> list = list(new QueryWrapper<MetaDataTables>().orderByAsc("cast(Sort_ as int)")
                .orderByAsc("SortId_").orderByDesc("CreateTime_"));
        List<IndexListVo> returnList = Lists.newArrayList();
        for (MetaDataTables tables : list) {
            List<MetaDataFileds> metaDataFileds = metaDataFiledsMapper.selectList(new QueryWrapper<MetaDataFileds>().lambda().eq(MetaDataFileds::getTableId, tables.getId()));
            IndexListVo indexListVo = IndexListVo.builder()
                    .id(tables.getId())
                    .catalog(tables.getCatalog())
                    .chineseTableName(tables.getChineseTableName())
                    .tableName(tables.getTableName())
                    .remark(tables.getRemark())
                    .metaDataFiledsList(metaDataFileds).build();
            returnList.add(indexListVo);
        }
        return returnList;
    }

    @Override
    public WelcomeVo findCountList() {
        return metaDataTablesMapper.findCountList();
    }

    @Override
    public List<List<Map<String, String>>> findTheDataMap() {
        List<List<Map<String, String>>> lists = Lists.newArrayList();
        List<Map<String, String>> chartsData = metaDataTablesMapper.findChartsData();
        List<Map<String, String>> chartsLinks = metaDataTablesMapper.findChartsLinks();
        List<Map<String, String>> chartsCategories = metaDataTablesMapper.findChartsCategories();
        lists.add(chartsData);
        lists.add(chartsLinks);
        lists.add(chartsCategories);
        return lists;
    }

    @Override
    public List<MetaDataTablesVo> mapperListPage(List<String> list) {
        List<String> ids = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(list)) {
            List<MapperFieldVo> mapperFieldVos = metaDataFiledsService.showLabelDataTables(list);
            mapperFieldVos.forEach(obj -> ids.add(obj.getTwoTableId()));
        }
        return metaDataTablesMapper.mapperListPage(RequestHolder.getCurrenSysUser().getSchoolCode(), ids);

    }

    @Override
    public void insertListMaps(List<Map<String, Object>> integers, Map<Integer, MetaDataFileds> fields, String tableName) {
        metaDataTablesMapper.insertListMaps(integers, fields, tableName);
    }


    private List<MetaDataFileds> getMetaDataFileds(String id) {
        List<MetaDataFileds> metaDataFileds = metaDataFiledsMapper.selectList(new QueryWrapper<MetaDataFileds>().lambda().eq(MetaDataFileds::getTableId, id));
        metaDataFileds.forEach(obj -> {
            obj.setStorageType(CreateTableUtils.FILESYSTEMS.get(obj.getStorageType()));
            obj.setConstraints(CreateTableUtils.FILESTAUTS.get(obj.getConstraints()));
        });
        return metaDataFileds;
    }
}
