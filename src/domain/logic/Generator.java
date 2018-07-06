package domain.logic;

import domain.game.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

/**
 * The Generator class contains all the methods that can be called in order to
 * generate a new Hidato. This Hidato will have some properties depending
 * of some variables such as difficulty or size.
 *
 * @author Nicolas Gouttefangeas Ballo
 * @author Marcos Riverola Errando
 * @author Samuel Hoenle
 */
public class Generator {
    /**
     * Coords is an inner class that has two attributes.
     * x represents a X coordinate
     * y represents de Y coordinate
     */
    private static class Coords {
        private int x;
        private int y;

        /**
         * Constructs a point X,Y
         * @param x the coordinate x
         * @param y the coordinate y
         */
        Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Returns de X coordinate
         * @return x coordinate
         */
        int getX() {
            return x;
        }

        /**
         * returns de Y coordinate
         * @return y coordinate
         */
        public int getY() {
            return y;
        }
    }

    private static boolean personalized = false;

    /**
     * Sets the boolean value to the given boolean
     * @param b the value to set the personalized attribute
     */
    public static void setPersonalized(boolean b){
        personalized = b;
    }

    /**
     * Generates a new Hidato game with the parameters passed and returns a {@link Problem} object containing it.
     *
     * @param type              A {@link Type} object describing the type of the Hidato (Square, Triangle, Hexagon)
     * @param numRows           The number of rows of the game map.
     * @param numColumns        The number of colums of the game map.
     * @param difficulty        A {@link Difficulty} object describing the difficulty the game should have.
     * @param creatorName       The name of the creator.
     * @param cornerAdjacent    Whether fields sharing a corner should be counted as neighbours or only those sharing a
     *                          face (not relevant for {@link HexagonProblem}s).
     * @return A {@link Problem} object in which the generated Problem is stored.
     */
    public static Problem generate(Type type, int numRows, int numColumns, Difficulty difficulty, String creatorName, boolean cornerAdjacent) {
        Problem p = null;

        String[][] map = createBoard(numRows, numColumns);
        if(!personalized) {
            map = fillBoard(map, type, cornerAdjacent, difficulty);
        }
        switch (type) {
            case Square:
                p = new SquareProblem(map, numRows, numColumns, difficulty, creatorName, cornerAdjacent);
                break;
            case Triangle:
                p = new TriangleProblem(map, numRows, numColumns, difficulty, creatorName, cornerAdjacent);
                break;
            case Hexagon:
                p = new HexagonProblem(map, numRows, numColumns, difficulty, creatorName);
        }

        return p;
    }

    public static void addToProblem(Problem p, int x, int y, String s){
        String[][] map = p.getMap();
        map[x][y] = s;
        p.updateMap(map);
    }
    public static Problem generatePersonalizedProblem(Problem p, List<String> numbers){
        Problem prob = p;
        Solver s = new Solver();
        String[][] map = s.solve(p);
        if(map != null) {
            map = deepcopyreplace(map);
            String adj = "-";
            if (p instanceof TriangleProblem) {
                TriangleProblem tp = (TriangleProblem) p;
                if (tp.isCornerAdjacent()) adj = "CA";
            } else if (p instanceof SquareProblem) {
                SquareProblem sp = (SquareProblem) p;
                if (sp.isCornerAdjacent()) adj = "CA";
            }
            if (Validator.validate(map, String.valueOf(p.getType()), adj)) {
                print(map);
                Difficulty diff = p.getDifficulty();
                int max = 0;
                for (int i = 0; i < numbers.size(); i++) {
                    int n = Integer.parseInt(numbers.get(i));
                    if (n > max) max = n;
                }
                int take = getNumbersToTake(diff, max);
                map = emptyMapPersonalized(map, take, numbers);
                prob.updateMap(map);
            } else prob = null;
        }
        else prob = null;
        return prob;
    }

    private static String[][] emptyMapPersonalized(String[][] matrix, int take, List<String> numbers) {
        Random rand = new Random() ;
        int numbersleft = 0;
        for (int i = 0; i < matrix.length ; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if(!matrix[i][j].equals("?")  &&
                        !matrix[i][j].equals("#")  &&
                        !matrix[i][j].equals("*")) numbersleft++;
            }
        }
        int took = 0;
        while(take > 0  && numbersleft - numbers.size() > take){
            int x = rand.nextInt(matrix.length) ;
            int y = rand.nextInt(matrix[0].length);
            if (!matrix[x][y].equals("?")  &&
                    !matrix[x][y].equals("#")  &&
                    !matrix[x][y].equals("*")  &&
                    !inNumbers(numbers, matrix[x][y])){
                matrix[x][y] = "?";
                --take;
                --numbersleft;
                ++took;
            }
        }
        if(took == 0){
            for (int i = 0; i < matrix.length ; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if(!matrix[i][j].equals("?")  &&
                            !matrix[i][j].equals("#")  &&
                            !matrix[i][j].equals("*") &&
                            !inNumbers(numbers, matrix[i][j])){
                        matrix[i][j] = "?";
                    }
                }
            }
        }
        return matrix;
    }

    private static boolean inNumbers(List<String> numbers, String s) {
        for (int i = 0; i < numbers.size(); i++) {
            if(numbers.get(i).equals(s)) return true;
        }
        return false;
    }

    private static int getNumbersToTake(Difficulty diff, int max) {
        int take = 0;
        switch (diff) {
            case Beginner:
                take = (int)  (max* .2);
                break;
            case Easy:
                take = (int)  (max* .35) ;
                break;
            case Medium:
                take = (int)  (max* .5) ;
                break;
            case Hard:
                take = (int)  (max * .65) ;
                break;
            case Pro:
                take = (int)  (max* .8) ;
                break;
            case Insane:
                take = (int)  (max* .9) ;
                break;

        }
        return take;
    }

    /**
     * Fills de matrix from the params with a specific number of # depending of a percentage.
     *
     * @param numRows       The number of rows in the matrix
     * @param numColumns    The number of columns in the matrix
     * @return the matrixs from the param filled with some #
     */
    private static String[][] createBoard(int numRows, int numColumns){
        String[][] matrix = new String[numRows][numColumns];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = "?";
            }
        }
        if(!personalized) {
            int maxHashes = (int) (matrix.length * matrix[0].length * .01) + 1;

            Random rand = new Random();

            for (int i = 0; i < maxHashes; ) {
                int x = rand.nextInt(matrix.length);
                int y = rand.nextInt(matrix[0].length);
                if (matrix[x][y].equals("?")) {
                    if (y == 0 || x == 0 || y == matrix[0].length - 1 || x == matrix.length - 1 ||
                            matrix[x - 1][y].equals("#") ||
                            matrix[x + 1][y].equals("#") ||
                            matrix[x][y - 1].equals("#") ||
                            matrix[x][y + 1].equals("#")) {

                        matrix[x][y] = "#";
                        i++;

                        if (!((x - 1 < 0 || isValidNeighbour(matrix, x - 1, y)) &&
                                (y - 1 < 0 || isValidNeighbour(matrix, x, y - 1)) &&
                                (x + 1 >= matrix.length || isValidNeighbour(matrix, x + 1, y)) &&
                                (y + 1 >= matrix[0].length || isValidNeighbour(matrix, x, y + 1)))) {
                            matrix[x][y] = "?";
                            i--;
                        }
                    }
                }
            }
        }
        return matrix;
    }


    /**
     * Fills the matrix from the param with some numbers ready to play.
     * Depending on the adjacency, type of case and difficulty
     * @param matrix the matrix that is going to be filled
     * @param type the type of case
     * @param adj the type of adjacency
     * @param diff the difficulty
     * @return the matrix filled with numbers.
     */
    private static String[][] fillBoard(String[][] matrix, Type type, boolean adj, Difficulty diff){
        int num = 1;
        String[][] matrixC = deepcopy(matrix);
        Random rand = new Random();

        int ex = matrix.length;
        int ey = matrix[0].length;
        int tamtot = ex*ey;
        double perc;
        if(type == Type.Triangle && !adj) {
            if (tamtot <= 225) perc = 0.65;
            else if (tamtot <= 400) perc = 0.5;
            else if (tamtot <= 625) perc = 0.4;
            else perc = 0.3;
            //max 30
        }
        else if(type == Type.Triangle){
            if(tamtot <= 900 ) perc = 0.65;
            else if(tamtot <= 1600) perc = 0.5;
            else if(tamtot <= 2025) perc = 0.45;
            else if(tamtot <= 3025) perc = 0.4;
            else if(tamtot <= 3600) perc = 0.35;
            else perc = 0.3;
            //max 80

        }
        else if(type == Type.Square && !adj){
            if(tamtot <= 225) perc = 0.65;
            else if(tamtot <= 400) perc = 0.5;
            else if(tamtot <= 625) perc = 0.45;
            else if(tamtot <= 900) perc = 0.35;
            else perc = 0.3;
            // max 35
        }
        else if (type == Type.Square){
            if(tamtot <= 400 ) perc = 0.65;
            else if(tamtot <= 900) perc = 0.5;
            else if(tamtot <= 1225) perc = 0.45;
            else if(tamtot <= 1600) perc = 0.4;
            else if(tamtot <= 2025) perc = 0.35;
            else perc = 0.3;
            //max = 50
        }
        else{
            if(tamtot <= 225) perc = 0.65;
            else if(tamtot <= 400) perc = 0.5;
            else if(tamtot <= 625) perc = 0.45;
            else perc = 0.3;
            //max = 35
        }


        while(num < tamtot*perc) {
            num = 1;
            matrixC = deepcopy(matrix);
            int x = rand.nextInt(matrix.length);
            int y = rand.nextInt(matrix[0].length);
            while(matrix[x][y].equals("#")){
                x = rand.nextInt(matrix.length);
                y = rand.nextInt(matrix[0].length);
            }
            ArrayList<Coords> neigh = getNeighbours(matrixC, x,y,type,adj);
            num = fill(matrixC, x, y, neigh, num, type, adj);
        }

        int max = num;
        int take = getNumbersToTake(diff,max);
        matrix = deepcopyreplace(matrixC) ;
        return emptyMap(matrix, num, take );
    }

    /**
     * prints the matrix from the param
     * @param matrix the matrix that is going to be printed
     */
    private static void print(String[][] matrix) {
        String out = "";
        for (String[] aMatrix : matrix) {
            out += aMatrix[0];
            for (int j = 1; j < aMatrix.length; j++) {
                out += "," + aMatrix[j];
            }
            out += "\n";
        }

        System.out.println(out);
    }

    /**
     * Returns true or false depending if the X,Y point is a valid neighbour
     * @param matrix the board that contains the X Y point
     * @param x the X coordinate from the point
     * @param y the Y coordinate from the point
     * @return true o false depending of the conditions
     */
    private static boolean isValidNeighbour(String[][] matrix, int x, int y) {
        int c = 0;

        if (matrix[x][y].equals("?")) {
            if (x-1 >= 0 && matrix[x-1][y].equals("?")) c++;
            if (y-1 >= 0 && matrix[x][y-1].equals("?")) c++;
            if (x+1 < matrix.length && matrix[x+1][y].equals("?")) c++;
            if (y+1 < matrix[0].length && matrix[x][y+1].equals("?")) c++;
        }

        return c >= 2;
    }

    /**
     * Returns an arrayList of Coords that cointains all the available neighbours from the X,Y point.
     * Depends on the type of case and adjacency.
     * @param matrix the matrix that cointais de X Y point
     * @param x the X coordinate from the point
     * @param y the Y coordinate from the point
     * @param type the type of case
     * @param adj the type of adjacency
     * @return returns an arrayList containing all the neighbours of the X Y point
     */
    private static ArrayList<Coords> getNeighbours(String[][] matrix, int x, int y, Type type, boolean adj){
        ArrayList<Coords> neigh = new ArrayList<>();
        switch (type){
            case Triangle:
                //right
                if(y+1 < matrix[0].length && matrix[x][y+1].equals("?")){
                    Coords c = new Coords(x,y+1);
                    neigh.add(c);
                }
                //left
                if(y-1 >= 0 && matrix[x][y-1].equals("?")){
                    Coords c = new Coords(x,y-1);
                    neigh.add(c);
                }
                //down
                if( (y % 2 == 0 && x % 2 == 0) || (y % 2 == 1 && x % 2 == 1) ) {
                    if (x + 1 < matrix.length && matrix[x+1][y].equals("?")) {
                        Coords c = new Coords(x+1, y );
                        neigh.add(c);
                    }
                }
                //up
                else{
                    if (x - 1 >= 0 && matrix[x-1][y].equals("?")) {
                        Coords c = new Coords(x-1, y);
                        neigh.add(c);
                    }
                }
                if(adj) {
                    //izq lejos
                    if(y-2 >= 0 && matrix[x][y-2].equals("?")) {
                        Coords c = new Coords(x,y-2);
                        neigh.add(c);
                    }
                    //der lejos
                    if(y+2 < matrix[0].length && matrix[x][y+2].equals("?")) {
                        Coords c = new Coords(x,y+2);
                        neigh.add(c);
                    }
                    //diag arriba izq
                    if(x-1 >= 0 && y-1 >= 0 && matrix[x-1][y-1].equals("?")) {
                        Coords c = new Coords(x-1,y-1);
                        neigh.add(c);
                    }
                    //diag arriba der
                    if(x-1 >= 0 && y+1 < matrix[0].length && matrix[x-1][y+1].equals("?")) {
                        Coords c = new Coords(x-1,y+1);
                        neigh.add(c);
                    }
                    //diag abajo izq
                    if(x+1 < matrix.length && y-1 >= 0 && matrix[x+1][y-1].equals("?")) {
                        Coords c = new Coords(x+1,y-1);
                        neigh.add(c);
                    }
                    //diag abajo der
                    if(x+1 < matrix.length && y+1 < matrix[0].length && matrix[x+1][y+1].equals("?")) {
                        Coords c = new Coords(x+1,y+1);
                        neigh.add(c);
                    }

                    if( (y % 2 == 0 && x % 2 == 0) || (y % 2 == 1 && x % 2 == 1) ) {
                        if (x - 1 >= 0 && matrix[x-1][y].equals("?")) {
                            Coords c = new Coords(x-1, y);
                            neigh.add(c);
                        }
                        if(x+1 < matrix.length && y-2 >= 0 && matrix[x+1][y-2].equals("?")) {
                            Coords c = new Coords(x+1,y-2);
                            neigh.add(c);
                        }
                        if(x+1 < matrix.length && y+2 < matrix[0].length && matrix[x+1][y+2].equals("?")) {
                            Coords c = new Coords(x+1,y+2);
                            neigh.add(c);
                        }
                    }
                    else{
                        if (x + 1 < matrix.length && matrix[x+1][y].equals("?")) {
                            Coords c = new Coords(x+1, y );
                            neigh.add(c);
                        }
                        if(x-1 >= 0 && y-2 >= 0 && matrix[x-1][y-2].equals("?")) {
                            Coords c = new Coords(x-1,y-2);
                            neigh.add(c);
                        }
                        if(x-1 >= 0 && y+2 < matrix[0].length && matrix[x-1][y+2].equals("?")) {
                            Coords c = new Coords(x-1,y+2);
                            neigh.add(c);
                        }
                    }
                }
                break;
            case Square:
                if(x+1 < matrix.length && matrix[x+1][y].equals("?")){
                    Coords c = new Coords(x+1,y);
                    neigh.add(c);
                }
                if(x-1 >= 0 && matrix[x-1][y].equals("?")){
                    Coords c = new Coords(x-1,y);
                    neigh.add(c);
                }
                if (y + 1 < matrix[0].length && matrix[x][y + 1].equals("?")) {
                    Coords c = new Coords(x, y + 1);
                    neigh.add(c);
                }
                if (y - 1 >= 0 && matrix[x][y - 1].equals("?")) {
                    Coords c = new Coords(x, y - 1);
                    neigh.add(c);
                }
                if (adj){
                    if(x+1 < matrix.length && y+1 < matrix[0].length && matrix[x+1][y+1].equals("?")){
                        Coords c = new Coords(x+1,y+1);
                        neigh.add(c);
                    }
                    if(x-1 >= 0  && y-1 >= 0 && matrix[x-1][y-1].equals("?")){
                        Coords c = new Coords(x-1,y-1);
                        neigh.add(c);
                    }
                    if (y + 1 < matrix[0].length && x-1 >= 0 && matrix[x-1][y + 1].equals("?")) {
                        Coords c = new Coords(x-1, y + 1);
                        neigh.add(c);
                    }
                    if (y - 1 >= 0 && x+1 < matrix.length && matrix[x+1][y - 1].equals("?")) {
                        Coords c = new Coords(x+1, y - 1);
                        neigh.add(c);
                    }
                }
                break;
            case Hexagon:
                if(x+1 < matrix.length && matrix[x+1][y].equals("?")){
                    Coords c = new Coords(x+1,y);
                    neigh.add(c);
                }
                if(x-1 >= 0 && matrix[x-1][y].equals("?")){
                    Coords c = new Coords(x-1,y);
                    neigh.add(c);
                }
                if (y + 1 < matrix[0].length && matrix[x][y + 1].equals("?")) {
                    Coords c = new Coords(x, y + 1);
                    neigh.add(c);
                }
                if (y - 1 >= 0 && matrix[x][y - 1].equals("?")) {
                    Coords c = new Coords(x, y - 1);
                    neigh.add(c);
                }
                if(x%2 == 1) {
                    if (x + 1 < matrix.length && y + 1 < matrix[0].length && matrix[x + 1][y + 1].equals("?")) {
                        Coords c = new Coords(x + 1, y + 1);
                        neigh.add(c);
                    }
                    if (y + 1 < matrix[0].length && x - 1 >= 0 && matrix[x - 1][y + 1].equals("?")) {
                        Coords c = new Coords(x - 1, y + 1);
                        neigh.add(c);
                    }
                }
                else{
                    if (x + 1 < matrix.length && y - 1 >= 0 && matrix[x + 1][y - 1].equals("?")) {
                        Coords c = new Coords(x + 1, y - 1);
                        neigh.add(c);
                    }
                    if (y - 1 >= 0 && x - 1 >= 0 && matrix[x - 1][y - 1].equals("?")) {
                        Coords c = new Coords(x - 1, y - 1);
                        neigh.add(c);
                    }
                }
                break;

        }
        return neigh;
    }

    /**
     * Recursive function that fills the matrix with numbers and returns the maximum one.
     * @param matrix the matrix that is going to be filled
     * @param x the X coordinate from the point
     * @param y the Y coordinate from the point
     * @param neighbours the arrayList that cointains all the available of X Y
     * @param num the num that is going to be filled in each recursion
     * @param type the type of case
     * @param adj the type of adjacency
     * @return the maximum number filled
     */
    private static int fill(String[][] matrix, int x, int y, ArrayList<Coords> neighbours, int num, Type type, boolean adj){
        if (neighbours.size() == 0) {
            matrix[x][y] = "" + num;
            return num;
        }
        else{
            matrix[x][y] = "" + num;
            neighbours = getNeighbours(matrix, x, y, type, adj);
            Collections.shuffle(neighbours);
            if (neighbours.size() != 0) {
                x = neighbours.get(0).getX();
                y = neighbours.get(0).getY();
                num += 1;
            }
            return fill(matrix, x, y, neighbours, num , type, adj);
        }
    }

    /**
     * Makes a copy of the matrix position by position
     * @param matrix the matrix that is going to be copied
     * @return the copy of the matrix from the param
     */
    private static String[][] deepcopy(String[][] matrix){
        String[][] copia = new String[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                copia[i][j] = matrix[i][j];
            }
        }
        return copia;
    }

    /**
     * Makes a copy of the matrix from the param position by position
     * and replaces the "?" left no the map with "#"
     * @param matrix the matrix that is going to be copied and replaced
     * @return the copy and replacement of the matrix from the param
     */
    private static String[][] deepcopyreplace(String[][] matrix){
        String[][] copia = new String[matrix.length][matrix[0].length];
        for (int x = 0; x < matrix.length; x++) {
            for (int y = 0; y < matrix[0].length; y++) {
                if(matrix[x][y].equals("?") || matrix[x][y].equals("0") ){
                    if (y == 0 || x == 0 || y == matrix[0].length - 1 || x == matrix.length - 1 ||
                            matrix[x - 1][y].equals("#") ||
                            matrix[x + 1][y].equals("#") ||
                            matrix[x][y - 1].equals("#") ||
                            matrix[x][y + 1].equals("#")){
                        copia[x][y] = "#";
                    }
                    else{
                        copia[x][y] = "*";
                    }
                }
                else {
                    copia[x][y] = matrix[x][y];
                }
            }
        }
        return copia;
    }

    /**
     * Removes a specific amount of numbers from the Matrix so the game can be played.
     * The amount of numbers removed depends on a percentage that changed depending on
     * the difficulty and the max number param
     * @param matrix The matrix that is going the be empty
     * @param max The max number of the matrix
     * @param take the numbers from the matrix that is going to be removed
     * @return The matrix without the specified numbers
     */
    private static String[][] emptyMap(String[][] matrix, int max, int take){
        Random rand = new Random() ;
        while(take > 0 ){
            int x = rand.nextInt(matrix.length) ;
            int y = rand.nextInt(matrix[0].length);
            if (!matrix[x][y].equals("?")  &&
                    !matrix[x][y].equals("#")  &&
                    !matrix[x][y].equals("*")  &&
                    !matrix[x][y].equals("1")  &&
                    !matrix[x][y].equals(""+ max)){
                matrix[x][y] = "?";
                --take;
            }
        }
        return matrix;
    }
}
