package com.saving.metadata.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
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
 * @since 2020-05-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@TableName("METADATA_B_DWDXXBZDMYS")
@ApiModel(value = "Dwdxxbzdmys对象", description = "")
public class Dwdxxbzdmys extends Model<Dwdxxbzdmys> {

    private static final long serialVersionUID = 1L;
    @TableId("ID_")
    private String id;
    @TableField("DYL_")
    private String dyl;
    @TableField("DYLMC_")
    private String dylmc;
    @TableField("Hierarchy_ID_")
    private String hierarchyId;
    @TableField("Hierarchy_Key_")
    private String hierarchyKey;
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

    @Tolerate
    public Dwdxxbzdmys() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
