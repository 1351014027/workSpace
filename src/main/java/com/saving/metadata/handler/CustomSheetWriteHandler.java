package com.saving.metadata.handler;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.saving.metadata.pojo.Hierarchy;
import com.saving.metadata.pojo.MetaDataFileds;
import com.saving.metadata.pojo.Option;
import com.saving.metadata.service.HierarchyService;
import com.saving.metadata.service.MetaDataFiledsService;
import com.saving.metadata.service.OptionService;
import com.saving.metadata.utils.LambdaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: 陈志强
 * @date: 2020/8/5 10:10
 * @Description:
 */
@Slf4j
public class CustomSheetWriteHandler implements SheetWriteHandler {

    private MetaDataFiledsService metaDataFiledsService;
    private HierarchyService hierarchyService;
    private OptionService optionService;
    private String tableId;

    public CustomSheetWriteHandler(String tableId, MetaDataFiledsService metaDataFiledsService, HierarchyService hierarchyService, OptionService optionService) {
        this.metaDataFiledsService = metaDataFiledsService;
        this.hierarchyService = hierarchyService;
        this.optionService = optionService;
        this.tableId = tableId;
    }

    public CustomSheetWriteHandler() {

    }

    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {

    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        List<MetaDataFileds> list = metaDataFiledsService.list(new QueryWrapper<MetaDataFileds>().eq("TableID_", tableId)
                .orderByAsc("cast(Sort_ as int)")
                .orderByAsc("SortId_")
                .orderByAsc("CreateTime_"));
        AtomicInteger hideSheet = new AtomicInteger();
        list.forEach(LambdaUtils.consumerWithIndex((obj, index) -> {
            List<String> str = Lists.newArrayList();
            Hierarchy hierarchy = hierarchyService.getOne(new QueryWrapper<Hierarchy>().lambda().eq(Hierarchy::getTypeKey, obj.getValueSpace()));
            if (hierarchy != null) {
                List<Option> lists = optionService.list(new QueryWrapper<Option>().eq("Hierarchy_ID_", hierarchy.getId())
                        .orderByAsc("cast(Sort_ as int)")
                        .orderByAsc("SortId_")
                        .orderByAsc("CreateTime_"));
                lists.forEach(curObj -> {
                    str.add(curObj.getName());
                });
            }
            if (CollectionUtils.isNotEmpty(str)) {
                DataValidationHelper helper = writeSheetHolder.getSheet().getDataValidationHelper();
                Workbook workbook = writeWorkbookHolder.getWorkbook();
                String hiddenName = "hidden" + index + "x";
                Sheet hidden = workbook.createSheet(hiddenName);
                for (int i = 0, length = str.size(); i < length; i++) {
                    hidden.createRow(i).createCell(0).setCellValue(str.get(i));
                }
                Name category1Name = workbook.createName();
                hideSheet.getAndIncrement();
                category1Name.setNameName(hiddenName);
                category1Name.setRefersToFormula(hiddenName + "!$A$1:$A$" + str.size());
                CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 50000, index, index);
                DataValidationConstraint constraint = helper.createFormulaListConstraint(hiddenName);
                DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
                workbook.setSheetHidden(hideSheet.get(), true);
                writeSheetHolder.getSheet().addValidationData(dataValidation);
            }
        }));
    }


}
