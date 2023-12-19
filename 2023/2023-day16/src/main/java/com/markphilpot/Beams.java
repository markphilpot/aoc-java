package com.markphilpot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.markphilpot.Beams.Direction.*;

public class Beams {
    private static final Logger log = LogManager.getLogger(Beams.class);

    public enum Direction {
        N, S, E, W;

        public Direction cross() {
            return switch (this) {
                case N -> S;
                case S -> N;
                case E -> W;
                case W -> E;
            };
        }
    }

    public enum Tile {
        EMPTY("."),
        SPLIT_H("-"),
        SPLIT_V("|"),
        MIRROR_LD("\\"),
        MIRROR_LU("/");

        private final String value;

        Tile(String s) {
            this.value = s;
        }

        public List<Direction> getExit(Direction enter) {
            return switch (this) {
                case EMPTY -> List.of(enter.cross());
                case SPLIT_H -> switch(enter) {
                    case N, S -> List.of(W, E);
                    case E, W -> List.of(enter.cross());
                };
                case SPLIT_V -> switch(enter) {
                    case N, S -> List.of(enter.cross());
                    case E, W -> List.of(N, S);
                };
                case MIRROR_LD -> switch (enter) {
                    case N -> List.of(E);
                    case S -> List.of(W);
                    case E -> List.of(N);
                    case W -> List.of(S);
                };
                case MIRROR_LU -> switch (enter) {
                    case N -> List.of(W);
                    case S -> List.of(E);
                    case E -> List.of(S);
                    case W -> List.of(N);
                };
            };
        }

        public static Tile fromString(String s) {
            return Arrays.stream(Tile.values())
                    .filter(t -> t.value.equals(s))
                    .findFirst()
                    .orElseThrow(IllegalStateException::new);
        }


        @Override
        public String toString() {
            return value;
        }
    }

    public record Point(int x, int y) {}

    public static Point getNextPoint(Point p, Direction exit) {
        return switch (exit) {
            case N -> new Point(p.x, p.y - 1);
            case S -> new Point(p.x, p.y + 1);
            case E -> new Point(p.x + 1, p.y);
            case W -> new Point(p.x -1, p.y);
        };
    }

    public record Grid(List<List<Tile>> grid) {
        public Tile get(int x, int y) {
            return grid.get(y).get(x);
        }

        public Tile get(Point p) { return grid.get(p.y).get(p.x); }

        public int xDim() {
            return grid.getFirst().size();
        }

        public int yDim() {
            return grid.size();
        }
    }



    public static Grid parse(InputStream inputStream) throws IOException {
        var input = new String(inputStream.readAllBytes());

        return new Grid(input.lines()
                .map(line -> ParsingUtils.lineToStream(line)
                        .map(Tile::fromString)
                        .toList())
                .toList());
    }

    public static record CacheKey(Point p, Direction enterDirection) {}

    public static Set<CacheKey> cache = new HashSet<>();

    public static List<Point> followBeam(Grid grid, Point point, Direction enterDirection, List<Point> currentPath) {
        // Beam starts at (0,0) entering from W
        var key = new CacheKey(point, enterDirection);

        if(shouldStop(grid, point, key)) {
            // Stop
            return currentPath;
        } else {
            cache.add(key);

            // Energize this point
            currentPath.add(point);

            var currentPoint = point;
            var currentTile = grid.get(point);
            var currentExits = currentTile.getExit(enterDirection);

            while(currentExits.size() == 1) {
                currentPoint = getNextPoint(currentPoint, currentExits.getFirst());
                var currentEnterDirection = currentExits.getFirst().cross();

                key = new CacheKey(currentPoint, currentEnterDirection);

                if(shouldStop(grid, currentPoint, key)) {
                    return currentPath;
                } else {
                    cache.add(key);
                    currentPath.add(currentPoint);

                    currentTile = grid.get(currentPoint);
                    currentExits = currentTile.getExit(currentEnterDirection);
                }
            }

            Point finalCurrentPoint = currentPoint;
            return currentExits.stream()
                    .flatMap(exit -> followBeam(grid, getNextPoint(finalCurrentPoint, exit), exit.cross(), new ArrayList<>(currentPath)).stream()).toList();
        }
    }

    private static boolean shouldStop(Grid grid, Point point, CacheKey key) {
        return cache.contains(key) || point.x() < 0 || point.x() >= grid.xDim() || point.y() < 0 || point.y() >= grid.yDim();
    }
}
