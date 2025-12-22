package configs;
import graph.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;


public class Node {
    private String name;
    private List<Node> edges;
    private Message msg;

    public Node(String name) {
        this.name = name;
        this.edges = new ArrayList<>();
        this.msg = null;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Node> getEdges() {
        return edges;
    }

    public void setEdges(List<Node> edges) {
        this.edges = edges;
    }

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }

    public void addEdge(Node node) {
        edges.add(node);
    }

    public boolean hasCycles() {
        Set<Node> visited = new HashSet<>();
        return hasCyclesHelper(this, visited);
    }

    private boolean hasCyclesHelper(Node target, Set<Node> visited) {
        if (visited.contains(this)) {
            return true;
        }

        visited.add(this);

        for (Node neighbor : edges) {
            if (neighbor == target) {
                return true;
            }

            if (!visited.contains(neighbor)) {
                if (neighbor.hasCyclesHelper(target, visited)) {
                    return true;
                }
            }
        }

        return false;
    }


}
