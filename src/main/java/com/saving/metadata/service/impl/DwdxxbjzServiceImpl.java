package com.saving.metadata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.saving.metadata.dao.DwdxxbjzMapper;
import com.saving.metadata.dao.HierarchyMapper;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.pojo.Dwdxxbjz;
import com.saving.metadata.pojo.Dwdxxbzdmys;
import com.saving.metadata.pojo.Hierarchy;
import com.saving.metadata.service.DwdxxbjzService;
import com.saving.metadata.service.DwdxxbzdmysService;
import com.saving.metadata.service.OperationTableLogService;
import com.saving.metadata.utils.DateUtil;
import com.saving.metadata.utils.LambdaUtils;
import com.saving.metadata.utils.UUID64Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 陈志强
 * @since 2020-05-18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DwdxxbjzServiceImpl extends ServiceImpl<DwdxxbjzMapper, Dwdxxbjz> implements DwdxxbjzService {

    private final DwdxxbjzMapper dwdxxbjzMapper;

    private final HierarchyMapper hierarchyMapper;

    private final OperationTableLogService operationTableLogService;

    private final DwdxxbzdmysService dwdxxbzdmysService;

    @Autowired
    public DwdxxbjzServiceImpl(DwdxxbjzMapper dwdxxbjzMapper, HierarchyMapper hierarchyMapper, OperationTableLogService operationTableLogService, DwdxxbzdmysService dwdxxbzdmysService) {
        this.dwdxxbjzMapper = dwdxxbjzMapper;
        this.hierarchyMapper = hierarchyMapper;
        this.operationTableLogService = operationTableLogService;
        this.dwdxxbzdmysService = dwdxxbzdmysService;
    }


    @Override
    public void importExcel(List<Map<Integer, String>> listMap, String hierarchyId, String hierarchyKey) {
        if (StringUtils.isEmpty(hierarchyId) || StringUtils.isEmpty(hierarchyKey)) {
            throw new ParamException("选项类型不能为空,请重新登录后尝试!");
        }
        ArrayList<Dwdxxbjz> hierarchies = Lists.newArrayList();
        int count = count(new QueryWrapper<Dwdxxbjz>().lambda().eq(Dwdxxbjz::getIsDelete, 0));
        AtomicInteger num = new AtomicInteger(count);
        Hierarchy hierarchy = hierarchyMapper.selectById(hierarchyId);
        if (hierarchy == null) {
            throw new ParamException("导入的选项类型不存在请检查选项类型是否存在!");
        }
        List<Dwdxxbzdmys> list = dwdxxbzdmysService.list(new QueryWrapper<Dwdxxbzdmys>().lambda().eq(Dwdxxbzdmys::getHierarchyId, hierarchyId));
        if (list.size() != listMap.get(0).size()) {
            throw new ParamException("导入的列数不符合!");
        }
        listMap.forEach(LambdaUtils.consumerWithIndex((obj, index) -> {

            num.getAndIncrement();
            Dwdxxbjz build = Dwdxxbjz.builder()
                    .id(UUID64Utils.get64UUIDString())
                    .isStandard(hierarchy.getIsStandard())
                    .hierarchyId(hierarchyId)
                    .hierarchyKey(hierarchy.getTypeName())
                    .sort(String.valueOf(num.get()))
                    .cdmpVersion(DateUtil.getYyyyMmDd()).build();
            for (int i = 0; i < list.size(); i++) {
                int nums = i + 1;
                switch (nums) {
                    case 1:
                        build.setFiled1Key(obj.get(i));
                        break;
                    case 2:
                        build.setFiled1Value(obj.get(i));
                        break;
                    case 3:
                        build.setFiled2Key(obj.get(i));
                        break;
                    case 4:
                        build.setFiled2Value(obj.get(i));
                        break;
                    case 5:
                        build.setFiled3Key(obj.get(i));
                        break;
                    case 6:
                        build.setFiled3Value(obj.get(i));
                        break;
                    case 7:
                        build.setFiled4Key(obj.get(i));
                        break;
                    case 8:
                        build.setFiled4Value(obj.get(i));
                        break;
                    case 9:
                        build.setFiled5Key(obj.get(i));
                        break;
                    case 10:
                        build.setFiled5Value(obj.get(i));
                        break;
                    case 11:
                        build.setFiled6Key(obj.get(i));
                        break;
                    case 12:
                        build.setFiled6Value(obj.get(i));
                        break;
                    case 13:
                        build.setFiled7Key(obj.get(i));
                        break;
                    case 14:
                        build.setFiled7Value(obj.get(i));
                        break;
                    case 15:
                        build.setFiled8Key(obj.get(i));
                        break;
                    case 16:
                        build.setFiled8Value(obj.get(i));
                        break;
                    case 17:
                        build.setFiled9Key(obj.get(i));
                        break;
                    case 18:
                        build.setFiled9Value(obj.get(i));
                        break;
                    case 19:
                        build.setFiled10Key(obj.get(i));
                        break;
                    case 20:
                        build.setFiled10Value(obj.get(i));
                        break;
                    default:
                        break;
                }
            }
            hierarchies.add(build);
        }));
        saveBatch(hierarchies, 220);
    }
}
