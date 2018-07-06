package domain.logic;

import domain.game.Problem;
import domain.game.SquareProblem;
import domain.game.TriangleProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Solver is the algorithm which manages to solve Hidato games using the recursive technique.
 * The class the following global variables:
 * 1 int matrix called matrixProblem
 * 1 int arrayList called numDonats
 * 2 Strings called type and ang that are used to know the type of the game and the adjacencies.
 * 1 Tuple called inici that give us the initial position to start solving the Hidato game
 * @author Sergi Serrano Casalins
 * @author Samuel Hoenle
 */

public class Solver
{
    /**
     * Int matrix bigger than the original string matrix of the problem used to solve the game
     */
    private int[][] matrixProblem;

    /**
     * Int arrayList with the numbers that the problem has at the beggining of the game.
     * It will contain other numbers if the cells are filled with "#"(-1), "*"(-2) or if it is a number(0)
     */
    private int[] numDonats;

    /**
     * Initial tuple with the coordinates x and y of the number 1 in the game
     */
    public Tuple inici;

    /**
     * Strings used to know the adjencies and the type of the problem we want to solve
     */
    private String ang, type;

    /**
     * Private inner class used to know the coordinates of every position needed to solve the game
     */
    private class Tuple {
        /**
         * X coordinate
         */
        int x;

        /**
         * Y coordinate
         */
        int y;

        /**
         * Creator function for the inner class Tuple
         * @param a X coordinate
         * @param b Y coordinate
         */
        public Tuple(int a, int b) {
            this.x = a;
            this.y = b;
        }

        /**
         * Getter function for X coordinate
         * @return The X coordinate
         */
        public int getX() {
            return x;
        }

        /**
         * Getter function for Y coordinate
         * @return The Y cordinate
         */
        public int getY() {
            return y;
        }
    }

    /**
     * Global variable initializer function.
     * Thefunction will analyze the problem fields and treat them according to their type.
     * Moreover,it will sort the numbers given in the initial problem.
     * @param problem Problem given by the controller
     */
    public String[][] solve(Problem problem) {
        int row, col;
        String[][] givenMatrix;
        if (problem instanceof SquareProblem){
            row = problem.getNumRows();
            col = problem.getNumColumns();
            givenMatrix = problem.getMap();
            if (((SquareProblem) problem).isCornerAdjacent()) ang = "CA";
            else ang ="C";
            matrixProblem = new int[row+2][col+2];
            type = "S";
        }
        else if (problem instanceof TriangleProblem) {
            row = problem.getNumRows();
            col = problem.getNumColumns();
            givenMatrix = problem.getMap();
            if (((TriangleProblem) problem).isCornerAdjacent()) ang = "CA";
            else ang ="C";
            matrixProblem = new int[row+4][col+4];
            type = "T";
        }
        else {
            row = problem.getNumRows();
            col = problem.getNumColumns();
            givenMatrix = problem.getMap();
            matrixProblem = new int[row+4][col+4];
            type = "H";
        }

        List<Integer> llista = new ArrayList<>(givenMatrix.length*givenMatrix[0].length);

        for (int i = 0; i < matrixProblem.length; i++)
            for (int j = 0; j < matrixProblem[0].length; j++) matrixProblem[i][j] = -1;

        for (int r = 0; r < givenMatrix.length; r++) {
            String[] fila = givenMatrix[r];
            for (int c = 0; c < givenMatrix[0].length; c++) {
                String cell = fila[c];
                switch (cell) {
                    case "?":
                        if (type.equals("S"))
                            matrixProblem[r+1][c+1] = 0;
                        else
                            matrixProblem[r+2][c+2] = 0;
                        break;
                    case "#":
                        break;
                    case "*":
                        if (type.equals("S"))
                            matrixProblem[r+1][c+1] = -2;
                        else
                            matrixProblem[r+2][c+2] = -2;
                        break;
                    default:
                        int val = Integer.parseInt(cell);
                        if (type.equals("S"))
                            matrixProblem[r+1][c+1] = val;
                        else
                            matrixProblem[r+2][c+2] = val;
                        llista.add(val);
                        if (val == 1) {
                            if (type.equals("S"))
                                inici = new Tuple(r+1,c+1);
                            else
                                inici = new Tuple(r+2,c+2);
                        }
                }
            }
        }
        //Ordenem els números que ens donen i els afegin a una variable global numDonats per poder treballar amb ells
        Collections.sort(llista);
        numDonats = new int[llista.size()];
        for (int i = 0; i < numDonats.length;i++) {
            numDonats[i] = llista.get(i);
        }
        if(!solver(inici,1,0)){
            return null;
        }
        return printProblem();
    }

    /**
     * Prints the actual situation of the matrixProblem
     */
    public String[][] printProblem() {
        String[][] map;
        if (type.equals("S")) {
            map  = new String[matrixProblem.length-2][matrixProblem[0].length-2];
            for (int i = 1; i < matrixProblem.length - 1; i++) {
                if (matrixProblem[i][1] == -1) {
                    map[i-1][1] = "#";
                } else if (matrixProblem[i][1] == -2) {
                    map[i-1][1] = "*";
                }
                for (int j = 1; j < matrixProblem[1].length - 1; j++) {
                    map[i-1][j-1] = fillMatrix(matrixProblem[i][j]);
                }
            }
        }
        else {
            map  = new String[matrixProblem.length-4][matrixProblem[0].length-4];
            for (int i = 2; i < matrixProblem.length - 2; i++) {
                if (matrixProblem[i][2] == -1) {
                    map[i-2][0] = "#";
                } else if (matrixProblem[i][2] == -2) {
                    map[i-2][0] = "*";
                } else
                    map[i-2][0] = String.valueOf(matrixProblem[i][2]);
                for (int j = 2; j < matrixProblem[2].length - 2; j++) {
                    map[i-2][j-2] = fillMatrix(matrixProblem[i][j]);
                }
            }
        }
        return map;
    }

    private String fillMatrix(int i) {
        String s;
        if (i == -1) {
           s = "#";
        } else if (i == -2) {
            s = "*";
        } else
            s = "" + i;
        return s;
    }

    /**
     * Solver function for the problem.
     * It will analize and treat the one of the possible solutions that the Problem problem could have.
     * @param a Actual position of the matrixProblem that will be treat.
     * @param n Next number will be added to the matrixProblem to solve it.
     * @param seg Next position in the arrayLIst numsDonats to know if the number we need to add is already given by the problem
     * @return True if the problem has a solution. Otherwise, false.
     */
    private boolean solver (Tuple a, int n, int seg) {
        int rw,cl;
        rw = a.getX();
        cl = a.getY();
        if (n > numDonats[numDonats.length-1])
            return true;
        if (matrixProblem[rw][cl] != 0 && matrixProblem[rw][cl] != n)
            return  false;
        if (matrixProblem[rw][cl] == 0 && numDonats[seg] == n)
            return false;
        int anterior = matrixProblem[rw][cl];
        if (anterior == n)
            seg++;

        matrixProblem[rw][cl] = n;
        Tuple tuple;
        switch (type) {
            case "S":
                if(ang.equals("CA")) {
                    for (int i = -1; i < 2; i++)
                        for (int j = -1; j < 2; j++) {
                            tuple = new Tuple(rw + i, cl + j);
                            if (solver(tuple, n + 1, seg))
                                return true;
                        }
                }
                else {
                    tuple = new Tuple(rw-1, cl);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw+1, cl);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw, cl+1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw, cl-1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                }
                break;
            case "T":
                if (ang.equals("C")) {
                    tuple = new Tuple(rw, cl + 1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw, cl - 1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    if(rw%2==cl%2) {
                        tuple = new Tuple(rw+1,cl);
                        if (solver(tuple, n + 1, seg))
                            return true;
                    }
                    else {
                        tuple = new Tuple(rw-1,cl);
                        if (solver(tuple, n + 1, seg))
                            return true;
                    }
                }
                else {
                    tuple = new Tuple(rw, cl + 1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw, cl - 1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw, cl - 2);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw, cl + 2);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw - 1, cl - 1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw - 1, cl);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw - 1, cl + 1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw + 1, cl - 1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw + 1, cl);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw + 1, cl + 1);
                    if (solver(tuple, n + 1, seg))
                        return true;

                    if (rw % 2 == cl % 2) {
                        tuple = new Tuple(rw + 1, cl - 2);
                        if (solver(tuple, n + 1, seg))
                            return true;
                        tuple = new Tuple(rw + 1, cl + 2);
                        if (solver(tuple, n + 1, seg))
                            return true;
                    } else {
                        tuple = new Tuple(rw - 1, cl - 2);
                        if (solver(tuple, n + 1, seg))
                            return true;
                        tuple = new Tuple(rw - 1, cl + 2);
                        if (solver(tuple, n + 1, seg))
                            return true;
                    }
                }
                break;
            case "H":
                tuple = new Tuple(rw, cl + 1);
                if (solver(tuple, n + 1, seg))
                    return true;
                tuple = new Tuple(rw, cl - 1);
                if (solver(tuple, n + 1, seg))
                    return true;
                tuple = new Tuple(rw - 1, cl);
                if (solver(tuple, n + 1, seg))
                    return true;
                tuple = new Tuple(rw + 1, cl);
                if (solver(tuple, n + 1, seg))
                    return true;

                if (rw%2==1) {
                    tuple = new Tuple(rw - 1, cl + 1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw + 1, cl + 1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                }
                else {
                    tuple = new Tuple(rw - 1, cl - 1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                    tuple = new Tuple(rw + 1, cl - 1);
                    if (solver(tuple, n + 1, seg))
                        return true;
                }
            default:
                break;
        }
        matrixProblem[rw][cl] = anterior;
        return false;
    }
}

