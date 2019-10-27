package com.typee.ui.report;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;

import com.typee.commons.core.LogsCenter;
import com.typee.model.engagement.Engagement;
import com.typee.ui.UiPart;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Controller class for Report Window.
 */
public class ReportWindow extends UiPart<Region> {
    public static final String FXML = "ReportWindow.fxml";
    private final Logger logger = LogsCenter.getLogger(getClass());
    private final Path filePath = Paths.get(System.getProperty("user.dir") + "/reports");

    @FXML
    private TreeView treeViewReports;

    @FXML
    private Label lblStatus;

    public ReportWindow(ObservableList<Engagement> engagementList) {
        super(FXML);

        logger.info(filePath.toString());
        treeViewReports.setRoot(getNodesForDirectory(filePath.toFile()));
        treeViewReports.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> handleMouseClicked(event));
    }

    /**
     * Handles mouse click event for treeview cell by openeing the file.
     */
    private void handleMouseClicked(MouseEvent event) {
        boolean isDoubleClick = false;
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                isDoubleClick = true;
            }
        }
        if (isDoubleClick) {
            Node node = event.getPickResult().getIntersectedNode();
            if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                String name = (String) ((TreeItem) treeViewReports
                        .getSelectionModel().getSelectedItem()).getValue();
                logger.info("Node click: " + name);;
                try {
                    Desktop.getDesktop().open(new File(name));
                } catch(IOException e) {
                    logger.severe(e.getMessage());
                }
            }
        }
    }

    /**
     * Recursively maps the given directory and only allows .pdf format files in the tree view.
     */
    private TreeItem<String> getNodesForDirectory(File directory) {
        TreeItem<String> root = new TreeItem<String>(directory.getName());
        for (File f : directory.listFiles()) {
            if (f.isDirectory()) {
                root.getChildren().add(getNodesForDirectory(f));
            } else if (FilenameUtils.getExtension(f.getName()).equals("pdf")) {
                TreeItem<String> item = new TreeItem<>(f.getName());
                item.setValue(f.getPath());
                root.getChildren().add(item);
            }
        }
        root.setExpanded(true);
        return root;
    }

    /**
     * Refreshes the tree view after click.
     * @param event Mouse Click Event.
     */
    @FXML
    private void refreshTreeView(MouseEvent event) {
        treeViewReports.setRoot(getNodesForDirectory(filePath.toFile()));
        lblStatus.setText("Refreshed Directory");
    }
}
