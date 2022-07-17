package task;

import app.FileMeta;
import util.DBUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: rongduo
 * @description: 根据选择文件夹路径和用户输入的内容查询指定的内容并返回
 * @date: 2022-07-17
 */
public class FileSearch {
    /**
     * 搜索功能
     * @param dir 用户选择的检索文件夹路径
     * @param content 用户搜索框中的内容 - 可以为空，为空展示选择文件夹下的所有内容
     * @return
     */
    public static List<FileMeta> search(String dir, String content) {
        List<FileMeta> result = new ArrayList<>();
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DBUtil.getConnection();
            // 先根据用户选择的文件夹dir查询内容
            String sql = "select name,path,size,is_directory,last_modified from file_meta " +
                    " where (path = ? or path like ?)";
            if (content != null && content.trim().length() != 0) {
                // 此时用户搜索框中的内容不为空,此处支持文件全名称，拼音全名称，以及拼音首字母的模糊查询
                sql += " and (name like ? or pinyin like ? or pinyin_first like ?)";
            }
            ps = connection.prepareStatement(sql);
            ps.setString(1,dir);
            ps.setString(2,dir + File.separator + "%");
            // 根据搜索框的内容查询数据库，都是模糊匹配
            if (content != null && content.trim().length() != 0) {
                ps.setString(3,"%" + content + "%");
                ps.setString(4,"%" + content + "%");
                ps.setString(5,"%" + content + "%");
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                FileMeta meta = new FileMeta();
                meta.setName(rs.getString("name"));
                meta.setPath(rs.getString("path"));
                meta.setIsDirectory(rs.getBoolean("is_directory"));
                if (!meta.getIsDirectory()) {
                    // 是文件，保存大小
                    meta.setSize(rs.getLong("size"));
                }
                meta.setLastModified(new Date(rs.getTimestamp("last_modified").getTime()));
                result.add(meta);
            }
        }catch (SQLException e) {
            System.err.println("从数据库中搜索用户查找内容时出错，请检查SQL语句");
            e.printStackTrace();
        }finally {
            DBUtil.close(ps,rs);
        }
        return result;
    }
}
