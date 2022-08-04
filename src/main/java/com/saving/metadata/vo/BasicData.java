package com.saving.metadata.vo;

import lombok.*;

import java.math.BigDecimal;

/**
 * 树形对象中的自带参数
 *
 * @author Mr.Saving
 * @date 2019-01-21 17:06
 **/
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class BasicData {
    @Builder.Default
    private Boolean isHreaf = false;
    @Builder.Default
    private Boolean isSfzkd = false;
    @Builder.Default
    private Boolean isRw = false;

    private BigDecimal bz;

    private String xzz;

    private String wjm;

    private String bcwjm;

}
