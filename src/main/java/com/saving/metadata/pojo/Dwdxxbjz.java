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
@TableName("METADATA_B_DWDXXBJZ")
@ApiModel(value = "Dwdxxbjz对象", description = "")
@Builder
public class Dwdxxbjz extends Model<Dwdxxbjz> {

    private static final long serialVersionUID = 1L;

    @TableId("ID_")
    private String id;
    @ExcelIgnore
    @TableField("Hierarchy_ID_")
    private String hierarchyId;
    @ExcelIgnore
    @TableField("Hierarchy_Key_")
    private String hierarchyKey;
    @TableField("FILED_1_KEY_")
    private String filed1Key;
    @TableField("FILED_1_VALUE_")
    private String filed1Value;
    @TableField("FILED_2_KEY_")
    private String filed2Key;
    @TableField("FILED_2_VALUE_")
    private String filed2Value;
    @TableField("FILED_3_KEY_")
    private String filed3Key;
    @TableField("FILED_3_VALUE_")
    private String filed3Value;
    @TableField("FILED_4_KEY_")
    private String filed4Key;
    @TableField("FILED_4_VALUE_")
    private String filed4Value;
    @TableField("FILED_5_KEY_")
    private String filed5Key;
    @TableField("FILED_5_VALUE_")
    private String filed5Value;
    @TableField("FILED_6_KEY_")
    private String filed6Key;
    @TableField("FILED_6_VALUE_")
    private String filed6Value;
    @TableField("FILED_7_KEY_")
    private String filed7Key;
    @TableField("FILED_7_VALUE_")
    private String filed7Value;
    @TableField("FILED_8_KEY_")
    private String filed8Key;
    @TableField("FILED_8_VALUE_")
    private String filed8Value;
    @TableField("FILED_9_KEY_")
    private String filed9Key;
    @TableField("FILED_9_VALUE_")
    private String filed9Value;
    @TableField("FILED_10_KEY_")
    private String filed10Key;
    @TableField("FILED_10_VALUE_")
    private String filed10Value;
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
    public Dwdxxbjz() {
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
