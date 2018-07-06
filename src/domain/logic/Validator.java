package domain.logic;
import java.util.ArrayList;
/**
 * The Validator algorithm checks if an Hidato is valid.
 *
 * @author Marcos
 */

public class Validator {

    /**
     * Coords is an inner class that has two atributes.
     * x represents a X coordinate
     * y represents de Y coordinate
     */
    private static class coords {
        private int x;
        private int y;

        /**
         * Constructs a point X,Y
         *
         * @param x the coordinate x
         * @param y the coordinate y
         */
        private coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Sets the X coordinate
         *
         * @param x the X coordinate that is going to be set
         */
        private void setX(int x) {
            this.x = x;
        }

        /**
         * Sets the Y coordinate
         *
         * @param y the Y coordinate that is going to be set
         */
        private void setY(int y) {
            this.y = y;
        }

        /**
         * Returns de X coordinate
         *
         * @return x coordinate
         */
        private int getX() {
            return x;
        }

        /**
         * returns de Y coordinate
         *
         * @return y coordinate
         */
        private int getY() {
            return y;
        }
    }

    /**
     * Returns an arrayList with all the neighbours from the X Y point depending on the adjaceny and type
     *
     * @param matrix the matrix that contains the X Y point
     * @param x      the X coordinate from the point
     * @param y      the Y coordinate from the point
     * @param type   the type of case
     * @param adj    the type of adjacency
     * @return ArrayList with all the neighbours
     */
    private static ArrayList<coords> getNeighbours(String[][] matrix, int x, int y, String type, String adj) {
        ArrayList<coords> neigh = new ArrayList<>();
        switch (type) {
            case "Triangle":
                //right
                if (y + 1 < matrix[0].length) {
                    coords c = new coords(x, y + 1);
                    neigh.add(c);
                }
                //left
                if (y - 1 >= 0) {
                    coords c = new coords(x, y - 1);
                    neigh.add(c);
                }
                //down
                if ((y % 2 == 0 && x % 2 == 0) || (y % 2 == 1 && x % 2 == 1)) {
                    if (x + 1 < matrix.length) {
                        coords c = new coords(x + 1, y);
                        neigh.add(c);
                    }
                }
                //up
                else {
                    if (x - 1 >= 0) {
                        coords c = new coords(x - 1, y);
                        neigh.add(c);
                    }
                }
                if (adj.equals("CA")) {
                    //izq lejos
                    if (y - 2 >= 0) {
                        coords c = new coords(x, y - 2);
                        neigh.add(c);
                    }
                    //der lejos
                    if (y + 2 < matrix[0].length) {
                        coords c = new coords(x, y + 2);
                        neigh.add(c);
                    }
                    //diag arriba izq
                    if (x - 1 >= 0 && y - 1 >= 0) {
                        coords c = new coords(x - 1, y - 1);
                        neigh.add(c);
                    }
                    //diag arriba der
                    if (x - 1 >= 0 && y + 1 < matrix[0].length) {
                        coords c = new coords(x - 1, y + 1);
                        neigh.add(c);
                    }
                    //diag abajo izq
                    if (x + 1 < matrix.length && y - 1 >= 0) {
                        coords c = new coords(x + 1, y - 1);
                        neigh.add(c);
                    }
                    //diag abajo der
                    if (x + 1 < matrix.length && y + 1 < matrix[0].length) {
                        coords c = new coords(x + 1, y + 1);
                        neigh.add(c);
                    }

                    if ((y % 2 == 0 && x % 2 == 0) || (y % 2 == 1 && x % 2 == 1)) {
                        if (x - 1 >= 0) {
                            coords c = new coords(x - 1, y);
                            neigh.add(c);
                        }
                        if (x + 1 < matrix.length && y - 2 >= 0) {
                            coords c = new coords(x + 1, y - 2);
                            neigh.add(c);
                        }
                        if (x + 1 < matrix.length && y + 2 < matrix[0].length) {
                            coords c = new coords(x + 1, y + 2);
                            neigh.add(c);
                        }
                    } else {
                        if (x + 1 < matrix.length) {
                            coords c = new coords(x + 1, y);
                            neigh.add(c);
                        }
                        if (x - 1 >= 0 && y - 2 >= 0) {
                            coords c = new coords(x - 1, y - 2);
                            neigh.add(c);
                        }
                        if (x - 1 >= 0 && y + 2 < matrix[0].length) {
                            coords c = new coords(x - 1, y + 2);
                            neigh.add(c);
                        }
                    }
                }
                break;
            case "Square":
                if (x + 1 < matrix.length) {
                    coords c = new coords(x + 1, y);
                    neigh.add(c);
                }
                if (x - 1 >= 0) {
                    coords c = new coords(x - 1, y);
                    neigh.add(c);
                }
                if (y + 1 < matrix[0].length) {
                    coords c = new coords(x, y + 1);
                    neigh.add(c);
                }
                if (y - 1 >= 0) {
                    coords c = new coords(x, y - 1);
                    neigh.add(c);
                }
                if (adj.equals("CA")) {
                    if (x + 1 < matrix.length && y + 1 < matrix[0].length) {
                        coords c = new coords(x + 1, y + 1);
                        neigh.add(c);
                    }
                    if (x - 1 >= 0 && y - 1 >= 0) {
                        coords c = new coords(x - 1, y - 1);
                        neigh.add(c);
                    }
                    if (y + 1 < matrix[0].length && x - 1 >= 0) {
                        coords c = new coords(x - 1, y + 1);
                        neigh.add(c);
                    }
                    if (y - 1 >= 0 && x + 1 < matrix.length) {
                        coords c = new coords(x + 1, y - 1);
                        neigh.add(c);
                    }
                }
                break;
            case "Hexagon":
                if (x + 1 < matrix.length) {
                    coords c = new coords(x + 1, y);
                    neigh.add(c);
                }
                if (x - 1 >= 0) {
                    coords c = new coords(x - 1, y);
                    neigh.add(c);
                }
                if (y + 1 < matrix[0].length) {
                    coords c = new coords(x, y + 1);
                    neigh.add(c);
                }
                if (y - 1 >= 0) {
                    coords c = new coords(x, y - 1);
                    neigh.add(c);
                }
                if(x%2 == 0) {
                    if (x + 1 < matrix.length && y - 1 >= 0) {
                        coords c = new coords(x + 1, y - 1);
                        neigh.add(c);
                    }
                    if (y - 1 >= 0 && x - 1 >= 0) {
                        coords c = new coords(x - 1, y - 1);
                        neigh.add(c);
                    }
                }
                else {
                    if (x + 1 < matrix.length && y + 1 < matrix[0].length) {
                        coords c = new coords(x + 1, y + 1);
                        neigh.add(c);
                    }
                    if (y +1 < matrix[0].length && x - 1 >= 0) {
                        coords c = new coords(x - 1, y + 1);
                        neigh.add(c);
                    }
                }
                break;

        }
        return neigh;
    }

    /**
     * Returns True if the Hidato is valid or false if not
     *
     * @param matrix The Hidato that is going to be validated
     * @param type   The type of case
     * @param adj    The type of adjacency
     * @return True if valid, false if not
     */
    public static boolean validate(String[][] matrix, String type, String adj) {
        coords c = new coords(0, 0);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j].equals("1")) {
                    c.setX(i);
                    c.setY(j);
                }
            }
        }
        int num = 1;
        int max = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (!matrix[i][j].equals("#") && !matrix[i][j].equals("*")) {
                    if (Integer.parseInt(matrix[i][j]) > max) {
                        max = Integer.parseInt(matrix[i][j]);

                    }
                }
            }
        }
        while (num < max) {
            ArrayList<coords> neigh = getNeighbours(matrix, c.x, c.y, type, adj);
            boolean b = false;
            for (int i = 0; i < neigh.size(); i++) {
                coords p = neigh.get(i);
                if (!matrix[p.getX()][p.getY()].equals("#") && !matrix[p.getX()][p.getY()].equals("*") && Integer.parseInt(matrix[p.getX()][p.getY()]) == num + 1) {
                    c.setX(p.getX());
                    c.setY(p.getY());
                    b = true;
                }
            }
            if (!b) return false;
            ++num;

        }
        return true;
    }
}