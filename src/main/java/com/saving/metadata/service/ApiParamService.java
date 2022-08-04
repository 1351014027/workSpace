package com.saving.metadata.service;

import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.pojo.ApiTable;

import java.util.List;
import java.util.Map;

/**
 * @author: 陈志强
 * @date: 2020/6/1 10:19
 * @Description:
 */
public interface ApiParamService {

    <T> ServerResponse<T> apiMetaDataInterface(String tableName, String schoolCode, String interfaceType, String apiKey, String sysNumber, Map<String, List<Map<String, String>>> paramMaps);

    <T> ServerResponse<T> saveTableData(String tableName, String schoolCode, ApiTable apiTable, Map<String, List<Map<String, String>>> paramMaps);

    <T> ServerResponse<T> deleteTableData(String tableName, String schoolCode, ApiTable apiTable, Map<String, List<Map<String, String>>> paramMaps);

    <T> ServerResponse<T> updateTableData(String tableName, String schoolCode, ApiTable apiTable, Map<String, List<Map<String, String>>> paramMaps);

    <T> ServerResponse<T> selectTableData(String tableName, String schoolCode, ApiTable apiTable, Map<String, List<Map<String, String>>> paramMaps);

}
