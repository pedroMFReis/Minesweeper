package pt.isel.poo.li21d.g10.minesweeper.model;


/**
 * Class that represents all the numbers in the game
 */
public class Number extends Cell {

    /**
     * The number of mines in the cell surroundings
     */
    public  int mines = 0;

    public  boolean hidden = false;

    /**
     * Initiates the Number cell Instance
     *
     * @param x     horizontal coordinate
     * @param y     vertical coordinate
     * @param mines mines in the surrounding cells
     */
    public Number(int x, int y, int mines, boolean hide) {
        super(x, y);
        this.mines = mines;
        this.hidden = hide;
    }

    public Number(int x, int y) {
        super(x,y);
    }

    public void setMines(int n){
        mines = n;
    }

}

