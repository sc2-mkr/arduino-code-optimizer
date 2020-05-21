package it.sc2.iregon.aco.gui;

import com.jfoenix.controls.JFXCheckBox;
import it.sc2.iregon.aco.config.MapperFactory;
import it.sc2.iregon.aco.config.chip.mappers.Mapper;
import it.sc2.iregon.aco.engine.AcoEngine;
import it.sc2.iregon.aco.engine.plugin.plugins.Plugin;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Controller {

    // Chooser
    private final FileChooser sourceSketchChooser = new FileChooser();
    private final DirectoryChooser destDirectoryChooser = new DirectoryChooser();

    // Optimization Engine
    MapperFactory mappingFactory;
    AcoEngine engine;

    // GUI components
    @FXML
    private Label lblSourceSketch;
    @FXML
    private Label lblDstPath;
    @FXML
    private CheckBox ckbSameDstAsSrc;
    @FXML
    private Button btnDstPath;
    @FXML
    private VBox vboxOptions;
    @FXML
    private VBox vboxCentralPanel;
    @FXML
    private ComboBox<String> comboChips;
    @FXML
    private Button btnOptimize;

    // Main window stage
    private Stage stage;

    // Source file
    private File sourceFile;

    // Destination path
    private File destinationPath;

    @FXML
    public void initialize() {
        // Set source sketch file chooser
        sourceSketchChooser.setTitle("Open source sketch");
        sourceSketchChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All files", "*.*"),
                new FileChooser.ExtensionFilter("INO", "*.ino")
        );

        // Set destination directory chooser
        destDirectoryChooser.setTitle("Select destination directory");

        // Init engine
        engine = new AcoEngine();

        // Add option from loaded plugins
        engine.getAllPlugins().forEach(plugin -> {
            JFXCheckBox checkBox = generateOptionCheckbox(
                    plugin.getPluginName(),
                    plugin.getImpactType(),
                    plugin.getViewOption().isEnableAsDefault());
            vboxOptions.getChildren().add(checkBox);
//            System.out.println("Added plugin: " + checkBox.getText());
        });

        // Add chips mapper to combobox
        mappingFactory = new MapperFactory();
        comboChips.setItems(FXCollections.observableArrayList(
                mappingFactory.getAllMapping().stream()
                        .map(Mapper::getMapName)
                        .collect(Collectors.toList())));
        if (comboChips.getItems().size() > 0) comboChips.getSelectionModel().select(0);
    }

    public JFXCheckBox generateOptionCheckbox(String value, Plugin.ImpactLevelType impactLevel, boolean selected) {
        JFXCheckBox checkBox = new JFXCheckBox(value);
        checkBox.checkedColorProperty().set(Paint.valueOf("#03a9f4"));
        checkBox.getStyleClass().add("jfx-checkbox-options-" + impactLevel.toString().toLowerCase());
        checkBox.setSelected(selected);
        checkBox.setPadding(new Insets(0, 0, 5, 0));

        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            engine.setOption(checkBox.getText(), newValue);
//            System.out.println("Plugin: " + checkBox.getText() + " state: " + newValue);
        });

        return checkBox;
    }

    public void btnSourceSketch_Click(MouseEvent mouseEvent) {
        sourceFile = sourceSketchChooser.showOpenDialog(stage);
        if (sourceFile != null) {
            lblSourceSketch.setText(sourceFile.getAbsolutePath());
            ckbSameDstAsSrc_Click(null);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void ckbSameDstAsSrc_Click(MouseEvent mouseEvent) {
        btnDstPath.setDisable(ckbSameDstAsSrc.isSelected());
        if (ckbSameDstAsSrc.isSelected()) {
            if (sourceFile != null) {
                destinationPath = new File(
                        sourceFile.getAbsolutePath().substring(
                                0,
                                sourceFile.getAbsolutePath().lastIndexOf("\\")
                        ));
            } else {
                destinationPath = new File(System.getProperty("user.home"));
            }
        } else {
            if (sourceFile == null) destinationPath = new File(System.getProperty("user.home"));
        }
        lblDstPath.setText(destinationPath.getAbsolutePath());

        if (sourceFile != null) {
            vboxCentralPanel.setDisable(false);
            btnOptimize.setDisable(false);
        }
    }

    public void btnDstPath_Click(MouseEvent mouseEvent) {
        destinationPath = destDirectoryChooser.showDialog(stage);
        if (destinationPath != null) {
            lblDstPath.setText(destinationPath.getAbsolutePath());
            if (sourceFile != null) btnOptimize.setDisable(false);
        }
    }

    public void btnOptimize_Click(MouseEvent mouseEvent) {
        try {
            engine.load(new FileInputStream(sourceFile));
        } catch (IOException e) {
            showErrorAndWait(
                    e.getMessage(),
                    Arrays.toString(Arrays.stream(e.getStackTrace()).limit(10).collect(Collectors.toList()).toArray(new StackTraceElement[10])));
        }
        engine.setChip(comboChips.getSelectionModel().getSelectedItem());

        vboxOptions.getChildren().forEach(node -> {
            JFXCheckBox checkbox = (JFXCheckBox) node;
            engine.setOption(checkbox.getText(), checkbox.isSelected());
        });

        engine.optimize();

        // TODO: move into new method
        String out = engine.getOutput();

        String dstPath = destinationPath.getAbsolutePath() +
                "\\" +
                FilenameUtils.removeExtension(sourceFile.getName()) +
                "_optimized.ino";
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(new File(dstPath), false));
        } catch (IOException e) {
            e.printStackTrace(); // TODO: manage "impossible to open file" exception
        }
        try {
            writer.write(out);
        } catch (IOException e) {
            e.printStackTrace(); // TODO: manage "impossible to write" exception
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace(); // TODO: manage "impossible to close file" exception
        }
    }

    private void showErrorAndWait(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
