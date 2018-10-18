package domain.game;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Implementation of the abstract {@link Problem} class. A problem with fields in the shape of squares. In these kind
 * of maps, different types of adjacency exist: faces only or also edges (diagonally).
 *
 * @author Samuel Hoenle
 */

public class SquareProblem extends Problem implements Serializable{

    private boolean cornerAdjacent;

    /**
     * Default constructor for a square problem. In addition to calling the default constructor of {@link Problem}, the
     * type of adjacency is also set.
     *
     * @see Problem#Problem(String[][], int, int, Difficulty, String)
     *
     * @param map               A two-level String array containing the number the field contains, '#' for a field that
     *                          is part of the outside, '*' for a not playable field and '?' for a field in which the
     *                          user can put a number. != null, map.length == numRows, map[n].length == numColumns
     * @param numRows       The number of rows in the map. Needs to be greater than 0
     * @param numColumns    The number of columns in the map. Needs to be greater than 0
     * @param difficulty        A {@link Difficulty} object. != null
     * @param creatorName       A String identifying the {@link Player} who created the problem. != null or empty
     * @param cornerAdjacent    true = corners adjacency is activated, it is possible to enter numbers diagonally
     *                          false = only faces are counted as adjacent
     */
    public SquareProblem(String[][] map, int numRows, int numColumns, Difficulty difficulty, String creatorName, boolean cornerAdjacent) {
        super(map, numRows, numColumns, difficulty, creatorName);
        this.cornerAdjacent = cornerAdjacent;
        this.id = hashCode();
    }

    /**
     * Getter method of the {@link #cornerAdjacent} attribute.
     * @return  true = corners adjacency is activated, it is possible to enter numbers diagonally
     *          false = only faces are counted as adjacent
     */
    public boolean isCornerAdjacent() {
        return cornerAdjacent;
    }

    /**
     * The hash code of an instance also constitutes its {@link #id}. It is created by combining the hash codes of its
     * attributes.
     *
     * @return  The combined hash code of all attributes of this instance except {@link #id} in which this value is
     *          saved. Equals the combination of {@link Arrays#deepHashCode(Object[])} of {@link #map}, {@link #numRows},
     *          {@link #numColumns}, {@link Difficulty#hashCode()}, {@link String#hashCode()} of {@link #creatorName} and
     *          {@link #cornerAdjacent}
     */
    @Override
    public int hashCode() {
        return 37*super.hashCode() + (cornerAdjacent ? 1 : 0);
    }

    /**
     * A method to compare this object to another one
     *
     * @param o Any object
     * @return  Whether or not passed the object is equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SquareProblem)) return false;
        SquareProblem problem = (SquareProblem) o;
        return getNumRows() == problem.getNumRows() &&
                getNumColumns() == problem.getNumColumns() &&
                getDifficulty() == problem.getDifficulty() &&
                Arrays.deepEquals(getMap(), problem.getMap()) &&
                Objects.equals(getCreatorName(), problem.getCreatorName()) &&
                isCornerAdjacent() == problem.isCornerAdjacent();
    }

    /**
     * Returns the {@link Type} of this Problem
     * @return {@link Type#Square}
     */
    @Override
    public Type getType() {
        return Type.Square;
    }
}
