package com.saving.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saving.metadata.pojo.BackupMessage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 陈志强
 * @since 2020-02-11
 */
@Mapper
@Repository
public interface BackupMessageMapper extends BaseMapper<BackupMessage> {

    /**
     * 备份数据库存储过程调用
     */
    String backupData();
}
