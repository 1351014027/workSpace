package com.saving.metadata.param;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author: 陈志强
 * @date: 二〇二〇年五月十五日 14:27:52
 * @Description:
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ApiTableParam extends Model<ApiTableParam> {

    private static final long serialVersionUID = 1L;

    private String id;

    @NotEmpty(message = "软件系统名称不能为空!")
    @Length(max = 100, message = "软件系统名称必须在50字以下！")
    private String sysName;

    @NotEmpty(message = "软件编号不能为空!")
    @Length(max = 10, message = "软件编号字数不能超过10个字符！")
    private String rjbh;

    @Length(max = 40, message = "版本号字数不能超过40个字符！")
    private String bbh;

    @NotEmpty(message = "来源不能为空!")
    @Length(max = 1, message = "来源不能超过1个字符！")
    private String ly;

    @Length(max = 18, message = "(准)捐赠单位代码不能超过18个字符！")
    private String jzdwdm;

    @Length(max = 100, message = "(准)捐赠者名称不能超过100个字符！")
    private String jzzmc;

    @NotEmpty(message = "生产国别码不能为空!")
    @Length(max = 3, message = "生产国别码不能超过不能超过3个字符！")
    private String scgbm;

    @NotEmpty(message = "购置日期不能为空!")
    @Length(max = 8, message = "购置日期不能超过不能超过8个字符！")
    private String gzrq;

    @Length(max = 18, message = "生产厂家社会信用码不能超过不能超过18个字符！")
    private String sccjxym;

    @Length(max = 100, message = "生产厂家名称不能超过不能超过100个字符！")
    private String sccjmc;


    @NotNull(message = "单价不能为空！")
    private BigDecimal dj;

    private String gnjj;

    @Length(max = 1, message = "现状码不能超过1个字符！")
    private String xzm;

    private String sysUrl;

    @NotEmpty(message = "key不能为空!")
    @Length(max = 500, message = "key字数不能超过500个字符！")
    private String key;

    @Length(max = 500, message = "生产厂家联系人字数不能超过500个字符！")
    private String manufacturersUser;

    @Length(max = 50, message = "生产厂家联系人电话字数不能超过50个字符！")
    private String manufacturersPhone;

    private Integer issjyxt;

    private Integer issjmbxt;


    @Tolerate
    public ApiTableParam() {
    }
}
