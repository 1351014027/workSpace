package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.dao.OperationTableLogMapper;
import com.saving.metadata.pojo.OperationTableLog;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.OperationTableLogService;
import com.saving.metadata.utils.DateUtil;
import com.saving.metadata.utils.IpUtils;
import com.saving.metadata.utils.UUID64Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 陈志强
 * @since 2020-01-08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OperationTableLogServiceImpl extends ServiceImpl<OperationTableLogMapper, OperationTableLog> implements OperationTableLogService {


    @Override
    public void saveLog(String tableName, String chineseTableName, String operationType, String operationExplain) {
        User user = RequestHolder.getCurrenSysUser();
        if (StringUtils.isNotEmpty(tableName) && StringUtils.isNotEmpty(chineseTableName) && StringUtils.isNotEmpty(operationType) && StringUtils.isNotEmpty(operationExplain)) {
            save(OperationTableLog.builder().id(UUID64Utils.get64UUIDString()).tableName(tableName + "(" + chineseTableName + ")")
                    .operationIp(IpUtils.getIpAddr(RequestHolder.getCurrenRequest()))
                    .operationType(operationType)
                    .operationUser(user.getUsername())
                    .operationUserName(user.getName())
                    .operationExplain(operationExplain + ";执行时间为:" + DateUtil.getCurrDateTimeStr())
                    .operationTime(new Date())
                    .build());
        }
    }


    @Override
    public void saveLog(String tableName, String operationType, String operationExplain) {
        User user = RequestHolder.getCurrenSysUser();
        if (StringUtils.isNotEmpty(tableName) && StringUtils.isNotEmpty(operationType) && StringUtils.isNotEmpty(operationExplain)) {
            save(OperationTableLog.builder().id(UUID64Utils.get64UUIDString()).tableName(tableName)
                    .operationIp(IpUtils.getIpAddr(RequestHolder.getCurrenRequest()))
                    .operationType(operationType)
                    .operationUser(user.getUsername())
                    .operationUserName(user.getName())
                    .operationExplain(operationExplain + ";执行时间为:" + DateUtil.getCurrDateTimeStr())
                    .operationTime(new Date())
                    .build());
        }
    }
}
