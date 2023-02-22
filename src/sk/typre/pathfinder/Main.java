package sk.typre.pathfinder;

import java.io.*;


public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
//        Console console = System.console();
//        if (console == null && !GraphicsEnvironment.isHeadless()) {
//            String filename = Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
//            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "cmd", "/k", "java -jar \"" + filename + "\""});
//        } else {
//        new Main().startApp();
//        }
        new Main().startApp();
    }


    public void startApp() throws IOException, InterruptedException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        AbstractFindPathInputReader reader;
        String answer = null;
        File file = null;
        boolean diagonal;
        do {
            if (answer != null) {
                System.out.println("Wrong answer.");
            }
            System.out.print("Use diagonal steps to solve the maze ? y/n: ");
        } while (!(answer = in.readLine()).matches("[yn]"));

        diagonal = answer.equals("y");
        answer=null;

        do {
            if (answer != null) {
                System.out.println("Wrong answer.");
            }
            System.out.print("Do you want to load maze from the file ? y/n: ");
        } while (!(answer = in.readLine()).matches("[yn]"));

        if (answer.equals("y")) {
            do {
                if (file != null && !file.exists()) {
                    System.out.println("File does not exists.");
                }
                System.out.print("Please enter the file name: ");
            } while (!(file = new File(in.readLine())).exists());
            reader = new FindPathInputReaderFile(file);
        } else {
            System.out.println();
            System.out.println("Please draw the rectangle map.");
            System.out.println("Materials: \".\" - Air , \"#\" - Solid.");
            System.out.println("Points: \"S\" - Start point, \"X\" - Destination point.");
            System.out.println("Then write DONE on the new line.");
            System.out.println();
            reader = new FindPathReaderStdIn(System.in);
        }
        if (reader instanceof FindPathInputReaderFile) {
            System.out.println("Calculating path...");
            calculatePath(reader,diagonal);
            do {
                answer = null;
                do {
                    if (answer != null) {
                        System.out.println("Wrong answer.");
                    }
                    System.out.print("Reload the maze file ? y/n: ");
                } while (!(answer = in.readLine()).matches("[yn]"));

                if (answer.equals("y")) {
                    calculatePath(new FindPathInputReaderFile(file),diagonal);
                }

            } while (!answer.equals("n"));

        } else {
            System.out.println("Calculating path...");
            calculatePath(reader,diagonal);
        }

    }

    public void calculatePath(AbstractFindPathInputReader reader,boolean diagonal) throws InterruptedException {
        Thread mazeSolverThread = new Thread(new PathFinder(reader.getCharMap(),diagonal));
        mazeSolverThread.start();
        mazeSolverThread.join();
    }


}