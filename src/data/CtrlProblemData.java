package data;

/**
 * Problem controller of the Persistance Layer
 */
public class CtrlProblemData extends CtrlGenericData{

    /**
     * Self instance of CtrlProblemData
     */
    private static CtrlProblemData ctrlProblemData = new CtrlProblemData();

    /**
     * Creator function of Problem Controller
     */
    private CtrlProblemData() {
        super("Problem");
    }

    /**
     * Getter function of the Problem Controller instance
     * @return Problem controller instance
     */
    public static CtrlProblemData getCtrlProblemData() {
        return ctrlProblemData;
    }

    /**
     * Builds a path to a file in which a {@link domain.game.Problem} object is stored
     * @param id The file identifier of a {@link domain.game.Problem} object
     * @return A path in the form of "Game/Data/{{@link #folderName}}/{id}.prob"
     */
    @Override
    protected String buildPath(String id) {
        return buildPathWithSuffix(id, "prob");
    }
}