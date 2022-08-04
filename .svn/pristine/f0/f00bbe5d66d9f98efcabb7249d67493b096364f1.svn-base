package com.saving.metadata.controller;

/**
 * @author: 陈志强
 * @date: 2019/12/16 16:36
 * @Description:
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.pojo.*;
import com.saving.metadata.service.*;
import com.saving.metadata.utils.JsonMapperUtil;
import com.saving.metadata.vo.MetaDataFiledCodeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @author: 陈志强
 * @date: 2019/12/12 10:11
 * @Description: 页面跳转相关
 */
@Controller
@Api(tags = "弹窗页面跳转相关接口")
@Slf4j
@RequestMapping("/popup")
public class MetaDataPopupController {

    private final HierarchyService hierarchyService;
    private final OptionService optionService;
    private final MetaDataTablesService metaDataTablesService;
    private final MetaDataFiledsService metaDataFiledsService;
    private final UserService userService;
    private final PermissionService permissionService;
    private final SchooleCodeService schooleCodeService;
    private final ApiTableService apiTableService;
    private final TysjglService tysjglService;
    private final RevisionlogService revisionlogService;

    @Value("${application.isCloud}")
    private Boolean isCloud;

    @Autowired
    public MetaDataPopupController(HierarchyService hierarchyService,
                                   OptionService optionService,
                                   MetaDataTablesService metaDataTablesService,
                                   MetaDataFiledsService metaDataFiledsService,
                                   UserService userService,
                                   PermissionService permissionService,
                                   SchooleCodeService schooleCodeService,
                                   ApiTableService apiTableService,
                                   TysjglService tysjglService, RevisionlogService revisionlogService) {
        this.hierarchyService = hierarchyService;
        this.optionService = optionService;
        this.metaDataTablesService = metaDataTablesService;
        this.metaDataFiledsService = metaDataFiledsService;
        this.userService = userService;
        this.permissionService = permissionService;
        this.schooleCodeService = schooleCodeService;
        this.apiTableService = apiTableService;
        this.tysjglService = tysjglService;
        this.revisionlogService = revisionlogService;
    }

    @GetMapping("typePopup.page")
    @ApiOperation("类型弹窗页面映射URL")
    public String typePopup(String id, Model model) {
        Hierarchy hierarchy = hierarchyService.getById(id);
        model.addAttribute("hierarchy", hierarchy);
        return MessageFormat.format("popup/{0}", "typePopup");
    }

    @GetMapping("metadataVerifiedPopup.page")
    @ApiOperation("元数据字段效验页面映射URL")
    public String metadataVerifiedPopup(String id, Model model) {
        List<MetaDataFiledCodeVo> updatedFiledCodeCache = metaDataFiledsService.setUpdatedFiledCodeCache(id, null);
        String string = JsonMapperUtil.object2String(updatedFiledCodeCache);
        model.addAttribute("lists", string);
        model.addAttribute("id", id);
        return MessageFormat.format("popup/{0}", "metadataVerifiedPopup");
    }

    @GetMapping("optionPopup.page")
    @ApiOperation("类型选项弹窗页面映射URL")
    public String optionPopup(String id, Model model) {
        Option option = optionService.getById(id);
        model.addAttribute("option", option);
        return MessageFormat.format("popup/{0}", "optionPopup");
    }


    @GetMapping("tablePopup.page")
    @ApiOperation("表格弹窗页面映射URL")
    public String tablePopup(String id, Model model, Boolean isStandard) {
        List<Hierarchy> metadataType = hierarchyService.list(new QueryWrapper<Hierarchy>().lambda().eq(Hierarchy::getParent, 7)
                .eq(Hierarchy::getSchoolCode, RequestHolder.getCurrenSysUser().getSchoolCode()));
        MetaDataTables metaDataTables = metaDataTablesService.getById(id);

        model.addAttribute("metaDataTables", metaDataTables);
        model.addAttribute("metadataType", metadataType);
        model.addAttribute("isStandard", isStandard != null && isStandard);
        return MessageFormat.format("popup/{0}", "tablePopup");
    }

    @GetMapping("fieldPopup.page")
    @ApiOperation("元数据字段弹窗页面映射URL")
    public String fieldPopup(String id, Integer isStandard, Model model) {
        MetaDataFileds filed = metaDataFiledsService.getById(id);
        List<Option> szList = optionService.list(new QueryWrapper<Option>().lambda().eq(Option::getHierarchyId, "20191230150329457633b5ae2c69545b399214dcc9759fbf4989332421791579"));
        List<Option> ysList = optionService.list(new QueryWrapper<Option>().lambda().eq(Option::getHierarchyId, "2019123015062770018f9464a594a44b791878053774962fd461947319397162"));
        isStandard = (isStandard == null ? 0 : isStandard);
        model.addAttribute("filed", filed);
        model.addAttribute("szList", szList);
        model.addAttribute("ysList", ysList);
        model.addAttribute("isStandard", isStandard);
        return MessageFormat.format("popup/{0}", "fieldPopup");
    }

    @GetMapping("filedListsPopup.page")
    @ApiOperation("字段表格弹窗页面映射URL")
    public String filedListsPopup(String id, Integer isStandard, Model model) {
        MetaDataTables filed = metaDataTablesService.getById(id);

        model.addAttribute("tableId", filed.getId());
        model.addAttribute("tableName", filed.getTableName() + "(" + filed.getChineseTableName() + ")");
        model.addAttribute("isStandard", isStandard == null ? 0 : isStandard);
        return MessageFormat.format("popup/{0}", "filedListsPopup");
    }


    @GetMapping("permissionPopup.page")
    @ApiOperation("权限弹窗页面映射URL")
    public String permissionPopup(String id, Model model) {
        Map<Object, Object> dep = userService.getDepMaps();
        if (StringUtils.isNotEmpty(id)) {
            List<Object> list = permissionService.listObjs(new QueryWrapper<Permission>().lambda().select(Permission::getAuthorityId).eq(Permission::getUserName, id));
            model.addAttribute("permissionsList", list);
        }
        model.addAttribute("roleList", permissionService.roleList(isCloud ? 1 : 0));
        model.addAttribute("dep", dep);
        return MessageFormat.format("popup/{0}", "permissionPopup");
    }


    @GetMapping("apiAccreditPopup.page")
    @ApiOperation("API授权弹窗页面映射URL")
    public String apiAccreditPopup(String id, Model model) {
        List<SchooleCode> schooleCodeList = schooleCodeService.opiton();
        SchooleCode schooleCode = schooleCodeService.getById(id);
        model.addAttribute("schooleCodeList", schooleCodeList);
        model.addAttribute("schooleCode", schooleCode);
        return MessageFormat.format("popup/{0}", "apiAccreditPopup");
    }

    @GetMapping("apiTablePopup.page")
    @ApiOperation("软件系统弹窗页面映射URL")
    public String apiTablePopup(String id, String findType, Model model) {
        ApiTable apiTable = apiTableService.getById(id);
        List<Option> yqdm = optionService.list(new QueryWrapper<Option>()
                .inSql("Hierarchy_ID_", "SELECT ID_ FROM METADATA_B_HIERARCHY  WHERE  TypeKey_='YQXZ'  and IsDelete_ !=1")
                .orderByAsc("Sort_"));
        if (StringUtils.isEmpty(findType)) {
            findType = "false";
        }
        model.addAttribute("apiTable", apiTable);
        model.addAttribute("yqdm", yqdm);
        model.addAttribute("findType", findType);
        return MessageFormat.format("popup/{0}", "apiTablePopup");
    }

    @GetMapping("tysjglPopup.page")
    @ApiOperation("贴源数据管理弹窗页面映射URL")
    public String tysjglPopup(String id, String findType, Model model) {
        Tysjgl tysjgl = tysjglService.getById(id);

        if (StringUtils.isEmpty(findType)) {
            findType = "false";
        }
        model.addAttribute("tysjgl", tysjgl);
        model.addAttribute("findType", findType);
        return MessageFormat.format("popup/{0}", "tysjglPopup");
    }

    @GetMapping("revisionLogPopup.page")
    @ApiOperation("修订日志弹窗页面映射URL")
    public String revisionLogPopup(String id, Model model) {
        Revisionlog filed = revisionlogService.getById(id);
        List<Option> szList = optionService.list(new QueryWrapper<Option>().lambda().eq(Option::getHierarchyId, "20191230150329457633b5ae2c69545b399214dcc9759fbf4989332421791579"));
        List<Option> ysList = optionService.list(new QueryWrapper<Option>().lambda().eq(Option::getHierarchyId, "2019123015062770018f9464a594a44b791878053774962fd461947319397162"));
        model.addAttribute("filed", filed);
        model.addAttribute("szList", szList);
        model.addAttribute("ysList", ysList);
        return MessageFormat.format("popup/{0}", "revisionLogPopup");
    }

    @GetMapping("dwdxxbzdmysPopup.page")
    @ApiOperation("多键值页面列名设置弹窗页面映射URL")
    public String dwdxxbzdmysPopup(String id, Model model) {
        Hierarchy hierarchy = hierarchyService.getById(id);
        model.addAttribute("hierarchyId", hierarchy.getId());
        model.addAttribute("hierarchyKey", hierarchy.getTypeName());

        return MessageFormat.format("popup/{0}", "dwdxxbzdmysPopup");
    }

    @GetMapping("dwdxxbjzPopup.page")
    @ApiOperation("多键值页面列名设置弹窗页面映射URL")
    public String dwdxxbjzPopup(String id, Model model) {
        Hierarchy hierarchy = hierarchyService.getById(id);
        model.addAttribute("hierarchyId", hierarchy.getId());
        model.addAttribute("hierarchyKey", hierarchy.getTypeName());
        return MessageFormat.format("popup/{0}", "dwdxxbjzPopup");
    }
}
