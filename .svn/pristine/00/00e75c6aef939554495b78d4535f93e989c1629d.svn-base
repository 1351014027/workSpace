package com.saving.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.pojo.ApiPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
public interface ApiPermissionMapper extends BaseMapper<ApiPermission> {

    Page<ApiPermission> getApiPermission(@Param("page") Page page, @Param("sysName") String sysName, @Param("schoolCode") String schoolCode, @Param("tableId") String tableId);
}
