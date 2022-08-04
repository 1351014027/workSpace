package com.saving.metadata.service.impl;

import com.google.common.collect.Lists;
import com.saving.metadata.dao.AntVMapper;
import com.saving.metadata.pojo.AntV;
import com.saving.metadata.service.AntVService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AntVServiceImpl implements AntVService {

    private final AntVMapper antVMapper;

    @Autowired
    public AntVServiceImpl(AntVMapper antVMapper) {
        this.antVMapper = antVMapper;
    }

    @Override
    public List<AntV> getData(String tableId) {
        List<AntV> treeData = antVMapper.finddata(tableId);
        List<AntV> antvList = new ArrayList<>();
        for (int i = 0; i < treeData.size(); i++) {
            if (treeData.get(i).getId().equals(tableId)) {
                antvList.add(treeData.get(i));
                i = treeData.size();
            }
        }
        for (AntV antv : antvList) {
            antv.setChildren(getChild(antv.getId(), treeData));
        }
        return antvList;
    }

    @Override
    public List<List<Map<String, String>>> getChartsData(String tableId) {
        List<List<Map<String, String>>> lists = Lists.newArrayList();
        List<Map<String, String>> chartsData = antVMapper.findChartsData(tableId);
        List<Map<String, String>> chartsLinks = antVMapper.findChartsLinks(tableId);
        List<Map<String, String>> chartsCategories = antVMapper.findChartsCategories(tableId);
        lists.add(chartsData);
        lists.add(chartsLinks);
        lists.add(chartsCategories);
        return lists;
    }

    /**
     * 子菜单递归
     */
    private List<AntV> getChild(String id, List<AntV> rootAntv) {
        // 子菜单
        List<AntV> childList = new ArrayList<>();
        for (AntV antV : rootAntv) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (antV.getParentId().equals(id)) {
                childList.add(antV);
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (AntV menu : childList) {
            menu.setChildren(getChild(menu.getId(), rootAntv));
        }
        // 判断递归结束
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }


}
