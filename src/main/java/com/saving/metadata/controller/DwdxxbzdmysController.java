package com.saving.metadata.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.pojo.Dwdxxbzdmys;
import com.saving.metadata.pojo.Hierarchy;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.DwdxxbzdmysService;
import com.saving.metadata.service.HierarchyService;
import com.saving.metadata.utils.DateUtil;
import com.saving.metadata.utils.IpUtils;
import com.saving.metadata.utils.UUID64Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 陈志强
 * @since 2020-05-18
 */
@RestController
@RequestMapping("/metadata/dwdxxbzdmys")
@Api(tags = "多键值映射控制器接口")
@Slf4j
public class DwdxxbzdmysController {


    private final DwdxxbzdmysService dwdxxbzdmysService;

    private final HierarchyService hierarchyService;

    @Value("${application.isCloud}")
    private Boolean isCloud;


    @Autowired
    public DwdxxbzdmysController(DwdxxbzdmysService dwdxxbzdmysService, HierarchyService hierarchyService) {
        this.dwdxxbzdmysService = dwdxxbzdmysService;
        this.hierarchyService = hierarchyService;
    }


    @PostMapping("listPage.json")
    @ApiOperation(value = "多键值映射查询接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "类型主键过滤，用于过滤")
    })
    public ServerResponse<Page<Dwdxxbzdmys>> listPage(
            @RequestParam(defaultValue = ResponseCode.PAGENUM) int pageNum
            , @RequestParam(defaultValue = ResponseCode.PAGESIZE) int pageSize
            , String id) {
        Page<Dwdxxbzdmys> page = dwdxxbzdmysService.page(new Page<>(pageNum, pageSize), new QueryWrapper<Dwdxxbzdmys>()
                .eq("Hierarchy_ID_", id)
                .orderByAsc("cast(Sort_ as int )"));
        return ServerResponse.createBySuccess(page);
    }

    @PostMapping(value = "deletes.json")
    @ApiOperation(value = "批量删除类型接口", notes = "字段删除")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids", value = "选项对象主键组", required = true, paramType = "query")
    })
    public ServerResponse del(@RequestBody List<String> ids) {
        if (!ids.isEmpty()) {
            dwdxxbzdmysService.removeByIds(ids);
            dwdxxbzdmysService.update(Dwdxxbzdmys.builder().cdmpVersion(DateUtil.getYyyyMmDd()).build(), new QueryWrapper<Dwdxxbzdmys>().lambda().in(Dwdxxbzdmys::getId, ids));
            HttpServletRequest request = RequestHolder.getCurrenRequest();
            User user = RequestHolder.getCurrenSysUser();
            log.error("【数据中台系统】卡号为{}的用户删除了ID为{}的多键值映射下拉选项数据，删除记录数量为{},删除IP为{}", user.getUsername(), ids, ids.size(), IpUtils.getIpAddr(request));
        }
        return ServerResponse.createBySuccess();
    }

    @PostMapping(value = "update.json")
    @ApiOperation(value = "更新选项接口", notes = "更新类型")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "param", value = "选项对象，用于更新", required = true, paramType = "query")
    })
    public ServerResponse update(Dwdxxbzdmys dwdxxbzdmys) {
        Dwdxxbzdmys byId = dwdxxbzdmysService.getById(dwdxxbzdmys.getId());
        if (byId == null) {
            throw new ParamException("待更新的对象不存在!");
        }
        if (StringUtils.isEmpty(dwdxxbzdmys.getDylmc())) {
            throw new ParamException("映射字段名不能为空!");
        }
        if (StringUtils.isEmpty(dwdxxbzdmys.getHierarchyId()) || StringUtils.isEmpty(dwdxxbzdmys.getHierarchyKey())) {
            throw new ParamException("选项类型不能为空!");
        }
        if (checkExist(dwdxxbzdmys.getDylmc(), dwdxxbzdmys.getHierarchyId(), dwdxxbzdmys.getId())) {
            throw new ParamException("存在相同的映射字段名");
        }
        if (dwdxxbzdmys.getIsStandard() == null) {
            dwdxxbzdmys.setIsStandard(0);
        }
        dwdxxbzdmys.setCdmpVersion(DateUtil.getYyyyMmDd());
        dwdxxbzdmysService.updateById(dwdxxbzdmys);
        return ServerResponse.createBySuccess();
    }

    private boolean checkExist(String dylmc, String hierarchyId, String id) {

        return dwdxxbzdmysService.count(new QueryWrapper<Dwdxxbzdmys>().lambda().eq(Dwdxxbzdmys::getDylmc, dylmc).eq(Dwdxxbzdmys::getHierarchyId, hierarchyId).ne(StringUtils.isNotEmpty(id), Dwdxxbzdmys::getId, id)) > 0;
    }

    @PostMapping(value = "save.json")
    @ApiOperation(value = "新增选项接口", notes = "新增类型")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "param", value = "选项对象，用于新增", required = true, paramType = "query")
    })
    public ServerResponse save(Dwdxxbzdmys dwdxxbzdmys) {
        if (StringUtils.isEmpty(dwdxxbzdmys.getDyl()) || StringUtils.isEmpty(dwdxxbzdmys.getDylmc())) {
            throw new ParamException("映射字段名不能为空!");
        }
        if (StringUtils.isEmpty(dwdxxbzdmys.getHierarchyId())) {
            throw new ParamException("尚未选择类型!");
        }
        if (checkExist(dwdxxbzdmys.getDylmc(), dwdxxbzdmys.getHierarchyId(), null)) {
            throw new ParamException("存在相同的映射字段名");
        }
        if (StringUtils.isEmpty(dwdxxbzdmys.getHierarchyId()) || StringUtils.isEmpty(dwdxxbzdmys.getHierarchyKey())) {
            throw new ParamException("选项类型不能为空!");
        }
        Hierarchy byId = hierarchyService.getById(dwdxxbzdmys.getHierarchyId());
        if (byId == null) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        dwdxxbzdmys.setHierarchyKey(byId.getTypeName() + "(" + byId.getTypeKey() + ")");
        dwdxxbzdmys.setId(UUID64Utils.get64UUIDString());
        dwdxxbzdmys.setSort(String.valueOf(dwdxxbzdmysService.count(new QueryWrapper<Dwdxxbzdmys>().lambda()
                .eq(Dwdxxbzdmys::getHierarchyId, dwdxxbzdmys.getHierarchyId()).eq(Dwdxxbzdmys::getIsDelete, 0)) + 1));
        dwdxxbzdmys.setCdmpVersion(DateUtil.getYyyyMmDd());
        if (isCloud) {
            dwdxxbzdmys.setIsStandard(1);
        }
        dwdxxbzdmysService.save(dwdxxbzdmys);
        return ServerResponse.createBySuccess();
    }


    @PostMapping(value = "findById.json")
    @ApiOperation(value = "根据ID进行查询接口", notes = "根据ID进行查询获取列名")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "hierarchyId", value = "查询ID用于查询作用", required = true, paramType = "query")
    })
    public ServerResponse<List<Dwdxxbzdmys>> findByHierarchyId(String hierarchyId) {
        return ServerResponse.createBySuccess(dwdxxbzdmysService.list(new QueryWrapper<Dwdxxbzdmys>().lambda().eq(Dwdxxbzdmys::getHierarchyId, hierarchyId)));
    }
}

