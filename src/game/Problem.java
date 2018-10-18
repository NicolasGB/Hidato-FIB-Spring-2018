package domain.game;

import domain.logic.Generator;

import java.io.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;

/**
 * A Problem is a representation of a Hidato. It contains the starting map, the difficulty, the number of rows and
 * columns and the name of the creator. It is identified by its id, which is its hash code. Instances of Problem serve
 * as blueprints for Hidato games, that are solved in {@link Attempt}.
 *
 * @author Samuel Hoenle
 */
public abstract class Problem implements Serializable{

    /**
     * The identifier of a Hidato. Value taken from {@link #hashCode()}.
     */
    protected int id;
    /**
     * The difficulty of the Hidato. {@link Difficulty}
     */
    protected Difficulty difficulty;
    /**
     * This two-level String array stores the map of the Hidato. Numbers or "#", "?", "*"
     */
    protected String[][] map;
    /**
     * The number of rows of the map. Must be equal to {@link #map}.length
     */
    protected int numRows;
    /**
     * The number of columns of the map. Must be equal to {@link #map}[i].length
     */
    protected int numColumns;
    /**
     * The name of the creator of the problem. Should correspond to a {@link Player}.
     */
    protected String creatorName;

    /**
     * Constructor for a new Problem. All parameters are saved into the instance's attributes. Additionally, the 'id'
     * attribute is set, containing the {@link #hashCode()} of itself.
     *
     * @param map           A two-level String array containing the number the field contains, '#' for a field that is
     *                      part of the outside, '*' for a not playable field and '?' for a field in which the user can
     *                      put a number. != null, map.length == numRows, map[n].length == numColumns
     * @param numRows       The number of rows in the map. Needs to be greater than 0
     * @param numColumns    The number of columns in the map. Needs to be greater than 0
     * @param difficulty    A {@link Difficulty} object. != null
     * @param creatorName   A String identifying the {@link Player} who created the problem. != null or empty
     */
    public Problem(String[][] map, int numRows, int numColumns, Difficulty difficulty, String creatorName) {
        if (difficulty == null) throw new IllegalArgumentException("Difficulty cannot be null");
        this.difficulty = difficulty;

        if (creatorName == null || creatorName.equals("")) throw new IllegalArgumentException("Creator name cannot be null or empty");
        this.creatorName = creatorName;

        if (numRows <= 0) throw new IllegalArgumentException("Number of rows <= 0");
        this.numRows = numRows;

        if (numColumns <= 0) throw new IllegalArgumentException("Number of columns <= 0");
        this.numColumns = numColumns;

        if (map == null) throw new IllegalArgumentException("Matrix cannot be null");
        if (map.length != numRows) throw new IllegalArgumentException("Matrix does not match provided number of rows");
        if (map[0].length != numColumns) throw new IllegalArgumentException("Matrix does not match provided number of columns");
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                if (!(map[i][j].equals("#") || map[i][j].equals("*") || map[i][j].equals("?") || map[i][j].matches("\\d+"))) {
                    throw new IllegalArgumentException("Matrix contains unexpected content");
                }
            }
        }
        this.map = map;
    }

    /**
     * Getter function of the map attribute. Returns a copy of the {@link #map} attribute in order to avoid mistakes
     * related to call-by-reference
     *
     * @return  A two-level String array containing the number the field contains, '#' for a field that is part of the
     *          outside, '*' for a not playable field and '?' for a field in which the user can put a number.
     */
    public String[][] getMap() {
        String[][] copia = new String[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                copia[i][j] = map[i][j];
            }
        }
        return copia;
    }

    /**
     * Getter function for the numRows attribute.
     * @return  The number of rows in the map.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Getter function for the numColumns attribute.
     * @return  The number of columns in the map.
     */
    public int getNumColumns() {
        return numColumns;
    }

    /**
     * Getter function for the difficulty attribute.
     * @return  A {@link Difficulty} object.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * Getter function for the id attribute. This function should return the same value as {@link #hashCode()}.
     * @return  The id of the instance, which should be its {@link #hashCode()}.
     */
    public int getId() {
        return id;
    }

    /**
     * Parses Problem ids (integers) to their hexadecimal representation as Strings
     *
     * @param id An id of a problem as integer
     * @return The hexadecimal representation of the id of the Problem
     */
    public static String idAsHex(int id) {
        return Integer.toHexString(id);
    }

    /**
     * Parses Problem ids hexadecimal representation as Strings back to integers
     *
     * @param hexId An id of a problem as hexadecimal representation of an integer
     * @return The id of a problem as integer
     */
    public static int hexAsId(String hexId) {
        return (int) Long.parseLong(hexId, 16);
    }

    /**
     * Getter function for the creatorName attribute. Should correspond with a {@link Player}.
     * @return  A String containing the name that identifies a {@link Player}.
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * A static function to determine the difficulty of a hidato map. Same metrics as in {@link Generator}
     *
     *    0% - 20%: Beginner
     * 20.1% - 35%: Easy
     * 35.1% - 50%: Medium
     * 50.1% - 65%: Hard
     * 65.1% - 80%: Pro
     * 80.1% -100%: Insane
     *
     * Returns null if there are more empty cells than the value of the highest number. No validity checks of the map
     * are performed.
     *
     * @see Generator
     *
     * @param map   A hidato map containing numbers or "#", "?", "*"
     * @return      A {@link Difficulty} object dependent on the percentage of empty to filled cells
     */
    public static Difficulty determineDifficulty(String[][] map) {
        int highest = 0;
        int countEmpty = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                try {
                    int val = Integer.parseInt(map[i][j]);
                    if (val > 0) highest = val > highest ? val : highest;
                } catch (NumberFormatException e) {
                    if (map[i][j].equals("?")) countEmpty++;
                }
            }
        }

        float ratio = (float) countEmpty/highest;
        if (ratio <= .2) return Difficulty.Beginner;
        if (ratio <= .35) return Difficulty.Easy;
        if (ratio <= .5) return Difficulty.Medium;
        if (ratio <= .65) return Difficulty.Hard;
        if (ratio <= .8) return Difficulty.Pro;
        if (ratio <= 1) return Difficulty.Insane;
        return null;
    }

    /**
     * Returning a 2D representation of the {@link #map} of this problem. Unlike {@link #getMap()} (which returns a
     * String[][] object) this function returns a String representing the map more visually. And unlike
     * {@link Arrays#deepToString(Object[])}, every new second-level object is represented by a new line.
     *
     * A field is either printed as the number the field contains, '#' for a field that is part of the outside,
     * '*' for a not playable field and '?' for a field in which the user can put a number. Each field is separated by
     * a ','.
     *
     * @return  A String representing the {@link #map} attribute. Separation of first-level objects by '\n', no
     *          separation of second-level objects, creating a 2D representation of the content.
     */
    public String printMap() {
        String out = "";
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                out+= map[i][j];
                if (j != map[i].length-1) out+=",";
            }
            out+="\n";
        }
        return out.trim();
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
        int result = 1;
        result = 31 * result + getDifficulty().ordinal();
        result = 31 * result + getCreatorName().hashCode();
        result = 31 * result + numRows;
        result = 31 * result + numColumns;
        result = 31 * result + Arrays.deepHashCode(getMap());
        return result;
    }

    /**
     * A method to compare this object to another one
     *
     * @param o Any object
     * @return  Whether or not passed the object is equal
     */
    @Override
    public abstract boolean equals(Object o);

    /**
     * A textual representation of this instance.
     *
     * @return  The values of all attributes of this instance in one row ({@link #id}, {@link #difficulty},
     *          {@link #numRows}, {@link #numColumns}, {@link #creatorName}), except for {@link #map} which is
     *          represented by its 2D representation generated by {@link #printMap()}.
     */
    @Override
    public String toString() {
        return "Problem: " +
                "id=" + id +
                ", difficulty=" + difficulty +
                ", numRows=" + numRows +
                ", numColumns=" + numColumns +
                ", creatorName='" + creatorName + '\'' +
                "\n" + printMap();
    }

    /**
     * Returns the {@link Type} of the concrete instance of a subclass
     * @return A {@link Type}
     */
    public abstract Type getType();

    /**
     * Set the map of this problem to the passed map
     * @param map The new of the problem
     */
    public void updateMap(String[][] map){
        for (int i = 0; i < map.length ; i++) {
            for (int j = 0; j < map[0].length; j++) {
                this.map[i][j] = map[i][j];
            }
        }
    }
}
