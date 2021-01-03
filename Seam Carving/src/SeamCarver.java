import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

/**
 * This class represents a data type that re-sizes a W-by-H image using the seam-carving technique.
 */
public class SeamCarver {
    private Picture p;
    private int W;
    private int H;
    private final int[][] color;


    /**
     * Create a seam carver object based on the given picture
     * @param picture the picture object used to construct a new seam carver object
     */
    public SeamCarver(Picture picture) {
        if (picture != null) {
            p = new Picture(picture);
        } else {
            throw new IllegalArgumentException("Input picture is null");
        }

        W = p.width();
        H = p.height();
        //initial color[][]
        color = new int[W][H];
        for (int col = 0; col < W; col++) {
            for (int row = 0; row < H; row++) {
                color[col][row] = p.get(col, row).getRGB();
            }
        }

    }

    /**
     * Return the current picture
     * @return the current picture
     */
    public Picture picture() {
        Picture newPicture = new Picture(W, H);
        for (int c = 0; c < W; c++) {
            for (int r = 0; r < H; r++) {
                newPicture.set(c, r, new Color(color[c][r]));
            }
        }
        return newPicture;
    }

    // width of current picture

    /**
     * Return the width of current picture
     * @return the width of current picutre
     */
    public int width() {
        return W;
    }

    // height of current picture

    /**
     * Return the height of current picture
     * @return height of current picture
     */
    public int height() {
        return H;
    }

    // energy of pixel at column x and row y

    /**
     * Calculate the energy of pixel at column x and row y.
     * The energy is a measure of the importance of each pixel.
     * The higher the energy, the less likely that the pixel will be included as part of a seam.
     * This method uses dual-gradient energy function to compute the energy at a pixel.
     * The energy of pixel (x,y) is sqrt(Δx(x,y)^2 + Δy(x,y)^2)
     * where the square of the x-gradient Δx(x,y)^2 = Rx(x,y)^2 + Gx(x,y)^2 + Bx(x,y)^2,
     * Rx(x,y) , Gx(x,y), and Bx(x,y) are the differences in the red, green, and blue components
     * between pixel (x + 1, y) and pixel (x − 1, y), respectively.
     * Δy(x,y)^2 is defined in an analogous manner.
     * @param x the column of a pixel
     * @param y the row of a pixel
     * @return the energy of pixel at (x,y)
     */
    public  double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IllegalArgumentException("Input pixel is out of bounds.");

        //We define the energy of a pixel at the border of the image to be 1000,
        // so that it is strictly larger than the energy of any interior pixel.
        if (x == 0 || x == (width() - 1) || y == 0 || y == (height() - 1))
            return 1000;

        double xGardientSquared =
                Math.pow((((color[x-1][y] >> 16) & 0xFF) - ((color[x+1][y] >> 16) & 0xFF)), 2)
                + Math.pow((((color[x-1][y] >> 8) & 0xFF) - ((color[x+1][y] >> 8) & 0xFF)), 2)
                + Math.pow(((color[x-1][y] & 0xFF) - (color[x+1][y] & 0xFF)), 2);

        double yGardientSquared =
                Math.pow((((color[x][y-1] >> 16) & 0xFF) - ((color[x][y+1] >> 16) & 0xFF)), 2)
                + Math.pow((((color[x][y-1] >> 8) & 0xFF) - ((color[x][y+1] >> 8) & 0xFF)), 2)
                + Math.pow(((color[x][y-1] & 0xFF) - (color[x][y+1] & 0xFF)), 2);

        return Math.sqrt(xGardientSquared + yGardientSquared);
    }

    /**
     * Returns an array of length W such that
     * entry x is the row number of the pixel to be removed from column x of the image.
     * @return  an array of length W where each entry is the row number of pixel to be removed from column x.
     */
    public int[] findHorizontalSeam() {
        double[][] energyArray = getAllEnergy();
        // Since in image processing, pixel (x, y) refers to the pixel in column x and row y,
        // with pixel (0, 0) at the upper left corner and pixel (W − 1, H − 1) at the bottom right corner.
        // We need to reverse the energyArray first.
        energyArray = reverse(energyArray);

        double[][] distTo = new double[H][W];
        for (double i[] : distTo) {
            for (double j : i) {
                j = Double.MAX_VALUE;
            }
        }
        short[][] edgeTo = new short[H][W];

        int[] result = findSeam(energyArray, distTo, edgeTo, energyArray.length, energyArray[0].length);

        //reverse result back
        for (int i = 0; i < result.length; i++) {
            result[i] = H - 1 - result[i];
        }
        return result;
    }

    /**
     * Returns an array of length H such that
     * entry y is the column number of the pixel to be removed from row y of the image.
     * @return  an array of length H where each entry is the column number of pixel to be removed from row y.
     */
    public int[] findVerticalSeam() {
        double[][] energyArray = getAllEnergy();
        double[][] distTo = new double[W][H];
        for (double i[] : distTo) {
            for (double j : i) {
                j = Double.MAX_VALUE;
            }
        }
        short[][] edgeTo = new short[W][H];

        return findSeam(energyArray, distTo, edgeTo, this.width(), this.height());
    }

    //

    /**
     * Remove horizontal seam from current picture
     * @param seam the horizontal seam to be removed.
     */
    public void removeHorizontalSeam(int[] seam) {
        if (H < 2) {
            throw new IllegalArgumentException("Picture height less than 2");
        }
        isValid(seam, false);
        for (int c = 0; c < W; c++) {
            for (int r = seam[c]; r < H - 1; r++) {
                color[c][r] = color[c][r + 1];
            }
        }
        H--;
    }

    /**
     * Remove vertical seam from current picture
     * @param seam the vertical seam to be removed.
     */
    public void removeVerticalSeam(int[] seam) {
        if (W < 2) {
            throw new IllegalArgumentException("Picture width less than 2");
        }
        isValid(seam, true);
        for (int r = 0; r < H; r++) {
            for (int c = seam[r]; c < W - 1; c++) {
                color[c][r] = color[c + 1][r];
            }
        }
        W--;
    }

    /**
     * A helper method that checks if the input seam is valid.
     * @param seam the input seam array.
     * @param isVertical true if the input seam is a vertical seam, otherwise it is a horizontal seam.
     */
    private void isValid(int[] seam, boolean isVertical) {
        if (seam == null) {
            throw new IllegalArgumentException("Input seam is null");
        }

        if (isVertical && seam.length != H) {
            throw new IllegalArgumentException("Input seam is an invalid vertical seam");
        }

        if (!isVertical && seam.length != W) {
            throw new IllegalArgumentException("Input seam is an invalid horizontal seam");
        }

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0) {
                throw new IllegalArgumentException("Input seam is illegal because at least one of its pixel is invalid.");
            }
            if (isVertical && seam[i] >= W) {
                throw new IllegalArgumentException("Input seam is illegal because at least one of its pixel is out of bound.");
            }
            if (!isVertical && seam[i] >= H) {
                throw new IllegalArgumentException("Input seam is illegal because at least one of its pixel is out of bound.");
            }
            if ((i != seam.length -1) && (seam[i] - seam[i + 1] > 1 || seam[i] - seam[i + 1] < -1)) {
                throw new IllegalArgumentException("Input seam is illegal because it doesn't have proper values.");
            }
        }
    }

    /**
     * A helper method that would reverse the given 2d array.
     * @param matrix the 2d array to be reversed.
     * @return the reversed 2d array.
     */
    private double[][] reverse(double[][] matrix) {
        if (matrix == null) {
            throw new IllegalArgumentException("Input is null");
        }
        double[][] result = new double[H][W];
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                result[i][j] = matrix[j][H - 1 - i];
            }
        }
        return result;
    }

    /**
     * A helper method that would find the seam.
     * @param energy the energy array of all pixels.
     * @param distTo the array containing distance to every pixels.
     * @param edgeTo the array containing the edge to every pixels.
     * @param W the width of the picture.
     * @param H the width of the picture.
     * @return the seam represented by a one dimension array.
     */
    private int[] findSeam(double[][] energy, double[][] distTo, short[][] edgeTo, int W, int H) {
        for (int row = 0; row < H; row++) {
            for (int col = 0; col < W; col++) {
                relax(energy, distTo, edgeTo, col, row, W, H);
            }
        }
        int minID = 0;
        for (int col = 0; col < W; col++) {
            if (distTo[col][H - 1] < distTo[minID][H - 1]) {
                minID = col;
            }
        }
        int[] result = new int[H];
        result[H - 1] = minID;
        for (int i = H - 2; i >= 0; i--) {
            result[i] = edgeTo[result[i + 1]][i + 1];
        }
        return result;
    }

    /**
     * A helper method that calculates the energy of every pixels, and return an 2d array containing them.
     * @return an 2d array containing every pixel's energy.
     */
    private double[][] getAllEnergy() {
        double[][] result = new double[W][H];
        for (int col = 0; col < width(); col++){
            for (int row = 0; row < height(); row++) {
                result[col][row] = energy(col, row);
            }
        }
        return result;
    }

    //update the distTo and edgeTo according to the input column c and row r

    /**
     * A helper method that would update the {@code distTo} and {@code edgeTo} arrays depending on pixel at column c and row r
     * @param energy the 2d array containing all energy of every pixel
     * @param distTo the array containing distance to every pixels.
     * @param edgeTo the array containing edge to every pixels.
     * @param col the column of a pixel
     * @param row the row of a pixel
     * @param W the width of the picture
     * @param H the height of the picture
     */
    private void relax(double[][] energy, double[][] distTo, short[][] edgeTo, int col, int row, int W, int H) {
        if (col < 0 || col >= W || row < 0 || row >= H) {
            throw new IllegalArgumentException("Input row or column is out of bounds");
        }
        if (row == 0) {
            distTo[col][row] = 1000;
            edgeTo[col][row] = -1;
            return;
        } else if (W == 1) {
            distTo[col][row] = distTo[col][row - 1] + energy[col][row];
            edgeTo[col][row] = (short)col;
            return;
        } else if (col == 0) {
            if (distTo[col][row - 1] <= distTo[col + 1][row - 1]) {
                distTo[col][row] = distTo[col][row - 1] + energy[col][row];
                edgeTo[col][row] = (short)col;
            } else {
                distTo[col][row] = distTo[col + 1][row - 1] + energy[col][row];
                edgeTo[col][row] = (short)(col + 1);
            }
            return;
        } else if (col == W-1) {
            if (distTo[col - 1][row - 1] <= distTo[col][row - 1]) {
                distTo[col][row] = distTo[col - 1][row - 1] + energy[col][row];
                edgeTo[col][row] = (short)(col - 1);
            } else {
                distTo[col][row] = distTo[col][row - 1] + energy[col][row];
                edgeTo[col][row] = (short)col;
            }
            return;
        }
        if (distTo[col - 1][row - 1] <= distTo[col][row - 1] && distTo[col - 1][row - 1] <= distTo[col + 1][row - 1]) {
            distTo[col][row] = distTo[col - 1][row - 1] + energy[col][row];
            edgeTo[col][row] = (short)(col - 1);
        }
        else if (distTo[col][row - 1] <= distTo[col + 1][row - 1]) {
            distTo[col][row] = distTo[col][row - 1] + energy[col][row];
            edgeTo[col][row] = (short)col;
        } else {
            distTo[col][row] = distTo[col + 1][row - 1] + energy[col][row];
            edgeTo[col][row] = (short)(col + 1);
        }
    }
}