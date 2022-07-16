package util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author: rongduo
 * @description: 在界面初始化创建数据表
 * 读取sql语句并执行
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
//                if (str.contains("--")) {
//                    str = str.replaceAll("--", "");
//                }
                ret.add(str);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return ret;
    }

    /**
     * 在界面初始化时先初始化数据库，创建数据表
     */
    public static void init(){
        Connection connection = null;
        Statement statement = null;
        try{
            //获取数据库的链接
            connection = DBUtil.getConnection();
            //获取要执行的SQL语句
            List<String> sqls = readSQL();
            //创建Statement对象，封装sql语句发送给数据库
            //采用普通statement
            statement = connection.createStatement();
            //循环执行SQL语句
            for(String sql: sqls){
                System.out.println("执行SQL操作" + sql);
                statement.executeUpdate(sql);
            }
        }catch (SQLException e){
            System.out.println("数据库初始化失败");
            e.printStackTrace();
        } finally {
            DBUtil.close(connection,statement);
        }
    }

    public static void main(String[] args) {
        init();
    }
}
