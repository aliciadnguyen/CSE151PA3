/**
 * Created by Alicia on 5/4/2016.
 */

import org.ejml.simple.SimpleMatrix;
import sun.java2d.pipe.SpanShapeRenderer;

import java.util.Arrays;

public class houseHolder {

    public SimpleMatrix houseHoldersOnX(SimpleMatrix x) {
        //R is used to store X, Q1X, Q2Q1X, ...
        SimpleMatrix rDiag = new SimpleMatrix(x);
        SimpleMatrix vminus;

        //For loop for i = 1 ~ d iterations, calculated Qi
        for(int i = 0; i < x.numCols(); i++) {

            // 1. Obtain target column (zi) -- first column of submatrix
            double[][] zi = new double[rDiag.numRows()-i][1];
            System.out.print("ZI is : ");
            for(int j = i; j < rDiag.numRows(); j++) {
                zi[j-i][0] = rDiag.get(j, i);
                System.out.print(zi[j-i][0] + " ");
            }

            System.out.println();

            // Find ||zi||2
            SimpleMatrix Zi = new SimpleMatrix(zi);
            double magZi = Math.sqrt(Zi.dot(Zi));
            System.out.println("Znorm is " + magZi);

            // Find ei1 = {{1}, {0}, ... }
            double[][] e1 = new double[Zi.numRows()][1];
            e1[0][0] = 1;
            SimpleMatrix E1 = new SimpleMatrix(e1);

            // 2. Find vi as vi = -||zi||2ei1 - zi if first element of zi is +
            //            or vi = ||zi||2ei1 - zi otherwise
            System.out.println("First element of Z is " + zi[0][0]);
            if(zi[0][0] < 0) {
                vminus = Zi.minus(E1.scale(magZi));
                System.out.println("It's negative!");
            } else {
                vminus = Zi.negative().minus(E1.scale(magZi));
                System.out.println("It's positive!");
            }

            System.out.println("V is " + vminus);

            // 3. Find Householder matrix Pi as follows: Pi = I - 2vivi^t/vi^tvi
            SimpleMatrix vivit = vminus.mult(vminus.transpose());
            double mag2 = vminus.dot(vminus);
            SimpleMatrix twoVi = vivit.scale(2.0/mag2);
            SimpleMatrix Pi = SimpleMatrix.identity(rDiag.numRows() - i).minus(twoVi);
            System.out.println("P array is " + Pi);

            // 4. Let Qi be a n * n matrix
            int n = rDiag.numRows();
            SimpleMatrix Qi = new SimpleMatrix(n, n);

            SimpleMatrix QiIdentity = SimpleMatrix.identity(Qi.numRows());

            SimpleMatrix QiPi = QiIdentity.combine(n - Pi.numRows(), n - Pi.numRows(), Pi);


            // 5. Update R as R <-- QiR
            System.out.println("Q array is " + QiPi);
            rDiag = QiPi.mult(rDiag);
            System.out.println("R array is " + rDiag);

            SimpleMatrix Qacc = QiPi.transpose();
            System.out.println("Q accumlated is " + Qacc);
        }

        return rDiag;
    }
}
