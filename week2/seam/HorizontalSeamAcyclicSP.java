import edu.princeton.cs.algs4.Stack;

public class HorizontalSeamAcyclicSP {
    private double[][] distTo;
    private Point[][] edgeTo;

    private final double[][] energies;
    private final int width;


    public HorizontalSeamAcyclicSP(double[][] energies) {
        this.energies = energies.clone();
        width = energies.length;
        int height = energies[0].length;

        distTo = new double[width][height];
        edgeTo = new Point[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                distTo[x][y] = Double.POSITIVE_INFINITY;
            }
        }

        // border always has 1000 energy
        for (int y = 0; y < height; y++) {
            distTo[0][y] = 1000;
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Point point = new Point(x, y);
                if (point.getX() >= width - 1) {
                    continue;
                }

                if (point.getY() > 0) {
                    Point point1 = new Point(point.getX() + 1, point.getY() - 1);
                    relax(point, point1);
                }

                Point point2 = new Point(point.getX() + 1, point.getY());
                relax(point, point2);

                if (point.getY() < height - 1) {
                    Point point3 = new Point(point.getX() + 1, point.getY() + 1);
                    relax(point, point3);
                }
            }
        }
    }


    public double distTo(int x, int y) {
        return distTo[x][y];
    }

    public boolean hasPathTo(int x, int y) {
        return distTo[x][y] < Double.POSITIVE_INFINITY;
    }

    public int[] pathTo(int y) {
        if (!hasPathTo(width - 1, y)) {
            return new int[0];
        }

        Stack<Point> path = new Stack<>();
        path.push(new Point(width - 1, y));
        for (Point p = edgeTo[width - 1][y]; p != null; p = edgeTo[p.getX()][p.getY()]) {
            path.push(p);
        }

        int[] pathArr = new int[path.size()];
        int i = 0;
        for (Point p : path) {
            pathArr[i] = p.getY();
            i++;
        }

        return pathArr;
    }

    private void relax(Point from, Point to) {
        if (distTo[to.getX()][to.getY()] > distTo[from.getX()][from.getY()] + energies[to.getX()][to.getY()]) {
            distTo[to.getX()][to.getY()] = distTo[from.getX()][from.getY()] + energies[to.getX()][to.getY()];
            edgeTo[to.getX()][to.getY()] = from;
        }
    }

    public static void main(String[] args) {
        // empty
    }
}
