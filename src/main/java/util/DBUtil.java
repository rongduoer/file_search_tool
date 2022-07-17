package util;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.sql.*;

/**
 * @author: rongduo
 * @description: SQLite数据库的工具类，创建数据源，创建数据库的连接
 * @date: 2022-07-16
 */
public class DBUtil {
    //获取数据源方法，设置为单例模式获取数据源对象
    private volatile static DataSource DATASOURCE;
    //提供方法，获取单例模式的唯一实例，数据源不能让外部使用，所以用private
    //单例数据连接
    private volatile static Connection CONNECTION;
    private static DataSource getDataSource(){
        if (DATASOURCE == null){
            //多线程下，仅有一个线程能进入同步代码块
            synchronized (DBUtil.class){
                //防止其他线程释放锁之后，再次进入同步代码块又创建了对象
                if (DATASOURCE == null){
                    //SQLite没有账户密码，只需要配置日期格式即可
                    SQLiteConfig config = new SQLiteConfig();
                    config.setDateStringFormat(Util.DATE_PATTERN);
                    DATASOURCE = new SQLiteDataSource();
                    //向下转型
                    ((SQLiteDataSource) DATASOURCE).setUrl(getUrl());
                }
            }
        }
        return DATASOURCE;
    }

    /**
     * 配置SQLite数据库的地址
     * @return
     */
    private static String getUrl(){
        //数据库路径
        String path = "D:\\个人\\项目\\search_everything\\target";
        String url = "jdbc:sqlite://" + path + File.separator + "search_evertyjing.db";
        System.out.println("获取数据库的链接为 : " + url);
        return url;
    }

    /**
     * 获取数据库链接
     * 多线程场景下，要求SQLite多个线程使用同一个链接进行处理
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        if (CONNECTION == null){
            synchronized (DBUtil.class){
                if (CONNECTION == null){
                    CONNECTION = getDataSource().getConnection();
                }
            }
        }
        return CONNECTION;
    }

    public static void close(Statement statement){
        if (statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void close(PreparedStatement ps, ResultSet rs) {
        close(ps);
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
