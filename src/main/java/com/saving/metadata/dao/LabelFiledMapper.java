package com.saving.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saving.metadata.param.LabelFiledParam;
import com.saving.metadata.pojo.LabelFiled;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
public interface LabelFiledMapper extends BaseMapper<LabelFiled> {

    List<String> checkViewFieldExist(@Param("list") List<LabelFiledParam> labelParam);

}
