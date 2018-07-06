package data;

/**
 * Attempt controller of the Persistance Layer
 */
public class CtrlAttemptData extends CtrlGenericData{

    /**
     * Creator function of Attempt Controller
     */
    public CtrlAttemptData(String playerName) {
        super("Attempt/" + playerName);
    }

    /**
     * Builds a path to a file in which a {@link domain.game.Attempt} object is stored
     * @param id The file identifier of a {@link domain.game.Attempt} object
     * @return A path in the form of "Game/Data/{{@link #folderName}}/{id}.att"
     */
    @Override
    protected String buildPath(String id) {
        return buildPathWithSuffix(id, "att");
    }


}