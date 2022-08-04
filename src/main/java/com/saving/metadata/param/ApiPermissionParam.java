package com.saving.metadata.param;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotEmpty;

/**
 * @author Administrator
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ApiPermissionParam extends Model<ApiPermissionParam> {

    private static final long serialVersionUID = 1L;
    private String id;
    @NotEmpty(message = "表格ID不能为空!")
    private String tableId;
    private String chieseTableName;
    private String tag;
    @NotEmpty(message = "业务系统UUID不能为空!")
    private String apiTableId;
    private String sysName;
    private Integer isSave;
    private Integer isUpdate;
    private Integer isDeleteCur;
    private Integer isGet;

    @Tolerate
    public ApiPermissionParam() {
    }
}
