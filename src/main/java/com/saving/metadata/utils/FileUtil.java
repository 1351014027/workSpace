package com.saving.metadata.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.shaded.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * @author Saving
 */
@Slf4j
public class FileUtil {

    private static int count = 0;

    private FileUtil() {

    }

    public static void deleteTempExcel(String filename) {
        File file = new File(filename);
        File temp;
        File[] fileList = file.listFiles();
        if (fileList != null && fileList.length > 0) {
            for (File value : fileList) {
                temp = value;
                if (temp.getName().endsWith("xls")) {
                    temp.delete();
                }
                if (temp.getName().endsWith("xlsx")) {
                    temp.delete();
                }
            }
        }

    }

    /**
     * 复制单个文件
     *
     * @param oldPath 源文件路径
     * @param newPath 复制后路径
     * @return 文件大小
     */
    public static int copyFile(String oldPath, String newPath) {
        int bytesum = 0;
        int byteread;
        File oldfile = new File(oldPath);
        if (oldfile.exists()) {
            try (InputStream inStream = new FileInputStream(oldPath);
                 FileOutputStream fs = new FileOutputStream(newPath)) {
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
            } catch (Exception e) {
                log.error("复制单个文件操作出错", e);
                return 0;
            }
        }
        return bytesum;

    }

    /**
     * 复制文件流到新的文件
     *
     * @param inStream 文件流
     * @param file     新文件
     * @return 是否复制成功
     */
    public static boolean copyInputStreamToFile(final InputStream inStream, File file) {
        int byteread;
        byte[] buffer = new byte[1024];
        try (FileOutputStream fs = new FileOutputStream(file)) {
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
        } catch (IOException e) {
            log.error("copyInputStreamToFile方法文件操作出错", e);
        }
        return true;
    }

    /**
     * 删除指定路径下的文件
     *
     * @param filePathAndName 文件路径
     */
    public static void delFile(String filePathAndName) {
        try {
            Files.delete(Paths.get(filePathAndName));
        } catch (Exception e) {
            log.error("删除文件操作出错", e);
        }

    }

    /**
     * 判断文件是否是图像文件
     */
    public static boolean isImage(String name) {
        boolean valid = true;
        try {
            Image image = ImageIO.read(new File(name));
            if (image == null) {
                valid = false;
            }
        } catch (IOException ex) {
            valid = false;
        }
        return valid;
    }


    /**
     * @return void
     * @Author Mr.Saving
     * @Description 获取文件数量
     * @Date 2019/4/9 9:46
     * @Param [filepath]
     **/
    public static int getFileNumber(String filepath) {
        File file = new File(filepath);
        File[] listfile = file.listFiles();
        for (int i = 0; i < listfile.length; i++) {
            if (!listfile[i].isDirectory()) {
                count++;
            } else {
                getFileNumber(listfile[i].toString());
            }
        }
        return count;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }


    public static String getEntryName(String base, File file) {
        File baseFile = new File(base);
        String filename = file.getPath();
        if (baseFile.getParentFile().getParentFile() == null) {
            return filename.substring(baseFile.getParent().length());
        }
        return filename.substring(baseFile.getParent().length() + 1);
    }

}
