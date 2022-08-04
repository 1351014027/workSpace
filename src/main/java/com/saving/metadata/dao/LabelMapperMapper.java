package com.saving.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saving.metadata.pojo.LabelMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
public interface LabelMapperMapper extends BaseMapper<LabelMapper> {

}
