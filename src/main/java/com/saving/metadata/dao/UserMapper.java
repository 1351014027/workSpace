package com.saving.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.pojo.User;
import com.saving.metadata.vo.AicFieldsVo;
import com.saving.metadata.vo.AicTablesVo;
import com.saving.metadata.vo.ResGxrzMap;
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
 * @author saving
 * @since 2019-12-15
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * @return 查询所有部门信息
     */
    @MapKey("ID_")
    List<Map<String, Object>> getDepMaps();

    List<AicTablesVo> findZtpt(@Param("page") Page page);

    /**
     * @param CataNo
     * @return
     * @Author:严嘉炜
     * @describe:根据状态平台目录编号查询该表格所有字段，可与findZtpt合并方法，为了不影响之前业务，此处不合并
     */
    List<AicFieldsVo> findZtptByCataNo(@Param("CataNo") String CataNo);

    /**
     * @author 严嘉炜
     * @describe: 查询EIC常量信息的bi域名
     */
    String findDomainName();


    String selCsxy();

    String selJwIp();

    String selHttp();

    String findSchoolName();


    List<ResGxrzMap> getShowVersion(@Param("sysName") String sysName);
}
