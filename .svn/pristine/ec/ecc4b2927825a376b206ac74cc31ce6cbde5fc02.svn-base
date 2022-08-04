package com.saving.metadata.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.param.LabelFiledParam;
import com.saving.metadata.pojo.LabelFiled;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.LabelFiledService;
import com.saving.metadata.utils.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 陈志强
 * @since 2020-06-08
 */
@RestController
@RequestMapping("/metadata/labelFiled")
@Api(tags = "标签字段控制器接口")
@Slf4j
public class LabelFiledController<T> {

    @Resource
    private LabelFiledService labelFiledService;


    @PostMapping("save.json")
    @ApiOperation(value = "新增接口", notes = "新增接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "labelParam", value = "标签效验参数对象", required = true, paramType = "query"),
    })
    public ServerResponse<T> save(LabelFiledParam labelParam) {
        labelFiledService.saveParam(labelParam);
        return ServerResponse.createBySuccess();
    }

    @PostMapping("saves.json")
    @ApiOperation(value = "批量新增接口", notes = "批量新增接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "labelParam", value = "标签效验参数对象", required = true, paramType = "query"),
    })
    public ServerResponse<T> saves(@RequestBody List<LabelFiledParam> labelParam) {
        labelFiledService.savesParam(labelParam, true);
        return ServerResponse.createBySuccess();
    }


    @PostMapping("update.json")
    @ApiOperation(value = "更新接口", notes = "更新接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "labelParam", value = "标签效验参数对象", required = true, paramType = "query"),
    })
    public ServerResponse<T> update(LabelFiledParam labelParam) {
        labelFiledService.updateParam(labelParam);
        return ServerResponse.createBySuccess();
    }


    @PostMapping("del.json")
    @ApiOperation(value = "批量删除接口", notes = "批量根据ID进行删除")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids", value = "主键ID数组", required = true, paramType = "query"),
    })
    public ServerResponse<T> delete(@RequestBody List<String> ids) {
        labelFiledService.deleteByList(ids);
        HttpServletRequest request = RequestHolder.getCurrenRequest();
        User user = RequestHolder.getCurrenSysUser();
        log.error("【数据中台系统】卡号为{}的用户删除了ID为{}的标签字段数据，删除记录数量为{},删除IP为{}", user.getUsername(), ids, ids.size(), IpUtils.getIpAddr(request));
        return ServerResponse.createBySuccess();
    }


    @PostMapping("list.json")
    @ApiOperation(value = "分页查询接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "labelFiled", value = "标签对象，用于过滤")
    })
    public ServerResponse<Page<LabelFiled>> listPage(@RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "1") int pageSize,
                                                     LabelFiled labelFiled) {
        return ServerResponse.createBySuccess(labelFiledService.listPage(pageNum, pageSize, labelFiled));
    }

    @PostMapping("updateSortUser.json")
    @ApiOperation(value = "更新排序接口", notes = "两个排序对象ID必须传")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "oneId", value = "第一个交换sort值的对象", required = true, paramType = "query"),
            @ApiImplicitParam(name = "twoId", value = "第二个交换sort值的对象", required = true, paramType = "query")
    })
    public ServerResponse<T> updateSortUser(String oneId, String twoId) {
        if (StringUtils.isBlank(oneId) || StringUtils.isBlank(twoId)) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        LabelFiled fileStore1 = labelFiledService.getById(oneId), fileStore2 = labelFiledService.getById(twoId);
        if (fileStore1 == null || fileStore2 == null) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        String store1Sort = String.valueOf(Integer.parseInt(fileStore2.getSort()) - 1);
        LabelFiled fileStoreOne = LabelFiled.builder().id(fileStore1.getId()).sort(store1Sort).build();
        labelFiledService.updateById(fileStoreOne);
        return ServerResponse.createBySuccess();
    }


    @PostMapping("listNoPage.json")
    @ApiOperation(value = "查询接口", notes = "无分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "label", value = "标签对象，用于过滤")
    })
    public ServerResponse<Page<LabelFiled>> listNoPage(LabelFiled labelFiled) {
        return listPage(1, 1000000, labelFiled);
    }
}

