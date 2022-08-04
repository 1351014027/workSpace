package com.saving.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.pojo.Label;
import com.saving.metadata.pojo.LabelFiled;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 陈志强
 * @since 2020-06-08
 */
@Repository
@Mapper
public interface LabelMapper extends BaseMapper<Label> {

    int checkViewExist(@Param("labelName") String labelName);

    String checkViewSourceExist(@Param("source") String source);

    List<Map<String, Object>> tableLists(@Param("page") Page<Map<String, Object>> page, @Param("tableName") String tableName,
                                         @Param("list") List<LabelFiled> labelFields, @Param("likeStr") String likeStr);
}
