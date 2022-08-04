package com.saving.metadata.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.Tolerate;

import java.io.Serializable;

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
@ApiModel(value = "状态数据列表对象", description = "")
@Builder
public class AicTablesVo extends Model<AicTablesVo> {


    private static final long serialVersionUID = 1L;
    private String id;

    /**
     * 加入目录编号
     */
    private String cataNo;
    private String catalog;
    private String tableName;
    private String typeName;
    private Integer fieldSize;

    @Tolerate
    public AicTablesVo() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
