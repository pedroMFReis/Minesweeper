package pt.isel.poo.li21d.g10.minesweeper.model;

/**
 * Class that captures commonalities between all game's cells.
 */

public abstract class Cell {

    /**
     * The cell current position
     */
    protected Coordinate position;


    protected Game game;

    /**
     *
     */

    private boolean flag = false;
    /**
     * The cell visible state
     */
    private boolean visible = false;

    /**
     * Initiates the cell at the given coordinates
     *
     * @param x The horizontal coordinate
     * @param y The vertical coordinate
     */
    public Cell(int x, int y) {
        this.position = new Coordinate(x, y);
    }

    /**
     * Initiates the cell at the given coordinates
     *
     * @param pos The initial coordinate
     */
    public Cell(Coordinate pos,Game game) {
        position = pos;
        this.game = game;
    }



    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * Gets the cell position
     *
     * @return the cell position
     */

    public Coordinate getPosition() {
        return position;
    }

    /**
     * Change the flag state
     */
    public void changeFlag() {
        flag = !flag;
    }

    /**
     * Get the flag state of the cell
     *
     * @return the cell flag state
     */
    public boolean getFlag() {
        return flag;
    }

    /**
     * Change the visible state of the cell
     */
    public void stepOn() {
        visible = true;
    }

    /**
     * Get the state visible state
     *
     * @return the cell visible state
     */
    public boolean isVisible() {
        return visible;
    }
}