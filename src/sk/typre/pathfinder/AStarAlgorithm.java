package sk.typre.pathfinder;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A* Search Algorithm is a simple and efficient search algorithm that can be used to find the optimal path between two nodes.
 */

public class AStarAlgorithm {
    /**
     * Calculate the path from the start position start(y,x) to the destiny position end(y,x).
     *
     * @param intMaze Maze array contains 1 as solid and 0 as air.
     * @param start   Start position vertex array.
     * @param end     End position vertex array.
     * @return Path composed of vertices represented as array list.
     */
    public static List<int[]> getPath(int[][] intMaze, int[] start, int[] end, boolean diagonal) {
        //Create start and end node
        Node start_node = new Node(null, start);
        start_node.g = start_node.h = start_node.f = 0;
        Node end_node = new Node(null, end);
        end_node.g = end_node.h = end_node.f = 0;
        //Initialize both open and closed list
        List<Node> open_list = new ArrayList<>();
        List<Node> closed_list = new ArrayList<>();
        //Add the start node
        open_list.add(start_node);
        //Loop until find the end
        while (!open_list.isEmpty()) {
            //Get the current node
            Node current_node = open_list.get(0);
            int current_index = 0;
            for (int i = 0; i < open_list.size(); i++) {
                Node item = open_list.get(i);
                if (item.f < current_node.f) {
                    current_node = item;
                    current_index = i;
                }
            }
            //Pop current off open list, add to closed list
            open_list.remove(current_index);
            closed_list.add(current_node);
            //Found the goal
            if (current_node.equals(end_node)) {
                List<int[]> path = new ArrayList<>();
                Node current = current_node;
                while (current != null) {
                    path.add(0, current.position);
                    current = current.parent;
                }
                return path;
            }
            //Generate children
            List<Node> children = new ArrayList<>();
            int[][] directions;
            if (diagonal) {
                directions = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
            } else {
                directions = new int[][]{{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
            }

            for (int[] new_position : directions) {
                //Get node position
                int[] new_pos = {current_node.position[0] + new_position[0], current_node.position[1] + new_position[1]};

                //Make sure within range
                if (new_pos[0] > intMaze.length - 1 || new_pos[0] < 0 || new_pos[1] > intMaze[intMaze.length - 1].length - 1 || new_pos[1] < 0) {
                    continue;
                }

                //Make sure walkable terrain
                if (intMaze[new_pos[0]][new_pos[1]] != 0) {
                    continue;
                }

                //Make sure it's not a diagonal step through wall
                if (diagonal) {
                    int cy = current_node.position[0];
                    int cx = current_node.position[1];

                    if (new_position[0] == -1 && new_position[1] == -1) {
                        if (intMaze[cy][cx - 1] != 0 && intMaze[cy - 1][cx] != 0) {
                            continue;
                        }
                    } else if (new_position[0] == -1 && new_position[1] == 1) {
                        if (intMaze[cy - 1][cx] != 0 && intMaze[cy][cx + 1] != 0) {
                            continue;
                        }
                    } else if (new_position[0] == 1 && new_position[1] == -1) {
                        if (intMaze[cy][cx - 1] != 0 && intMaze[cy + 1][cx] != 0) {
                            continue;
                        }
                    } else if (new_position[0] == 1 && new_position[1] == 1) {
                        if (intMaze[cy + 1][cx] != 0 && intMaze[cy][cx + 1] != 0) {
                            continue;
                        }
                    }
                }

                //Create new node
                Node new_node = new Node(current_node, new_pos);
                //Append
                children.add(new_node);
            }
            //Loop through children
            cont:
            for (Node child : children) {
                //Child is on the closed list
                for (Node closed_child : closed_list) {
                    if (child.equals(closed_child)) {
                        continue cont;
                    }
                }

                //Create the f, g, and h values
                child.g = current_node.g + 1;
                if (!diagonal) {
                    child.h = Math.abs(child.position[0] - end_node.position[0]) + Math.abs(child.position[1] - end_node.position[1]);
                }
                child.f = child.g + child.h;

                //Child is already in the open list
                for (Node open_node : open_list) {
                    if (child.equals(open_node) && child.g >= open_node.g) {
                        continue cont;
                    }
                }
                //Add the child to the open list
                open_list.add(child);
            }
        }
        return null;
    }

    /**
     * The nested Node class used by this Algorithm.
     */
    private static class Node {
        public Node parent;
        public int[] position;
        public int g, h, f;

        public Node(Node parent, int[] position) {
            this.parent = parent;
            this.position = position;
            this.g = 0;
            this.h = 0;
            this.f = 0;
        }

        public boolean equals(Node other) {
            return Arrays.equals(position, other.position);
        }
    }

}