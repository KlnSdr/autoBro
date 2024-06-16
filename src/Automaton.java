import java.util.*;

public class Automaton {
    private final ArrayList<Node> starts = new ArrayList<>();
    private final ArrayList<Node> finals = new ArrayList<>();

    public void addStart(Node node) {
        starts.add(node);
    }

    public void addFinal(Node node) {
        finals.add(node);
    }

    public ArrayList<Node> getStarts() {
        return starts;
    }

    public ArrayList<Node> getFinals() {
        return finals;
    }

    public List<Node> getNodes() {
        Set<Node> visited = new HashSet<>();
        ArrayList<Node> nodes = new ArrayList<>();

        // Start the traversal from each start node
        for (Node start : starts) {
            traverse(start, visited, nodes);
        }

        return nodes;
    }

    private void traverse(Node current, Set<Node> visited, List<Node> nodes) {
        if (!visited.contains(current)) {
            visited.add(current);
            nodes.add(current);

            // Traverse each edge symbolically connected to the current node
            for (ArrayList<Node> edgeNodes : current.getEdges().values()) {
                for (Node neighbor : edgeNodes) {
                    traverse(neighbor, visited, nodes);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Starts: [");
        for (Node node : starts) {
            sb.append(node.getName()).append(" ");
        }
        sb.append("]");
        sb.append("\nEnds: [");
        for (Node node : finals) {
            sb.append(node.getName()).append(" ");
        }
        sb.append("]\n");

        final List<Node> nodes = getNodes();

        for (Node node: nodes) {
            ArrayList<String> delta = node.getDelta();
            for (String edge : delta) {
                sb.append(edge).append("\n");
            }
        }

        return sb.toString();
    }
}
