
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class JavaFX_Main_Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private LineChart<Number, Number> lineChart;
    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis numberAxis;

    @FXML
    private TextField distance;
    @FXML
    private TextField eccentricity;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField ea;
    @FXML
    private ChoiceBox<String> choiceBox_MethodList;
    private ObservableList<String> availableChoices = FXCollections.observableArrayList("Bisection", "Falsi", "Fixed point iteration", "Newton-Raphson", "Transversal");
    @FXML
    private Button btn_Start;

    @FXML
    void onBtnStartPressed(ActionEvent event) {
        textField_NameOfSaveFile.appendText("Start");
        Double distance_value = Double.valueOf(distance.getText());
        Double eccentricity_value = Double.valueOf(eccentricity.getText());


        String selectedChoice = choiceBox_MethodList.getSelectionModel().getSelectedItem();
        Double ea_value = Double.valueOf(ea.getText());
        ArrayList<double[]> solve;

        switch (selectedChoice) {
            case "Bisection":
                bisectionSolverMethod(distance_value, ea_value, eccentricity_value);
                //BisectionSolver bisectionSolver = new BisectionSolver(function);
                //solve = bisectionSolver.solver(0, 3, ea_value, eccentricity_value);
                break;
            case "Falsi":
                //FalsiSolver falsiSolver =new FalsiSolver(function);
                //solve=falsiSolver.solver(0,3, ea_value, eccentricity_value);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectedChoice);
        }
        // textField_NameOfSaveFile.appendText(String.valueOf(solve.get(solve.size() - 1)[2]));
        //double x = Double.parseDouble(distance.getText()) * Math.cos(solve.get(solve.size() - 1)[2] - eccentricity_value);
        //double y = Double.parseDouble(distance.getText()) * Math.sqrt(1 - (eccentricity_value * eccentricity_value)) * Math.sin(solve.get(solve.size() - 1)[2]);
        // textField_NameOfSaveFile.appendText(String.valueOf(x));
        // textField_NameOfSaveFile.appendText(String.valueOf(y));
        // textArea.appendText("Distance A.U="+distance.getText()+" eccentricity="+eccentricity_value+" Method= "+selectedChoice+"\n E="+solve.get(solve.size() - 1)[2]+ "\n x="+x+"\n y="+y);
        // Plot plot=new Plot(Double.parseDouble(distance.getText()),x,y,eccentricity_value);
        // XYChart.Series series = plot.getValues();
        // lineChart.getData().addAll(series);


    }

    @FXML
    private TextField textField_NameOfSaveFile;

    @FXML
    private Button btn_Save;

    @FXML
    void onBtnSavePressed(ActionEvent event) {
        double x;
        double y;
        String fileName = "src/test/resources/" + textField_NameOfSaveFile.getText() + ".txt";
        try (FileWriter fileWriter = new FileWriter(fileName)) {
            for (int i = 1; i < 365; i++)
                fileWriter.write(numberAxis.getValueForDisplay(i) + "\t " + categoryAxis.getValueForDisplay(i)+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(numberAxis.getValueForDisplay(i));

    }


    @FXML
    void initialize() {
        assert lineChart != null : "fx:id=\"lineChart\" was not injected: check your FXML file 'JavaFX_Main.fxml'.";
        assert distance != null : "fx:id=\"distance\" was not injected: check your FXML file 'JavaFX_Main.fxml'.";
        assert eccentricity != null : "fx:id=\"eccentricity\" was not injected: check your FXML file 'JavaFX_Main.fxml'.";
        assert choiceBox_MethodList != null : "fx:id=\"choiceBox_MethodList\" was not injected: check your FXML file 'JavaFX_Main.fxml'.";
        assert btn_Start != null : "fx:id=\"btn_Start\" was not injected: check your FXML file 'JavaFX_Main.fxml'.";
        assert textField_NameOfSaveFile != null : "fx:id=\"textField_NameOfSaveFile\" was not injected: check your FXML file 'JavaFX_Main.fxml'.";
        assert btn_Save != null : "fx:id=\"btn_Save\" was not injected: check your FXML file 'JavaFX_Main.fxml'.";
        choiceBox_MethodList.setItems(availableChoices);


    }

    public void bisectionSolverMethod(double distance_value, double ea_value, double eccentricity_value) {
        XYChart.Series series = new XYChart.Series();
        BisectionSolver bisectionSolver;
        ArrayList<double[]> solve;
        double x;
        double y;
        double M = 1;
        Function function = new Function() {
            @Override
            public double function(double e, double E, double M) {
                return M + e * Math.sin(E) - E;
            }
        };
        bisectionSolver = new BisectionSolver(function);
        solve = bisectionSolver.solver(-0.5, 8, ea_value, eccentricity_value, M);
        double E0 = solve.get(solve.size() - 1)[2];
        double En;
        for (int i = 1; i <= 365; i++) {
            M = Math.PI * 2 / 365 * (double) i;
            En = function.function(eccentricity_value, E0, M);
            x = Double.parseDouble(distance.getText()) * Math.cos(En - eccentricity_value);
            y = Double.parseDouble(distance.getText()) * Math.sqrt(1 - (eccentricity_value * eccentricity_value)) * Math.sin(En);
            series.getData().add(new XYChart.Data(String.valueOf(x), y));
        }
        //x = Double.parseDouble(distance.getText()) * Math.cos(solve.get(solve.size() - 1)[2] - eccentricity_value);
        // y = Double.parseDouble(distance.getText()) * Math.sqrt(1 - (eccentricity_value * eccentricity_value)) * Math.sin(solve.get(solve.size() - 1)[2]);


        lineChart.getData().addAll(series);

    }


}
