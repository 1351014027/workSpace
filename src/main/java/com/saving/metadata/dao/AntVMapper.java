package com.saving.metadata.dao;


import com.saving.metadata.pojo.AntV;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Mapper
@Repository
public interface AntVMapper {

    List<AntV> finddata(@Param("tableId") String tableId);

    List<Map<String, String>> findChartsData(@Param("tableId") String tableId);

    List<Map<String, String>> findChartsLinks(@Param("tableId") String tableId);

    List<Map<String, String>> findChartsCategories(@Param("tableId") String tableId);
}
