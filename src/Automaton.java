import java.util.*;

public class Automaton {
    private final ArrayList<Node> starts = new ArrayList<>();
    private final ArrayList<Node> finals = new ArrayList<>();
    private final ArrayList<String> symbols = new ArrayList<>();

    public void addStart(Node node) {
        starts.add(node);
    }

    public void addFinal(Node node) {
        finals.add(node);
    }

    public void addSymbol(String symbol) {
        symbols.add(symbol);
    }

    public ArrayList<String> getSymbols() {
        return symbols;
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

        sb.append("NFA\n{\n");

        sb.append("alphabet = mkSet \"");
        for (String symbol : symbols) {
            sb.append(symbol);
        }
        sb.append("\",\n");

        final List<Node> nodes = getNodes();

        sb.append("states = mkSet [");
        for (Node node : nodes) {
            sb.append(node.getName()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("],\n");

        sb.append("starts = mkSet [");
        for (Node node : starts) {
            sb.append(node.getName()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("],");
        sb.append("\nfinals = mkSet [");
        for (Node node : finals) {
            sb.append(node.getName()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("],\n");

        sb.append("trans = collect [\n");
        for (Node node: nodes) {
            ArrayList<String> delta = node.getDelta();
            for (String edge : delta) {
                sb.append(edge).append(",\n");
            }
        }
        sb.delete(sb.length() - 2, sb.length());

        sb.append("]");
        sb.append("\n}");

        return sb.toString();
    }

    private HashMap<String, HashSet<Node>> getDefaultMap() {
        final HashMap<String, HashSet<Node>> startMap = new HashMap<>();
        for (String symbol : symbols) {
            HashSet<Node> nextSet = new HashSet<>();
            startMap.put(symbol, nextSet);
        }

        return startMap;
    }

    public Automaton toDFA() {
        final Queue<HashSet<Node>> queue = new LinkedList<>();
        final HashMap<HashSet<Node>, HashMap<String, HashSet<Node>>> table = new HashMap<>();

        // Initialize the queue and table
        HashSet<Node> startSet = new HashSet<>(starts);
        queue.add(startSet);

        List<String> symbols = getSymbols();

        while (!queue.isEmpty()) {
            HashSet<Node> currentSet = queue.poll();

            if (table.containsKey(currentSet)) {
                continue;
            }

            HashMap<String, HashSet<Node>> currentMap = getDefaultMap();
            // Z   | a  | b
            // {5} | {1, 4, 6} | {2, 3}


            for (String symbol : symbols) {
                for (Node node : currentSet) {
                    for (Node neighbor: node.getEdges(symbol)) {
                        currentMap.get(symbol).add(neighbor);
                    }
                }
            }

            table.put(currentSet, currentMap);

            for (HashSet<Node> nextSet : currentMap.values()) {
                if (!table.containsKey(nextSet)) {
                    queue.add(nextSet);
                }
            }
        }

        // Create the new DFA nodes
        Automaton dfa = new Automaton();
        for (String symbol : symbols) {
            dfa.addSymbol(symbol);
        }

        final ArrayList<HashSet<Node>> multiNodes = new ArrayList<>(table.keySet());
        final HashMap<HashSet<Node>, Node> dfaNodes = new HashMap<>();
        for (HashSet<Node> multiNode : multiNodes) {
            Node node = new Node(multiNodes.indexOf(multiNode));
            dfaNodes.put(multiNode, node);

            if (multiNode.equals(startSet)) {
                dfa.addStart(node);
            }
        }


        for (HashSet<Node> multiNode : multiNodes) {
            Node node = dfaNodes.get(multiNode);

            for (Node n : multiNode) {
                if (finals.contains(n)) {
                    dfa.addFinal(node);
                }
            }

            for (String symbol : symbols) {
                HashSet<Node> nextMultiNode = table.get(multiNode).get(symbol);
                Node nextDfaNode = dfaNodes.get(nextMultiNode);

                node.addEdge(symbol, nextDfaNode);
            }
        }

        return dfa;
    }

    public Automaton invert() {
        Automaton inverted = new Automaton();
        for (String symbol : symbols) {
            inverted.addSymbol(symbol);
        }

        final List<Node> nodes = getNodes();
        final HashMap<Integer, Node> invertedNodes = new HashMap<>();
        for (Node node : nodes) {
            Node invertedNode = new Node(node.getName());
            invertedNodes.put(invertedNode.getName(), invertedNode);
        }

        for (Node node: getStarts()) {
            inverted.addFinal(invertedNodes.get(node.getName()));
        }

        for (Node node: getFinals()) {
            inverted.addStart(invertedNodes.get(node.getName()));
        }

        for (Node node: nodes) {
            final HashMap<String, ArrayList<Node>> edges = node.getEdges();
            for (String symbol : edges.keySet()) {
                for (Node neighbor : edges.get(symbol)) {
                    invertedNodes.get(neighbor.getName()).addEdge(symbol, invertedNodes.get(node.getName()));
                }
            }
        }

        return inverted;
    }

    public Automaton minimizeByBrzozowski(boolean verboseOutput) {
        final Automaton dfa = toDFA();
        final Automaton inverted = dfa.invert();
        final Automaton dfa2 = inverted.toDFA();
        final Automaton inverted2 = dfa2.invert();
        final Automaton minimized = inverted2.toDFA();

        if (verboseOutput) {
            System.out.println("Original DFA");
            System.out.println(dfa);
            System.out.println();
            System.out.println("Inverted DFA");
            System.out.println(inverted);
            System.out.println();
            System.out.println("DFA 2");
            System.out.println(dfa2);
            System.out.println();
            System.out.println("Inverted 2");
            System.out.println(inverted2);
            System.out.println();
            System.out.println("Minimized DFA");
        }

        return minimized;
    }
}
