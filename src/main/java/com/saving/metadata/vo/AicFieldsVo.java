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
 * @author 严嘉炜
 * @since 2020年3月30日 15:01:19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "状态数据字段列表对象", description = "")
@Builder
public class AicFieldsVo extends Model<AicFieldsVo> {


    private static final long serialVersionUID = 1L;
    private String id;
    private String zdm;
    private String zdlx;
    private String zdkd;
    private String rqgs;

    @Tolerate
    public AicFieldsVo() {
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
