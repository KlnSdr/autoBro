package com.klnsdr;

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
        if (!edges.containsKey(symbol)) {
            return new ArrayList<>();
        }
        return edges.get(symbol);
    }

    public ArrayList<String> getDelta() {
        ArrayList<String> delta = new ArrayList<>();
        for (String symbol : edges.keySet()) {
            for (Node node : edges.get(symbol)) {
                delta.add("(" + name + ", '" + symbol + "', " + node.getName() + ")");
            }
        }
        return delta;
    }

    public void setEdges(String symbol, ArrayList<Node> nodes) {
        edges.put(symbol, nodes);
    }

    @Override
    public String toString() {
        return Integer.toString(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Node)) {
            return false;
        }
        Node node = (Node) obj;
        return name == node.name;
    }

    @Override
    public int hashCode() {
        return name;
    }
}
