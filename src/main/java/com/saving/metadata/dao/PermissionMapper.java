package com.saving.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.pojo.Permission;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 陈志强
 * @since 2020-03-15
 */
@Mapper
@Repository
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * @param page 翻页对象，可以作为 xml 参数直接使用，传递参数 Page 即自动分页
     * @return 权限集合
     */
    Page<Permission> listPage(@Param("page") Page<Permission> page, @Param("str") String str);

    @MapKey("ID_")
    List<Map<String, Object>> roleList(@Param("type") int type);
}
