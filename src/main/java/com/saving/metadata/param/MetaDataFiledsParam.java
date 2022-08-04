package com.saving.metadata.param;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: 陈志强
 * @date: 2019/12/27 15:19
 * @Description:
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class MetaDataFiledsParam implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    @NotEmpty(message = "表ID不能为空!")
    private String tableId;
    private String tableName;
    @NotEmpty(message = "字段编号不能为空!")
    private String filedNumber;
    @NotEmpty(message = "字段名称不能为空!")
    private String filedName;
    @NotEmpty(message = "中文字段释义不能为空!")
    private String chineseFiledName;
    @NotEmpty(message = "存储类型不能为空!")
    private String storageType;
    @NotNull(message = "字段长度不能为空!")
    @Min(value = 0, message = "字段长度不能小于零!")
    private Integer filedLength;
    @NotEmpty(message = "约束不能为空!")
    private String constraints;
    @Length(max = 255, message = "值空间必须在120字以下")
    private String valueSpace;
    @Length(max = 255, message = "数据项说明必须在120字以下")
    private String dataItemDescription;
    @Length(max = 255, message = "引用序号必须在120字以下")
    private String referenceNumber;
    @Length(max = 255, message = "标准来源必须在120字以下")
    private String theStandardSource;
    @Length(max = 255, message = "字段格式必须在120字以下")
    private String filedFormat;
    private Integer decimalLength;
    private Integer isNotNull;
    private Integer isDecimals;
    private Integer isStandard;
    private Integer isPrimary;
    @Length(max = 255, message = "字段注释必须在120字以下")
    private String annotation;
    @Length(max = 255, message = "默认值必须在120字以下")
    private String defaultValue;
    @Length(max = 255, message = "字段索引必须在120字以下")
    private String fieldIndex;

    private Integer userOperation;

    @Tolerate
    public MetaDataFiledsParam() {
    }

}
