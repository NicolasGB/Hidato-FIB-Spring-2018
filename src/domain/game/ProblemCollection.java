package domain.game;

import java.util.*;

/**
 * A ProblemCollection represents a storage of problems
 * that gives the possibility to the player to select all
 * the available problems in with different criteria
 *
 * @author Nicolas Gouttefangeas Ballo
 * @author Samuel Hoenle
 */
public class ProblemCollection implements Collection<Problem> {

    private List<Problem> problemCollection;

    /**
     * Constructs an empty ProblemCollection array
     */
    public ProblemCollection() {
        this.problemCollection = new ArrayList<>();
    }

    /**
     * Constructs a ProblemCollection from a given ArrayList
     * @param problemCollection An {@link ArrayList} of Problems to be enclosed in this instance
     */
    public ProblemCollection(List<Problem> problemCollection) {
        this.problemCollection = problemCollection;
    }

    /**
     * Adds a given {@link Problem} to the collection
     *
     * @param p a Problem that we want to add to the collection
     * @return  if the Problem has been added
     * @see ArrayList#add(Object)
     */
    public boolean add(Problem p) {
        return this.problemCollection.add(p);
    }

    /**
     * Removes a given {@link Problem} from the collection
     *
     * @param o The {@link Problem} object to be removed.
     * @return  If the object has been successfully removed from the collection
     * @see ArrayList#remove(Object)
     */
    public boolean remove(Object o) {
        return this.problemCollection.remove(o);
    }

    /**
     * Check if all elements of the passed {@link Collection} are contained
     * @param c A {@link Collection} to be checked
     * @return  Whether or not c is contained
     * @see ArrayList#containsAll(Collection)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return problemCollection.containsAll(c);
    }

    /**
     * Adds all objects from a passed {@link Collection}
     * @param c The {@link Collection} of which all elements should be added
     * @return  Whether or not all elements have been successfully added
     * @see ArrayList#addAll(Collection)
     */
    @Override
    public boolean addAll(Collection<? extends Problem> c) {
        return problemCollection.addAll(c);
    }

    /**
     * Removes all elements contained in a passed {@link Collection} from this collection
     * @param c The {@link Collection} of elements to be removed
     * @return  Whether or not all contained elements have been successfully removed
     * @see ArrayList#removeAll(Collection)
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return problemCollection.removeAll(c);
    }

    /**
     * Retains all elements contained in a passed {@link Collection} from this collection
     * @param c The {@link Collection} of elements to be retained
     * @return  Whether or not all contained elements have been successfully retained
     * @see ArrayList#retainAll(Collection)
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return problemCollection.retainAll(c);
    }

    /**
     * Clear this collection.
     * @see ArrayList#clear()
     */
    @Override
    public void clear() {
        problemCollection.clear();
    }

    /**
     * Whether the collection contains a given {@link Problem}
     *
     * @param o The object to be checked
     * @return  Whether or not the object exists in the collection
     * @see ArrayList#contains(Object)
     */
    public boolean contains(Object o) {
        return this.problemCollection.contains(o);
    }

    /**
     * The size of the collection.
     *
     * @return The size of the collection.
     * @see ArrayList#size()
     */
    public int size() {
        return problemCollection.size();
    }

    /**
     * If this collection is empty.
     *
     * @return If this collection is empty.
     * @see ArrayList#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return problemCollection.isEmpty();
    }

    /**
     * Selects all the problems of a given difficulty
     *
     * @param difficulty is the given difficulty that the player wants to play
     * @return an array with all the available problems of the given difficulty
     */
    public List<Problem> select(Difficulty difficulty) {
        if (difficulty == null) return new ArrayList<>(problemCollection);
        return selectByDifficulty(problemCollection, difficulty);
    }

    /**
     * Selects all the problems of a given problem type
     *
     * @param type is the given problem {@link Type} that the player wants to play
     * @return an array with all the available problems of the given type
     */
    public List<Problem> select(Type type) {
        if (type == null) return new ArrayList<>(problemCollection);
        return selectByType(problemCollection, type);
    }

    /**
     * Selects all the problems created by the given creator
     *
     * @param name is the name of the creator that the player wants the problems from
     * @return an array with all the available problems created by the given creator
     */
    public List<Problem> select(String name){
        if (name == null || name.equals("")) return new ArrayList<>(problemCollection);
        return selectByPlayer(problemCollection, name);
    }

    /**
     * Selects all the problems of given {@link List}s of {@link Difficulty}s, {@link Type}s and {@link Player#name}s
     *
     * @param difficulties  A {@code List} of {@link Difficulty}s to select by
     * @param types         A {@code List} of {@link Type}s to select by
     * @param names         A {@code List} of {@link Player#name}s ({@code String}s) to select by
     * @return A {@code List} with all {@link Problem}s matching the selection criteria
     */
    public List<Problem> select(List<Difficulty> difficulties, List<Type> types, List<String> names) {
        List<Problem> result = new ArrayList<>(problemCollection);

        if (difficulties != null && !difficulties.isEmpty()) {
            List<Problem> newResult = new ArrayList<>();
            for (Difficulty d :
                    difficulties) {
                newResult.addAll(selectByDifficulty(result, d));
            }
            result = newResult;
        }

        if (types != null && !types.isEmpty()) {
            List<Problem> newResult = new ArrayList<>();
            for (Type t :
                    types) {
                newResult.addAll(selectByType(result, t));
            }
            result = newResult;
        }

        if (names != null && !names.isEmpty() && !(names.size() == 1 && names.contains(""))) {
            List<Problem> newResult = new ArrayList<>();
            for (String n :
                    names) {
                newResult.addAll(selectByPlayer(result, n));
            }
            result = newResult;
        }

        return result;
    }

    /**
     * Helper method to select {@link Problem}s by the name of a {@link Player}
     *
     * @param pc    A {@link List} of {@link Problem}s to be selected from
     * @param name  The {@link Player#name} to select by
     * @return A {@link List} of {@link Problem}s only containing the Problems in {@code pc} matching {@code name}
     */
    private List<Problem> selectByPlayer(List<Problem> pc, String name) {
        List<Problem> result = new ArrayList<>();
        for(int i = 0; i < pc.size(); i++) {
            if (name.equals(pc.get(i).getCreatorName())){
                result.add(pc.get(i));
            }
        }
        return result;
    }

    /**
     * Helper method to select {@link Problem}s by a {@link Difficulty}
     *
     * @param pc            A {@link List} of {@link Problem}s to be selected from
     * @param difficulty    A {@link Difficulty} to select by
     * @return A {@link List} of {@link Problem}s only containing the Problems in {@code pc} matching {@code difficulty}
     */
    private List<Problem> selectByDifficulty(List<Problem> pc, Difficulty difficulty) {
        List<Problem> result = new ArrayList<>();
        for(int i = 0; i < pc.size(); i++) {
            if (difficulty.equals(pc.get(i).getDifficulty())){
                result.add(pc.get(i));
            }
        }
        return result;
    }

    /**
     * Helper method to select {@link Problem}s by a {@link Difficulty}
     *
     * @param pc    A {@link List} of {@link Problem}s to be selected from
     * @param type  A {@link Type} to select by
     * @return A {@link List} of {@link Problem}s only containing the Problems in {@code pc} matching {@code type}
     */
    private List<Problem> selectByType(List<Problem> pc, Type type) {
        ArrayList<Problem> result = new ArrayList<>();
        for(int i = 0; i < pc.size(); i++) {
            if(type.equals(Type.Triangle) && pc.get(i) instanceof TriangleProblem){
                result.add(pc.get(i));
            }
            else if(type.equals(Type.Square) && pc.get(i) instanceof SquareProblem){
                result.add(pc.get(i));
            }
            else if (type.equals(Type.Hexagon) && pc.get(i) instanceof HexagonProblem){
                result.add(pc.get(i));
            }
        }
        return result;
    }

    /**
     * Returns an alphabetically sorted list of all {@link Player#name}s who created problems in this collection
     * @return An alphabetically sorted {@link List<String>} containing {@link Player#name}s
     */
    public List<String> getAllCreatorNames() {
        List<String> result = new ArrayList<>();
        for (Problem p :
                problemCollection) {
            String name = p.getCreatorName();
            if (!result.contains(name)) result.add(name);
        }
        Collections.sort(result);
        return result;
    }

    /**
     * To get an {@link Iterator} of this collection
     *
     * @return An {@link Iterator} of this collection
     * @see ArrayList#iterator()
     */
    @Override
    public Iterator<Problem> iterator() {
        return problemCollection.iterator();
    }

    /**
     * Returns the elements of this collection in an array.
     *
     * @return The elements of this collection in an array.
     * @see ArrayList#toArray()
     */
    @Override
    public Object[] toArray() {
        return problemCollection.toArray();
    }

    /**
     * Returns te elements of this collection in the passed array, if big enough, or returns them as a new array.
     *
     * @param a     The output array in which all the elements in this collection should be inserted
     * @param <T>   The type in which the contained elements should be inserted
     * @return      an array containing all of the elements in this collection
     * @see ArrayList#toArray(Object[])
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return problemCollection.toArray(a);
    }
}
