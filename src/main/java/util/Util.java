package util;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: rongduo
 * @description: 通用工具类
 * @date: 2022-07-16
 */
public class Util {
    //Sqlite日期格式
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 单位转换
     * @param size
     * @return
     */
    public static String parseSize(Long size) {
        String[] unit = {"B","KB","MB","GB"};
        int flag = 0;
        while (size > 1024) {
            size /= 1024;
            flag ++;
        }
        return size + unit[flag];
    }

    /**
     * 文件类型展示
     * @param directory
     * @return
     */
    public static String parseFileType(Boolean directory) {
        return directory ? "文件夹" : "文件";
    }

    /**
     * 修改时间按照时分秒展示
     * @param lastModified
     * @return
     */
    public static String parseDate(Date lastModified) {
        return new SimpleDateFormat(DATE_PATTERN).format(lastModified);
    }
}
