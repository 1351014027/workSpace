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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
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
@TableName("METADATA_B_LABEL_MAPPER")
@ApiModel(value = "LabelMapper对象", description = "标签映射对象")
public class LabelMapper extends Model<LabelMapper> {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "标签ID不能为空!")
    @Length(max = 64, message = "标签ID名称必须是64字符！")
    @TableField("Label_Parent_ID_")
    private String labelParentId;

    @TableId("ID_")
    private String id;

    @Tolerate
    public LabelMapper() {
    }

    @NotEmpty(message = "来源表名不能为空!")
    @Length(max = 255, message = "来源表名必须是125字符以下！")
    @TableField("Source_Table_Name_")
    private String sourceTableName;

    @NotEmpty(message = "来源中文表名不能为空!")
    @Length(max = 255, message = "来源中文表名必须是125字符以下！")
    @TableField("Source_Table_ZN_Name_")
    private String sourceTableZnName;

    @NotEmpty(message = "目标中文表名不能为空!")
    @Length(max = 255, message = "目标中文表名必须是125字符以下！")
    @TableField("Target_Table_Name_")
    private String targetTableName;

    @NotEmpty(message = "目标中文表名不能为空!")
    @Length(max = 255, message = "目标中文表名必须是125字符以下！")
    @TableField("Target_Table_ZN_Name_")
    private String targetTableZnName;

    @NotEmpty(message = "来源字段名不能为空!")
    @Length(max = 255, message = "来源字段名必须是125字符以下！")
    @TableField("Source_Filed_Name_")
    private String sourceFiledName;

    @NotEmpty(message = "来源字段中文名不能为空!")
    @Length(max = 255, message = "来源字段中文名必须是125字符以下！")
    @TableField("Source_Filed_ZN_Name_")
    private String sourceFiledZnName;

    @NotEmpty(message = "目标字段名不能为空!")
    @Length(max = 255, message = "目标字段名必须是125字符以下！")
    @TableField("Target_Filed_Name_")
    private String targetFiledName;

    @NotEmpty(message = "目标字段中文名不能为空!")
    @Length(max = 255, message = "目标字段中文名必须是125字符以下！")
    @TableField("Target_Filed_ZN_Name_")
    private String targetFiledZnName;

    @TableField("SchoolCode_")
    private String schoolcode;

    @TableField("Creator_")
    private String creator;

    @TableField("CreateTime_")
    private Date createtime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
