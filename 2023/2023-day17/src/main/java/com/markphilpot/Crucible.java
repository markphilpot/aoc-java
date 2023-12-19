package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class Crucible {
    private static final Logger log = LogManager.getLogger(Crucible.class);

    // https://www.baeldung.com/java-dijkstra

    public static record Point(int x, int y) {}

    public static record Tile(Point p, int weight) {};

    public static class Node {
        private final Tile tile;
        private final Graph graph;
        private List<Node> shortestPath = new LinkedList<>();
        private Integer distance = Integer.MAX_VALUE;

        public Node(Tile t, Graph graph) {
            this.tile = t;
            this.graph = graph;
        }

        public Tile getTile() {
            return tile;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        public Integer getDistance() {
            return distance;
        }

        public Set<Node> getAdjacentNodes() {
            // Need to filter based on current considered shortest path
            var p = tile.p;

            var targets =
                    List.of(
                            new Point(p.x, p.y - 1),
                            new Point(p.x - 1, p.y),
                            new Point(p.x + 1, p.y),
                            new Point(p.x, p.y + 1)
                    );

            var base = targets.stream()
                    .filter(point -> point.x >= 0 && point.y >= 0 && point.x < graph.xDim && point.y < graph.yDim)
                    .toList();

            // Can't intersect path
            var pathPoints = shortestPath.stream().map(n -> n.getTile().p).collect(Collectors.toSet());
            base = base.stream().filter(point -> !pathPoints.contains(point)).toList();

            if(this.getTile().p.equals(new Point(5, 0))) {
                log.info("debug");
            }

            // Can't make a straight segment of length three (need 4, but includes self)
            if(shortestPath.size() > 3) {
                var self = this;
                var parent = shortestPath.get(shortestPath.size() -1);
                var gparent = shortestPath.get(shortestPath.size() -2);
                var ggparent = shortestPath.get(shortestPath.size() -3);

                if(self.getTile().p.x == parent.getTile().p.x &&
                        parent.getTile().p.x == gparent.getTile().p.x &&
                        gparent.getTile().p.x == ggparent.getTile().p.x) {
                    var x = parent.getTile().p.x;
                    // Can't continue in a horizontal line
                    base = base.stream().filter(point -> point.x != x).toList();
                } else if(self.getTile().p.y == parent.getTile().p.y &&
                        parent.getTile().p.y == gparent.getTile().p.y &&
                        gparent.getTile().p.y == ggparent.getTile().p.y) {
                    var y = parent.getTile().p.y;
                    // Can't continue in a vertical line
                    base = base.stream().filter(point -> point.y != y).toList();
                }
            }

            return base.stream().map(point -> graph.map.get(point)).collect(Collectors.toSet());
        }

        public List<Node> getShortestPath() {
            return shortestPath;
        }

        public void setShortestPath(List<Node> shortestPath) {
            this.shortestPath = shortestPath;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "tile=" + tile +
                    ", distance=" + distance +
                    '}';
        }
    }

    public static class Graph {
        private final int xDim;
        private final int yDim;
        private Set<Node> nodes = new HashSet<>();

        private Map<Point, Node> map = new HashMap<>();

        public Graph(int xDim, int yDim) {
            this.xDim = xDim;
            this.yDim = yDim;
        }

        public void addNode(Node n) {
            nodes.add(n);
            map.put(n.tile.p, n);
        }

        public Set<Node> getNodes() {
            return nodes;
        }

        public int getxDim() {
            return xDim;
        }

        public int getyDim() {
            return yDim;
        }

        public Map<Point, Node> getMap() {
            return map;
        }
    }

    public static Graph parse(InputStream inputStream) throws IOException {
        var input = new String(inputStream.readAllBytes());
        var lines = input.lines().toList();

        var traffic = new ArrayList<List<Integer>>();

        for(var line : lines) {
            var row = ParsingUtils.lineToList(line).stream().map(Integer::valueOf).toList();
            traffic.add(row);
        }

        var yDim = traffic.size();
        var xDim = traffic.getFirst().size();

        var graph = new Graph(xDim, yDim);

        for(var y = 0; y < yDim; y++) {
            for(var x = 0; x < xDim; x++) {
                var n = new Node(new Tile(new Point(x, y), traffic.get(y).get(x)), graph);
                graph.addNode(n);
            }
        }

        return graph;
    }


    public static void dijkstra(Node start) {
        start.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(start);

        while(!unsettledNodes.isEmpty()) {
            var currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);

            for(Node n : currentNode.getAdjacentNodes()) {
                var edgeWeight = n.getTile().weight;
                if(!settledNodes.contains(n)) {
                    calculateMinDistance(n, edgeWeight, currentNode);
                    unsettledNodes.add(n);
                }
            }
            settledNodes.add(currentNode);
        }
    }

    public static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        return unsettledNodes.stream().min(Comparator.comparing(Node::getDistance)).orElseThrow();
    }

    public static void calculateMinDistance(Node evalNode, int edgeWeight, Node sourceNode) {
        var sourceDistance = sourceNode.getDistance();

        if(sourceDistance + edgeWeight < evalNode.getDistance()) {
            evalNode.setDistance(sourceDistance + edgeWeight);

            var shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evalNode.setShortestPath(shortestPath);
        }
    }
}
