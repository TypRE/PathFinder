package sk.typre.pathfinder;

import java.io.*;

public abstract class AbstractFindPathInputReader {

    private final char[][] MAP;
    private final InputType INPUT_TYPE;

    public AbstractFindPathInputReader(File file) {
        this.INPUT_TYPE = InputType.FILE;
        MAP = loadFileMap(file);
    }

    public AbstractFindPathInputReader(InputStream stringMap) {
        this.INPUT_TYPE = InputType.CONSOLE;
        MAP = loadCharMap(stringMap);
    }
    /**
     * Reads the map from the File.
     *
     * @param file any file contains map.
     * @return map represented as char array.
     */
    private char[][] loadFileMap(File file) {
        char[][] map;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            map = loadMap(bufferedReader);
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
    /**
     * Reads the map from the InputStream.
     *
     * @param inputStream an InputStream.
     * @return map represented as char array.
     */
    private char[][] loadCharMap(InputStream inputStream) {
        char[][] map;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            map = loadMap(bufferedReader);
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    /**
     * Reads the map from the BufferedReader.
     *
     * @param bufferedReader an BufferedReader.
     * @return map represented as char array.
     */

    private char[][] loadMap(BufferedReader bufferedReader) {
        char[][] temp = new char[256][256];
        boolean init = true;
        int mazeWidth = 0;
        int posY = 0;

        String mapLine;
        try {
            while ((mapLine = bufferedReader.readLine()) != null) {
                if (INPUT_TYPE == InputType.CONSOLE && mapLine.toLowerCase().matches("done")) {
                    break;
                }
                if (init) {
                    init = false;
                    mazeWidth = mapLine.length();
                    if (mazeWidth > 256){
                        return null;
                    }
                } else if (mazeWidth != mapLine.length()) {
                    return null;
                }
                char[] line = mapLine.toCharArray();
                System.arraycopy(line, 0, temp[posY], 0, line.length);
                posY++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        char[][] reduced = new char[posY][mazeWidth];
        for (int n = 0; n < posY; n++) {
            System.arraycopy(temp[n], 0, reduced[n], 0, mazeWidth);
        }

        return reduced;
    }

    public char[][] getCharMap() {
        return MAP;
    }

}
