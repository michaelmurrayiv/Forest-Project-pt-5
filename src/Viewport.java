public final class Viewport {
    private int row;
    private int col;
    private int numRows;
    public int numCols;

    public Viewport(int numRows, int numCols) {
        this.setNumRows(numRows);
        this.numCols = numCols;
    }
    public void shift(int col, int row) {
        this.setCol(col);
        this.row = row;
    }
    public Point viewportToWorld(int col, int row) {
        return new Point(col + this.getCol(), row + this.row);
    }

    public Point worldToViewport(int col, int row) {
        return new Point(col - this.getCol(), row - this.row);
    }
    public boolean contains(Point p) {
        return p.y >= row && p.y < row + getNumRows() && p.x >= getCol() && p.x < getCol() + numCols;
    }

    public int getRow() {return row;}

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }
}
