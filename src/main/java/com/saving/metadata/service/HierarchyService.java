package com.saving.metadata.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.saving.metadata.param.HierarchyParam;
import com.saving.metadata.pojo.Hierarchy;
import com.saving.metadata.vo.LayuiTreeVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 类型服务类
 * </p>
 *
 * @author 陈志强
 * @since 2019-12-18
 */
public interface HierarchyService extends IService<Hierarchy> {

    /**
     * 新增
     *
     * @param param
     */
    void save(HierarchyParam param, Boolean isCloud);

    /**
     * 更新
     *
     * @param param
     */
    void update(HierarchyParam param);

    /**
     * 对排序号进行处理后的list
     *
     * @param hierarchy
     * @return
     */
    List<Hierarchy> treeList(Hierarchy hierarchy);

    void importExcel(List<Map<Integer, String>> listMap, String parent, String remark, Boolean isCloud);

    List<LayuiTreeVo> lastList(String nodeId, String parentId, String level, String isReload, String schoolCode, Boolean isTag);
}
