package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.param.MetaDataFiledsParam;
import com.saving.metadata.pojo.MetaDataFileds;
import com.saving.metadata.pojo.User;
import com.saving.metadata.vo.*;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 陈志强
 * @since 2019-12-18
 */
public interface MetaDataFiledsService extends IService<MetaDataFileds> {

    List<LayuiTreeVo> lastList(Integer isStandard);


    MetaDataFileds saveCheck(MetaDataFiledsParam param);

    MetaDataFileds updateCheck(MetaDataFiledsParam param);

    MetaDataFileds checkUpdateParam(MetaDataFiledsParam param, MetaDataFileds byId);

    String checkFiled(MetaDataFileds metaDataFileds);


    List<MetaDataFiledCodeVo> setUpdatedFiledCodeCache(String id, List<String> ids);

    void delUpdatedFiledCodeCache(String id);


    int executeUpdatedFiledCode(List<MetaDataFiledCodeVo> metaDataFiledCodeVos);

    Page<MetaDataFileds> listPage(int pageNum, int pageSize, MetaDataFileds metaDataFileds, String startDate, String endDate, User user);

    /**
     * @param cataNo
     * @return
     * @author:严嘉炜
     * @describe:根据状态平台目录编号查询该目录编号下的字段
     */
    List<AicFieldsVo> getZtptListByCataNo(String cataNo);


    List<MetaDataFileds> getViewFiled(String tableId);


    void saveRevisionlog(MetaDataFileds metaDataFileds, String type, Integer updateFiledLength, Integer updateDecimallength);

    Boolean checkExist(String filedName, String id, String tableId);

    void executeUpdatedFiledCodeAll(String id);

    List<MapperFieldVo> showLabelDataTable(List<String> lists);

    List<MapperFieldVo> showLabelDataTables(List<String> list);

    List<MetaDataFileds> getShowFieldList(List<MapperFieldListVo> lists);

    ServerResponse createView(List<MapperFieldListVo> lists);
}
