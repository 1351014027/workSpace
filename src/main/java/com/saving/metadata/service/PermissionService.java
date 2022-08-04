package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.pojo.Permission;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 陈志强
 * @since 2020-03-15
 */
public interface PermissionService extends IService<Permission> {

    void save(String kh, String xm, Map<String, String> map);

    Page<Permission> listPage(Page<Permission> page, String str);

    Map<Object, Object> roleList(Integer type);

    /**
     * 获取用户的权限功能点
     *
     * @param userName 用户名
     * @return 返回权限功能点集合
     */
    List<String> roleLists(String userName);
}
