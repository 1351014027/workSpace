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
 * @since 2020-03-15
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@TableName("METADATA_B_PERMISSION")
@ApiModel(value = "Permission对象", description = "")
public class Permission extends Model<Permission> {

    private static final long serialVersionUID = 1L;
    @TableId("ID_")
    private String id;
    @TableField("User_Name_")
    private String userName;
    @TableField("Name_")
    private String name;
    @TableField("Authority_Id_")
    private String authorityId;
    @TableField("Authority_")
    private String authority;
    @TableField("Creator_")
    private String creator;
    @TableField("CreateTime_")
    private Date createtime;

    @Tolerate
    public Permission() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
