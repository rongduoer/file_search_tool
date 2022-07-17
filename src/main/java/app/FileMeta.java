package app;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import util.Util;

import java.util.Date;

/**
 * @author: rongduo
 * @description: 获取数据库记录
 * 对应数据库表的表名，数据表中的一行记录对应该类的一个对象
 * 数据表的所有内容就是这个类的对象数组
 * @date: 2022-07-16
 */

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class FileMeta {
    private String name;
    private String path;
    private Boolean isDirectory;
    private Long size;
    private Date lastModified;

    //属性名要与app.fxml内的名字一样
    //文件类型
    private String isDirectoryText;
    //文件大小
    private String sizeText;
    //文件修改时间
    private String lastModifiedText;


    public void setSize(Long size)  {
        this.size = size;
        this.sizeText = Util.parseSize(size);
    }
    public void setIsDirectory(Boolean directory){
        isDirectory = directory;
        this.isDirectoryText = Util.parsetFileType(directory);
    }
    public void setLastModified(Date lastModified){
        this.lastModified = lastModified;
        this.isDirectoryText = Util.parseData(lastModified);
    }

    public FileMeta(String name, String path, Boolean isDirectory, Long size, Date lastModified) {
        this.name = name;
        this.path = path;
        this.isDirectory = isDirectory;
        this.size = size;
        this.lastModified = lastModified;
    }

    //全拼
    private String pinYin;
    //首字母
    private String pinYinFirst;
}