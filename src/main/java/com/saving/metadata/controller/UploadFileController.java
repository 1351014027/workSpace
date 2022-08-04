package com.saving.metadata.controller;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.saving.metadata.common.RequestHolder;
import com.saving.metadata.common.ResponseCode;
import com.saving.metadata.common.ServerResponse;
import com.saving.metadata.exception.ParamException;
import com.saving.metadata.exception.PermissionException;
import com.saving.metadata.handler.CommentWriteHandler;
import com.saving.metadata.handler.CustomSheetWriteHandler;
import com.saving.metadata.listener.ImportTableDataListener;
import com.saving.metadata.pojo.*;
import com.saving.metadata.service.*;
import com.saving.metadata.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: 陈志强
 * @date: 2019/12/18 15:53
 * @Description: 文件上传控制器
 */
@Controller
@Slf4j
@Api(tags = "文件上传控制器相关接口")
@RequestMapping("/metadata/upload")
public class UploadFileController {
    private static final String ERRMSG = "文件输出流关闭错误";

    private final FileStoreService fileStoreService;
    private final String LABEL_TYPE = "labelType";
    private final String TABLE_TYPE = "tableType";

    @Resource
    private LabelService labelService;

    @Resource
    private OperationTableLogService operationTableLogService;

    @Value("${application.importFilePath}")
    private String importFilePath;
    @Value("${application.importPdfFilePath}")
    private String importPdfFilePath;
    @Value("${application.readFilePath}")
    private String readFilePath;
    @Value("${application.readPdfFilePath}")
    private String readPdfFilePath;

    @Resource
    private MetaDataFiledsService metaDataFiledsService;
    @Resource
    private OptionService optionService;
    @Resource
    private HierarchyService hierarchyService;

    @PostMapping("uploadFile.json")
    @ResponseBody
    @ApiOperation(value = "单文件上传", notes = "单文件上传")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "文件流", required = true, paramType = "query")
    })
    public ServerResponse<? extends Object> uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) {
        return uploadMultipleFile(new MultipartFile[]{file}, importFilePath);
    }

    @PostMapping("uploadFiles.json")
    @ResponseBody
    @ApiOperation(value = "多文件上传", notes = "多文件上传")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "files", value = "文件流", required = true, paramType = "query")
    })
    public ServerResponse<? extends Object> uploadMultipleFile(@RequestParam(value = "files", required = false) MultipartFile[] files) {
        return uploadMultipleFile(files, importFilePath);
    }


    private ServerResponse<? extends Object> uploadMultipleFile(@RequestParam(value = "files", required = false) MultipartFile[] files
            , String dirPath) {
        User user = RequestHolder.getCurrenSysUser();
        HttpSession session = RequestHolder.getCurrenRequest().getSession();
        if (user == null) {
            throw new PermissionException(ResponseCode.ERRORPPERSSION);
        }
        log.info("用户名:{} --> 用户:{} ,上传文件数为:{}", user.getUsername(), user.getName(), files.length);
        List<String> arr = Lists.newArrayList();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    dirPath = session.getServletContext().getRealPath(dirPath);
                    File dir = new File(dirPath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    String filePrefixFormat = "yyyyMMddHHmmssS", fileName = file.getOriginalFilename();
                    String savedName = DateUtil.format(new Date(), filePrefixFormat) + "_" + ThreadLocalRandom.current().ints(1, 10)
                            .distinct().limit(1).findFirst().orElse(1) + 1;
                    File serverFile = new File(dir.getAbsolutePath() + File.separator + savedName);
                    file.transferTo(serverFile);
                    FileStore storeBuilder = FileStore.builder().id(UUID64Utils.get64UUIDString())
                            .fileName(fileName)
                            .filePath(dirPath + File.separator + savedName)
                            .fileSize(String.valueOf(file.getSize()))
                            .saveFileName(savedName)
                            .fileType(fileName.substring(fileName.lastIndexOf(".") + 1)).build();
                    fileStoreService.save(storeBuilder);
                    log.info("Server File Location=" + serverFile.getAbsolutePath());
                } catch (Exception e) {
                    log.error(file.getOriginalFilename() + "上传发生异常，异常原因：" + e.getMessage(), e);
                    arr.add(file.getOriginalFilename());
                }
            } else {
                arr.add(file.getOriginalFilename());
            }
        }
        if (!arr.isEmpty()) {
            log.error("用户名:{} --> 用户:{} --->{}上传文件失败！", user.getUsername(), user.getName(), StringUtils.join(arr.toArray(), ","));
            return ServerResponse.createByErrorMessage("文件上传失败！");
        }
        return ServerResponse.createBySuccess();
    }


    private void delete(String id) {
        User user = RequestHolder.getCurrenSysUser();
        HttpServletRequest request = RequestHolder.getCurrenRequest();
        if (id == null || user == null) {
            throw new PermissionException(ResponseCode.ERRORPPERSSION);
        }
        FileStore fileStore = fileStoreService.getById(id);
        FileUtil.delFile(importFilePath + fileStore.getFilePath());
        File newFile = new File(importFilePath + fileStore.getFilePath().replace(fileStore.getSaveFileName(), fileStore.getFileName()));
        if (newFile.exists()) {
            FileUtil.delFile(newFile.getPath());
        }
        fileStoreService.removeById(id);
        log.info("用户{}删除文件,文件名为:{},删除IP为{}", user.getName(), fileStore.getFileName(), IpUtils.getIpAddr(request));
    }

    @PostMapping("deletes.json")
    @ResponseBody
    @ApiOperation(value = "批量删除", notes = "批量删除文件，逻辑删除记录，实际删除文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids", value = "文件主键数组", required = true, paramType = "query")
    })
    public ServerResponse delete(@RequestBody List<String> ids) {
        ids.forEach(this::delete);
        return ServerResponse.createBySuccess();
    }

    @GetMapping("download/{id}.json")
    @ResponseBody
    @ApiOperation(value = "下载接口", notes = "下载接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "文件主键", required = true, paramType = "query"),
            @ApiImplicitParam(name = "isNoDown", value = "是否下载", required = true, paramType = "query")
    })
    public void download(@PathVariable("id") String id, HttpServletResponse response, boolean isNoDown) {
        User user = RequestHolder.getCurrenSysUser();
        FileStore fileStore = fileStoreService.getById(id);
        String path = null, filename = null;
        if (fileStore != null) {
            filename = fileStore.getFileName();
            path = fileStore.getFilePath();
        }
        FileStore updatedObj = FileStore.builder().id(id)
                .downNumber(fileStore.getDownNumber() == null ? 0 : fileStore.getDownNumber() + 1)
                .lastDownCreator(user.getUsername()).lastDownTime(Date.from(LocalDateTime.now()
                        .atZone(ZoneId.systemDefault()).toInstant())).build();
        fileStoreService.updateById(updatedObj);
        downloadFile(path, user, filename, response, isNoDown, fileStore);

    }

    @GetMapping("download/{filename}")
    @ResponseBody
    @ApiOperation(value = "下载接口", notes = "系统temp里的文件下载接口")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "filename", value = "文件名称", required = true, paramType = "query"),
    })
    public ResponseEntity<byte[]> download(@PathVariable("filename") String filename, HttpServletResponse response) throws UnsupportedEncodingException {
        String path = "c:/temp/";
        byte[] body = null;
        File file = new File(path + filename);
        try (InputStream is = new FileInputStream(file)) {
            body = new byte[is.available()];
            is.read(body);
        } catch (Exception e) {
            log.error("下载文件错误!,文件名为：{}", filename, e);
        }
        FileUtil.deleteTempExcel(path);
        return setEntity(response, (int) file.length(), new String(file.getName().getBytes("GBK"), StandardCharsets.UTF_8), body);
    }

    @PostMapping("download/getZipFile.json")
    public void getZipFile(String ids, HttpServletResponse response) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(ids)) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        List<String> idsList = JsonMapperUtil.string2Object(ids, new TypeReference<List<String>>() {
        });
        HttpServletRequest request = RequestHolder.getCurrenRequest();
        String fileName = URLEncoder.encode(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分ss秒")) + "批量导出文件", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;fileName=\"" + fileName + ".zip\"");
        List<FileStore> list = fileStoreService.list(new QueryWrapper<FileStore>().lambda().in(FileStore::getId, idsList));
        try (OutputStream outputStream = response.getOutputStream();
             ZipArchiveOutputStream zeus = new ZipArchiveOutputStream(outputStream)) {
            zeus.setUseZip64(Zip64Mode.AsNeeded);
            zeus.setEncoding("utf-8");
            for (FileStore store : list) {
                FileInputStream fis = new FileInputStream(new File(store.getFilePath()));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int iLength;
                while ((iLength = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, iLength);
                }
                fis.close();
                byte[] bytes = out.toByteArray();
                ArchiveEntry entry = new ZipArchiveEntry(store.getFileName());
                zeus.putArchiveEntry(entry);
                zeus.write(bytes);
                zeus.closeArchiveEntry();
                out.close();
            }
        } catch (Exception e) {
            log.error("文件下载异常,异常:{}", e, e);
        }
    }

    private ResponseEntity<byte[]> setEntity(HttpServletResponse response, int fileLength, String fileName, byte[] body) {
        response.setContentType("application/x-msdownload");
        response.setContentLength(fileLength);
        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        HttpHeaders headers = new HttpHeaders();
        HttpStatus statusCode = HttpStatus.OK;

        return new ResponseEntity<>(body, headers, statusCode);
    }

    private void downloadFile(String path, User user, String fileName, HttpServletResponse response, boolean isNoDown, FileStore fileStore) {
        File file;
        file = new File(path);
        if (fileName.endsWith("pdf")) {
            response.setContentType("application/pdf");
        }
        try (InputStream is = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            if (file.exists() && file.isFile() && StringUtils.isNoneBlank(fileName)) {
                long fileLength = file.length();
                if (isNoDown) {
                    response.setContentType("multipart/form-data");
                    if (fileName.endsWith("png")) {
                        response.setContentType("image/png");
                    } else if (fileName.endsWith("jpeg") || fileName.endsWith("jpg")) {
                        response.setContentType("image/jpeg");
                    }
                } else {
                    response.setContentType("application/octet-stream");
                    response.addHeader("Content-Disposition", "attachment; filename=\"" + java.net.URLEncoder.encode(fileName, "UTF-8") + "\"");
                }
                response.setContentLength((int) fileLength);
                byte[] b = new byte[4096];
                int len;
                if (isNoDown) {
                    log.warn("用户{}查看文件,文件名为:{}", user.getName(), fileName);
                } else {
                    log.warn("用户{}下载文件,文件名为:{}", user.getName(), fileName);
                }
                while ((len = is.read(b)) > 0) {
                    os.write(b, 0, len);
                }
            } else {
                response.getWriter().println("<script>");
                response.getWriter().println(" alert('文件不存在!');");
                response.getWriter().println("history.go(-1);");
                response.getWriter().println("</script>");
            }
        } catch (Exception e) {
            log.error("文件下载文件错误：", e);
        }
    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @PostMapping("export.json")
    @ApiOperation(value = "导出Excel接口", notes = "ID必须传")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids", value = "对象主键,数组格式，jSON字符串", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pojoName", value = "对象类名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pojoChineseName", value = "对象中文名", required = true, paramType = "query"),
    })
    public void export(String ids, String pojoName, String pojoChineseName, HttpServletResponse response) throws IOException {
        List<String> idsList = JsonMapperUtil.string2Object(ids, new TypeReference<List<String>>() {
        });
        Object obj;
        if (CollectionUtils.isEmpty(idsList)) {
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        try {
            Class<? extends Object> clz = Class.forName("com.saving.metadata.pojo." + pojoName);
            obj = clz.newInstance();
        } catch (Exception e) {
            log.error("pojoName非法!", e);
            throw new ParamException(ResponseCode.ERRORPARAM);
        }
        IService<Object> service = SpringUtils.getBean(Character.toLowerCase(pojoName.charAt(0)) + pojoName.substring(1) + "ServiceImpl");
        List<Object> storeList = service.list(new QueryWrapper<>().in("ID_", idsList));
        if (CollectionUtils.isNotEmpty(storeList)) {
            try {
                response.setContentType("application/vnd.ms-excel");
                response.setCharacterEncoding("utf-8");
                String fileName = URLEncoder.encode(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分ss秒")) + pojoChineseName + "记录", "UTF-8");
                response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
                EasyExcel.write(response.getOutputStream(), obj.getClass()).autoCloseStream(Boolean.FALSE).sheet("模板")
                        .doWrite(storeList);
            } catch (Exception e) {
                getErrorExcelMsg(response, e.getMessage());
            }
        } else {
            getErrorExcelMsg(response, StringUtils.EMPTY);
        }
    }

    @Resource
    private ChartDisplayService chartDisplayService;
    @Resource
    private MetaDataTablesService metaDataTablesService;

    @Autowired
    public UploadFileController(FileStoreService fileStoreService) {

        this.fileStoreService = fileStoreService;
    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @PostMapping("exportMap.json")
    @ApiOperation(value = "导出Excel接口", notes = "ID必须传")
    public void exportMap(String exportType
            , String id
            , String str
            , @RequestParam(defaultValue = "1") Integer pageNum
            , @RequestParam(defaultValue = "100") Integer pageSize, HttpServletResponse response) throws IOException {
        try {
            List<List<String>> head = Lists.newArrayList();
            List<List<Object>> dataList = Lists.newArrayList();
            String sheetName = "";
            Page<Map<String, Object>> page = new Page<>();
            if (LABEL_TYPE.equalsIgnoreCase(exportType) && StringUtils.isNotEmpty(id)) {
                Label label = labelService.getById(id);
                sheetName = label.getLabelZnName() + "(" + label.getLabelName() + ")";
                page = labelService.showLabelData(pageNum, pageSize, id, StringUtils.isEmpty(str) ? null : str);

            } else if (TABLE_TYPE.equalsIgnoreCase(exportType) && StringUtils.isNotEmpty(id)) {
                MetaDataTables tables = metaDataTablesService.getById(id);
                sheetName = tables.getChineseTableName() + "(" + tables.getTableName() + ")";
                page = chartDisplayService.tableLists(id, pageNum, pageSize, StringUtils.isEmpty(str) ? null : str, null).getData();
            } else {
                getErrorExcelMsg(response, "检查exportType和id参数是否正确!");
            }
            List<Map<String, Object>> records = page.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                records.get(0).forEach((key, value) -> {
                    if (!"__row_number__".equals(key)) {
                        head.add(Lists.newArrayList(String.valueOf(key)));
                    }
                });
                records.forEach(obj -> {
                    List<Object> arrayList = Lists.newArrayList();
                    head.forEach(twoObj -> {
                        if (!"__row_number__".equals(twoObj.get(0))) {
                            arrayList.add(StringUtils.isNotEmpty(String.valueOf(obj.get(twoObj.get(0)))) ? String.valueOf(obj.get(twoObj.get(0))) : "");
                        }
                    });
                    dataList.add(arrayList);
                });
            }
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日HH时mm分ss秒")) + sheetName + "表格导出记录", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream()).inMemory(Boolean.TRUE).registerWriteHandler(new CommentWriteHandler(id, metaDataFiledsService))
                    .registerWriteHandler(new CustomSheetWriteHandler(id, metaDataFiledsService, hierarchyService, optionService)).head(head).sheet(sheetName).doWrite(dataList);
        } catch (Exception e) {
            getErrorExcelMsg(response, e.getMessage());
        }
    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @PostMapping("exportTemplateMap.json")
    @ApiOperation(value = "导出Excel接口", notes = "ID必须传")
    public void exportTemplateMap(String id, HttpServletResponse response) throws IOException {
        try {
            List<List<String>> head = Lists.newArrayList();
            String sheetName = "";
            List<MetaDataFileds> list = Lists.newArrayList();
            List<List<String>> dataList = Lists.newArrayList();
            if (StringUtils.isNotEmpty(id)) {
                list = metaDataFiledsService.list(new QueryWrapper<MetaDataFileds>().eq("TableID_", id)
                        .orderByAsc("cast(Sort_ as int)")
                        .orderByAsc("SortId_")
                        .orderByAsc("CreateTime_"));
            } else {
                getErrorExcelMsg(response, "检查id参数是否正确!");
            }
            if (CollectionUtils.isNotEmpty(list)) {
                sheetName = list.get(0).getTableName();
                list.forEach(obj -> {
                    head.add(Lists.newArrayList(obj.getChineseFiledName()));
                    dataList.add(Lists.newArrayList(""));
                });
            }
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode(sheetName + "表格导出模版", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream()).inMemory(Boolean.TRUE).registerWriteHandler(new CommentWriteHandler(id, metaDataFiledsService))
                    .registerWriteHandler(new CustomSheetWriteHandler(id, metaDataFiledsService, hierarchyService, optionService)).head(head).sheet(sheetName).doWrite(dataList);
        } catch (Exception e) {
            getErrorExcelMsg(response, e.getMessage());
            log.error(e.getMessage(), e);
        }
    }

    @PostMapping("import.json")
    @ResponseBody
    @ApiOperation(value = "批量导入", notes = "批量导入")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "文件流", required = true, paramType = "query"),
    })
    public ServerResponse<? extends Object> importExcel(@RequestParam(value = "file", required = false) MultipartFile file, String tableId) throws IOException {
        EasyExcel.read(file.getInputStream(), new ImportTableDataListener(tableId, metaDataFiledsService, metaDataTablesService, optionService, hierarchyService)).sheet().doRead();
        return ServerResponse.createBySuccess();
    }


    private void getErrorExcelMsg(HttpServletResponse response, String errorMsg) throws IOException {
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Map<String, String> map = new HashMap<>(2);
        map.put("status", "failure");
        map.put("message", "下载文件失败" + errorMsg);
        response.getWriter().println(JsonMapperUtil.object2String(map));
    }


}
