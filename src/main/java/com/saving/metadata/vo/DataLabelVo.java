package com.saving.metadata.vo;

import lombok.*;

import java.io.Serializable;
/**
 * @author : 陈志强
 * @date : 二〇二〇年五月十五日 14:27:52
 * @Description :
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DataLabelVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String yjlm;

    private String ejlm;

    private String sjlm;

    private String bqm;
}
