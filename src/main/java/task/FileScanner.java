package task;

import callback.FileScannerCallBack;
import lombok.Getter;

import java.io.File;
/**
 * @author: rongduo
 * @description: 进行文件的扫描
 * @date: 2022-07-16
 */

@Getter
public class FileScanner {
    //保存文件的个数
    //最开始扫描的根路径没有统计到
    private int fileNum = 1;
    //保存文件夹的个数
    private int dirNum;
    //文件扫描回调对象
    private FileScannerCallBack callBack;

    public FileScanner(FileScannerCallBack callBack){
        this.callBack = callBack;
    }

    /**
     * 根据传入的文件夹进行扫描任务
     * @param filePath
     */
    public void scan(File filePath){
        if (filePath == null){
            return;
        }
        //scan是扫描的功能，回调函数是保存的功能
        //两者相加便实现了将指定目录下的所有文件和文件夹扫描出来后保存到数据库中
        //使用回调函数，将当前目录下的所有内容把保存到指定终端
        this.callBack.callBack(filePath);
        //将当前这一级目录下的对象获取出来
        File[] files = filePath.listFiles();
        //遍历file对象，根据是否是文件夹进行区别处理
        for(File file : files){
            if (file.isDirectory()){
                dirNum++;
                //继续递归扫描
                scan(file);
            } else{
                fileNum++;
            }
        }
    }
}
