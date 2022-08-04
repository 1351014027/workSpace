package com.saving.metadata.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.saving.metadata.pojo.Tysjgl;
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
 * @since 2020-05-03
 */
@Mapper
@Repository

public interface TysjglMapper extends BaseMapper<Tysjgl> {

    void delDataBase(@Param("list") List<Tysjgl> list);

    void createDataBase(@Param("userName") String userName, @Param("userPassWord") String userPassWord, @Param("dataBaseName") String dataBaseName, @Param("path") String path);

    String checkDataBaseUser(@Param("userName") String userName);
}
