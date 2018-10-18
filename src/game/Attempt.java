package domain.game;

import java.io.*;
import java.text.ParseException;
import java.util.*;

/**
 *  A Attempt represents one try for a game by a Player
 *  It has a Timer, the name of the Player who does the attempt, the Id of the Problem which is being solved and the attempt ID
 *  @author Sergi Serrano Casalins
 */

public class Attempt implements Serializable{

    private boolean isFinished;
    protected int id;
    protected String attemptPlayer;
    protected Integer attemptProblem;
    protected Integer attemptSeconds;
    protected String[][] solution;

    /**
     * Contructs an Attempt with the ID, the problem and the player from the params and the timer initialaized in 0s
     * @param player Player who is doing the Attempt
     * @param problem Problem that has been solved
     */
    public Attempt(String player, Problem problem) {
        this.isFinished = false;
        this.attemptPlayer = player;
        this.attemptProblem = problem.getId();
        this.attemptSeconds = 0;
        this.solution = new String[problem.getNumRows()][problem.getNumColumns()];
        this.id = hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attempt attempt = (Attempt) o;
        return Objects.equals(attemptPlayer, attempt.attemptPlayer) &&
                Objects.equals(attemptProblem, attempt.attemptProblem) &&
                Objects.equals(attemptSeconds, attempt.attemptSeconds) &&
                Arrays.equals(solution, attempt.solution);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(attemptPlayer, attemptProblem);
        return result;
    }

    /**
     * Parses Attempt ids (integers) to their hexadecimal representation as Strings
     * @param id An id of an attempt as integer
     * @return The hexadecimal representation of the id of the Attempt
     */
    public static String idAsHex(int id) {
        return Integer.toHexString(id);
    }

    /**
     * Parses Attempt ids hexadecimal representation as Strings back to integers
     * @param hexId An id of an attempt as hexadecimal representation of an integer
     * @return The id of an attempt as integer
     */
    public static int hexAsId(String hexId) {
        return (int) Long.parseLong(hexId, 16);
    }

    /**
     * Getter function of the atribute id
     * @return the id of the attempt
     */
    public int getId() {
        return id;
    }

    /**
     * Setter function of the atribute attemptSeconds
     * @param time new time if integer format
     */
    public void setAttemptSeconds(int time) {
        this.attemptSeconds = time;
    }

    /**
     * Returns the name of the Player who is doing the Attempt
     * @return the Name of the Player
     */
    public String getAttemptPlayer() {
        return attemptPlayer;
    }

    /**
     * Returns the ID of the Problem which is being solved in the Attempt
     * @return ID of the Problem
     */
    public Integer getAttemptProblem() {
        return attemptProblem;
    }

    /**
     * Returns the seconds passed in String Format
     * @return Seconds Passed
     */
    public Integer getAttemptSeconds() {
        return this.attemptSeconds;
    }



    /**
     * Sets the current solution that the player is doing.
     * @param map the current player's solution
     */
    public void setSolution(String[][] map){
        for (int i = 0; i < map.length ; i++) {
            for (int j = 0; j < map[0].length ; j++) {
                solution[i][j] = map[i][j];
            }
        }
    }

    /**
     * Returns the saved solution from the attempt
     * @return the saved solution from the attempt
     */
    public String[][] getSolution(){
        return solution;
    }
}
