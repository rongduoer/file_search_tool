package callback;

import java.io.File;

/**
 * 文件信息扫描的回调接口
 */
public interface FileScannerCallBack {
    /**
     * 文件扫描的回调接口，扫描文件时由具体的子类决定将当前文件目录下的信息持久化到哪个终端，可以是数据库或者网络等
     * @param dir
     */
    void callback(File dir);
}
