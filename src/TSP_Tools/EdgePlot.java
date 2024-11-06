package TSP_Tools;

import TSP_Datasets.*;

import java.awt.*;
import java.util.Scanner;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

import javax.swing.*;

public class EdgePlot {

    private static class Point {

        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class MapPanel extends JPanel {

        private final int width;
        private final int height;

        private final Image map;
        private final Point[] pointsA;
        private final Point[] pointsB;

        public MapPanel(int width, int height, Image map, Point[] pointsA, Point[] pointsB) {
            this.setPreferredSize(new Dimension(width, height));
            this.width = width;
            this.height = height;
            this.pointsA = pointsA;
            this.pointsB = pointsB;
            this.map = map;
        }


        private void drawLine(Graphics g, Point from, Point to) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3));
            int xfrom = (int) (width * from.x);
            int yfrom = (int) (height * from.y);
            int xto   = (int) (width * to.x);
            int yto   = (int) (height * to.y);
            g2.drawLine(xfrom, yfrom, xto, yto);
        }

        private void drawLines(Graphics g, Point[] pointsA, Point[] pointsB) {
            for (int i = 0; i < pointsA.length; i++) {
                drawLine(g, pointsA[i], pointsB[i]);
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.red);
            g.drawImage(map, 0, 0, null);
            if (pointsA.length > 1 && pointsB.length > 1) {
                drawLines(g, pointsA, pointsB);
            }
        }
    }


    public static void plot(String title, Image image, int width, int height, Point[] pointsA, Point[] pointsB) {
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        if (width == 0) {
            if (height == 0) height = imageHeight;
            width = height * imageWidth / imageHeight;
        } else if (height == 0) {
            height = width * imageHeight / imageWidth;
        }

        Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        JFrame frame = new JFrame(title);
        JPanel panel = new MapPanel(width, height, scaled, pointsA, pointsB);

        panel.setPreferredSize(new Dimension(width, height));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.setSize(width, height);
        frame.setVisible(true);
        frame.pack();
    }


    public static Image readMap(String filename) throws IOException {
        return ImageIO.read(new File(filename));
    }

    public static Point[] readLocations(Scanner scanner, Region[] regions) {
        Point[] locations = new Point[regions.length];
        while (scanner.hasNextLine()) {
            String[] fields = scanner.nextLine().split(",");
            Region region = Region.find(fields[0], regions);
            if (region == null) {
                String kind = Region.kind(regions);
                throw new IllegalArgumentException(String.format("Invalid %s: %s", kind, fields[0]));
            }
            double x = Double.parseDouble(fields[1]);
            double y = Double.parseDouble(fields[2]);
            locations[region.ordinal()] = new Point(x, y);
        }
        return locations;
    }

    public static String basename(String filename) {
        int dot = filename.lastIndexOf('.');
        return filename.substring(0, dot);
    }


    public static void main(String[] args) {
        Locations.Regions loc = Locations.Regions.US;

        for (String arg : args) {
            for (Locations.Regions region : Locations.Regions.values()) {
                if (arg.equalsIgnoreCase(region.toString())) loc = region;
            }
        }

        Region[] regions = Locations.values(loc);
        String mapfile = loc.name().replace('_', ' ').toLowerCase() + ".png";
        String folder = "src/TSP_Tools/Maps";
        int height = 650;
        int width = 0;

        Image image;
        Point[] locations;

        mapfile = folder + "/" + mapfile;
        try {
            image = readMap(mapfile);
        } catch (IOException e) {
            System.err.println("Could not read map file: " + mapfile);
            return;
        }

        try {
            locations = readLocations(new Scanner(new File(basename(mapfile) + ".csv")), regions);
        } catch (Exception e) {
            System.err.println("Could not read csv file: " + basename(mapfile) + ".csv");
            return;
        }

        String name = basename(mapfile);

        Scanner scanner = new Scanner(System.in);

        ArrayList<Point> pointsA = new ArrayList<>();
        ArrayList<Point> pointsB = new ArrayList<>();

        String line = scanner.nextLine();

        while (!line.isEmpty()) {
            String[] subs = line.split("[ _]");
            Region stateA = Locations.find(subs[1], loc);
            pointsA.add(locations[stateA.ordinal()]);

            Region stateB = Locations.find(subs[2], loc);
            pointsB.add(locations[stateB.ordinal()]);

            line = scanner.nextLine();
        }

        plot(name, image, width, height, pointsA.toArray(new Point[0]), pointsB.toArray(new Point[0]));
    }
}