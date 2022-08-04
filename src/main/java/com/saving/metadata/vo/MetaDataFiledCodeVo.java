package com.saving.metadata.vo;

import lombok.*;

import java.io.Serializable;

/**
 * @author: 陈志强
 * @date: 2020/1/9 17:37
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MetaDataFiledCodeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String dataBaseTableName;

    private String dataBaseFileName;

    private String metadataFiledType;

    private String dataBaseFiledType;

    private String metadataIsPrimary;

    private String dataBaseIsPrimary;

    private String dataBaseAllowNull;

    private String metaDataAllowNull;

    private String metadataFiledLength;

    private String dataBaseFiledLength;

    private String metadataFiledLensec;

    private String dataBaseFiledLensec;

    private String pendingStatement;
}
