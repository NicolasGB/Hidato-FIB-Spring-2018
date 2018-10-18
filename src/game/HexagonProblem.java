package domain.game;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * Implementation of the abstract {@link Problem} class. A problem with fields in the shape of hexagons. In these kind
 * of maps, only one type of adjacency exists.
 *
 * @author Samuel Hoenle
 */

public class HexagonProblem extends Problem implements Serializable{

    /**
     * A constructor calling the default constructor of {@link Problem}.
     *
     * @see Problem#Problem(String[][], int, int, Difficulty, String)
     *
     * @param map           A two-level String array containing the number the field contains, '#' for a field that is
     *                      part of the outside, '*' for a not playable field and '?' for a field in which the user can
     *                      put a number. != null, map.length == numRows, map[n].length == numColumns
     * @param numRows       The number of rows in the map. Needs to be greater than 0
     * @param numColumns    The number of columns in the map. Needs to be greater than 0
     * @param difficulty    A {@link Difficulty} object. != null
     * @param creatorName   A String identifying the {@link Player} who created the problem. != null or empty
     */
    public HexagonProblem(String[][] map, int numRows, int numColumns, Difficulty difficulty, String creatorName) {
        super(map, numRows, numColumns, difficulty, creatorName);
        this.id = hashCode();
    }

    /**
     * The hash code of an instance also constitutes its {@link #id}. It is created by combining the hash codes of its
     * attributes.
     *
     * @return  The combined hash code of all attributes of this instance except {@link #id} in which this value is
     *          saved. Equals the combination of {@link Arrays#deepHashCode(Object[])} of {@link #map}, {@link #numRows},
     *          {@link #numColumns}, {@link Difficulty#hashCode()} and {@link String#hashCode()} of {@link #creatorName}
     */
    @Override
    public int hashCode() {
        return 17 * super.hashCode();
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
        if (!(o instanceof HexagonProblem)) return false;
        HexagonProblem problem = (HexagonProblem) o;
        return getNumRows() == problem.getNumRows() &&
                getNumColumns() == problem.getNumColumns() &&
                getDifficulty() == problem.getDifficulty() &&
                Arrays.deepEquals(getMap(), problem.getMap()) &&
                Objects.equals(getCreatorName(), problem.getCreatorName());
    }

    /**
     * Returns the {@link Type} of this Problem
     * @return {@link Type#Hexagon}
     */
    @Override
    public Type getType() {
        return Type.Hexagon;
    }
}
