package util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author: rongduo
 * @description: 在界面初始化创建数据表
 * @date: 2022-07-16
 */
public class DBinit {
    /**
     * 读取SQL文件,输入输出
     * @return
     */
    public static List<String> readSQL(){
        List<String> ret = new ArrayList<>();
        try {
            InputStream in = DBUtil.class.getClassLoader()
                    .getResourceAsStream("init.sql");
            Scanner scanner = new Scanner(in);
            //读取的数据以分号分隔
            scanner.useDelimiter(";");
            while (scanner.hasNext()) {
                String str = scanner.next();
                //\r\n换行符
                if ("".equals(str) || "\r\n".equals(str)) {
                    continue;
                }
                if (str.contains("--")) {
                    str = str.replaceAll("--", "");
                }
                ret.add(str);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return ret;
    }
}
