package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic Controller of the Persistence Layer. Has all the main functionality used by its subclasses
 */
public abstract class CtrlGenericData {
    /**
     * Name of the Folder where the controller will store its data
     */
    private final String folderName;

    /**
     * Creates a new controller and assigns the correct folder name to it
     * @param folderName The name of the folder where it will be created
     */
    public CtrlGenericData(String folderName) {
        this.folderName = folderName;
    }

    /**
     * Creates all the folder structure required to save the data and an empty file
     * @param id The name of the file to create
     */
    private void createDirectoryStructure(String id) throws IOException {
        File newFile = new File(buildPath(id));
        newFile.getParentFile().mkdirs();
        newFile.createNewFile();
    }

    /**
     * Gets an object from the file system
     * @param id The name which identifies the saved file
     * @return Object with the content
     * @throws IOException  Could not read the data from the file because it is either locked or it does not have enough
     *                      permissions
     * @throws ClassNotFoundException The file can not be loaded
     */
    public Object get(String id) throws IOException, ClassNotFoundException {
        File file = new File(buildPath(id));
        if (!file.exists()) return null;
        return new ObjectInputStream(new FileInputStream(file)).readObject();
    }

    /**
     * Gets all the Object stored in the controller's folder
     * @return a list of objects
     * @throws IOException Could not read the data from the file because it is either locked or
     * it does not have enough permissions
     * @throws ClassNotFoundException The class of the stored object does not exist
     */
    public List<Object> getAll() throws IOException, ClassNotFoundException {
        File[] listOfFiles = new File("Game/Data/" + folderName + "/").listFiles();
        List<Object> objects = new ArrayList<Object>();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (!file.getName().startsWith(".")) {
                    objects.add(new ObjectInputStream(new FileInputStream(file)).readObject());
                }
            }
        }

        return objects;
    }

    /**
     * Stores an object into a file with serialization
     * @param id The name which identifies the file
     * @param object Object wanted to store
     * @throws IOException Could not write the data from the file because it is either locked or
     * it does not have enough permissions
     */
    public void save(String id, Object object) throws IOException
    {
        createDirectoryStructure(id);
        File file = new File(buildPath(id));
        new ObjectOutputStream(new FileOutputStream(file)).writeObject(object);
    }

    /**
     * Checks if the object exists in the file system
     * @param id The name which identifies the object
     * @return True if it exists. False otherwise
     * @see File#exists()
     */
    public boolean exists(String id) {
        return new File(buildPath(id)).exists();
    }

    /**
     * Returns a path to a file identifier. Overwritten in subclasses for specific suffixes.
     * @param id The file identifier
     * @return A path in the form of "Game/Data/{{@link #folderName}}/{id}.{suffix}"
     */
    protected abstract String buildPath(String id);

    /**
     * Helper method for {@link #buildPath(String)} to build a path to a file with a specific suffix.
     * @param id The file identifier
     * @param suffix The suffix to be added to the file (without a leading .)
     * @return A path i the form of "Game/Data/{{@link #folderName}}/{id}.{suffix}"
     */
    protected String buildPathWithSuffix(String id, String suffix) {
        if (suffix.startsWith(".")) suffix = suffix.replaceFirst(".", "");
        String path = "Game/Data/" + folderName + "/" + id;
        path += id.endsWith("." + suffix) ? "" : "." + suffix;
        return path;
    }

    /**
     * Deletes the file with the given identifier from the file system. Uses {@link #buildPath(String)} to build the path
     *
     * @param id The identifier of the file to be deleted
     * @return If the file has been successfully deleted
     * @see File#delete()
     */
    public boolean delete(String id) {
        return new File(buildPath(id)).delete();
    }
}
