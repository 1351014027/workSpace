package com.saving.metadata.param;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * @author: 陈志强
 * @date: 2020/6/8 15:51
 * @Description:
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class LabelFiledParam {

    @Tolerate
    public LabelFiledParam() {
    }

    private String id;

    @NotEmpty(message = "标签字段英文名称不能为空!")
    @Length(max = 64, message = "标签英文字段名称必须在32字以下！")
    private String labelFiledName;

    @NotEmpty(message = "标签字段中文名称不能为空!")
    @Length(max = 64, message = "标签中文字段名称必须在32字以下！")
    private String labelFiledZnName;

    @Length(max = 255, message = "标签字段描述必须在125字以下！")
    private String labelFiledDescribe;

    @Length(max = 64, message = "标签数据来源必须在64字以下！")
    private String labelFiledSource;

    @NotEmpty(message = "标签ID不能为空!")
    @Length(max = 64, message = "标签ID名称必须是64字符！")
    private String labelParentId;


}
