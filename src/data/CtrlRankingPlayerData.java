package data;

/**
 * Ranking Player Controller of persistence layer
 */

public class CtrlRankingPlayerData extends CtrlGenericData {

    /**
     * Self instance of CtrlRankingPlayerData, corresponds to a Singleton pattern
     */
    private static  CtrlRankingPlayerData ctrlRankingPlayerData = new CtrlRankingPlayerData();

    /**
     *  Creator function of Ranking Player Controller
     */
    private CtrlRankingPlayerData() {
        super("RankingPlayer");
    }

    /**
     * Getter function of the Ranking Player Controller instance
     * @return Ranking Player Controller instance
     */
    public static CtrlRankingPlayerData getCtrlRankingPlayerData(){
        return ctrlRankingPlayerData;
    }

    /**
     * Builds a path to a file in which a {@link domain.game.RankingPlayer} object is stored
     * @param id The file identifier of a {@link domain.game.RankingPlayer} object
     * @return A path in the form of "Game/Data/{{@link #folderName}}/{id}.rank.play"
     */
    @Override
    protected String buildPath(String id) {
        return buildPathWithSuffix(id, "rank.play");
    }
}
