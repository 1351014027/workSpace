package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.pojo.MetaDataFileds;
import com.saving.metadata.pojo.MetaDataTables;
import com.saving.metadata.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 陈志强
 * @since 2019-12-18
 */
public interface MetaDataTablesService extends IService<MetaDataTables> {

    void importExcel(List<Map<Integer, String>> listMap, String metadataTypeId, Integer isStandard);

    Page<MetaDataTablesVo> getMetaDataTablesVo(Page<MetaDataTablesVo> page, String name, Integer isStandard,
                                               Integer tableStatus, String startTime, String endTime, String metadataTypeId, Integer nature);

    void createDataBase(List<String> ids);

    void updateTableFiledNumber(String tableId);

    void delDataBase(List<String> ids);

    void syncDataBase();

    Page<AicTablesVo> getZtptListPage(Page<AicTablesVo> page);

    ServerResponse syncAllData();

    ServerResponse syncMaxVersion();

    String updateSync(String str);

    String getMaxVersion();

    Page<DataLabelVo> getDataLabelVo(Page<DataLabelVo> objectPage, String bqm);

    List<IndexListVo> getIndexListVo();

    WelcomeVo findCountList();

    List<List<Map<String, String>>> findTheDataMap();

    List<MetaDataTablesVo> mapperListPage(List<String> list);

    void insertListMaps(List<Map<String, Object>> integers, Map<Integer, MetaDataFileds> fields, String tableName);
}
