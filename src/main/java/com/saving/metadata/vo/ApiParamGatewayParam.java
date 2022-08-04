package com.saving.metadata.vo;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import java.util.List;
import java.util.Map;

/**
 * @author: 陈志强
 * @date: 2020/6/2 16:37
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "网关接口参数对象", description = "网关接口参数对象")
@Builder
public class ApiParamGatewayParam extends Model<ApiParamGatewayParam> {
    private static final long serialVersionUID = 1L;
    private String tableName;
    private String schoolCode;
    private String interfaceType;
    private String apiKey;
    private String sysNumber;
    private Map<String, List<Map<String, String>>> paramMaps;


    @Tolerate
    public ApiParamGatewayParam() {
    }

}
