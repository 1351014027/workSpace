package com.saving.metadata.vo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 树形结构的视图解析对象
 *
 * @author Mr.Saving
 * @date 2019-01-21 17:01
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LayuiTreeVo {

    private String id;

    private String title;

    private String parentId;

    private String primary;

    /**
     * 是否展开节点
     */
    private Boolean spread;

    /**
     * 是否最后一级节点
     */
    private Boolean last;

    /**
     * 是否隐藏
     */
    private Boolean hide;
    /**
     * 是否禁用
     */
    private Boolean disabled;
    /**
     * 自定义图标class
     */
    private String iconClass;
    /**
     * 表示用户自定义需要存储在树节点中的数据
     */
    @Builder.Default
    private BasicData basicData = new BasicData();
    /**
     * 复选框集合
     */
    @Builder.Default
    private List<CheckArr> checkArr = new ArrayList<>();
    /**
     * 子节点集合
     */
    @Builder.Default
    private List<LayuiTreeVo> children = new ArrayList<>();

}
