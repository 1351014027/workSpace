package com.saving.metadata.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.pojo.MetaDataFileds;
import com.saving.metadata.vo.MetaDataTableListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: 陈志强
 * @date: 2020/1/6 10:42
 * @Description:
 */
@Mapper
@Repository
public interface ChartDisplayMapper {
    List<Map<String, Object>> tableLists(@Param("page") Page<Map<String, Object>> page,
                                         @Param("tableName") String tableName,
                                         @Param("list") List<MetaDataFileds> list,
                                         @Param("likeStr") String likeStr);

    List<MetaDataTableListVo> getMetaTableList(@Param("schoolCode") String schoolCode);
}
