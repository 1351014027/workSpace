package com.saving.metadata.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.listener.UpdateMetaDataFiledsListener;
import com.saving.metadata.param.MetaDataFiledsParam;
import com.saving.metadata.pojo.MetaDataFileds;
import com.saving.metadata.pojo.MetaDataTables;
import com.saving.metadata.pojo.User;
import com.saving.metadata.service.MetaDataFiledsService;
import com.saving.metadata.service.MetaDataTablesService;
import com.saving.metadata.service.OperationTableLogService;
import com.saving.metadata.utils.DateUtil;
import com.saving.metadata.utils.UUID64Utils;
import com.saving.metadata.vo.LayuiTreeVo;
import com.saving.metadata.vo.MapperFieldListVo;
import com.saving.metadata.vo.MapperFieldVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 陈志强
 * @since 2019-12-18
 */
@RestController
@RequestMapping("/metadata/metaDataFileds")
@Slf4j
@Api(tags = "元数据字段控制器")
public class MetaDataFiledsController {


    private final MetaDataFiledsService metaDataFiledsService;
    private final MetaDataTablesService metaDataTablesService;
    private final OperationTableLogService operationTableLogService;


    @Autowired
    public MetaDataFiledsController(MetaDataFiledsService metaDataFiledsService, MetaDataTablesService metaDataTablesService,
                                    OperationTableLogService operationTableLogService) {
        this.metaDataFiledsService = metaDataFiledsService;
        this.metaDataTablesService = metaDataTablesService;
        this.operationTableLogService = operationTableLogService;

    }

    @PostMapping("lastList.json")
    @ApiOperation(value = "树形数据请求", notes = "查询")
    public ServerResponse<List<LayuiTreeVo>> lastList(Integer isStandard) {
        return ServerResponse.createBySuccess(metaDataFiledsService.lastList(isStandard));
    }

    @PostMapping(value = "deletes.json")
    @ApiOperation(value = "批量删除接口", notes = "字段删除")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids", value = "对象主键组", required = true, paramType = "query")
    })
    public ServerResponse del(@RequestBody List<String> ids) {
        List<MetaDataFileds> metaDataFileds = metaDataFiledsService.listByIds(ids);
        if (!ids.isEmpty()) {
            metaDataFiledsService.removeByIds(ids);
            metaDataFiledsService.update(MetaDataFileds.builder().cdmpVersion(DateUtil.getYyyyMmDd()).build(), new QueryWrapper<MetaDataFileds>().lambda().in(MetaDataFileds::getId, ids));
        }
        StringBuilder msg = new StringBuilder();
        metaDataFileds.forEach(obj -> msg.append(obj.getFiledName()).append("(").append(obj.getChineseFiledName()).append(" ),"));
        operationTableLogService.saveLog(metaDataFileds.get(0).getTableName(), "字段删除", "删除" + msg.substring(0, msg.toString().length() - 1) + "字段成功!");
        metaDataFileds.forEach(obj -> metaDataTablesService.updateTableFiledNumber(obj.getTableId()));
        return ServerResponse.createBySuccess();
    }

    @PostMapping(value = "update.json")
    @ApiOperation(value = "更新接口", notes = "更新类型")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "param", value = "对象，用于更新", required = true, paramType = "query")
    })
    public ServerResponse update(MetaDataFiledsParam param) {
        MetaDataFileds metaDataFileds = metaDataFiledsService.updateCheck(param);
        try {
            metaDataFiledsService.saveRevisionlog(metaDataFileds, "更新", param.getFiledLength(), param.getDecimalLength());
        } catch (Exception e) {
            log.error("插入云端数据，更新字段错误!", e);
        }
        operationTableLogService.saveLog(metaDataFileds.getTableName(), "字段更新", "更新" + metaDataFileds.getFiledName() + "(" + metaDataFileds.getChineseFiledName() + ")字段成功!更新前参数为:" + JSONObject.fromObject(metaDataFileds).toString() + "更新后参数为:" + JSONObject.fromObject(param).toString());
        return ServerResponse.createBySuccess();
    }

    @PostMapping(value = "save.json")
    @ApiOperation(value = "新增接口", notes = "新增类型")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "param", value = "对象，用于新增", required = true, paramType = "query")
    })
    public ServerResponse save(MetaDataFiledsParam param) {
        MetaDataFileds metaDataFileds = metaDataFiledsService.saveCheck(param);
        try {
            metaDataFiledsService.saveRevisionlog(metaDataFileds, "新增", metaDataFileds.getFiledLength(), metaDataFileds.getDecimalLength());
        } catch (Exception e) {
            log.error("插入云端数据，新增字段错误!", e);
        }
        operationTableLogService.saveLog(metaDataFileds.getTableName(), "字段新增", "新增" + metaDataFileds.getFiledName() + "(" + metaDataFileds.getChineseFiledName() + ")字段成功!参数为:" + JSONObject.fromObject(metaDataFileds).toString());
        return ServerResponse.createBySuccess();
    }

    @PostMapping("listPage.json")
    @ApiOperation(value = "字段分页查询接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "startDate", value = "过滤上传日期开始时间"),
            @ApiImplicitParam(name = "endDate", value = "过滤上传日期结束时间"),
            @ApiImplicitParam(name = "MetaDataFileds", value = "对象，用于过滤")
    })
    public ServerResponse<Page<MetaDataFileds>> listPage(
            @RequestParam(defaultValue = ResponseCode.PAGENUM) int pageNum
            , @RequestParam(defaultValue = ResponseCode.PAGESIZE) int pageSize
            , MetaDataFileds metaDataFileds
            , String startDate
            , String endDate) {
        User user = RequestHolder.getCurrenSysUser();
        Page<MetaDataFileds> page = metaDataFiledsService.listPage(pageNum, pageSize, metaDataFileds, startDate, endDate, user);
        return ServerResponse.createBySuccess(page);
    }

    @PostMapping("updateSort.json")
    @ApiOperation(value = "更新排序接口", notes = "两个排序对象ID必须传")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "oneId", value = "第一个交换sort值的对象", required = true, paramType = "query"),
            @ApiImplicitParam(name = "twoId", value = "第二个交换sort值的对象", required = true, paramType = "query")
    })
    public ServerResponse updateSort(String oneId, String twoId) {
        if (StringUtils.isBlank(oneId) || StringUtils.isBlank(twoId)) {
            return ServerResponse.createByErrorMessage(ResponseCode.ERRORPARAM);
        }
        MetaDataFileds fileStore1 = metaDataFiledsService.getById(oneId), fileStore2 = metaDataFiledsService.getById(twoId);
        String store1Sort = fileStore1.getSort(), store2Sort = fileStore2.getSort();
        MetaDataFileds fileStoreOne = MetaDataFileds.builder()
                .id(fileStore1.getId())
                .sort(store2Sort)
                .cdmpVersion(DateUtil.getYyyyMmDd()).build(),
                fileStoreTwo = MetaDataFileds.builder()
                        .id(fileStore2.getId())
                        .cdmpVersion(DateUtil.getYyyyMmDd())
                        .sort(store1Sort).build();
        metaDataFiledsService.updateById(fileStoreOne);
        metaDataFiledsService.updateById(fileStoreTwo);
        operationTableLogService.saveLog(fileStoreOne.getTableName(), "表字段交换排序", "[" + fileStoreOne.getChineseFiledName() + "]和[" + fileStoreTwo.getChineseFiledName() + "]互相交换排序");
        return ServerResponse.createBySuccess();
    }


    @PostMapping("import.json")
    @ApiOperation(value = "批量导入", notes = "批量导入")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "文件流", required = true, paramType = "query"),
    })
    public ServerResponse<? extends Object> importExcel(@RequestParam(value = "file", required = false) MultipartFile file, String tableId, Integer isStandard) throws IOException {
        EasyExcel.read(file.getInputStream(), MetaDataFileds.class, new UpdateMetaDataFiledsListener(tableId, isStandard, metaDataFiledsService, metaDataTablesService)).sheet().doRead();
        return ServerResponse.createBySuccess();
    }


    @PostMapping("getUpdatedFiledCode.json")
    @ApiOperation(value = "获取变更语句执行代码", notes = "获取变更语句执行代码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids", value = "表主键组", required = true, paramType = "query"),
    })
    public ServerResponse<String> getUpdatedFiledCode(@RequestBody List<String> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        String uuidKey = UUID64Utils.get64UUIDString();
        metaDataFiledsService.setUpdatedFiledCodeCache(uuidKey, ids);
        return ServerResponse.createBySuccess(uuidKey);
    }

    @PostMapping("getUpdatedFiledCodeBZ.json")
    @ApiOperation(value = "获取已创建云端标准表字段变更语句执行代码", notes = "获取已创建云端标准表字段变更语句执行代码")
    public ServerResponse<String> getUpdatedFiledCodeBZ() {
        List ids = metaDataTablesService.listObjs(new QueryWrapper<MetaDataTables>().lambda()
                .select(MetaDataTables::getId).eq(MetaDataTables::getIsStandard, 1).eq(MetaDataTables::getTableStatus, 1));
        return ServerResponse.createBySuccess(getUpdatedFiledCode(ids).getData().toString());
    }

    @PostMapping("executeUpdatedFiledCode.json")
    @ApiOperation(value = "执行变更语句执行代码", notes = "执行变更语句执行代码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "缓存主键", required = true, paramType = "query"),
    })
    public ServerResponse executeUpdatedFiledCode(String id) {
        if (StringUtils.isEmpty(id)) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        metaDataFiledsService.executeUpdatedFiledCodeAll(id);

        return ServerResponse.createBySuccess();
    }


    @PostMapping("updateSortUser.json")
    @ApiOperation(value = "更新排序接口", notes = "两个排序对象ID必须传")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "oneId", value = "第一个交换sort值的对象", required = true, paramType = "query"),
            @ApiImplicitParam(name = "twoId", value = "第二个交换sort值的对象", required = true, paramType = "query")
    })
    public ServerResponse updateSortUser(String oneId, String twoId) {
        if (StringUtils.isBlank(oneId) || StringUtils.isBlank(twoId)) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        MetaDataFileds fileStore1 = metaDataFiledsService.getById(oneId), fileStore2 = metaDataFiledsService.getById(twoId);
        if (fileStore1 == null || fileStore2 == null) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        String store1Sort = String.valueOf(Integer.parseInt(fileStore2.getSort()) - 1);
        MetaDataFileds fileStoreOne = MetaDataFileds.builder().id(fileStore1.getId()).sort(store1Sort).cdmpVersion(DateUtil.getYyyyMmDd()).build();
        metaDataFiledsService.updateById(fileStoreOne);
        operationTableLogService.saveLog(fileStoreOne.getTableName(), "表字段更换排序", "[" + fileStoreOne.getChineseFiledName() + "]更换排序到[" + fileStore2.getChineseFiledName() + "]之前");
        return ServerResponse.createBySuccess();
    }

    @PostMapping("getMapperByTableId.json")
    @ApiOperation(value = "获取关联关系接口", notes = "获取关联关系字段")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "lists", value = "标签对象，用于过滤")
    })
    public ServerResponse<List<MapperFieldVo>> showLabelDataTable(@RequestBody List<String> lists) {

        return ServerResponse.createBySuccess(metaDataFiledsService.showLabelDataTable(lists));
    }

    @PostMapping("getShowFieldList.json")
    @ApiOperation(value = "获取关联关系接口", notes = "获取关联关系字段")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "lists", value = "标签对象，用于过滤")
    })
    public ServerResponse<List<MetaDataFileds>> getShowFieldList(@RequestBody List<MapperFieldListVo> lists) {

        return ServerResponse.createBySuccess(metaDataFiledsService.getShowFieldList(lists));
    }

    @PostMapping("createView.json")
    @ApiOperation(value = "获取关联关系接口", notes = "获取关联关系字段")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "lists", value = "标签对象，用于过滤")
    })
    public ServerResponse createView(@RequestBody List<MapperFieldListVo> lists) {

        return metaDataFiledsService.createView(lists);
    }

}

