package domain.game;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A RankingPlayer represents a ranking of all the players that
 * have solved Hidato problems by the points they've got from
 * the player that has more points to the one that has less
 * @author Nicolas Gouttefangeas Ballo
 */
public class RankingPlayer extends Ranking implements Serializable  {
    /**
     * ProblemHolder is an inner class that stores
     * the PlayerName and the the points that the player has
     */
    private class PlayerHolder implements Serializable{
        String PlayerName;
        int points;

        /**
         * Constructs a PlayerHolder with the attributes of a given Player
         * @param p the given player that needs to be ranked
         */
        private PlayerHolder(Player p){
            this.PlayerName = p.getName();
            this.points = p.getPoints();
        }
    }

    ArrayList<PlayerHolder> rankingPlayer; // Player ranking

    /**
     * Constructs an empty rankingPlayer Array
     */
    public RankingPlayer(){
        this.rankingPlayer =  new ArrayList<>();
    }

    /**
     * Returns the rankingPlayer array
     * @return rankingPlayer
     */
    public ArrayList<PlayerHolder> getRankingPlayer() {
        return rankingPlayer;
    }

    /**
     * Returns the name of the player from a given position in the rankingPlayer array
     * @param pos the position of the player in rankingPlayer array
     * @return the name of the player in the given position
     */
    public String getPlayerName(int pos){
        return rankingPlayer.get(pos).PlayerName;
    }

    /**
     * Returns the points of the player from a given position in the rankingPlayer array
     * @param pos the position of the player in rankingPlayer array
     * @return the points of the player in the given position
     */
    public int getPlayerPoints(int pos){
        return rankingPlayer.get(pos).points;
    }

    /**
     * Adds a given player to the rankingPlayer array
     * @param p is a player that needs to be ranked
     */
    public void addRankingPlayer(Player p){
        PlayerHolder ph = new PlayerHolder(p);
        this.rankingPlayer.add(ph);
        updateRankingPlayer();
    }

    /**
     * Updates the point of a player already existing in the rankingPlayer array
     * @param p is a player that needs his points to be updated
     */
    public void updatePlayerPointsInRanking(Player p){
        PlayerHolder ph = new PlayerHolder(p);
        for (int i = 0; i < rankingPlayer.size(); i++){
            if(p.getName().equals(rankingPlayer.get(i).PlayerName)){
                rankingPlayer.set(i,ph);
            }
        }
        updateRankingPlayer();
    }

    /**
     * Updates the rankingPlayer array by using the quicksort method
     */
    private void updateRankingPlayer(){
        this.rankingPlayer = quickSortPlayer(this.rankingPlayer);
    }

    /**
     * Sorts a given array using the quickSort method comparing the points of two players.
     * @param list a given array of PlayerHolder
     * @return the given array sorted
     */
    private ArrayList<PlayerHolder> quickSortPlayer(ArrayList<PlayerHolder> list)
    {
        if (list.size() <= 1)
            return list; // Already sorted

        ArrayList<PlayerHolder> lesser = new ArrayList<>();
        ArrayList<PlayerHolder> greater = new ArrayList<>();
        PlayerHolder pivot = list.get(list.size()-1); // Use last Player as pivot
        for (int i = 0; i < list.size()-1; i++)
        {
            if (compareToPlayer(pivot, list.get(i)))
                lesser.add(list.get(i));
            else
                greater.add(list.get(i));
        }

        lesser = quickSortPlayer(lesser);
        greater = quickSortPlayer(greater);

        lesser.add(pivot);
        lesser.addAll(greater);

        return lesser;
    }

    /**
     * Compares the points of two given PlayerHolder and returns a boolean
     * @param pivot a given PlayerHolder object
     * @param actual a given PlayerHolder object
     * @return returns true if pivot's points attribute is smaller than actual's one
     */
    private boolean compareToPlayer(PlayerHolder pivot, PlayerHolder actual){
        return pivot.points < actual.points;
    }

}
