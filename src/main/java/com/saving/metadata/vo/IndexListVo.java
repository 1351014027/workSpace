package com.saving.metadata.vo;

import com.saving.metadata.pojo.MetaDataFileds;
import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author: 陈志强
 * @date: 2020/4/18 10:42
 * @Description:
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class IndexListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;

    private String catalog;

    private String tableName;

    private String chineseTableName;

    private String remark;

    private List<MetaDataFileds> metaDataFiledsList;

}
