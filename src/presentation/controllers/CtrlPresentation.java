package presentation.controllers;

import domain.controllers.*;
import domain.game.*;
import presentation.view.*;

import data.CtrlGenericData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Domain controller, manages the presentation layer
 *
 * @author Sergi Serrano Casalins, Nicolas Gouttefangeas Ballo, Marcos Riverola Errando, Samuel Hoenle
 */
public class CtrlPresentation extends Application {

    /**
     * Domain Layer controller
     */
    private CtrlDomain ctrlDomain;

    /**
     * Main stage
     */
    private Stage primaryStage;

    /**
     * Root Layout
     */
    private BorderPane baseLayout;

    /**
     * F X     V I E W S     L I S T
     */
    private ArrayList<GenericViewFX> viewsFX;

    /**
     * View responsible of showing the ranking of an Problem
     */
    private ViewRankingAttemptFX viewRankingAttemptFX;

    /**
     * View responsible of showing the ranking of the Players in the DB
     */
    private ViewRankingPlayerFX viewRankingPlayerFX;

    /**
     * View responsible of showing the LogIn of the Player
     */
    private ViewLoginFX viewLoginFX;

    /**
     * View responsible of showing the Register of the player
     */
    private ViewRegisterFX viewRegisterFX;

    /**
     * View responsible for listing {@link domain.game.Problem}s
     */
    private ViewProblemListFX viewProblemListFX;

    /**
     * View responsible of showing the Menu
     */
    private ViewMenuFX viewMenuFX;
    
    /**
     * View responsible of showing the Settings
     */
    private ViewSettingsFX viewSettingsFX;

    /**
     * View responsible of showing the Generator interface
     */
    private ViewGenerateFX viewGenerateFX;

    /**
     * View responsible of showing the attempt interface
     */
    private ViewAttemptFX viewAttemptFX;

    /**
     * View responsible of showing the started attempts interface
     */
    private ViewAttemptListFX viewAttemptListFX;

    /**
     * Creates a new Presentation Layer controller and initializes a new Domain Layer controller and all the Views
     */
    public CtrlPresentation() throws IOException, ClassNotFoundException {
        ctrlDomain = new CtrlDomain();
        viewsFX = new ArrayList<>();
        initializePresentation();
    }

    /**
     * Initializes all the views of the controller
     */
    private void initializePresentation() {
        viewRankingAttemptFX = new ViewRankingAttemptFX(this);
        viewRankingPlayerFX = new ViewRankingPlayerFX(this);
        viewLoginFX = new ViewLoginFX(this);
        viewRegisterFX = new ViewRegisterFX(this);
        viewProblemListFX = new ViewProblemListFX(this);
        viewLoginFX = new ViewLoginFX(this);
        viewRegisterFX = new ViewRegisterFX(this);
        viewMenuFX = new ViewMenuFX(this);
        viewSettingsFX = new ViewSettingsFX(this);
        viewGenerateFX = new ViewGenerateFX(this);
        viewAttemptFX = new ViewAttemptFX(this);
        viewAttemptListFX = new ViewAttemptListFX(this);

        viewsFX.add(viewLoginFX);
        viewsFX.add(viewRankingAttemptFX);
        viewsFX.add(viewRankingPlayerFX);
        viewsFX.add(viewRegisterFX);
        viewsFX.add(viewProblemListFX);
        viewsFX.add(viewMenuFX);
        viewsFX.add(viewSettingsFX);
        viewsFX.add(viewGenerateFX);
        viewsFX.add(viewAttemptFX);
        viewsFX.add(viewAttemptListFX);

    }

    /**
     * Hides all views
     */
    private void hideAllViews() {
        for (GenericViewFX view : viewsFX)
            view.hide();
    }

    /**
     * Swaps to Settings view
     */
    public void swapSettingsFX() throws IOException {
        hideAllViews();
        swapViewFX("Settings",viewSettingsFX);
    }

    /**
     * Swaps to Menu view
     */
    public void swapMenuFX() throws IOException {
        hideAllViews();
        swapViewFX("Menu", viewMenuFX);
    }

    /**
     * Swaps to Login View
     */
    public void swapLoginFX() throws IOException {
        hideAllViews();
        swapViewFX("LogIn", viewLoginFX);
    }

    /**
     * Swaps to RankingPlayer view
     */
    public void swapRankingPlayerFX() throws IOException {
        hideAllViews();
        swapViewFX("RankingPlayer", viewRankingPlayerFX);
    }

    /**
     * Swaps to Register view
     */
    public void swapRegisterFX() throws IOException {
        hideAllViews();
        swapViewFX("Register",viewRegisterFX);
    }

    /**
     * Swaps to Problem List view
     */
    public void swapProblemListFX() throws IOException {
        hideAllViews();
        swapViewFX("ProblemList", viewProblemListFX);
    }

    /**
     * Swaps to Attempt List view
     */
    public void swapAttemptListFX() throws IOException {
        hideAllViews();
        swapViewFX("AttemptList", viewAttemptListFX);
    }

    /**
     * Swaps to Ranking attempt view
     * @param problemId the problem's identifier of the ranking
     */
    public void swapRankingAttemptFX(String problemId) throws IOException {
        hideAllViews();
        viewRankingAttemptFX.setProblemId(problemId);
        swapViewFX("RankingAttempt", viewRankingAttemptFX);
    }

    /**
     * Swaps to  Generate view
     */
    public void swapGenerateFX() throws IOException {
        hideAllViews();
        swapViewFX("Generate", viewGenerateFX);
    }

    /**
     * Swaps to Attempt view with an already started problem
     * @param id problem's identifier
     * @param attemptId attempt's identifier
     * @throws IOException Could not read from Attempt or Problem file
     * @throws ClassNotFoundException Outdated Attempt or Problem file
     */
    public void swapStartedAttemptFX(String id, String attemptId) throws IOException, ClassNotFoundException {
        hideAllViews();
        String adj = "C";
        String[] problemInfo = ctrlDomain.getDisplayableProblemInfo(id);
        if(problemInfo[1].equals("Triangle") || problemInfo[1].equals("Square")){
            if(ctrlDomain.getAdjacency(problemInfo[0])){
                adj = "CA";
            }
        }
        String[][] map = ctrlDomain.getAttemptMap(attemptId);
        int seconds = ctrlDomain.getAttemptSeconds(attemptId);
        viewAttemptFX.setProblemInfo(problemInfo, adj);
        viewAttemptFX.setAttemptInfo(attemptId, seconds, map);
        swapViewFX("Attempt", viewAttemptFX);

    }

    /**
     * Swaps to Attempt view
     * @param problemId problem's identifier
     * @throws IOException Could not read from Problem file
     * @throws ClassNotFoundException Outdated Problem file
     */
    public void swapNewAttemptFX(String problemId) throws IOException, ClassNotFoundException {
        hideAllViews();
        String adj = "C";
        String[] problemInfo = ctrlDomain.getDisplayableProblemInfo(problemId);
        if(problemInfo[1].equals("Triangle") || problemInfo[1].equals("Square")){
            if(ctrlDomain.getAdjacency(problemInfo[0])){
                adj = "CA";
            }
        }
        viewAttemptFX.setTimer(0);
        viewAttemptFX.setProblemInfo(problemInfo, adj);
        swapViewFX("Attempt", viewAttemptFX);
    }

    /**
     * Loads a view and its controller
     * @param viewName Name of the view to be loaded
     * @param controller View's controller
     */
    private void swapViewFX(String viewName, GenericViewFX controller) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(CtrlPresentation.class.getResource("/presentation/view/" + viewName + ".fxml"));
        loader.setController(controller);
        controller.setStage(primaryStage);
        BorderPane layoutFX = loader.load();
        baseLayout.setCenter(layoutFX);
        controller.display();
    }

    /**
     * Initializes the main application stage
     * @param primaryStage The first stage to be set
     * @throws IOException could not read from RootLayout
     */
    private void initializeLayout(Stage primaryStage) throws IOException {
        baseLayout = FXMLLoader.load(getClass().getResource("/presentation/view/RootLayout.fxml"));
        Scene scene = new Scene(baseLayout);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    /**
     * Initializes the main application stage
     * @param primaryStage the first stage do be displayed
     * @throws IOException could nod read from RootLayout
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("HidatoTheGame");
        initializeLayout(primaryStage);
        swapLoginFX();
    }

    /**
     * The entry point of the program
     * @param args Arguments passed to the main function
     */
    public static void main(String[] args) {
        launch(args);
    }

    /*
     * D O M A I N      C A L L S
     */

    /**
     * Saves a new player
     * @param username the player's identifier
     * @param password the player's password
     * @param path the path of the player's avatar
     * @param changed boolean to know if the default path has been modified
     * @throws IOException Could not write on the player's file
     * @throws ClassNotFoundException Outdated player file
     */
    public void savePlayer(String username, String password, String path, Boolean changed) throws IOException {
        ctrlDomain.savePlayer(username, password, path, changed);
    }

    /**
     * Checks if the player already exists
     * @param username player's identifier
     * @return returns true if the player exists, false otherwise
     */
    public boolean existsPlayer(String username) {
        return ctrlDomain.existsPlayer(username);
    }

    /**
     * Checks if the player exists the pasword is correct
     * @param username player's identifier
     * @param password player's password
     * @return returns true if the player exists and the password is correct, false otherwise
     * @throws IOException Could not read from player file
     * @throws ClassNotFoundException Outdated player file
     */
    public boolean logInPlayer(String username, String password) throws IOException, ClassNotFoundException {
        return ctrlDomain.LogginPlayer(username, password);
    }

    /**
     * Returns the name of the logged player
     * @return the name of the logged player
     */
    public String getLoggedPlayerName() {
        return ctrlDomain.getLoggedPlayerName();
    }

    /**
     * Returns a string with the player's avatar path
     * @return the player's avatar path
     */
    public String getLoggedPlayerAvatar() {
        return ctrlDomain.getLoggedPlayerAvatar();
    }

    /**
     * Sets the player's avatar path to the given one
     * @param path the given avatar player path
     */
    public void setLoggedPlayerAvatar(String path){
        ctrlDomain.setLoggedPlayerAvatar(path);
    }

    /**
     * Returns the player's password
     * @param name the player's identifier
     * @return the player's password
     * @throws IOException Could not read from player file
     * @throws ClassNotFoundException Outdated player file
     */
    public String getPlayerPassword(String name) throws IOException, ClassNotFoundException {
       return ctrlDomain.getPlayerPassword(name);
    }

    /**
     * Returns a list with the displayable info of all the existing problems
     * @return a list with the displayable info of all the existing problems
     */
    public List<String[]> getListOfAllProblems() {
        return ctrlDomain.getListOfAllProblems();
    }

    /**
     * Returns a {@link List} of selected {@link Problem}s in {@link CtrlDomain#problemCollection} in {@code String} arrays to be
     * displayed. Problems can be selected after Difficulty, Type or the Player who created them
     *
     * @param difficulties  A {@link List} of {@code String}s representing {@link Difficulty}s to filter after
     * @param types         A {@link List} of {@code String}s representing {@link Type}s to filter after
     * @param names         A {@link List} of {@code String}s representing {@link Player#name}s to filter after
     * @return  A {@link List} of an array of {@code String}s, each containing all information of all selected
     *          {@link Problem}s to be displayed.
     * @see CtrlDomain#getListOfSelectedProblems(List, List, List)
     */
    public List<String[]> getListOfSelectedProblems(List<String> difficulties, List<String> types, List<String> names) throws IllegalArgumentException{
        return ctrlDomain.getListOfSelectedProblems(difficulties, types, names);
    }

    /**
     * Returns the displayable info of the Ranking by Player
     * @return the displayable info of the Ranking by Player
     * @throws IOException could not read from RankingPlayer file
     * @throws ClassNotFoundException Outdated RankingPlayer file
     */
    public List<String> getRankingPlayer() throws IOException, ClassNotFoundException {
        return ctrlDomain.getRankingPlayer();
    }

    /**
     * Returns the displayable info of the Ranking by Attempts
     * @param id the problem's identifier
     * @return the displayable info of the Ranking by Player
     * @throws IOException could not read from RankingPlayer file
     * @throws ClassNotFoundException Outdated RankingPlayer file
     */
    public List<String> getRankingAttempt(String id) throws IOException, ClassNotFoundException {
        return ctrlDomain.getRankingAttempt(Problem.hexAsId(id));
    }

    /**
     * Generates a problem and returns the displayable problem info
     * @param type the type of the problem
     * @param adjacency the adjacency of the problem
     * @param difficulty the difficulty of the problem
     * @param rows the number of rows of the problem's map
     * @param columns the number of columns of the problem's map
     * @return the displayable info of the generated problem
     * @throws IOException Could not write in problem file
     */
    public String[] generateProblem(String type, boolean adjacency, String difficulty, int rows, int columns) throws IOException {
        return ctrlDomain.generateProblem(type,rows,columns,difficulty, adjacency);
    }

    /**
     * Returns the problem's map
     * @param problemId problem's identifier
     * @return tge problem's map
     * @throws IOException Could not read from problem file
     * @throws ClassNotFoundException Outdated problem file
     */
    public String[][] getProblemMap(String problemId) throws IOException, ClassNotFoundException {
        return ctrlDomain.getProblemMap(problemId);
    }

    /**
     * Validates if a player's solution is correct
     * @param map the player's solution
     * @param type the problem's type
     * @param adj the problem's adjacency
     * @return true if solved false otherwise
     */
    public boolean validateProblem(String[][] map, String type, String adj){
        return ctrlDomain.validateProblem(map, type, adj);
    }

    /**
     * Sets to true the path changed boolean
     */
    public void pathChanged(){
        ctrlDomain.pathChanged();
    }

    /**
     * Returns if the path has been changed
     * @return true if changed, false otherwise
     */
    public boolean isPathChanged() {
        return ctrlDomain.isPathChanged();
    }

    /**
     * Generates a personalized problem and returns the displayable info
     * @param type the type of the personalized problem
     * @param adjacency the adjacency of the personalized problem
     * @param difficulty the difficulty of the personalized problem
     * @param rows the number of rows of the personalized problem
     * @param columns the number of columns of the personalized problem
     * @param map the personalized map to be used in the generation of the problem's map
     * @return displayable problem info
     * @throws IOException could not write in Problem file
     */
    public String[] generatePersonalizedProblem(String type, boolean adjacency, String difficulty, int rows, int columns, String[][] map) throws IOException {
        return ctrlDomain.generatePersonalizedProblem(type,rows,columns,difficulty,adjacency,map);
    }

    /**
     * Returns the description of the Hidato Game
     * @return the description of the Hidato Game
     */
    public String showHelp() {
        return ctrlDomain.getHidatoDescription();
    }

    /**
     * Loads a problem from a text file. See {@link CtrlDomain#loadProblemFromPath(String)} for formatting
     * requirements.
     *
     * @param path Path to the file to be loaded
     * @return Whether or not the file has been successfully loaded and added to the problem collection
     * @throws FileNotFoundException    Thrown when the passed path does not link to a file.
     * @throws IOException              Can be thrown by {@link CtrlDomain#loadProblemFromPath(String)}
     * @throws ParseException           The file does not fulfil the format requirements.
     * @see CtrlDomain#loadProblemFromPath(String)
     */
    public String[] loadProblemFromPath(String path) throws IOException, ParseException, CtrlDomain.ProblemExistsException, CtrlDomain.ProblemNotValidException {
        return ctrlDomain.loadProblemFromPath(path);
    }

    /**
     * Updates the points of a player to keep the ranking updated
     * @param points the points to be added to the player
     * @throws IOException Could not read/write from Player/RankingPlayer file
     * @throws ClassNotFoundException Outdated Player/RankingPlayer file
     */
    public void updateRankingPlayerPoints(int points) throws IOException, ClassNotFoundException {
        String playerName = ctrlDomain.getLoggedPlayerName();
        ctrlDomain.updateRankingPlayerPoints(points, playerName);
    }

    /**
     * Saves a started attempt to be played later
     * @param id the id of the problem
     * @param solution the current solution of the problem
     * @param time spent solving the problem up to the point to be stored
     * @throws IOException Could not write on Attempt file
     * @throws ClassNotFoundException Outdated Attempt file
     */
    public void saveAttempt(String id,String[][] solution, int time) throws IOException, ClassNotFoundException {
        String playerName = ctrlDomain.getLoggedPlayerName();
        ctrlDomain.saveAttempt(id, solution, time, playerName);
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
        return ctrlDomain.getHint(solution, adjacency, columns,rows,type,lastNumber);
    }

    /**
     * Updates the RankingAttempt adding a new attempt to it
     * @param problemId solved problem's identifier
     * @param time the time spent solving the problem
     * @throws IOException  Could not write on RankingAttempt file
     * @throws ClassNotFoundException Outdated RankingAttempt file
     */
    public void updateRankingAttempt(String problemId, Integer time) throws IOException, ClassNotFoundException {
        ctrlDomain.updateRankingAttempt(problemId, time);
    }

    /**
     * Checks if the attempt exists in the file system
     * @param id The name which identifies the attempt
     * @return True if it exists. False otherwise
     * @see CtrlGenericData#exists(String)
     */
    public boolean existsAttempt(String id) {
        return ctrlDomain.existsAttempt(id);
    }

    /**
     * Delete an attempt in the folder of the currently logged-in user
     *
     * @param id The {@link Attempt#id} of an Attempt
     * @return whether or not the attempt was successfully deleted
     * @see CtrlGenericData#delete(String)
     */
    public boolean deleteAttempt(String id){
        return ctrlDomain.deleteAttempt(id);
    }

    /**
     * Returns all the displayable info from all the Attempts of the logged Player
     * @return all the displayable info of all Attempts
     * @throws IOException Could not read from Attempt file
     * @throws ClassNotFoundException Outdated Attempt file
     */
    public List<String[]> getListOfAllAttempts() throws IOException, ClassNotFoundException {
        return ctrlDomain.getAllAttempts();
    }

    /**
     * Returns the names of all {@link Difficulty}s
     * @return A {@code String} list containing the names of all {@link Difficulty}s
     * @see CtrlDomain#getAllDifficulties()
     */
    public List<String> getAllDifficulties() {
        return ctrlDomain.getAllDifficulties();
    }

    /**
     * Returns the names of all {@link Type}s
     * @return A {@code String} list containing the names of all {@link Type}s
     * @see CtrlDomain#getAllTypes()
     */
    public List<String> getAllTypes() {
        return ctrlDomain.getAllTypes();
    }

    /**
     * Returns an alphabetically sorted list of all {@link Player#name}s who created problems in this collection
     * @return An alphabetically sorted {@link List<String>} containing {@link Player#name}s
     */
    public List<String> getAllCreatorNames() {
        return ctrlDomain.getAllCreatorNames();
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
        return ctrlDomain.getSolution(solution, adjacency, columns, rows, type);
    }
}
