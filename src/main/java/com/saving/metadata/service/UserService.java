package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.pojo.User;
import com.saving.metadata.vo.ResGxrzMap;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 服务类
 * </p>
 *
 * @author saving
 * @since 2019-12-15
 */
public interface UserService extends IService<User> {

    /**
     * @return 返回所有有效的部门信息【部门ID，部门名称】
     */
    Map<Object, Object> getDepMaps();

    /**
     * @author 严嘉炜
     * @describe 查询bi域名
     */
    String findDomainName();

    String ssoXxhx();


    String ssoGljsc();

    String findShcooleName();

    List<ResGxrzMap> getShowVersion(String sysName);
}
