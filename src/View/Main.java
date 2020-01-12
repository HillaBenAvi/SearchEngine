package View;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.*;

public class Main extends Application{

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        launch(args);

/*
        int docsNum = 0;
        long totalTime = 0;
        System.out.println("start");
        long startTime 	= System.currentTimeMillis();

        Model.ProcessManager pm = new Model.ProcessManager("C:\\Users\\Hilla Ben-Avi\\Desktop\\IRCORPUS","C:\\Users\\Hilla Ben-Avi\\Desktop\\IRINDEX",false);
       // pm.loadDictionaryFromFile();

        pm.manage();

        long finishTime = System.currentTimeMillis();
        totalTime = finishTime - startTime;

        System.out.println("total time:" + totalTime);

*/
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Search Engine");
        primaryStage.setWidth(650);
        primaryStage.setHeight(650);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("SearchEnginePartB.fxml").openStream());
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        SetStageCloseEvent(primaryStage);
        primaryStage.show();
    }

    private void SetStageCloseEvent(Stage primaryStage) {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    primaryStage.close();
                } else {
                    windowEvent.consume();
                }
            }
        });
    }

}
