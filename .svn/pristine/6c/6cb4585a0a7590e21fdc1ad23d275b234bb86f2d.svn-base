package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.vo.MetaDataTableListVo;

import java.util.List;
import java.util.Map;

/**
 * @author: 陈志强
 * @date: 2020/1/6 10:13
 * @Description:
 */
public interface ChartDisplayService {

    ServerResponse<Page<Map<String, Object>>> tableLists(String tableId, int pageNum, int pageSize, String likeStr, String isView);

    ServerResponse<List<MetaDataTableListVo>> getMetaTableList();
}
