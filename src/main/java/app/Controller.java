package app;

import callback.impl.FileSave2DB;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import task.FileScanner;
import task.FileSearch;
import util.DBinit;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author: rongduo
 * @description: 后端控制类
 * @date: 2022-07-16
 */
public class Controller implements Initializable {

    @FXML
    private GridPane rootPane;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<FileMeta> fileTable;

    @FXML
    private Label srcDirectory;
    //扫描线程
    private Thread scanThread;

    //点击运行项目，界面初始化时加载的一个方法
    public void initialize(URL location, ResourceBundle resources) {
        //在界面初始化时初始化数据库
        DBinit.init();
        // 添加搜索框监听器，内容改变时执行监听事件
        searchField.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                freshTable();
            }
        });
    }
    //点击选择目录，获取到最终页面上选择的是哪个文件夹
    public void choose(Event event) {
        // 选择文件目录
        DirectoryChooser directoryChooser=new DirectoryChooser();
        Window window = rootPane.getScene().getWindow();
        File file = directoryChooser.showDialog(window);
        if(file == null)
            return;
        // 获取选择的目录路径，并显示
        String path = file.getPath();
        // TODO 在界面中显示路径的内容
        this.srcDirectory.setText(path);
        //获取扫描的文件夹之后，进行文件的扫描工作
        //进行文件扫描任务时，需要决定将信息保存到哪个终端
        FileScanner fileScanner = new FileScanner(new FileSave2DB());
        if (scanThread != null){
            //创建过任务，且改任务还没执行完成
            scanThread.interrupt();
        }
        scanThread = new Thread(() -> {
            fileScanner.scan(file);
            //刷新界面，展示刚才扫描到的文件信息
            freshTable();
        });
        scanThread.start();
    }

    // 刷新表格数据
    private void freshTable(){
        ObservableList<FileMeta> metas = fileTable.getItems();
        metas.clear();
        String dir = srcDirectory.getText();
        if (dir != null && dir.trim().length() != 0) {
            // 界面中已经选择了文件，此时已经将最新的数据保存到了数据库中，
            // 只需要取出数据库中的内容展示到界面上即可
            // 获取用户在搜索框中输入的内容
            String content = searchField.getText();
            // 根据选择的路径 + 用户的输入(若为空就展示所有内容) 将数据库中的指定内容刷新到界面中
            List<FileMeta> filesFromDB = FileSearch.search(dir,content);
            metas.addAll(filesFromDB);
        }
    }
}