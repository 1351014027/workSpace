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
 * @since 2020-04-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("METADATA_B_API_PERMISSION_")
@ApiModel(value = "ApiPermission对象", description = "")
@Builder
public class ApiPermission extends Model<ApiPermission> {

    private static final long serialVersionUID = 1L;
    @TableId("ID_")
    private String id;
    @TableField("Table_Id_")
    private String tableId;
    @TableField("Chinese_Table_Name_")
    private String chieseTableName;
    @TableField("Tag_")
    private String tag;
    @TableField("Api_Table_Id_")
    private String apiTableId;
    @TableField("Sys_Name_")
    private String sysName;
    @TableField("Is_Save_")
    private Integer isSave;
    @TableField("Is_Update_")
    private Integer isUpdate;
    @TableField("Is_Delete_Cur_")
    private Integer isDeleteCur;
    @TableField("Is_Get_")
    private Integer isGet;
    @TableField("IsDelete_")
    private Integer isdelete;
    @TableField(value = "School_Code_", fill = FieldFill.INSERT)
    private String schoolCode;
    @TableField(value = "Creator_", fill = FieldFill.INSERT)
    private String creator;
    @TableField("Create_Time_")
    private Date createTime;
    @TableField(value = "Create_IP_")
    private String creatorIp;
    @TableField(value = "Updateor_", fill = FieldFill.UPDATE)
    private String updateor;
    @TableField(value = "Update_Time_", fill = FieldFill.UPDATE)
    private Date updateTime;
    @TableField(value = "Update_IP_")
    private String updateIp;

    @Tolerate
    public ApiPermission() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
