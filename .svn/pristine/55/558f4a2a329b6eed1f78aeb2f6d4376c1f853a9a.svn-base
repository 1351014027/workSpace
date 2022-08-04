package com.saving.metadata.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.io.Serializable;

/**
 * @author Saving
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@TableName(value = "SAV_IO_教工信息")
public class User implements Serializable {


    private static final long serialVersionUID = 1L;
    @TableId(value = "卡号")
    private String username;
    @TableField(value = "姓名")
    private String name;
    @TableField(value = "部门")
    private String department;
    @TableField(value = "部门ID")
    private String departmentId;
    @TableField(value = "密码")
    private String password;
    @TableField(value = "使用状态")
    private String status;
    @TableField(value = "学校代码")
    private String schoolCode;
    @TableField(value = "性别")
    private String sex;
    @TableField(value = "用工性质")
    private String workCharacteristics;
    @TableField(value = "顺序")
    private Long sort;

    @Tolerate
    public User() {
    }


}
