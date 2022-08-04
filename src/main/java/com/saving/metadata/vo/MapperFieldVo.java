package com.saving.metadata.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

/**
 * <p>
 *
 * </p>
 *
 * @author 陈志强
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "映射字段列表对象", description = "")
@Builder
public class MapperFieldVo extends Model<MapperFieldVo> {


    private static final long serialVersionUID = 1L;
    private String oneTableId;
    private String oneTableName;
    private String oneTableFieldName;
    private String oneTableFieldId;
    private String oneTableFieldNumber;
    private String oneTableChineseFiledName;
    private String twoTableId;
    private String twoTableName;
    private String twoTableFieldName;
    private String twoTableFieldId;
    private String twoTableFieldNumber;
    private String twoTableChineseFiledName;
    private Integer states;

    @Tolerate
    public MapperFieldVo() {
    }


}
