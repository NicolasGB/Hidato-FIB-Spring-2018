package domain.controllers;

import data.*;
import domain.game.*;
import domain.logic.Generator;
import domain.logic.Solver;
import domain.logic.Validator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Domain controller, manages the domain layer
 *
 * @author Sergi Serrano Casalins, Nicolas Gouttefangeas Ballo, Marcos Riverola Errando, Samuel Hoenle
 */
public class CtrlDomain {

    /**
     * An instance of the Player data controller
     */
    private CtrlPlayerData ctrlPlayerData;

    /**
     * An instance of the attempt data controller
     */
    private CtrlAttemptData ctrlAttemptData;

    /**
     * An instance of the Problem data controller
     */
    private CtrlProblemData ctrlProblemData;

    /**
     * An instance of the RankingAttempt data controller
     */
    private CtrlRankingAttemptData ctrlRankingAttemptData;

    /**
     * An instance of the Ranking Player data controller
     */
    private CtrlRankingPlayerData ctrlRankingPlayerData;

    /**
     * The logged in player
     */
    private Player loggedPlayer;

    /**
     * An instance of ranking by Attempts
     */
    private RankingAttempt rankingAttempt;

    /**
     * An instance of the ranking  by Players
     */
    private RankingPlayer rankingPlayer;

    /**
     * An instance of the actual problem collection
     */
    private ProblemCollection problemCollection;

    /**
     * Constructor for {@link CtrlDomain} initialising all controller attributes and loading all problems
     * (using {@link #loadAllProblems()}).
     *
     * @throws IOException              Could not read the data from the file because it is either locked or it does not
     *                                  have enough permissions
     *                                  (in {@link CtrlGenericData#get(String)} and {@link #loadAllProblems()}).
     * @throws ClassNotFoundException   The class of the stored object does not exist
     *                                  (in {@link CtrlGenericData#get(String)} and {@link #loadAllProblems()}).
     */
    public CtrlDomain() throws ClassNotFoundException, IOException {

        //Create the folder's structure
        ctrlPlayerData = CtrlPlayerData.getCtrlPlayerData();
        ctrlAttemptData = new CtrlAttemptData("");
        ctrlProblemData = CtrlProblemData.getCtrlProblemData();
        ctrlRankingAttemptData = CtrlRankingAttemptData.getCtrlRankingAttemptData();
        ctrlRankingPlayerData = CtrlRankingPlayerData.getCtrlRankingPlayerData();

        loggedPlayer = null;

        if (ctrlRankingAttemptData.exists("RankingAttempt")) {
            rankingAttempt = (RankingAttempt) ctrlRankingAttemptData.get("RankingAttempt");
        }
        else {
            rankingAttempt = new RankingAttempt();
        }
        if (ctrlRankingPlayerData.exists("RankingPlayer")) {
            rankingPlayer = (RankingPlayer) ctrlRankingPlayerData.get("RankingPlayer");
        }
        else {
            rankingPlayer = new RankingPlayer();
        }

        loadAllProblems();
    }

    /**
     * Loads all problems from the standard location using {@link CtrlProblemData#getAll()}
     *
     * @throws IOException              Could not read the data from the file because it is either locked or it does not
     *                                  have enough permissions
     * @throws ClassNotFoundException   The class of the stored object does not exist
     *
     * @see CtrlGenericData#getAll()
     */
    public void loadAllProblems() throws IOException, ClassNotFoundException {
        problemCollection = new ProblemCollection();
        List<Object> loadedProblems = ctrlProblemData.getAll();
        for (Object o : loadedProblems) {
            if (o instanceof Problem) problemCollection.add((Problem) o);
        }
    }

    /**
     * Returns a {@link List} of all {@link Problem}s in {@link #problemCollection} in {@link String} arrays to be
     * displayed.
     *
     * @return  A {@link List} of an array of {@link String}s, each containing all information of a {@link Problem} to
     *          be displayed.
     * @see #listOfProblems(Collection)
     */
    public List<String[]> getListOfAllProblems(){
        return listOfProblems(problemCollection);
    }

    /**
     * Returns a {@link List} of selected {@link Problem}s in {@link #problemCollection} in {@code String} arrays to be
     * displayed. Problems can be selected after Difficulty, Type or the Player who created them
     *
     * @param difficulties  A {@link List} of {@code String}s representing {@link Difficulty}s to filter after
     * @param types         A {@link List} of {@code String}s representing {@link Type}s to filter after
     * @param names         A {@link List} of {@code String}s representing {@link Player#name}s to filter after
     * @return  A {@link List} of an array of {@code String}s, each containing all information of all selected
     *          {@link Problem}s to be displayed.
     * @see #listOfProblems(Collection)
     */
    public List<String[]> getListOfSelectedProblems(List<String> difficulties, List<String> types, List<String> names)
            throws IllegalArgumentException {
        List<Difficulty> d = difficulties.stream().map((dif) -> Difficulty.valueOf(dif)).collect(Collectors.toList());
        List<Type> t = types.stream().map((type) -> Type.valueOf(type)).collect(Collectors.toList());
        return listOfProblems(problemCollection.select(d,t,names));
    }

    /**
     * Returns a {@link List} of {@link String} arrays, each containing the information of a {@link Problem} object to
     * be displayed.
     *
     * @param list The list of {@link Problem}s to be displayed
     * @return A list of {@code String} arrays generated by {@link #displayableProblemInfo(Problem)}
     * @see #displayableProblemInfo(Problem)
     */
    private List<String[]> listOfProblems(Collection<Problem> list){
        List<String[]> out = new ArrayList<>();
        int i = 0;
        for (Problem p : list) {
            out.add(displayableProblemInfo(p));
            i++;
        }
        return out;
    }

    /**
     * A {@code String} array containing the information of a {@link Problem} object to be displayed.
     * The format is:  [{@link Integer#toHexString(int)} of {@link Problem#getId()} as identifier,
     *                  {@link Type#name()} of {@link Problem#getType()},
     *                  {@link Problem#getNumRows()} as String,
     *                  {@link Problem#getNumColumns()} as String,
     *                  {@link Difficulty#name()} of {@link Problem#getDifficulty()},
     *                  {@link Problem#getCreatorName()}]
     *
     * @param p The {@link Problem} of which the info should be displayed
     * @return A {@code String} array of 6 fields
     */
    private String[] displayableProblemInfo(Problem p) {
        String[] curr = new String[6];
        curr[0] = Problem.idAsHex(p.getId());
        curr[1] = p.getType().name();
        curr[2] = Integer.toString(p.getNumRows());
        curr[3] = Integer.toString(p.getNumColumns());
        curr[4] = p.getDifficulty().name();
        curr[5] = p.getCreatorName();
        return curr;
    }

    /**
     * A {@code String} array containing the information of a {@link Problem} object to be displayed.
     * The format is:  [{@link Integer#toHexString(int)} of {@link Problem#getId()} as identifier,
     *                  {@link Type#name()} of {@link Problem#getType()},
     *                  {@link Problem#getNumRows()} as String,
     *                  {@link Problem#getNumColumns()} as String,
     *                  {@link Difficulty#name()} of {@link Problem#getDifficulty()},
     *                  {@link Problem#getCreatorName()}]
     *
     * @param problemId The id of a {@link Problem} as its hexadecimal representation (see {@link Problem#idAsHex(int)}
     * @return A {@code String} array of 6 fields
     * @throws IOException  Could not read the data from the file because it is either locked or it does not have enough
     *                      permissions
     * @throws ClassNotFoundException The file can not be loaded
     * @see #displayableProblemInfo(Problem) 
     */
    public String[] getDisplayableProblemInfo(String problemId) throws IOException, ClassNotFoundException {
        return displayableProblemInfo((Problem) ctrlProblemData.get(problemId));
    }

    /**
     * Updates the points of a given player in the Ranking by Players
     * @param points The points to be added to the Player
     * @param playerName The player's identifier
     * @throws IOException Could not read from either RankingPlayer or Player file our could not write on RankingPlayer file
     * @throws ClassNotFoundException Outdated RankingPlayer or Player file
     */
    public void updateRankingPlayerPoints(int points, String playerName) throws IOException, ClassNotFoundException {
         RankingPlayer rp = (RankingPlayer) ctrlRankingPlayerData.get("RankingPlayer");
         Player p = (Player) ctrlPlayerData.get(playerName);
         p.updatePoints(points);
         ctrlPlayerData.save(playerName, p);
         rp.updatePlayerPointsInRanking(p);
         ctrlRankingPlayerData.save("RankingPlayer", rp);
    }

    /**
     * Saves a started Attempt in order to be able to play it later
     * @param id problem's identifier
     * @param solution the current solution of the problem done by the player
     * @param time the time already spent of a player solving a Problem
     * @param playerName the player's identifier
     * @throws IOException Could not read from Problem file or could not write on Attempt file
     * @throws ClassNotFoundException
     */
    public String saveAttempt(String id, String[][] solution, int time, String playerName) throws IOException, ClassNotFoundException {
        Problem p = (Problem) ctrlProblemData.get(id);
        Attempt a = new Attempt(playerName, p);
        a.setSolution(solution);
        a.setAttemptSeconds(time);
        ctrlAttemptData.save(Attempt.idAsHex(a.getId()),a);
        return Attempt.idAsHex(a.getId());
    }

    /**
     * Delete an attempt in the folder of the currently logged-in user
     *
     * @param id The {@link Attempt#id} of an Attempt
     * @return whether or not the attempt was successfully deleted
     * @see CtrlGenericData#delete(String)
     */
    public boolean deleteAttempt(String id) {
        return ctrlAttemptData.delete(id);
    }

    /**
     * Checks if the attempt exists in the file system
     * @param id The name which identifies the attempt
     * @return True if it exists. False otherwise
     * @see CtrlGenericData#exists(String)
     */
    public boolean existsAttempt(String id) {
        return ctrlAttemptData.exists(id);
    }

    /**
     *  Returns the position and the number of the next step of a started player Attempt, empty string if no solution found;
     * @param solution the current player's solution to a problem
     * @param adjacency the adjacency attribute of the problem being solved
     * @param columns the number of columns of the solution
     * @param rows the number of rows of the solution
     * @param type the type attribute of the problem being solved
     * @param lastNumber the last number added to the solution by the player
     * @return The x and y position of the following number of a solution if solvable, empty string otherwise
     */
    public String[] getHint(String[][] solution, String adjacency, int columns, int rows, String type, int lastNumber) {
        Problem p;
        boolean adj = false;
        if (adjacency.equals("CA")){
            adj = true;
        }
        if(type.equals("Square")){
            p = new SquareProblem(solution, rows, columns, Difficulty.Medium, "hint",adj);
        }
        else if(type.equals("Triangle")){
            p = new TriangleProblem(solution, rows, columns, Difficulty.Medium, "hint",adj);
        }
        else{
            p = new HexagonProblem(solution, rows, columns, Difficulty.Medium, "hint");
        }
        Solver s =  new Solver();
        String map[][] = s.solve(p);
        if(map == null){
            return new String[0];
        }
        else {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (map[i][j].equals(String.valueOf((lastNumber + 1)))) {
                        String info[] = new String[2];
                        info[0] = String.valueOf(i);
                        info[1] = String.valueOf(j);
                        return info;
                    }
                }
            }
            //Should never be here
            return new String[0];
        }
    }

    /**
     * Updates the ranking by attempt of a problem
     * @param problemId the problem's identifier
     * @param time the time of the player spent doing the solution
     * @throws IOException Could not read from Problem file, could not read from RankingAttempt file
     *         cor could not write in RankingAttempt file
     * @throws ClassNotFoundException Outdated RankingAttempt file or Player file
     */
    public void updateRankingAttempt(String problemId, Integer time) throws IOException, ClassNotFoundException {
        String name = loggedPlayer.getName();
        Problem p = (Problem) ctrlProblemData.get(problemId);
        Attempt a = new Attempt(name,p);
        a.setAttemptSeconds(time);
        if(ctrlRankingAttemptData.exists("RankingAttempt")){
            rankingAttempt = (RankingAttempt) ctrlRankingAttemptData.get("RankingAttempt");
            rankingAttempt.addRankingAttempt(a);
            ctrlRankingAttemptData.save("RankingAttempt",rankingAttempt);
        }
        else{
            rankingAttempt = new RankingAttempt();
            rankingAttempt.addRankingAttempt(a);
            ctrlRankingAttemptData.save("RankingAttempt", rankingAttempt);
        }

    }

    /**
     * Gets the saved map in a specified attempt
     * @param attemptId The id of the attempt to be loaded
     * @return The map saved in the attempt
     * @throws IOException  Could not read the data from the file because it is either locked or it does not have enough
     *                      permissions
     * @throws ClassNotFoundException The file can not be loaded
     */
    public String[][] getAttemptMap(String attemptId) throws IOException, ClassNotFoundException {
        Attempt a = (Attempt) ctrlAttemptData.get(attemptId);
        return a.getSolution();
    }

    /**
     * Returns a matrix with the solution to the current attempt of the player
     * @param solution the current started solution of a problem by a player
     * @param adjacency adjacency used in the problem
     * @param columns number of columns of the problem's map
     * @param rows number of rows of the problem's map
     * @param type the type of the problem
     * @return the solution of the started attempt
     */
    public String[][] getSolution(String[][] solution, String adjacency, int columns, int rows, String type) {
        Problem p;
        boolean adj = false;
        if (adjacency.equals("CA")){
            adj = true;
        }
        if(type.equals("Square")){
            p = new SquareProblem(solution, rows, columns, Difficulty.Medium, "hint",adj);
        }
        else if(type.equals("Triangle")){
            p = new TriangleProblem(solution, rows, columns, Difficulty.Medium, "hint",adj);
        }
        else{
            p = new HexagonProblem(solution, rows, columns, Difficulty.Medium, "hint");
        }

        Solver s =  new Solver();
        String map[][] = s.solve(p);
        if(map == null){
            return new String[0][0];
        }
        else {
            return map;
        }
    }

    /**
     * An exception to flag that it was tried to add a duplicate {@link Problem}to the {@link #problemCollection}
     */
    public class ProblemExistsException extends Exception{};

    /**
     * An exception to flag that a problem is not valid
     */
    public class ProblemNotValidException extends Exception{};

    /**
     * Reads a Hidato problem from a file and parses it into a {@link Problem} instance. The format of the file is:
     *
     * |Type|Adjacency|#Rows|#Columns
     *
     * Q,CA,3,4
     * #,1,?,#
     * ?,?,?,?
     * 7,?,9,#
     *
     * Q=Square, T=Triangle, H=Hexagon
     * C=Faces, CA=Faces and Edges
     *
     * Various checks are performed and the {@link Difficulty} is determined with {@link Problem#determineDifficulty(String[][])}.
     *
     * @param path          The path to a text file containing an Hidato in the format above.
     * @return An instance of {@link Problem} corresponding to the file passed.
     * @throws FileNotFoundException    Thrown when the passed path does not link to a file.
     * @throws IOException              Can be thrown by {@link BufferedReader#readLine()}
     * @throws ParseException           The file does not fulfil the format requirements.
     */
    public String[] loadProblemFromPath(String path) throws FileNotFoundException, IOException, ParseException, ProblemExistsException, ProblemNotValidException {
        Problem p;
        String creatorName = loggedPlayer.getName();

        BufferedReader br = new BufferedReader(new FileReader(path));
        String line = br.readLine();
        String[] firstLine = line.split(",");
        if (firstLine.length != 4) throw new ParseException("Wrong length of first line", 0);

        if (!(firstLine[1].equals("C") || firstLine[1].equals("CA")))
            throw new ParseException("Unrecognised adjacency type", 0);

        int numRows = Integer.parseInt(firstLine[2]);
        int numColumns = Integer.parseInt(firstLine[3]);

        String[][] map = new String[numRows][numColumns];

        line = br.readLine();
        String[] gameLine;
        int c = 0;

        while (line != null) {
            gameLine = line.split(",");
            map[c++] = gameLine;
            line = br.readLine();
        }

        try {
            switch (firstLine[0]) {
                case "Q":
                    p = new SquareProblem(map, numRows, numColumns, Problem.determineDifficulty(map), creatorName, firstLine[1].toUpperCase().equals("CA"));
                    break;
                case "T":
                    p = new TriangleProblem(map, numRows, numColumns, Problem.determineDifficulty(map), creatorName, firstLine[1].toUpperCase().equals("CA"));
                    break;
                case "H":
                    p = new HexagonProblem(map, numRows, numColumns, Problem.determineDifficulty(map), creatorName);
                    break;
                default:
                    throw new ParseException("Unknown Hidato type", 0);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid rows or columns", 0);
        } catch (IllegalArgumentException e) {
            throw new ParseException("Invalid argument passed", 0);
        }

        Solver s = new Solver();
        if (s.solve(p) == null) throw new ProblemNotValidException();
        for (Problem item :
                problemCollection) {
            if (item.getId() == p.getId()) {
                throw new ProblemExistsException();
            }
        }
        problemCollection.add(p);
        ctrlProblemData.save(Problem.idAsHex(p.getId()), p);
        return displayableProblemInfo(p);
    }

    /**
     * Generates a new {@link Problem} with the parameters passed.
     *
     * @param type              A {@code String} corresponding with the name of a {@link Type}
     * @param numRows           The number of rows the new game should have
     * @param numColumns        The number of columns the new game should have
     * @param difficulty        A {@code String} corresponding with the name of a {@link Difficulty}
     * @param cornerAdjacent    Whether fields sharing a corner should be counted as neighbours or only those sharing a
     *                          face (not relevant for {@link HexagonProblem}s).
     * @return  The displayable information of the generated {@link Problem} as generated by
     *          {@link #displayableProblemInfo(Problem)}, or an empty {@code String} array if adding the new Problem to
     *          {@link #problemCollection} failed.
     * @throws IllegalArgumentException Is thrown if one of the argumets is in the wrong format. Should be caught.
     * @see Generator#generate(Type, int, int, Difficulty, String, boolean)
     */
    public String[] generateProblem(String type, int numRows, int numColumns, String difficulty, boolean cornerAdjacent)
            throws IllegalArgumentException, IOException {
        try {
            Type.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unrecognised type", e);
        }
        try {
            Difficulty.valueOf(difficulty);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unrecognised difficulty", e);
        }
        Problem p = Generator.generate(Type.valueOf(type), numRows, numColumns, Difficulty.valueOf(difficulty),
                loggedPlayer.getName(), cornerAdjacent);
                ctrlProblemData.save(Problem.idAsHex(p.getId()), p);
        if (problemCollection.add(p)) {
            return displayableProblemInfo(p);
        }
        return new String[0];
    }

    /**
     *
     * @param type A {@code String} corresponding with the name of a {@link Type}
     * @param numRows           The number of rows the new game should have
     * @param numColumns        The number of columns the new game should have
     * @param difficulty        A {@code String} corresponding with the name of a {@link Difficulty}
     * @param cornerAdjacent    Whether fields sharing a corner should be counted as neighbours or only those sharing a
     *                          face (not relevant for {@link HexagonProblem}s).
     * @param map A map to be used in the problem generation
     * @return  The displayable information of the generated {@link Problem} as generated by
     *          {@link #displayableProblemInfo(Problem)}, or an empty {@code String} array if adding the new Problem to
     *          {@link #problemCollection} failed.
     * @throws IllegalArgumentException  Is thrown if one of the argumets is in the wrong format. Should be caught.
     * @throws IOException Could not read from a Problem file
     */
    public String[] generatePersonalizedProblem(String type, int numRows, int numColumns, String difficulty, boolean cornerAdjacent, String[][] map)
                throws IllegalArgumentException, IOException {
        try {
            Type.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unrecognised type", e);
        }
        try {
            Difficulty.valueOf(difficulty);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unrecognised difficulty", e);
        }
        Generator.setPersonalized(true);
        Problem p = Generator.generate(Type.valueOf(type), numRows, numColumns, Difficulty.valueOf(difficulty),
                loggedPlayer.getName(), cornerAdjacent);
        List<String> numbers = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                Generator.addToProblem(p, i, j, map[i][j]);
                if(!map[i][j].equals("?") && !map[i][j].equals("#") && !map[i][j].equals("*"))  {
                    numbers.add(map[i][j]);
                }
            }
        }

        try{
            p = Generator.generatePersonalizedProblem(p, numbers);
            if(p != null){
                ctrlProblemData.save(Integer.toHexString(p.getId()), p);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Problem", e);
        }
        Generator.setPersonalized(false);
        if (p != null && problemCollection.add(p)) {
            return displayableProblemInfo(p);
        }
        return new String[0];
    }

    /**
     * Returns the map template from a problem identified by it's id
     * @param problemId the problem identifier
     * @return the map themplate
     * @throws IOException Could not read from Problem file
     * @throws ClassNotFoundException Outdated Problem file
     */
    public String[][] getProblemMap(String problemId) throws IOException, ClassNotFoundException {
        Problem p = (Problem) ctrlProblemData.get(problemId);
        return p.getMap();
    }

    /**
     * Returns the adjacency attribute from a problem identified by it's id
     * @param problemId the problem identifier
     * @return the adjacency of the problem true if CA , false otherwise
     * @throws IOException Could not read from Problem file
     * @throws ClassNotFoundException Outdated Problem file
     */
    public boolean getAdjacency(String problemId) throws IOException, ClassNotFoundException {
        Problem p = (Problem) ctrlProblemData.get(problemId);
        if (p instanceof TriangleProblem){
            TriangleProblem  tp = (TriangleProblem) p;
            return tp.isCornerAdjacent();
        }
        else if (p instanceof SquareProblem) {
            SquareProblem sp = (SquareProblem) p;
            return sp.isCornerAdjacent();
        }
        return false;
    }

    /**
     * Sets the new avatar Path for the logged player
     * @param path The new path
     * @throws IOException The path could not be read
     */
    public void setAvatar(String path) {
        loggedPlayer.setAvatar(path);
    }

    /**
     * Returns the logged player name
     * @return logged player name
     */
    public String getLoggedPlayerName(){
        return loggedPlayer.getName();
    }

    /**
     * Returns an object of the player with the username from the params
     * @param username The usernme of the player we want
     * @return Player object wiht the username from the params
     * @throws IOException Could not read from the player File
     * @throws ClassNotFoundException Outdated Player File
     */
    private Player getPlayer(String username) throws IOException, ClassNotFoundException {
        return (Player)ctrlPlayerData.get(username);
    }

    /**
     * Returns the password of the player with the username from the params
     * @param username The username of the player we want the password
     * @return Password from the username in params
     * @throws IOException Could not read from the Player File
     * @throws ClassNotFoundException Outdated Player File
     */
    public String getPlayerPassword(String username) throws IOException, ClassNotFoundException {
        return getPlayer(username).getPassword();
    }

    /**
     * Returns the path of the logged player
     * @return Path of the logged player
     */
    public String getLoggedPlayerAvatar() {
        return loggedPlayer.getAvatar();
    }

    /**
     * Returns the Hidato description
     * @return The Hidato description
     */
    public String getHidatoDescription() {
        return loggedPlayer.askForHelp();
    }

    /**
     * Get the seconds stored in an attempt
     * @param attemptId The id of the Attempt to be read
     * @return  The seconds stored in the Attempt
     * @throws IOException  Could not read the data from the file because it is either locked or it does not have enough
     *                      permissions
     * @throws ClassNotFoundException The file can not be loaded
     */
    public int getAttemptSeconds(String attemptId) throws IOException, ClassNotFoundException {
        Attempt a = (Attempt) ctrlAttemptData.get(attemptId);
        return a.getAttemptSeconds();
    }

    /**
     * Sets the new logged player avatar path
     * @param path The new path
     */
    public void setLoggedPlayerAvatar(String path){
        loggedPlayer.setAvatar(path);
    }

    /**
     * Changes the value of pathChanged boolean
     */
    public void pathChanged() {
        loggedPlayer.setPathChanged();
    }

    /**
     * Returns a boolean that indicates if the path of the avatar is changed
     * @return Boolean PathChanged
     */
    public boolean isPathChanged(){
        return loggedPlayer.isPathChanged();
    }

    /**
     * If the username does not exist on the Player one creates a new player with the atributes in param
     * Else overrides the atributs of the Player with the respective username
     * @param username The username of the player
     * @param password The password of the Player
     * @param path The path of the avatar of the Player
     * @param changed The boolean that shows if the avatar has benn changed
     * @throws IOException Could not write in the Player File
     * @throws ClassNotFoundException Outdated Player File
     */
    public void savePlayer(String username, String password, String path, Boolean changed) throws IOException {
        Player p = new Player(username,password,path,changed);
        ctrlPlayerData.save(username,p);
        rankingPlayer.addRankingPlayer(p);
        ctrlRankingPlayerData.save("RankingPlayer", rankingPlayer );
        //addPlayerToRanking(p);
    }

    /**
     * Returns a boolean that checks if the player with the username from the param exists
     * @param username The username of the player we want to check
     * @return True if the player exists else otherwise
     */
    public boolean existsPlayer(String username) {
        return ctrlPlayerData.exists(username);
    }

    /**
     * Returns a boolean that checks if the player could be logged and sets the logged player
     * @param username The player we want to log in
     * @param password The password of the player we want to log in
     * @return True if it was possible to log the player in, false otherwise
     * @throws IOException Could not read from the Player File
     * @throws ClassNotFoundException Outdated Player FIle
     */
    public boolean LogginPlayer(String username, String password) throws IOException, ClassNotFoundException {
        Player player = getPlayer(username);
        if(player.getPassword().equals(password)) {
            loggedPlayer = player;
            ctrlAttemptData = new CtrlAttemptData(loggedPlayer.getName());
            return true;
        }
        return false;
    }

    /**
     * Gets a RankingPlayer previously stored in the system
     * @return an ArrayList of strings, in each even position of the array there is the name of the player
     * and the following odd position has its points.
     * @throws ClassNotFoundException Outdated RankingPlayer file
     * @throws IOException Couldn't read the RankingPlayer file
     */
    public ArrayList<String> getRankingPlayer() throws IOException, ClassNotFoundException {
        ArrayList<String> result = new ArrayList<>();
        if(ctrlRankingPlayerData.exists("RankingPlayer")) {
            rankingPlayer = (RankingPlayer) ctrlRankingPlayerData.get("RankingPlayer");
            int size = rankingPlayer.getRankingPlayer().size();
            for (int i = 0; i < size ; i++) {
                result.add(rankingPlayer.getPlayerName(i));
                result.add(String.valueOf(rankingPlayer.getPlayerPoints(i)));
            }
        }
        return result;
    }

    /**
     * Gets a RankingAttempt previously stored in the system
     * @return an ArrayList of strings, in each even position of the array there is the name of the player
     * and the following odd position has its time.
     * @throws ClassNotFoundException Outdated RankingAttempt file
     * @throws IOException Couldn't read the RankingAttempt file
     */
    public ArrayList<String> getRankingAttempt(int id) throws IOException, ClassNotFoundException {
        ArrayList<String> result = new ArrayList<>();
        if(ctrlRankingAttemptData.exists("RankingAttempt")) {
            rankingAttempt = (RankingAttempt) ctrlRankingAttemptData.get("RankingAttempt");
            ArrayList<String> rap = rankingAttempt.getRankingFromProblem(id);
            int size = rap.size();
            for (int i = 1; i < size ; i+=2) {
                result.add(rap.get(i - 1));
                result.add(rap.get(i));
            }
        }
        return result;
    }

    /**
     * Validates if a problem solution is correct.
     * @param map The user's solution
     * @param type The type of the problem
     * @param adj The adjacency used in the problem
     * @return True if correct false otherwise
      */
    public boolean validateProblem(String[][] map, String type, String adj){
        return Validator.validate(map,type,adj);
    }

    /**
     * Returns all the displayable info from all the Attempts of the logged Player
     * @return all the displayable info of all Attempts
     * @throws IOException Could not read from Attempt file
     * @throws ClassNotFoundException Outdated Attempt file
     */
    public List<String[]> getAllAttempts() throws IOException, ClassNotFoundException {
        List<Object> llistAttempt = ctrlAttemptData.getAll();
        List<String[]> result = new ArrayList<>();
        for (Object aLlistAttempt : llistAttempt) {
            Attempt a = (Attempt) aLlistAttempt;
            String[] temp = getDisplayableProblemInfoAttempt(Attempt.idAsHex(a.getAttemptProblem()),a);
            result.add(temp);
        }
        return result;
    }

    /**
     * Returns the info from a problem in a way that allows it to be displayed and the attempt attached to it
     * @param p a Problem
     * @param a an Attempt
     * @return the info to be displayed in a String array
     */
    private String[] displayableProblemInfoAttempt(Problem p, Attempt a) {
        String[] curr = new String[7];
        curr[0] = Attempt.idAsHex(a.getId());
        curr[1] = Problem.idAsHex(p.getId());
        curr[2] = p.getType().name();
        curr[3] = Integer.toString(p.getNumRows());
        curr[4] = Integer.toString(p.getNumColumns());
        curr[5] = p.getDifficulty().name();
        curr[6] = p.getCreatorName();
        return curr;
    }

    /**
     * Returns the info from a problem of an Attempt
     * @param problemId a Problem identifier
     * @param a an Attempt
     * @return the info to be displayed in a String array
     * @throws IOException Could not read from Problem file
     * @throws ClassNotFoundException Outdated Problem file
     */
    private String[] getDisplayableProblemInfoAttempt(String problemId, Attempt a) throws IOException, ClassNotFoundException {
        return displayableProblemInfoAttempt((Problem) ctrlProblemData.get(problemId), a);
    }

    /**
     * Returns the names of all {@link Difficulty}s
     * @return A {@code String} List containing the names of all {@link Difficulty}s
     */
    public List<String> getAllDifficulties() {
        Difficulty[] difficulties = Difficulty.values();
        List<String> result = new ArrayList<>();
        for (Difficulty d : difficulties) {
            result.add(d.name());
        }
        return result;
    }

    /**
     * Returns the names of all {@link Type}s
     * @return A {@code String} List containing the names of all {@link Type}s
     */
    public List<String> getAllTypes(){
        Type[] types = Type.values();
        List<String> result = new ArrayList<>();
        for (Type t : types) {
            result.add(t.name());
        }
        return result;
    }

    /**
     * Returns an alphabetically sorted list of all {@link Player#name}s who created problems in this collection
     * @return An alphabetically sorted {@link List<String>} containing {@link Player#name}s
     */
    public List<String> getAllCreatorNames() {
        return problemCollection.getAllCreatorNames();
    }
}
