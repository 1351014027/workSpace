package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.dao.ChartDisplayMapper;
import com.saving.metadata.pojo.MetaDataFileds;
import com.saving.metadata.pojo.MetaDataTables;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.ChartDisplayService;
import com.saving.metadata.service.MetaDataFiledsService;
import com.saving.metadata.service.MetaDataTablesService;
import com.saving.metadata.vo.MetaDataTableListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author: 陈志强
 * @date: 2020/1/6 10:13
 * @Description:
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ChartDisplayServiceImpl implements ChartDisplayService {

    private final MetaDataTablesService metaDataTablesService;
    private final MetaDataFiledsService metaDataFiledsService;
    private final ChartDisplayMapper chartDisplayMapper;

    @Autowired
    public ChartDisplayServiceImpl(MetaDataTablesService metaDataTablesService, MetaDataFiledsService metaDataFiledsService, ChartDisplayMapper chartDisplayMapper) {
        this.metaDataTablesService = metaDataTablesService;
        this.metaDataFiledsService = metaDataFiledsService;
        this.chartDisplayMapper = chartDisplayMapper;
    }

    @Override
    public ServerResponse<Page<Map<String, Object>>> tableLists(String tableId, int pageNum, int pageSize, String likeStr, String isView) {
        Page<Map<String, Object>> page = new Page<>(pageNum, pageSize);
        List<Map<String, Object>> maps;
        MetaDataTables byId = MetaDataTables.builder().build();
        List<MetaDataFileds> list;
        if (!"1".equals(isView)) {
            byId = metaDataTablesService.getById(tableId);
            list = metaDataFiledsService.list(new QueryWrapper<MetaDataFileds>().eq("TableID_", tableId).ne("StorageType_", "B").orderByAsc("cast(Sort_ as int)")
                    .orderByAsc("SortId_")
                    .orderByAsc("CreateTime_"));

        } else {
            list = metaDataFiledsService.getViewFiled(tableId);
        }
        maps = chartDisplayMapper.tableLists(page, "1".equals(isView) ? "EIC.DBO." + tableId : byId.getTableName(), list, likeStr);
        page.setRecords(maps);
        return ServerResponse.createBySuccess(page);
    }

    @Override
    public ServerResponse<List<MetaDataTableListVo>> getMetaTableList() {
        User user = RequestHolder.getCurrenSysUser();
        List<MetaDataTableListVo> list = chartDisplayMapper.getMetaTableList(user.getSchoolCode());
        return ServerResponse.createBySuccess(list);
    }

}
