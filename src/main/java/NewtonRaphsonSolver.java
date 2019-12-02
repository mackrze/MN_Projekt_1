import java.util.ArrayList;

public class NewtonRaphsonSolver {
    private Function function;

    public NewtonRaphsonSolver(Function function) {
        this.function = function;
    }
    public ArrayList<double[]> solver(double xL, double xU, double ea, double e,double M,double xi) { //start solver
        ArrayList<double[]> solver = new ArrayList<>();
        double[] solve = new double[4];
        solve[0] = xL;
        solve[1] = xU;
        solve[2] = calculateXR(xi,e,M);
        solve[3]=100;
        solver.add(solve);
        while (solver.get(solver.size()-1)[3]>ea) {
            solve=calculateBoundaries(solve,e,M);
            solve[2]=calculateXR(solve[2],e,M);
            solve[3]=calculateaproximateError(solve[2],solver.get(solver.size()-1)[2]);
            solver.add(solve);
        }
        return solver;

    } //end solver
    private double calculateXR(double xi,double e,double M) {//start calculateXR
        double xR = 0;
        double dX=0.001;

        xR = xi-(function.function(e,xi,M))/((function.function(e,xi+dX,M)-function.function(e,xi,M))/dX);


        return xR;
    } //end calculateXR

    private double[] calculateBoundaries(double[] solve,double e,double M) { //start calculateBoundaries
        if (function.function(e,solve[0],M) * function.function(e,solve[2],M) < 0) {
            double countBoundaries[] = new double[solve.length];
            countBoundaries[0] = solve[0];
            countBoundaries[1] = solve[2];
            return countBoundaries;
        } else {
            double countBoundaries[] = new double[solve.length];
            countBoundaries[0] = solve[2];
            countBoundaries[1] = solve[1];
            return countBoundaries;
        }
    } //end calculateBoundaries
    private double calculateaproximateError(double presentApproximation,double previousApproximation){
        return Math.abs((presentApproximation-previousApproximation)/presentApproximation*100);
    }

}
