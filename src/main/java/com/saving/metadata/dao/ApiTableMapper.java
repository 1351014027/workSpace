package com.saving.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saving.metadata.pojo.ApiTable;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 陈志强
 * @since 2020-04-09
 */
@Mapper
@Repository
public interface ApiTableMapper extends BaseMapper<ApiTable> {

    void syncRjzc(String ip);

    void rjzcSync(String userName);

    List<ApiTable> getRjzcData();

    int checkTableName(@Param("tableName") String tableName);

    List<String> findByTableName(@Param("tableName") String tableName);
}
