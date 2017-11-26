public enum SearchType {
    BFS,
    DFS;

    @Override
    public String toString() {
        switch (this) {
            case BFS:
                return "Breadth-First Search";
            case DFS:
                return "Depth-First Search";
            default:
                return "";
        }
    }
}
