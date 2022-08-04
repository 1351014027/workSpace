package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.dao.ConversionRecordMapper;
import com.saving.metadata.pojo.ConversionRecord;
import com.saving.metadata.service.ConversionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 陈志强
 * @since 2020-01-15
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConversionRecordServiceImpl extends ServiceImpl<ConversionRecordMapper, ConversionRecord> implements ConversionRecordService {

    private final ConversionRecordMapper conversionRecordMapper;

    @Autowired
    public ConversionRecordServiceImpl(ConversionRecordMapper conversionRecordMapper) {
        this.conversionRecordMapper = conversionRecordMapper;
    }

    @Override
    public ServerResponse<Page<Map<String, String>>> listPageAnalysis(int pageNum, int pageSize, String str) {
        Page<Map<String, String>> page = new Page<>(pageNum, pageSize);
        page.setRecords(conversionRecordMapper.listPageAnalysis(page, str));
        return ServerResponse.createBySuccess(page);
    }
}
