/*******************************************************************************
 * © 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package application;

//import com.mvp.java.spring.config.SpringFXMLLoader;

import java.io.IOException;
import java.util.Objects;

//import org.jfree.chart.labels.StandardPieSectionLabelGenerator;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

/**
 * Manages switching Scenes on the Primary Stage
 */
public class StageManager {

//    private static final Logger LOG = getLogger(StageManager.class);
    private final Stage primaryStage;
    private Stage secondaryStage = new Stage();
    private Stage tertiaryStage = new Stage();
//    private final SpringFXMLLoader springFXMLLoader;

    public StageManager(Stage stage) {
//        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
        this.primaryStage.initStyle(StageStyle.UNDECORATED);
        secondaryStage.initModality(Modality.WINDOW_MODAL);
        secondaryStage.initOwner(primaryStage);
        secondaryStage.initStyle(StageStyle.UNDECORATED);
        tertiaryStage.initOwner(secondaryStage);
        tertiaryStage.initStyle(StageStyle.UNDECORATED);
    	Image imageLogo = new Image(this.getClass().getResourceAsStream("/res/icon.png"));
        primaryStage.getIcons().add(imageLogo);
        primaryStage.setTitle("Anesthesia Information Management System");


    }

    public void switchScene(final FxmlView view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        show(viewRootNodeHierarchy, "");
    }

    public void switchSceneAfterMain(final FxmlView view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        showAfterMain(viewRootNodeHierarchy, "");
    }

    public void switchSceneAfterSecondary(final FxmlView view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        showAfterSecondary(viewRootNodeHierarchy, "");
    }

    private void show(final Parent rootnode, String title) {
        Scene scene = prepareScene(rootnode);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit ("Unable to show scene for title" + title,  exception);
        }
    }

    private void showAfterMain(final Parent rootnode, String title) {
    	secondaryStage.close();
        Scene scene = prepareSceneAfterMain(rootnode);
        scene.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
        secondaryStage.setTitle(title);
        secondaryStage.setScene(scene);
        secondaryStage.sizeToScene();
        secondaryStage.centerOnScreen();
        try {
        	secondaryStage.show();
        } catch (Exception exception) {
            logAndExit ("Unable to show scene for title" + title,  exception);
        }
    }

    private void showAfterSecondary(final Parent rootnode, String title) {
    	tertiaryStage.close();
        Scene scene = prepareSceneAfterSecondary(rootnode);
        scene.getStylesheets().add(this.getClass().getResource("/styles/newStyles.css").toString());
        tertiaryStage.setTitle(title);
        tertiaryStage.setScene(scene);
        tertiaryStage.sizeToScene();
        tertiaryStage.centerOnScreen();
        try {
        	tertiaryStage.show();
        } catch (Exception exception) {
            logAndExit ("Unable to show scene for title" + title,  exception);
        }
    }

    public void closeSecondaryStage()
    {
    	secondaryStage.setScene(null);
    	secondaryStage.close();
    }

    public void closeTertiaryStage()
    {
    	tertiaryStage.setScene(null);
    	tertiaryStage.close();
    }

    private Scene prepareScene(Parent rootnode){
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }

    private Scene prepareSceneAfterMain(Parent rootnode){
        Scene scene = secondaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }

    private Scene prepareSceneAfterSecondary(Parent rootnode){
        Scene scene = tertiaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }

    /**
     * Loads the object hierarchy from a FXML document and returns to root node
     * of that hierarchy.
     *
     * @return Parent root node of the FXML document hierarchy
     */
    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = load(fxmlFilePath);
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (Exception exception) {
            logAndExit("Unable to load FXML view" + fxmlFilePath, exception);
        }
        return rootNode;
    }

    public Parent load(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        //loader.setControllerFactory(context::getBean); //Spring now FXML Controller Factory
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader.load();
    }


    private void logAndExit(String errorMsg, Exception exception) {
//        LOG.error(errorMsg, exception, exception.getCause());
        Platform.exit();
    }

}
