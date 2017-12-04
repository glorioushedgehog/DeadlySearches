/**
 * differentiate between BFS's and DFS's
 */
public enum SearchType {
    BFS,
    DFS;

    /**
     * used for telling the player what kind
     * of search found him
     * @return a string describing the search
     */
    @Override
    public String toString() {
        switch (this) {
            case BFS:
                return "Breadth-First Search";
            case DFS:
                return "Depth-First Search";
            default:
                return "Invalid search type";
        }
    }
}
