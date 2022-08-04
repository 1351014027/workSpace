package com.saving.metadata.pojo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
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
 * @since 2020-02-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("METADATA_CDMP_B_SCHOOLE_CODE")
@ApiModel(value = "SchooleCode对象", description = "")
@Builder
@ContentRowHeight(15)
@HeadRowHeight(20)
@ColumnWidth(25)
public class SchooleCode extends Model<SchooleCode> {

    private static final long serialVersionUID = 1L;
    @TableId("ID_")
    @ExcelIgnore
    private String id;
    @TableField("SchoolCode_")
    @ExcelProperty("学校代码")
    private String schoolcode;
    @TableField("SchoolName_")
    @ExcelProperty("学校名称")
    private String schoolname;
    @TableField("Remark_")
    @ExcelProperty("备注")
    private String remark;
    @TableField("ProvinceCode_")
    @ExcelProperty("省份代码")
    private String provincecode;
    @TableField("ProvinceName_")
    @ExcelProperty("省份名称")
    private String provincename;
    @TableField("CityCode_")
    @ExcelProperty("城市代码")
    private String citycode;
    @TableField("CityName_")
    @ExcelProperty("城市名称")
    private String cityname;
    @TableField("Address_")
    @ExcelProperty("地址")
    private String address;
    @TableField("Nature_")
    @ExcelProperty("性质")
    private String nature;
    @TableField("SchoolUrl_")
    @ExcelProperty("学校官网")
    private String schoolurl;
    @TableField("Status_")
    @ExcelIgnore
    private Integer status;
    @TableField("SchoolType_")
    @ExcelProperty("学校类型")
    private String schooltype;
    @TableField("ApiKey_")
    @ExcelProperty("API键")
    private String apikey;
    @TableField("StartTime_")
    @ExcelProperty("开始时间")
    private Date starttime;
    @TableField("EndTime_")
    @ExcelProperty("结束时间")
    private Date endtime;
    @TableField("IsDelete_")
    @ExcelProperty("是否删除")
    private Integer isdelete;
    @TableField(value = "Creator_", fill = FieldFill.INSERT)
    @ExcelIgnore
    private String creator;
    @TableField("CreateTime_")
    @ExcelIgnore
    private Date createtime;
    @TableField(value = "Updateor_", fill = FieldFill.UPDATE)
    @ExcelIgnore
    private String updateor;
    @TableField(value = "UpdateTime_", fill = FieldFill.UPDATE)
    @ExcelIgnore
    private Date updatetime;
    @TableField("LastUpdateTime_")
    @ExcelProperty("最后更新时间")
    private Date lastupdatetime;
    @TableField("LastUpdateSize_")
    @ExcelProperty("更新次数")
    private Integer lastupdatesize;
    @TableField("LastUpdateIp_")
    @ExcelProperty("最后更新IP")
    private String lastupdateip;

    @Tolerate
    public SchooleCode() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
