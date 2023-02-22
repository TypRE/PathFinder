package sk.typre.pathfinder;

import java.awt.*;
import java.util.List;

/**
 * An implementation for a PathFinder.
 *
 */
public class PathFinder implements Runnable {
    private final char[][] originalMap;
    private final boolean diagonal;

    public PathFinder(char[][] originalMap, boolean diagonal) {
        this.originalMap = originalMap;
        this.diagonal = diagonal;
    }

    /**
     * A new task that will print the result
     */

    @Override
    public void run() {
        if (checkMap(originalMap)) {
            System.out.println();
            int[][] convertedMap = convertMap(originalMap, '#');
            Point startPoint = getStartPoint(originalMap);
            Point destinyPoint = getDestinyPoint(originalMap);

            if (startPoint == null || destinyPoint == null) {
                System.out.println("Start or end point is not defined.");
                System.out.println();
                return;
            }

            long start = System.currentTimeMillis();
            List<int[]> path = calculateMapPath(convertedMap, startPoint, destinyPoint, diagonal);
            long end = System.currentTimeMillis();

            if (path == null) {
                System.out.println("Path not found.");
                System.out.println();
            } else {

                String map = getSolvedMap(path, originalMap);

                System.out.println(map);
                System.out.println();
                System.out.println("Path found in " + (end - start) + " ms.");
                System.out.println();
                if (!diagonal) {
                    String steps = getPathSteps(path);
                    System.out.println("Steps: " + steps);
                }else {
                    System.out.println("Don't use diagonal option to view the solution steps.");
                }
                System.out.println();
            }
        } else {
            System.out.println();
            System.out.println("Wrong map format.");
        }
    }

    /**
     * Verifies that the map is in the correct format
     *
     * @param originalMap Original char map.
     * @return returns true if the map passes otherwise false.
     */

    private boolean checkMap(char[][] originalMap) {
        if (originalMap == null) {
            return false;
        }
        boolean startFound = false;
        boolean destinyFound = false;

        int length = originalMap[0].length;
        for (char[] chars : originalMap) {
            for (char c : chars) {
                if (c != '#' && c != '.' && c != 'S' && c != 'X') {
                    return false;
                }
                if (length != chars.length) {
                    return false;
                }
                if (!startFound) {
                    if (c == 'S') {
                        startFound = true;
                    }
                } else if (c == 'S') {
                    return false;
                }
                if (!destinyFound) {
                    if (c == 'X') {
                        destinyFound = true;
                    }
                } else if (c == 'X') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Create path steps from the path list.
     *
     * @param path Path generated from the A* Algorithm.
     * @return returns a String composed of individual steps.
     */

    private String getPathSteps(List<int[]> path) {
        if (path == null) {
            throw new RuntimeException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        int[] last = null;
        int size = path.size() - 1;
        for (int i = 0; i <= size; i++) {
            if (i != 0) {
                if (last[0] < path.get(i)[0]) {
                    stringBuilder.append("d").append(i < size ? "," : "");
                } else if (last[0] > path.get(i)[0]) {
                    stringBuilder.append("u").append(i < size ? "," : "");
                } else if (last[1] < path.get(i)[1]) {
                    stringBuilder.append("r").append(i < size ? "," : "");
                } else if (last[1] > path.get(i)[1]) {
                    stringBuilder.append("l").append(i < size ? "," : "");
                }

                if (i%32 == 0){
                    stringBuilder.append("\n");
                }
            }
            last = path.get(i);
        }
        return new String(stringBuilder);
    }

    /**
     * Compute the map path with A* Algorithm.
     *
     * @param convertedMap Map converted to int array.
     * @param start Start point.
     * @param destiny Destiny point.
     * @param diagonal Include diagonal steps.
     * @return returns a Path composed of individual steps.
     */

    private List<int[]> calculateMapPath(int[][] convertedMap, Point start, Point destiny, boolean diagonal) {
        if (convertedMap == null || start == null || destiny == null) {
            throw new RuntimeException();
        }
        return AStarAlgorithm.getPath(convertedMap, new int[]{start.y, start.x}, new int[]{destiny.y, destiny.x}, diagonal);
    }

    /**
     * Creates a String map for output to the console.
     *
     * @param path Path generated from the A* Algorithm.
     * @param originalMap Original char map.
     * @return returns a String map.
     */

    private String getSolvedMap(List<int[]> path, char[][] originalMap) {
        if (path == null || originalMap == null) {
            throw new RuntimeException();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < originalMap.length; i++) {
            for (int n = 0; n < originalMap[i].length; n++) {
                stringBuilder.append(locInPath(path, i, n) ? '*' : originalMap[i][n]);
            }
            if (i < originalMap.length - 1) {
                stringBuilder.append("\n");
            }
        }
        return new String(stringBuilder);
    }

    /**
     * Checks if there is an x,y position in the path.
     *
     * @param path Path generated from the A* Algorithm.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @return returns true if the searched character is found otherwise false.
     */
    private boolean locInPath(List<int[]> path, int x, int y) {
        if (path == null) {
            throw new RuntimeException();
        }
        for (int i = 1; i < path.size() - 1; i++) {
            if (path.get(i)[0] == x && path.get(i)[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts the map from char array to integer array.
     *
     * @param originalMap Original char map.
     * @param solid Char represented as solid object.
     * @return returns the converted map from char array to integer array.
     */

    private int[][] convertMap(char[][] originalMap, char solid) {
        if (originalMap == null) {
            throw new RuntimeException();
        }
        int[][] converted = new int[originalMap.length][originalMap[0].length];
        for (int i = 0; i < originalMap.length; i++) {
            for (int n = 0; n < originalMap[i].length; n++) {
                converted[i][n] = originalMap[i][n] == solid ? 1 : 0;
            }
        }
        return converted;
    }
    /**
     * Calculates the starting position on the map
     *
     * @param originalMap Original char map.
     * @return returns the start point.
     */
    private Point getStartPoint(char[][] originalMap) {
        return getMapPoint(originalMap, 'S');
    }
    /**
     * Calculates the destiny position on the map
     *
     * @param originalMap Original char map.
     * @return returns the destiny point.
     */
    private Point getDestinyPoint(char[][] originalMap) {
        return getMapPoint(originalMap, 'X');
    }

    /**
     * Calculates at which position on the map the input character is located
     *
     * @param originalMap Original char map.
     * @param sign Character that needs to be found.
     * @return returns the point at which the input character is located otherwise null.
     */

    private Point getMapPoint(char[][] originalMap, char sign) {
        if (originalMap == null) {
            throw new RuntimeException();
        }
        for (int i = 0; i < originalMap.length; i++) {
            for (int n = 0; n < originalMap[i].length; n++) {
                if (originalMap[i][n] == sign) {
                    return new Point(n, i);
                }
            }
        }
        return null;
    }


}
