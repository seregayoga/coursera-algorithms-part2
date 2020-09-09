import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energies;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }

        this.picture = new Picture(picture);

        this.energies = new double[picture.width()][picture.height()];
        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                energies[x][y] = energy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= picture.width()) {
            throw new IllegalArgumentException();
        }

        if (y < 0 || y >= picture.height()) {
            throw new IllegalArgumentException();
        }

        if (x == 0 || x == picture.width() - 1) {
            return 1000;
        }
        if (y == 0 || y == picture.height() - 1) {
            return 1000;
        }

        // used hack from https://algs4.cs.princeton.edu/code/javadoc/edu/princeton/cs/algs4/Picture.html
        // to avoid creating additional Color objects.
        int rgbXPlus1 =  picture.getRGB(x + 1, y);
        int rXPlus1 = (rgbXPlus1 >> 16) & 0xFF;
        int gXPlus1 = (rgbXPlus1 >>  8) & 0xFF;
        int bXPlus1 = (rgbXPlus1 >>  0) & 0xFF;

        int rgbXMinus1 =  picture.getRGB(x - 1, y);
        int rXMinus1 = (rgbXMinus1 >> 16) & 0xFF;
        int gXMinus1 = (rgbXMinus1 >>  8) & 0xFF;
        int bXMinus1 = (rgbXMinus1 >>  0) & 0xFF;

        int rgbYPlus1 =  picture.getRGB(x, y + 1);
        int rYPlus1 = (rgbYPlus1 >> 16) & 0xFF;
        int gYPlus1 = (rgbYPlus1 >>  8) & 0xFF;
        int bYPlus1 = (rgbYPlus1 >>  0) & 0xFF;

        int rgbYMinus1 =  picture.getRGB(x, y - 1);
        int rYMinus1 = (rgbYMinus1 >> 16) & 0xFF;
        int gYMinus1 = (rgbYMinus1 >>  8) & 0xFF;
        int bYMinus1 = (rgbYMinus1 >>  0) & 0xFF;

        return Math.sqrt(
            Math.pow(rXPlus1 - rXMinus1, 2) + Math.pow(gXPlus1 - gXMinus1, 2) + Math.pow(bXPlus1 - bXMinus1, 2) +
            Math.pow(rYPlus1 - rYMinus1, 2) + Math.pow(gYPlus1 - gYMinus1, 2) + Math.pow(bYPlus1 - bYMinus1, 2)
        );
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] seam = null;

        HorizontalSeamAcyclicSP sp = new HorizontalSeamAcyclicSP(energies);

        double minDist = Double.POSITIVE_INFINITY;
        for (int y = 0; y < height(); y++) {
            double dist = sp.distTo(width() - 1, y);
            if (dist < minDist) {
                minDist = dist;
                seam = sp.pathTo(y);
            }
        }

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] seam = null;

        VerticalSeamAcyclicSP sp = new VerticalSeamAcyclicSP(energies);

        double minDist = Double.POSITIVE_INFINITY;
        for (int x = 0; x < width(); x++) {
            double dist = sp.distTo(x, height() - 1);
            if (dist < minDist) {
                minDist = dist;
                seam = sp.pathTo(x);
            }
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (height() <= 1) {
            throw new IllegalArgumentException();
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException();
        }
        for (int x = 1; x < width(); x++) {
            if (Math.abs(seam[x] - seam[x - 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }

        Picture scaledPicture = new Picture(width(), height() - 1);
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                if (y < seam[x]) {
                    scaledPicture.setRGB(x, y, picture.getRGB(x, y));
                } else if (y > seam[x]) {
                    scaledPicture.setRGB(x, y - 1, picture.getRGB(x, y));
                }
            }
        }

        picture = scaledPicture;

        energies = new double[width()][height()];
        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                energies[x][y] = energy(x, y);
            }
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (width() <= 1) {
            throw new IllegalArgumentException();
        }
        if (seam.length != height()) {
            throw new IllegalArgumentException();
        }
        for (int y = 1; y < height(); y++) {
            if (Math.abs(seam[y] - seam[y - 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }

        Picture scaledPicture = new Picture(width() - 1, height());
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (x < seam[y]) {
                    scaledPicture.setRGB(x, y, picture.getRGB(x, y));
                } else if (x > seam[y]) {
                    scaledPicture.setRGB(x - 1, y, picture.getRGB(x, y));
                }
            }
        }

        picture = scaledPicture;

        energies = new double[width()][height()];
        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                energies[x][y] = energy(x, y);
            }
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        // empty
    }
}
