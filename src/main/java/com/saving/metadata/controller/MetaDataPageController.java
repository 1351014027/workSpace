package com.saving.metadata.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.exception.PermissionException;
import com.saving.metadata.pojo.*;
import com.saving.metadata.service.*;
import com.saving.metadata.utils.CaptchaUtil;
import com.saving.metadata.utils.IpUtils;
import com.saving.metadata.utils.SsoUtil;
import com.saving.metadata.vo.AicFieldsVo;
import com.saving.metadata.vo.IndexListVo;
import com.saving.metadata.vo.MetaDataTableListVo;
import com.saving.metadata.vo.WelcomeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

/**
 * @author: 陈志强
 * @date: 2019/12/12 10:11
 * @Description: 页面跳转相关
 */
@Controller
@Api(tags = "页面跳转相关接口")
@Slf4j
public class MetaDataPageController {

    private final UserService userService;
    private final ServletContext servletContext;
    private final HierarchyService hierarchyService;
    private final ChartDisplayService chartDisplayService;
    private final MetaDataTablesService metaDataTablesService;
    private final MetaDataFiledsService metaDataFiledsService;
    private final ApiTableService apiTableService;
    private final PermissionService permissionService;

    @Value("${application.ssoUrl}")
    private String ssoUrl;

    @Value("${application.isCloud}")
    private Boolean isCloud;

    @Value("${application.syncUrl}")
    private String syncUrl;

    @Autowired
    public MetaDataPageController(UserService userService,
                                  ServletContext servletContext,
                                  HierarchyService hierarchyService,
                                  ChartDisplayService chartDisplayService,
                                  MetaDataTablesService metaDataTablesService,
                                  MetaDataFiledsService metaDataFiledsService,
                                  ApiTableService apiTableService, PermissionService permissionService) {
        this.userService = userService;
        this.servletContext = servletContext;
        this.hierarchyService = hierarchyService;
        this.chartDisplayService = chartDisplayService;
        this.metaDataTablesService = metaDataTablesService;
        this.metaDataFiledsService = metaDataFiledsService;
        this.apiTableService = apiTableService;
        this.permissionService = permissionService;
    }

    @GetMapping
    @ApiOperation("默认跳转接口")
    public String defaultIndex() {
        return "login";
    }

    @GetMapping("index.page")
    @ApiOperation("主页跳转接口")
    public String index(Model model) {
        model.addAttribute("ssoUrl", ssoUrl);
        return "index";
    }

    @GetMapping("login.page")
    @ApiOperation("登录跳转接口")
    public String login() {
        return "login";
    }

    @GetMapping("welcome.page")
    @ApiOperation("欢迎页面接口")
    public String welcome(Model model) {
        WelcomeVo countList = metaDataTablesService.findCountList();
        List<ApiTable> list = apiTableService.list(new QueryWrapper<ApiTable>().lambda()
                .eq(ApiTable::getIssjmbxt, 1));
        for (ApiTable apiTable : list) {
            apiTable.setIssjyxt(null);
        }
        List<ApiTable> apiTables = apiTableService.list(new QueryWrapper<ApiTable>().lambda()
                .eq(ApiTable::getIssjyxt, 1));
        for (ApiTable apiTable : apiTables) {
            apiTable.setIssjmbxt(null);
            apiTable.setId(apiTable.getId() + "s");
        }
        apiTables.addAll(list);
        model.addAttribute("countList", countList);
        model.addAttribute("apiTables", apiTables);
        return "welcome/index";
    }

    @GetMapping("loginCheck.json")
    @ApiOperation("请求登录验证码接口")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "-1");
        CaptchaUtil util = CaptchaUtil.instance();
        String checkCode = util.getString();
        log.info(checkCode);
        request.getSession().setAttribute("checkCode", checkCode);
        ImageIO.write(util.getImage(), "jpg", response.getOutputStream());
    }

    @GetMapping(value = "logout.page")
    @ApiOperation(value = "注销登录跳转接口", notes = "重定向登录页面")
    public String logout() {
        User user = RequestHolder.getCurrenSysUser();
        HttpSession session = RequestHolder.getCurrenRequest().getSession();
        if (user != null) {
            session.removeAttribute(ResponseCode.CURRENT_USER);
            log.info("用户：{} 注销登录", RequestHolder.getCurrenSysUser().getName());
        }
        RequestHolder.remove();
        servletContext.removeAttribute("session");
        session.invalidate();
        return "redirect:login.page";
    }


    @PostMapping(value = "login.json")
    @ResponseBody
    @ApiOperation(value = "登录校验接口", notes = "重定向登录页面")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "checkCode", value = "验证码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "checkCode", value = "验证码", required = true, paramType = "query")
    })
    public ServerResponse login(String username, String password, String checkCode, String isChartDisplay) {
        HttpSession session = Objects.requireNonNull(RequestHolder.getCurrenRequest()).getSession();
        String errMsg;
        String checkCodeSession = (String) session.getAttribute("checkCode");
        if (StringUtils.isBlank(checkCodeSession)) {
            return ServerResponse.createByErrorMessage("验证码已失效，请点击图片重新获取!");
        }
        if (StringUtils.isBlank(username)) {
            return ServerResponse.createByErrorMessage("账户不可以为空!");
        } else if (StringUtils.isBlank(password)) {
            return ServerResponse.createByErrorMessage("密码不可以为空!");
        } else if (!checkCodeSession.equalsIgnoreCase(checkCode)) {
            return ServerResponse.createByErrorMessage("验证码不正确!");
        }
        User user = userService.getOne(
                new QueryWrapper<User>().lambda().and(obj ->
                        obj.eq(User::getUsername, username).eq(User::getPassword, password)));
        String userStatus = "使用";
        if (user == null) {
            errMsg = "用户名或密码错误!";
        } else if (!userStatus.equals(user.getStatus())) {
            errMsg = "账户已被停用，请联系管理员!";
        } else {
            List<String> roleLists = permissionService.roleLists(username);
            user.setPassword(StringUtils.EMPTY);
            RequestHolder.add(user);
            log.info("----> 用户:{}  ,卡号:{} ,登录Ip:{}  登录系统成功!", user.getName(), user.getUsername(), IpUtils.getIpAddr(RequestHolder.getCurrenRequest()));
            session.setAttribute(ResponseCode.CURRENT_USER, user);
            session.setAttribute("metadataRoles", roleLists);
            session.setAttribute("isCloud", isCloud);
            session.setMaxInactiveInterval(60 * 60);
            servletContext.setAttribute("session", session);
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByErrorMessage(errMsg);
    }


    @GetMapping(value = "loginSso.json")
    @ApiOperation(value = "单点登录校验接口", notes = "重定向登录页面")
    public void loginSso(String url, String userid, String ticket, String urlTitle, HttpServletResponse response, HttpServletRequest request) {
        try {
            String result = SsoUtil.singleSignOn(userid, ticket);
            if (result != null) {
                User user = userService.getOne(new QueryWrapper<User>().lambda()
                        .eq(StringUtils.isNotEmpty(userid), User::getUsername, userid).eq(User::getStatus, "使用"));
                user.setPassword(StringUtils.EMPTY);
                List<String> roleLists = permissionService.roleLists(user.getUsername());
                request.getSession().setMaxInactiveInterval(60 * 60);
                request.getSession().setAttribute(ResponseCode.CURRENT_USER, user);
                request.getSession().setAttribute("metadataRoles", roleLists);
                request.getSession().setAttribute("isCloud", isCloud);
                log.info("----> 用户:{}  ,卡号:{} ,登录Ip:{}  登录系统成功!", user.getName(), user.getUsername(), IpUtils.getIpAddr(RequestHolder.getCurrenRequest()));
                if (url != null && urlTitle != null) {
                    request.getSession().setAttribute("url", url);
                    request.getSession().setAttribute("urlTitle", urlTitle);
                }
                response.sendRedirect(request.getContextPath() + "/index.page");
            } else {
                response.sendRedirect(request.getContextPath() + "/login.page");
            }
        } catch (Exception e) {
            log.error("单点失败!", e);
            throw new PermissionException(ResponseCode.ERRORPPERSSION);
        }

    }


    @GetMapping("/metaData/{pageName}.page")
    @ApiOperation("页面通用映射URL")
    public String commonPage(@PathVariable String pageName) {
        return MessageFormat.format("metaData/{0}/index", pageName);
    }


    @GetMapping("/chartDisplay.page")
    @ApiOperation("图表页面映射URL")
    public String chartDisplay(Model model) {
        geiChartDisplayData(model);
        return MessageFormat.format("metaData/{0}/index", "chartDisplay");
    }

    @GetMapping("/chartDisplayOne.page")
    @ApiOperation("图表页面映射URL")
    public String chartDisplayOne(Model model) {
        geiChartDisplayData(model);
        return MessageFormat.format("metaData/{0}/index", "chartDisplayOne");
    }


    @GetMapping("/chartDisplayTwo.page")
    @ApiOperation("图表页面映射URL")
    public String chartDisplayTwo(Model model) {
        geiChartDisplayData(model);
        return MessageFormat.format("metaData/{0}/index", "chartDisplayTwo");
    }

    private void geiChartDisplayData(Model model) {
        List<Hierarchy> metadataType = hierarchyService.list(new QueryWrapper<Hierarchy>().lambda().eq(Hierarchy::getParent, 7)
                .eq(Hierarchy::getSchoolCode, RequestHolder.getCurrenSysUser().getSchoolCode()));
        List<MetaDataTableListVo> data = chartDisplayService.getMetaTableList().getData();
        model.addAttribute("metadataType", metadataType);
        model.addAttribute("metadataTable", data);
    }


    @GetMapping("/formTheQuery.page")
    @ApiOperation("任意表格查看页面映射URL")
    public String formTheQuery(Model model, String id, String isView) {
        MetaDataTables byId = MetaDataTables.builder().build();
        if ("1".equals(isView)) {
            byId.setTableName(id);
            byId.setChineseTableName(id);
            byId.setId(id);
        } else {
            isView = "";
            byId = metaDataTablesService.getById(id);
        }
        model.addAttribute("metadataTable", byId);
        model.addAttribute("isView", isView);
        return MessageFormat.format("metaData/{0}/index", "formTheQuery");
    }

    @GetMapping("/formTheFiled.page")
    @ApiOperation("任意表格字段明细查看页面映射URL")
    public String formTheFiled(Model model, String id) {
        MetaDataTables byId = metaDataTablesService.getById(id);
        model.addAttribute("metadataTable", byId);
        return MessageFormat.format("metaData/{0}/index", "formTheFiled");
    }

    @GetMapping("/metaData/AntV.page")
    @ApiOperation("任意表格表关系查看页面映射URL")
    public String AntV(Model model, String id) {
        MetaDataTables byId = metaDataTablesService.getById(id);
        model.addAttribute("metadataTable", byId);
        return MessageFormat.format("metaData/{0}/index", "AntV");
    }

    /**
     * @param model
     * @param id
     * @return
     * @author :严嘉炜
     * @date 2020/03/28 15:23
     */
    @GetMapping("/formTheApi.page")
    @ApiOperation("任意表格表关系查看api")
    public String formTheApi(Model model, String id, String method) {
        User user = RequestHolder.getCurrenSysUser();
        String domainName = userService.findDomainName();
        String interfaceName = "";
        switch (method) {
            case "save":
                interfaceName = "单表新增接口";
                break;
            case "update":
                interfaceName = "单表修改接口";
                break;
            case "del":
                interfaceName = "单表删除接口";
                break;
            case "list":
                interfaceName = "分页查询接口";
                break;
            case "geiApi":
                interfaceName = "获取临时密钥接口";
                break;
            default:
                break;
        }
        if (StringUtils.isNotEmpty(id)) {
            MetaDataTables byId = metaDataTablesService.getById(id);
            model.addAttribute("metadataTable", byId);
            Page<MetaDataFileds> metaDataFiledsPage = metaDataFiledsService.listPage(1, 100, MetaDataFileds.builder().tableId(id).build(), null, null, user);
            List<MetaDataFileds> records = metaDataFiledsPage.getRecords();
            model.addAttribute("metaDataFileds", records);
        }
        model.addAttribute("method", method);
        model.addAttribute("interfaceName", interfaceName);
        model.addAttribute("domainname", domainName);
        return MessageFormat.format("metaData/{0}/index", "formTheApi");
    }

    /**
     * @param model
     * @param
     * @return
     * @author:严嘉炜
     */
    @GetMapping("/formTheMdwApi.page")
    @ApiOperation("任意表格表关系查看状态平台api")
    public String formTheMdwApi(Model model, String cataNo, String tableName) {
        List<AicFieldsVo> ztptListByCataNo = metaDataFiledsService.getZtptListByCataNo(cataNo);
        String domainName = userService.findDomainName();
        model.addAttribute("AicDataFileds", ztptListByCataNo);
        model.addAttribute("tableName", tableName);
        model.addAttribute("domainname", domainName);
        return MessageFormat.format("metaData/{0}/index", "formTheMdwApi");
    }

    @GetMapping("/metaData/fileManagement.page")
    @ApiOperation("文件管理页面映射URL")
    public String fileManagement(Boolean isStandard, Model model) {
        String loaclVersion = (String) metaDataTablesService.syncMaxVersion().getData();
        String maxVersion = metaDataTablesService.getMaxVersion();
        isStandard = (isStandard != null && isStandard);
        model.addAttribute("loaclVersion", loaclVersion);
        model.addAttribute("maxVersion", maxVersion);
        model.addAttribute("isStandard", isStandard && isCloud);
//        model.addAttribute("isStandard", isStandard);
        model.addAttribute("syncUrl", syncUrl);
        return MessageFormat.format("metaData/{0}/index", "fileManagement");
    }

    @GetMapping("/metaData/updatePlan.page")
    @ApiOperation("更新页面映射URL")
    public String updatePlan(Model model) {
        String loaclVersion = (String) metaDataTablesService.syncMaxVersion().getData();
        String maxVersion = metaDataTablesService.getMaxVersion();
        model.addAttribute("loaclVersion", loaclVersion);
        model.addAttribute("maxVersion", maxVersion);
        return MessageFormat.format("metaData/{0}/index", "updatePlan");
    }

    @GetMapping("/metaData/dataRelated.page")
    @ApiOperation("血缘查看页面映射URL")
    public String dataRelated(Model model, String id) {
        MetaDataTables byId = metaDataTablesService.getById(id);
        model.addAttribute("metadataTable", byId);
        return MessageFormat.format("metaData/{0}/index", "dataRelated");
    }

    @GetMapping("/metaData/tableIndexList.page")
    @ApiOperation("索引全部表格查看列表")
    public String tableIndexList(Model model) {
        List<IndexListVo> indexListVo = metaDataTablesService.getIndexListVo();

        model.addAttribute("indexListVo", indexListVo);
        model.addAttribute("shcoolName", userService.findShcooleName());
        return MessageFormat.format("metaData/{0}/index", "tableIndexList");
    }

//
//    @GetMapping("/metaData/permission.page")
//    @ApiOperation("权限页面映射URL")
//    public String permission(Model model) {
//        model.addAttribute("roleList", permissionService.roleList(isCloud?0:1));
//        return MessageFormat.format("metaData/{0}/index", "permission");
//    }

    @GetMapping("/metaData/version.page")
    @ApiOperation("更新记录查看")
    public String showVersion(Model model) {
        model.addAttribute("resgxrz", userService.getShowVersion("校本数据中台"));
        return MessageFormat.format("metaData/{0}/index", "version");
    }

}
