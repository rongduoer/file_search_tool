package task;

import callback.FileScannerCallBack;
import lombok.Getter;

import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: rongduo
 * @description: 进行文件的扫描
 * @date: 2022-07-16
 */

@Getter
public class FileScanner {
    //保存文件的个数
    //最开始扫描的根路径没有统计到
    //多线程下的原子类
    private AtomicInteger fileNum = new AtomicInteger();
    //保存文件夹的个数
    private AtomicInteger dirNum = new AtomicInteger(1);
    //所有扫描文件的子线程个数，只有当子线程个数为0时，主线程在继续执行
    private AtomicInteger threadCount = new AtomicInteger();
    //当最后一个子线程执行任务之后，再调用countDown()方法唤醒主线程
    private CountDownLatch latch = new CountDownLatch(1);
    //获取当前电脑可用的CPU个数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    //使用线程池创建对象
    private ThreadPoolExecutor pool = new ThreadPoolExecutor(CPU_COUNT,CPU_COUNT * 2,10, TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(),new ThreadPoolExecutor.AbortPolicy());
    //文件扫描回调对象
    private FileScannerCallBack callBack;

    public FileScanner(FileScannerCallBack callBack){
        this.callBack = callBack;
    }

    /**
     * 根据传入的文件夹进行扫描任务
     * 选择要扫描的菜单之后，执行的第一个方法，根目录，主线程
     * 主线程需要等待所有子线程全部扫描完成之后在恢复执行
     * @param filePath 要扫描的根目录
     */
    public void scan(File filePath){
        System.out.println("开始文件扫描任务，根目录为 : " + filePath);
        long start = System.nanoTime();
        //将具体地扫描任务交给子线程处理
        scanInternal(filePath);
        //根目录的扫描，初始值为1
        threadCount.incrementAndGet();
        try {
            latch.await();
        }catch (InterruptedException e){
            System.err.println("扫描任务中中断,根目录为 ： " + filePath);
        }finally {
            System.out.println("关闭线程池");
            //正常关闭
            //中断任务，需要立即停止所有还在扫描的子线程
            pool.shutdownNow();
        }
        long end = System.nanoTime();
        System.out.println("文件扫描任务结束，共耗时 : " + (end - start) * 1.0 / 1000000 + "ms");
        System.out.println("文件扫描任务结束，根目录为 : " + filePath);
        System.out.println("共扫描到 : " + fileNum.get() + "个文件");
        System.out.println("共扫描到 : " +dirNum.get() + "个文件夹");
    }

    /**
     * 具体扫描任务的子线程，递归
     * @param filePath
     */
    private void scanInternal(File filePath) {
        //终止条件
        if (filePath == null){
            return;
        }
        //将当前扫描的任务交给线程处理
        pool.submit(()->{
            //scan是扫描的功能，回调函数是保存的功能
            //两者相加便实现了将指定目录下的所有文件和文件夹扫描出来后保存到数据库中
            //使用回调函数，将当前目录下的所有内容把保存到指定终端
            this.callBack.callback(filePath);
            //将当前这一级目录下的对象获取出来
            File[] files = filePath.listFiles();
            //遍历file对象，根据是否是文件夹进行区别处理
            for(File file : files){
                if (file.isDirectory()){
                    //++i
                    dirNum.incrementAndGet();
                    //子文件夹交给子线程来处理
                    threadCount.incrementAndGet();
                    scanInternal(file);
                } else{
                    fileNum.incrementAndGet();
                }
            }
            //当前线程将这一级的目录下的文件夹和文件的扫描任务结束
            System.out.println(Thread.currentThread().getName() + "扫描" + filePath + "任务结束");
            //子线程--
            threadCount.decrementAndGet();
            if (threadCount.get() == 0){
                System.out.println("所有扫描任务结束");
                //唤醒主线程
                latch.countDown();
            }
        });
    }
}
