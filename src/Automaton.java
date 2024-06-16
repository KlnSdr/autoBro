import java.util.ArrayList;

public class Automaton {
    private final ArrayList<Node> starts = new ArrayList<>();
    private final ArrayList<Node> ends = new ArrayList<>();

    public void addStart(Node node) {
        starts.add(node);
    }

    public void addEnd(Node node) {
        ends.add(node);
    }

    public ArrayList<Node> getStarts() {
        return starts;
    }

    public ArrayList<Node> getEnds() {
        return ends;
    }
}
