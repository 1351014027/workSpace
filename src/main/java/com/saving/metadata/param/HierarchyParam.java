package com.saving.metadata.param;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 *
 * </p>
 *
 * @author 陈志强
 * @since 2019-12-18
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class HierarchyParam extends Model<HierarchyParam> {


    private static final long serialVersionUID = 1L;
    private String id;
    @NotEmpty(message = "类型代码不能为空!")
    private String typeKey;
    @NotEmpty(message = "类型名称不能为空!")
    private String typeName;
    private Integer parent;
    @NotEmpty(message = "排序不能为空!")
    private String sort;
    private String remark;
    private Integer isManyKeyValues;

    @Tolerate
    public HierarchyParam() {
    }


}
