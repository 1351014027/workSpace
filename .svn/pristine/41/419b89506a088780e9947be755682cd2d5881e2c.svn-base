package com.saving.metadata.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
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
@TableName("METADATA_B_API_TABLE_LOG")
@ApiModel(value = "ApiTableLog对象", description = "")
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(25)
@Builder
public class ApiTableLog extends Model<ApiTableLog> {

    private static final long serialVersionUID = 1L;
    @ExcelIgnore
    @TableId("ID_")
    private String id;
    @ExcelIgnore
    @TableField("Table_Id_")
    private String tableId;
    @ExcelProperty("操作表名")
    @TableField("Table_Name_")
    private String tableName;
    @ExcelProperty("操作类型")
    @TableField("Type_")
    private String type;
    @ExcelProperty("数据集")
    @TableField("Json_Data_")
    private String jsonData;
    @ExcelProperty("影响记录数")
    @TableField("Data_Count_")
    private Integer dataCount;
    @ExcelIgnore
    @TableField("Operation_Sys_Id_")
    private String operationSysId;
    @ExcelProperty("操作业务系统名称")
    @TableField("Operation_Sys_Name_")
    private String operationSysName;
    @ExcelProperty("操作IP")
    @TableField("Operation_Ip_")
    private String operationIp;
    @ExcelProperty("操作时间")
    @TableField("Operation_Time_")
    private Date operationTime;

    @Tolerate
    public ApiTableLog() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
