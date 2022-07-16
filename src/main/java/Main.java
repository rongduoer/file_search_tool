import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author: rongduo
 * @description: 程序入口
 * @date: 2022-07-16
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("app.fxml"));
        //标题
        primaryStage.setTitle("search_everything");
        //初始化窗口大小
        primaryStage.setScene(new Scene(root, 1000, 800));
        //启动
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}