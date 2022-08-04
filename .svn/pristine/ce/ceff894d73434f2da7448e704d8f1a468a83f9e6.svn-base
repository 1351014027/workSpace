package com.saving.metadata.pojo;

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
 * @since 2020-06-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@TableName("METADATA_B_LABEL_FILED")
@ApiModel(value = "LabelFiled对象", description = "标签字段对象")
public class LabelFiled extends Model<LabelFiled> {

    private static final long serialVersionUID = 1L;

    @Tolerate
    public LabelFiled() {
    }

    @TableId("ID_")
    private String id;

    @TableField("Label_Filed_Name_")
    private String labelFiledName;

    @TableField("Label_Filed_ZN_Name_")
    private String labelFiledZnName;

    @TableField("Label_Filed_Describe_")
    private String labelFiledDescribe;

    @TableField("Label_Filed_Source_")
    private String labelFiledSource;

    @TableField("Label_Parent_ID_")
    private String labelParentId;

    @TableField("Sort_")
    private String sort;

    @TableField("Status_")
    private Integer status;

    @TableField("IsDelete_")
    private Integer isdelete;

    @TableField("SchoolCode_")
    private String schoolcode;

    @TableField("Creator_")
    private String creator;

    @TableField("CreateTime_")
    private Date createtime;

    @TableField("Updateor_")
    private String updateor;

    @TableField("UpdateTime_")
    private Date updatetime;

    @TableField("SortId_")
    private Integer sortid;

    @TableField("IsStandard_")
    private Integer isstandard;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
