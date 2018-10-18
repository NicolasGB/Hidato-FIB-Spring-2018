package domain.game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  A RankingAttempt represents a ranking of all the solved attempts
 *  that are made in a same problem ordering them by the best time to
 *  the worse.
 *  @author Nicolas Gouttefangeas Ballo
 */
public class RankingAttempt extends Ranking implements Serializable{
    /**
     * AttemptHolder is an inner class that stores
     * the ProblemName, PlayerName and the time
     * of each attempt.
     */
    private class AttemptHolder implements Serializable{
        int ProblemName;
        String PlayerName;
        int time;

        /**
         * Constructs an AttemptHolder with the attribute of the given attempt
         * @param a is an Attempt done by a player that needs to be ranked
         */
        public AttemptHolder(Attempt a){
            this.ProblemName = a.getAttemptProblem();
            this.PlayerName = a.getAttemptPlayer();
            this.time = a.getAttemptSeconds();
        }
    }

    ArrayList<AttemptHolder> rankingAttempt;  // Attempt ranking

    /**
     * Constructs an empty rankingAttempt array
     */
    public RankingAttempt(){
        this.rankingAttempt = new  ArrayList<>();
    }

    /**
     * Returns the rankingAttempt array
     * @return rankingAttempt
     */
    public ArrayList<AttemptHolder> getRankingAttempt() {
        return rankingAttempt;
    }

    /**
     * Returns the rankingAttempt from a given problem
     * @param problemId the problemId from the problem the user wants the ranking from
     * @return an ArrayList of strings, in each even position of the array there is the name of the player
     * and the following odd position has its time.
     */
    public ArrayList<String> getRankingFromProblem(int problemId){
        ArrayList<String> players = new ArrayList<>();
        for (int i = 0; i < rankingAttempt.size(); i++) {
            AttemptHolder ah = rankingAttempt.get(i);
            if( ah.ProblemName == problemId){
                players.add(ah.PlayerName);
                players.add(String.valueOf(ah.time));
            }
        }
        return players;
    }
    /**
     * Adds an attempt to the rankingAttempt array
     * @param a is an attempt that needs to be ranked
     */
    public void addRankingAttempt(Attempt a){
        AttemptHolder ath = new AttemptHolder(a);
        this.rankingAttempt.add(ath);
        updateRankingAttempt();
    }

    /**
     * Updates the rankingAttempt array by using the quicksort method
     */
    private void updateRankingAttempt(){
        this.rankingAttempt = quickSort(this.rankingAttempt);
    }

    /**
     * Sorts a given array using the quickSort method comparing the times of two attempts.
     * @param list a given array of AttemptHolder
     * @return returns the given array sorted
     */
    private ArrayList<AttemptHolder> quickSort(ArrayList<AttemptHolder> list)
    {
        if (list.size() <= 1)
            return list; // Already sorted

        ArrayList<AttemptHolder> lesser = new ArrayList<>();
        ArrayList<AttemptHolder> greater = new ArrayList<>();
        AttemptHolder pivot = list.get(list.size()-1); // Use last Attempt as pivot
        for (int i = 0; i < list.size()-1; i++)
        {
            if (compareToAttempt(pivot, list.get(i)))
                lesser.add(list.get(i));
            else
                greater.add(list.get(i));
        }

        lesser = quickSort(lesser);
        greater = quickSort(greater);

        lesser.add(pivot);
        lesser.addAll(greater);

        return lesser;
    }

    /**
     * Compares the time of two given AttemptHolder objects and returns a boolean
     * @param pivot a given AttemptHolder object
     * @param actual a given AttemptHolder object
     * @return returns true if pivot's time attribute is bigger than actual's one
     */
    private boolean compareToAttempt(AttemptHolder pivot, AttemptHolder actual){
        return pivot.time > actual.time;
    }
}
