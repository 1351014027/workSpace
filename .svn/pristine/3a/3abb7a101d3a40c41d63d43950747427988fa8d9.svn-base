package com.saving.metadata.param;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotEmpty;

/**
 * @author: 陈志强
 * @date: 二〇二〇年五月十五日 14:27:52
 * @Description: 学校效验参数类
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class SchooleCodeParam {
    private static final long serialVersionUID = 1L;
    private String id;
    @NotEmpty(message = "学校代码不能为空!")
    private String schoolcode;
    @NotEmpty(message = "API键不能为空!")
    private String apikey;
    @NotEmpty(message = "开始时间不能为空!")
    private String starttime;
    @NotEmpty(message = "结束时间不能为空!")
    private String endtime;

    @Tolerate
    public SchooleCodeParam() {
    }


}
