package data;

/**
 * Player controller of the Persistance Layer
 */
public class CtrlPlayerData extends CtrlGenericData{

    /**
     * Self instance of CtrlPlayerData
     */
    private static CtrlPlayerData ctrlPlayerData = new CtrlPlayerData();

    /**
     * Creates a new controller and assigns the correct folder name to it
     */
    private CtrlPlayerData() {
        super("Player");
    }

    /**
     * Getter function of the Player Controller instance
     * @return Player controller instance
     */
    public static CtrlPlayerData getCtrlPlayerData() {
        return ctrlPlayerData;
    }

    /**
     * Builds a path to a file in which a {@link domain.game.Player} object is stored
     * @param id The file identifier of a {@link domain.game.Player} object
     * @return A path in the form of "Game/Data/{{@link #folderName}}/{id}.play"
     */
    @Override
    protected String buildPath(String id) {
        return buildPathWithSuffix(id, "play");
    }
}