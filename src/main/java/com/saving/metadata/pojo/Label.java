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
@TableName("METADATA_B_LABEL")
@ApiModel(value = "Label对象", description = "标签对象")
@Builder
public class Label extends Model<Label> {

    private static final long serialVersionUID = 1L;

    @Tolerate
    public Label() {
    }

    @TableId("ID_")
    private String id;

    @TableField("Hierarchy_ID_")
    private String hierarchyId;

    @TableField("Hierarchy_Key_")
    private String hierarchyKey;

    @TableField("Hierarchy_Name_")
    private String hierarchyName;

    @TableField("Label_Name_")
    private String labelName;

    @TableField("Label_ZN_Name_")
    private String labelZnName;

    @TableField("Label_Name_Describe_")
    private String labelNameDescribe;

    @TableField("DataBase_Name_")
    private String databaseName;

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
