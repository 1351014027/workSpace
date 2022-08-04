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
 * @since 2020-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ConversionRecord", description = "ETL转换作业记录")
@Builder
@TableName("METADATA_B_CONVERSION_RECORD_")
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(25)
public class ConversionRecord extends Model<ConversionRecord> {

    private static final long serialVersionUID = 1L;
    @ExcelIgnore
    @TableId("ID_")
    private String id;
    @ExcelProperty("转换名称")
    @TableField("Transformations_Name_")
    private String transformationsName;
    @ExcelProperty("转换路径")
    @TableField("Transformations_Path_")
    private String transformationsPath;
    @ExcelProperty("转换说明")
    @TableField("Transformations_Explained_")
    private String transformationsExplained;
    @ExcelProperty("转换状态")
    @TableField("Transformations_Stauts_")
    private String transformationsStauts;
    @ExcelProperty("开始时间")
    @TableField("Start_Time_")
    private Date startTime;
    @ExcelProperty("结束时间")
    @TableField("End_Time_")
    private Date endTime;
    @ExcelProperty("执行时间")
    @TableField("Execution_Time_")
    private String executionTime;
    @ExcelProperty("操作说明")
    @TableField("Operating_Record_")
    private Integer operatingRecord;
    @ExcelProperty("操作表记录")
    @TableField("Operating_Table_Length_")
    private Integer operatingTableLength;
    @ExcelProperty("源头表")
    @TableField("Source_Table_")
    private String sourceTable;
    @ExcelProperty("目标表")
    @TableField("Target_Table_")
    private String targetTable;
    @ExcelProperty("操作类型")
    @TableField("Operation_Type_")
    private String operationType;

    @Tolerate
    public ConversionRecord() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
