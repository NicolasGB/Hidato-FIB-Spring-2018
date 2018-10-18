package domain.game;

import java.io.*;


/**
 * A player represents an user who wants to play the Hidato Game
 * It has a username and password,a score and an avatar.
 * @author Marcos Riverola Errando
 *
 */
public class Player implements Serializable {
    private String name;
    private String password;
    private String avatarPath;
    private int points;
    private boolean pathChanged;
    //private ArrayList<int> StartedAttempts = new ArrayList<>();

    /**
     * Constructs a player with the name and username from the params and with 0 points and without avatar
     * @param name name of the user
     * @param password password of the user
     */
    public Player(String name, String password) {
        this.name = name;
        this.password = password;
        this.points = 0;
        this.avatarPath = "/presentation/view/Images/default.png";
        this.pathChanged = false;
    }

    public Player(String name, String password, String path, Boolean changed) {
        this.name = name;
        this.password = password;
        this.points = 0;
        this.avatarPath = path;
        this.pathChanged = changed;
    }

    /**
     * Setter for the avatar of the Player
     * @param path This is the path from the local file the user wants to set as the avatar.
     */
    public void setAvatar(String path) {
        this.avatarPath = path;
    }

    /**
     * Sets pathChanged true
     */
    public void setPathChanged(){
        this.pathChanged = true;
    }

    /**
     * Returns the boolean isPathChanged
     * @return pathChanged
     */
    public boolean isPathChanged() {
        return pathChanged;
    }

    /**
     * Returns the avatar Path
     * @return A string that contains the avatar path
     */
    public String getAvatar() {
        return this.avatarPath;
    }



    /**
     * Updates the player's points
     * @param points the amount of points to add to the player
     */
    public void updatePoints(int points) { this.points += points; }

    /**
     * Returns the player's username
     * @return player's username
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the player's password
     * @return player's password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Returns how many points does the player have.
     * @return players amount of points
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * Returns a string that cointains a description of the game Hidato
     * @return String with all the Hidato Rules
     */
    public String askForHelp() {
        return "Hidato is a logic game created by the Dr. Gyota Benedet. The objective is to " +
                "fill a board with consecutive numbers that can be adjacent by sides and also with corner adjacency." +
                "The first and the last numbers are always shown at the start, and depending on the difficulty you" +
                "can see more or less numbers. Have fun!";
    }
}

