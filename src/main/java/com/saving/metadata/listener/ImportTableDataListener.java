package com.saving.metadata.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.pojo.Hierarchy;
import com.saving.metadata.pojo.MetaDataFileds;
import com.saving.metadata.pojo.MetaDataTables;
import com.saving.metadata.pojo.Option;
import com.saving.metadata.service.HierarchyService;
import com.saving.metadata.service.MetaDataFiledsService;
import com.saving.metadata.service.MetaDataTablesService;
import com.saving.metadata.service.OptionService;
import com.saving.metadata.utils.JsonMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author: 陈志强
 * @date: 2019/12/24 17:26
 * @Description:
 */
@Slf4j
public class ImportTableDataListener extends AnalysisEventListener<Map<Integer, String>> {

    private final String tableId;
    List<Map<String, Object>> maps = Lists.newArrayList();
    private final OptionService optionService;
    private final HierarchyService hierarchyService;
    Map<String, Object> mapsData = Maps.newLinkedHashMap();
    private final MetaDataTablesService metaDataTablesService;
    private final MetaDataFiledsService metaDataFiledsService;
    public StringBuilder errorMsg = new StringBuilder();
    Map<String, MetaDataFileds> fieldMap = Maps.newLinkedHashMap();
    MetaDataTables curTable;
    Map<Integer, MetaDataFileds> fields = Maps.newLinkedHashMap();
    Map<String, Map<String, Option>> fieldOption = Maps.newLinkedHashMap();
    Map<String, Map<String, Option>> fieldValueOption = Maps.newLinkedHashMap();



    @Autowired
    public ImportTableDataListener(String tableId, MetaDataFiledsService metaDataFiledsService, MetaDataTablesService metaDataTablesService, OptionService optionService, HierarchyService hierarchyService) {
        this.tableId = tableId;
        this.metaDataFiledsService = metaDataFiledsService;
        this.metaDataTablesService = metaDataTablesService;
        this.optionService = optionService;
        this.hierarchyService = hierarchyService;
        curTable = metaDataTablesService.getOne(new QueryWrapper<MetaDataTables>()
                .lambda().eq(MetaDataTables::getId, tableId));

        if (curTable == null || curTable.getTableStatus() != 1) {
            throw new ParamException("导入的表尚未创建!");
        }
        List<MetaDataFileds> curFields = metaDataFiledsService.list(new QueryWrapper<MetaDataFileds>()
                .lambda().eq(MetaDataFileds::getTableId, tableId));
        curFields.forEach(obj -> {
            if (StringUtils.isNotEmpty(obj.getValueSpace())) {
                Hierarchy hierarchy = hierarchyService.getOne(new QueryWrapper<Hierarchy>().lambda().eq(Hierarchy::getTypeKey, obj.getValueSpace()));
                if (hierarchy != null) {
                    List<Option> list = optionService.list(new QueryWrapper<Option>().lambda().eq(Option::getHierarchyId, hierarchy.getId()));
                    if (CollectionUtils.isNotEmpty(list)) {
                        Map<String, Option> maps = Maps.newLinkedHashMap();
                        Map<String, Option> mapValues = Maps.newLinkedHashMap();
                        list.forEach(option -> {
                            maps.put(option.getKey(), option);
                            mapValues.put(option.getName(), option);
                        });
                        fieldOption.put(obj.getValueSpace(), maps);
                        fieldValueOption.put(obj.getValueSpace(), maps);
                    }
                }
            }
        });
        if (CollectionUtils.isEmpty(curFields)) {
            throw new ParamException("导入的表不存在字段,请检查后尝试!");
        }
        curFields.forEach(obj -> fieldMap.put(obj.getChineseFiledName(), obj));
    }

    /**
     * 这里会一行行的返回头
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("解析到一条头数据:{}", JsonMapperUtil.object2String(headMap));
        headMap.forEach((key, value) -> fields.put(key, fieldMap.get(value)));
    }

    public static boolean isInteger(String str) {
        Pattern pattern = compile("/^-?\\d+(\\.\\d+)?$/");
        return pattern.matcher(str).matches();
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        if (StringUtils.isEmpty(tableId)) {
            throw new ParamException("导入的表尚未创建!");
        }
        if (CollectionUtils.isNotEmpty(maps)) {
            if (StringUtils.isNotEmpty(errorMsg.toString())) {
                throw new ParamException(errorMsg.toString());
            }
            batchInsert(maps, fields, curTable.getTableName(), 50);
            log.info("{}条数据，开始存储数据库！", this.maps.size());
            log.info("存储数据库成功！");
        }


    }

    @Override
    public void invoke(Map<Integer, String> bodyMap, AnalysisContext context) {
        log.info("解析到一条数据:{}", JsonMapperUtil.object2String(bodyMap));
        StringBuilder fieldsErrorMsg = new StringBuilder();
        bodyMap.forEach((key, value) -> {
            String filedName = fields.get(key).getFiledName();
            if (StringUtils.isNotEmpty(filedName)) {
                MetaDataFileds metaDataFileds = fields.get(key);
                String checkVal = checkVal(metaDataFileds, value);
                if (StringUtils.isNotEmpty(checkVal)) {
                    fieldsErrorMsg.append("<span style='color: red;'>【" + fields.get(key).getChineseFiledName() + "】字段效验不通过</span>,问题：[").append(checkVal).append("];");
                }
                if (StringUtils.isNotEmpty(fields.get(key).getValueSpace())) {
                    Map<String, Option> optionMap = fieldOption.get(metaDataFileds.getValueSpace());
                    Map<String, Option> optionValuesMap = fieldValueOption.get(metaDataFileds.getValueSpace());
                    Option option = optionMap.get(value);
                    if (option == null) {
                        option = optionValuesMap.get(value);
                        if (option != null) {
                            mapsData.put(fields.get(key).getFiledName(), option.getKey());
                        } else {
                            mapsData.put(fields.get(key).getFiledName(), "");
                        }
                    } else {
                        mapsData.put(fields.get(key).getFiledName(), value);
                    }
                } else {
                    mapsData.put(fields.get(key).getFiledName(), value);
                }
            }

        });
        if (StringUtils.isNotEmpty(fieldsErrorMsg)) {
            errorMsg.append("第").append(maps.size() + 1).append("行的数据").append("存在以下问题:").append(fieldsErrorMsg).append("</br>");
        }
        maps.add(mapsData);
    }

    private void batchInsert(List<Map<String, Object>> ints, Map<Integer, MetaDataFileds> fields, String tableName, int count) {
        int begin = 0;
        int end = begin + count;
        while (begin <= ints.size() - 1) {
            List<Map<String, Object>> integers = ints.subList(begin, Math.min(end, ints.size()));
            metaDataTablesService.insertListMaps(integers, fields, tableName);
            begin = end;
            end = begin + count;
        }

    }

    public String checkVal(MetaDataFileds metaDataFileds, String value) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(metaDataFileds.getValueSpace())) {
            Map<String, Option> optionMap = fieldOption.get(metaDataFileds.getValueSpace());
            Map<String, Option> optionValuesMap = fieldValueOption.get(metaDataFileds.getValueSpace());
            if (MapUtils.isNotEmpty(optionMap) && MapUtils.isNotEmpty(optionValuesMap)) {
                boolean checkValue = checkField(optionMap, optionValuesMap, value);
                if (checkValue) {
                    stringBuilder.append("<span style='color: red;'>下拉选项值不对应;</span>");
                }
            }
        }
        boolean checkLength = checkField(metaDataFileds.getFiledLength(), value);
        if (checkLength) {
            stringBuilder.append("<span style='color: red;'>长度超出;</span>");
        }
        if (metaDataFileds.getDecimalLength() != null) {
            boolean checkField = checkField(metaDataFileds.getDecimalLength(), value);
            if (checkField) {
                stringBuilder.append("<span style='color: red;'>小数位数长度超出;</span>");
            }
        }
        if ("M".equalsIgnoreCase(metaDataFileds.getStorageType()) || "N".equalsIgnoreCase(metaDataFileds.getStorageType())) {
            boolean checkIsInt = checkIsInt(value);
            if (checkIsInt) {
                stringBuilder.append("<span style='color: red;'>值必须是数字类型;</span>");
            }
        }
        if (StringUtils.isNotEmpty(metaDataFileds.getFiledFormat())) {
            boolean checkFormat = checkFormat(metaDataFileds.getFiledFormat(), value);
            if (checkFormat) {
                stringBuilder.append("<span style='color: red;'>值格式不正确;</span>");
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 效验下拉选项
     **/
    private boolean checkField(Map<String, Option> lists, Map<String, Option> optionValuesMap, String str) {
        Option option = lists.get(str);
        if (option == null) {
            option = optionValuesMap.get(str);
        }
        return option == null;
    }

    /**
     * 效验长度
     **/
    private boolean checkField(Integer strLength, String str) {
        return str.length() > strLength;
    }

    /**
     * 效验类型
     **/
    private boolean checkIsInt(String str) {
        return !isInteger(str);
    }

    /**
     * 效验格式
     **/
    private boolean checkFormat(String strFormat, String str) {
        if (StringUtils.isNotEmpty(strFormat)) {
            return !str.matches(strFormat);
        } else {
            return false;
        }

    }
}
