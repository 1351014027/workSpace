package com.saving.metadata.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.service.ChartDisplayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: 陈志强
 * @date: 2020/1/6 10:10
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/metadata/chartDisplay")
@Api(tags = "图表展示内容相关接口")
public class ChartDisplayController {

    private final ChartDisplayService chartDisplayService;

    @Autowired
    public ChartDisplayController(ChartDisplayService chartDisplayService) {
        this.chartDisplayService = chartDisplayService;
    }


    @PostMapping("tableLists.json")
    @ApiOperation(value = "自定义分页分页查询接口", notes = "物理分页")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "pageNum", value = "第几页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "每页几条", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tableId", value = "表格主键"),
            @ApiImplicitParam(name = "likeStr", value = "用于模糊查询字段")
    })
    public ServerResponse<Page<Map<String, Object>>> tableLists(String tableId,
                                                                @RequestParam(defaultValue = "1") int pageNum,
                                                                @RequestParam(defaultValue = "100") int pageSize,
                                                                String likeStr
            , String isView) {
        return chartDisplayService.tableLists(tableId, pageNum, pageSize, likeStr, isView);
    }
}
