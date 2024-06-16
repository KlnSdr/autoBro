import java.util.ArrayList;
import java.util.HashMap;

public class Node {
    private final int name;
    private final HashMap<String, ArrayList<Node>> edges = new HashMap<>();

    public Node(int name) {
        this.name = name;
    }

    public void addEdge(String symbol, Node node) {
        if (!edges.containsKey(symbol)) {
            edges.put(symbol, new ArrayList<>());
        }
        edges.get(symbol).add(node);
    }

    public int getName() {
        return name;
    }

    public HashMap<String, ArrayList<Node>> getEdges() {
        return edges;
    }

    public ArrayList<Node> getEdges(String symbol) {
        return edges.get(symbol);
    }

    public void setEdges(String symbol, ArrayList<Node> nodes) {
        edges.put(symbol, nodes);
    }
}
