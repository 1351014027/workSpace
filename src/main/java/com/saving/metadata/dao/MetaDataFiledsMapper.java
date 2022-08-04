package com.saving.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saving.metadata.pojo.MetaDataFileds;
import com.saving.metadata.vo.MapperFieldVo;
import com.saving.metadata.vo.MetaDataFiledCodeVo;
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
 * @since 2019-12-18
 */
@Mapper
@Repository
public interface MetaDataFiledsMapper extends BaseMapper<MetaDataFileds> {

    List<MetaDataFiledCodeVo> getUpdatedFiledCode(@Param("list") List<String> ids);

    List<MetaDataFiledCodeVo> getUpdatedFiledCodeByid(@Param("id") String id);

    int executeUpdatedFiledCode(@Param("str") String str);

    List<MetaDataFileds> getViewFiled(@Param("tableId") String tableId);

    List<MapperFieldVo> showLabelDataTable(@Param("list") List<String> list);

    List<MapperFieldVo> showLabelDataTables(@Param("list") List<String> list);

    List<MetaDataFileds> getShowFieldList(@Param("maps") Map<String, String> maps);

    void createView(@Param("labelName") String labelName, @Param("list") List<MetaDataFileds> list, @Param("sqlStr") String sqlStr);


}
