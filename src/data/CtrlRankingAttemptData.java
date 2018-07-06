package data;

/**
 * Ranking Attempt Controller of persistence layer
 */

public class CtrlRankingAttemptData extends CtrlGenericData {

    /**
     * Self instance of CtrlRankingAttemptData, corresponds to a Singleton pattern
     */
    private static  CtrlRankingAttemptData ctrlRankingAttemptData = new CtrlRankingAttemptData();

    /**
     *  Creator function of Ranking Attempt Controller
     */
    private CtrlRankingAttemptData() {
        super("RankingAttempt");
    }

    /**
     * Getter function of the Ranking Player Controller instance
     * @return Ranking Player Controller instance
     */
    public static CtrlRankingAttemptData getCtrlRankingAttemptData(){
        return ctrlRankingAttemptData;
    }

    /**
     * Builds a path to a file in which a {@link domain.game.RankingAttempt} object is stored
     * @param id The file identifier of a {@link domain.game.RankingAttempt} object
     * @return A path in the form of "Game/Data/{{@link #folderName}}/{id}.rank.att"
     */
    @Override
    protected String buildPath(String id) {
        return buildPathWithSuffix(id, "rank.att");
    }
}
