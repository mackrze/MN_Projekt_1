import javafx.scene.chart.XYChart;

public class Plot {
    private double a;
    private double x;
    private double y;
    private double e;

    public Plot(double a, double x, double y, double e) {
        this.a = a;
        this.x = x;
        this.y = y;
        this.e = e;
    }
    public XYChart.Series getValues(){
        XYChart.Series values=new XYChart.Series();
        values.getData().add(new XYChart.Data(String.valueOf(x),y));
        for (int i=1;i<60;i++)
        values.getData().add(new XYChart.Data(String.valueOf(x+i),y+i));
        return values;
    }
}
