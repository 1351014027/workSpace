package com.saving.metadata.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
@TableName("METADATA_B_METADATATABLES")
@ApiModel(value = "MetaDataTables对象", description = "")
@Builder
public class MetaDataTablesVo extends Model<MetaDataTablesVo> {


    private static final long serialVersionUID = 1L;
    @TableId("ID_")
    @ExcelIgnore
    private String id;
    @ExcelProperty("目录")
    @TableField("Catalog_")
    private String catalog;
    @ExcelProperty("表名")
    @TableField("TableName_")
    private String tableName;
    @ExcelProperty("中文表名")
    @TableField("ChineseTableName_")
    private String chineseTableName;
    @ExcelProperty("字段数量")
    @TableField("FieldSize_")
    private Integer fieldSize;
    @TableField("IsDelete_")
    @TableLogic
    @ExcelIgnore
    private Integer isDelete;
    @TableField(value = "SchoolCode_", fill = FieldFill.INSERT)
    @ExcelIgnore
    private String schoolCode;
    @ExcelIgnore
    @TableField(value = "Creator_", fill = FieldFill.INSERT)
    private String creator;
    @ExcelIgnore
    @TableField("CreateTime_")
    private Date createTime;
    @TableField(value = "Updateor_", fill = FieldFill.UPDATE)
    @ExcelIgnore
    private String updateor;
    @TableField(value = "UpdateTime_", fill = FieldFill.UPDATE)
    @ExcelIgnore
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
    @TableField("TableStatus_")
    @ExcelIgnore
    private Integer tableStatus;
    @TableField("IsStandard_")
    @ExcelIgnore
    private Integer isStandard;
    @ExcelProperty("元数据类型")
    @TableField("TypeName_")
    private String typeName;
    @TableField("Sort_")
    @ExcelIgnore
    private String sort;
    @TableField("SortId_")
    @ExcelIgnore
    private Integer sortId;

    @TableField("Nature_")
    @ExcelIgnore
    private Integer nature;

    @Tolerate
    public MetaDataTablesVo() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
