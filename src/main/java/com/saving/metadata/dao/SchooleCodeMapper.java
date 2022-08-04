package com.saving.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saving.metadata.pojo.SchooleCode;
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
 * @since 2020-02-24
 */
@Mapper
@Repository
public interface SchooleCodeMapper extends BaseMapper<SchooleCode> {


    void updateByIdsIsDel(@Param("list") List<String> list);
}
