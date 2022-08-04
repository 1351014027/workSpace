package com.saving.metadata.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2020-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("METADATA_B_REVISIONLOG")
@ApiModel(value = "Revisionlog对象", description = "修订日志")
@Builder
public class Revisionlog extends Model<Revisionlog> {

    private static final long serialVersionUID = 1L;


    @TableId("ID_")
    private String id;
    @TableField("Update_FiledLength_")
    private Integer updateFiledLength;

    @TableField("TableID_")
    private String tableid;

    @TableField("Filed_ID_")
    private String filedId;

    @TableField("TableName_")
    private String tablename;

    @TableField("FiledNumber_")
    private String filednumber;

    @TableField("FiledName_")
    private String filedname;

    @TableField("ChineseFiledName_")
    private String chinesefiledname;

    @TableField("StorageType_")
    private String storagetype;

    @TableField("FiledLength_")
    private Integer filedlength;
    @TableField("Update_DecimalLength_")
    private Integer updateDecimallength;

    @TableField("Constraints_")
    private String constraints;

    @TableField("ValueSpace_")
    private String valuespace;

    @TableField("DataItemDescription_")
    private String dataitemdescription;

    @TableField("ReferenceNumber_")
    private String referencenumber;

    @TableField("TheStandardSource_")
    private String thestandardsource;

    @TableField("FiledFormat_")
    private String filedformat;
    @TableField("DecimalLength_")
    private Integer decimallength;
    @TableField("IsNotNull_")
    private Integer isnotnull;
    @TableField("IsDecimals_")
    private Integer isdecimals;
    @TableField("IsPrimary_")
    private Integer isprimary;
    @TableField("Annotation_")
    private String annotation;
    @TableField("DefaultValue_")
    private String defaultvalue;
    @TableField("FieldIndex_")
    private String fieldindex;
    @TableField("FieldStatus_")
    private Integer fieldstatus;
    @TableField("SchoolCode_")
    private String schoolcode;
    @TableField("SchoolName_")
    private String schoolname;
    @TableField("Operation_Type_")
    private String operationType;
    @TableField("Operation_Status_")
    private Integer operationStatus;
    @TableField("Remark_")
    private String remark;
    @TableField("IsDelete_")
    private Integer isdelete;
    @TableField(value = "Creator_", fill = FieldFill.INSERT)
    private String creator;
    @TableField("CreateTime_")
    private Date createtime;
    @TableField(value = "Updateor_", fill = FieldFill.UPDATE)
    private String updateor;
    @TableField(value = "UpdateTime_", fill = FieldFill.UPDATE)
    private Date updatetime;
    @TableField("CanYouEdit_")
    private Integer canyouedit;
    @TableField("CanYouDelete_")
    private Integer canyoudelete;
    @TableField(value = "Sort_", fill = FieldFill.INSERT)
    private String sort;
    @TableField("SortId_")
    private Integer sortid;
    @TableField("IsStandard_")
    private Integer isstandard;

    @Tolerate
    public Revisionlog() {
    }

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
