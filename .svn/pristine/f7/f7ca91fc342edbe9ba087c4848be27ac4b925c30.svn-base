package com.saving.metadata.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author 陈志强
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("METADATA_B_HIERARCHY")
@ApiModel(value = "Hierarchy对象", description = "下拉选项父对象，用于过滤下拉选项类型")
@Builder
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(25)
public class Hierarchy extends Model<Hierarchy> {


    private static final long serialVersionUID = 1L;
    @TableId("ID_")
    @ExcelIgnore
    private String id;
    @ExcelProperty("类型键值")
    @TableField(value = "TypeKey_", insertStrategy = FieldStrategy.NOT_EMPTY, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String typeKey;
    @ExcelProperty("类型名称")
    @TableField(value = "TypeName_", insertStrategy = FieldStrategy.NOT_EMPTY, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String typeName;
    @ExcelIgnore
    @TableField("Parent_")
    private Integer parent;
    @ExcelIgnore
    @TableField("Depth_")
    private Integer depth;
    @ExcelIgnore
    @TableField("Level_")
    private String level;
    @ExcelIgnore
    @TableField(value = "Sort_", fill = FieldFill.INSERT)
    private String sort;
    @ExcelIgnore
    @TableField("Status_")
    private Integer status;
    @ExcelIgnore
    @TableField("IsDelete_")
    @TableLogic
    private Integer isDelete;
    @ExcelIgnore
    @TableField(value = "SchoolCode_", fill = FieldFill.INSERT)
    private String schoolCode;
    @ExcelIgnore
    @TableField(value = "Creator_", fill = FieldFill.INSERT)
    private String creator;
    @ExcelIgnore
    @TableField("CreateTime_")
    private Date createTime;
    @ExcelIgnore
    @TableField(value = "Updateor_", fill = FieldFill.UPDATE)
    private String updateor;
    @ExcelIgnore
    @TableField(value = "UpdateTime_", fill = FieldFill.UPDATE)
    private Date updateTime;
    @ExcelProperty("备注")
    @TableField(value = "Remark_", insertStrategy = FieldStrategy.NOT_EMPTY, whereStrategy = FieldStrategy.NOT_EMPTY)
    private String remark;
    @TableField("CanYouEdit_")
    @ExcelIgnore
    private Integer canYouEdit;
    @TableField("CanYouDelete_")
    @ExcelIgnore
    private Integer canYouDelete;
    @TableField("SortId_")
    @ExcelIgnore
    private Integer sortId;
    @ExcelIgnore
    @TableField(value = "IsStandard_", insertStrategy = FieldStrategy.NOT_EMPTY)
    private Integer isStandard;
    @TableField("CdmpVersion_")
    @ExcelIgnore
    private String cdmpVersion;
    @TableField("IsManykeyValues_")
    @ExcelIgnore
    private Integer isManyKeyValues;

    @Tolerate
    public Hierarchy() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
