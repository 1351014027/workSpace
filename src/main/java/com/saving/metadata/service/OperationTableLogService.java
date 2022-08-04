package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.pojo.OperationTableLog;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 陈志强
 * @since 2020-01-08
 */
public interface OperationTableLogService extends IService<OperationTableLog> {

    void saveLog(String tableName, String chineseTableName, String operationType, String operationExplain);

    void saveLog(String tableName, String operationType, String operationExplain);

}
