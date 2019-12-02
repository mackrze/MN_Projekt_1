
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class JavaFX_Main_Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ScatterChart<Number, Number> lineChart;
    @FXML
    private NumberAxis categoryAxis;

    @FXML
    private NumberAxis numberAxis;

    @FXML
    private TextField distance;
    @FXML
    private TextField eccentricity;
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
        double E0 = 0;

        switch (selectedChoice) {
            case "Bisection":
                E0 = bisectionSolverMethod(ea_value, eccentricity_value);
                break;
            case "Falsi":
                E0 = falsiSolverMethod(ea_value, eccentricity_value);
                break;
            case "Fixed point iteration":
                E0 = fixedPointIterationSolverMethod(ea_value, eccentricity_value);
                break;
            case "Newton-Raphson":
                E0=newtonRaphsonSolverMethod(ea_value,eccentricity_value);
                break;
            case "Transversal":
                E0=transversalSolverMethod(ea_value,eccentricity_value);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + selectedChoice);
        }
        createPlot(E0, eccentricity_value);


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
            for (int i = 1; i <= 360; i++)
                fileWriter.write(numberAxis.getValueForDisplay(i) + "\t " + categoryAxis.getValueForDisplay(i) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void createPlot(double E0, double eccentricity_value) {
        double M;
        double x;
        double y;
        XYChart.Series series = new XYChart.Series();
        Function function = new Function() {
            @Override
            public double function(double e, double E, double M) {
                return M + e * Math.sin(E) - E;
            }
        };
        double En;
        for (int i = 1; i <= 360; i++) {
            M = Math.PI * 2 / 360 * (double) i;
            En = function.function(eccentricity_value, E0, M);
            x = Double.parseDouble(distance.getText()) * Math.cos(En - eccentricity_value);
            y = Double.parseDouble(distance.getText()) * Math.sqrt(1 - (eccentricity_value * eccentricity_value)) * Math.sin(En);
            series.getData().add(new XYChart.Data(x, y));
        }
        lineChart.getData().addAll(series);
    }

    public double bisectionSolverMethod(double ea_value, double eccentricity_value) {
        ArrayList<double[]> solve;
        double M = 1;
        Function function = new Function() {
            @Override
            public double function(double e, double E, double M) {
                return M + e * Math.sin(E) - E;
            }
        };
        BisectionSolver bisectionSolver = new BisectionSolver(function);
        solve = bisectionSolver.solver(-0.5, 8, ea_value, eccentricity_value, M);
        double E0 = solve.get(solve.size() - 1)[2];
        return E0;
    }
    public double newtonRaphsonSolverMethod(double ea_value, double eccentricity_value) {
        ArrayList<double[]> solve;
        double M = 1;
        Function function = new Function() {
            @Override
            public double function(double e, double E, double M) {
                return M + e * Math.sin(E) - E;
            }
        };
        NewtonRaphsonSolver newtonRaphsonSolver=new NewtonRaphsonSolver(function);
        solve = newtonRaphsonSolver.solver(-0.5, 8, ea_value, eccentricity_value, M,3);
        double E0 = solve.get(solve.size() - 1)[2];
        return E0;
    }

    public double fixedPointIterationSolverMethod(double ea_value, double eccentricity_value) {
        ArrayList<double[]> solve;
        double M = 1;
        Function function = new Function() {
            @Override
            public double function(double e, double E, double M) {
                return M + e * Math.sin(E) - E;
            }
        };
        FixedPointIterationSolver fixedPointIterationSolver = new FixedPointIterationSolver(function);
        solve = fixedPointIterationSolver.solver(-0.5, 8, ea_value, eccentricity_value, M, 3);
        double E0 = solve.get(solve.size() - 1)[2];
        return E0;
    }

    public double falsiSolverMethod(double ea_value, double eccentricity_value) {
        ArrayList<double[]> solve;
        double M = 1;
        Function function = new Function() {
            @Override
            public double function(double e, double E, double M) {
                return M + e * Math.sin(E) - E;
            }
        };
        FalsiSolver falsiSolver = new FalsiSolver(function);
        solve = falsiSolver.solver(-0.5, 8, ea_value, eccentricity_value, M);
        double E0 = solve.get(solve.size() - 1)[2];
        return E0;
    }
    public double transversalSolverMethod(double ea_value, double eccentricity_value) {
        ArrayList<double[]> solve;
        double M = 1;
        Function function = new Function() {
            @Override
            public double function(double e, double E, double M) {
                return M + e * Math.sin(E) - E;
            }
        };
        TransversalSolver transversalSolver = new TransversalSolver(function);
        solve = transversalSolver.solver(-0.5, 8, ea_value,eccentricity_value,M,3,4);
        double E0 = solve.get(solve.size() - 1)[2];
        return E0;
    }


}
