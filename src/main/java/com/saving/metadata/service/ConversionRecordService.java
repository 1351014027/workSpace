package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.pojo.ConversionRecord;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 陈志强
 * @since 2020-01-15
 */
public interface ConversionRecordService extends IService<ConversionRecord> {

    ServerResponse<Page<Map<String, String>>> listPageAnalysis(int pageNum, int pageSize, String str);
}
