package pt.isel.poo.li21d.g10.minesweeper.model;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * Represents the Mines Game State
 */
public class Game implements Iterable<Cell> {

    @Override
    public Iterator<Cell> iterator() {
        return new Iterator<Cell>() {
            private Iterator<Number> numberItr = numbers.iterator();
            private Iterator<Mine> mineItr = mines.iterator();

            @Override
            public boolean hasNext() {
                return numberItr.hasNext() || mineItr.hasNext();
            }

            @Override
            public Cell next() {
                if (numberItr.hasNext()) return numberItr.next();
                return mineItr.next();
            }
        };
    }

    /**
     * Contract to be supported by listeners of game events
     */
    public interface GameListener {

        /**
         * Signals that a cell has been stepped on
         *
         * @param cell the cell that has been stepped on
         */
        void onCellStep(Cell cell);

        /**
         * Signals that a flag has been changed
         *
         * @param cell the cell in which the flag status has been changed
         */
        void onFlagChange(Cell cell);

        /**
         * Signals that the game was Won
         */
        void onWin();
    }

    private Cell[][] board;
    private final int X_MAX = 12;
    private final int Y_MAX = 12;
    private final int LEVEL = 0;
    private final int NUMBOMBS = 20 + (LEVEL * 5);
    private LinkedList<Mine> mines;
    private LinkedList<Number> numbers;
    public int bomb_count;
    public int hiddenNumbers;
    public final long INIT_TIME;

    private GameListener listener;

    public Game() {
        board = new Cell[X_MAX][Y_MAX];
        bomb_count = 0;
        mines = new LinkedList<>();
        numbers = new LinkedList<>();
        INIT_TIME = System.currentTimeMillis();

        hiddenNumbers = (((Y_MAX * X_MAX) - NUMBOMBS) / NUMBOMBS) * LEVEL;

        setBombs();

        for (int i = 0; i < X_MAX; i++) {
            for (int j = 0; j < Y_MAX; j++) {
                if (!(board[i][j] instanceof Mine)) {
                    int mines = calculateMines(i, j);
                    boolean hidden = false;
                    if(hiddenNumbers!=0 && mines!=0) {
                        int hide =1 + (int) (Math.random()*10);
                        hidden = hide <= 3 ? true : false;
                        if (hidden) hiddenNumbers--;
                    }
                    Number num = new Number(i, j, mines, hidden);
                    board[i][j] = num;
                    numbers.add(num);
                }
            }
        }
    }

    /**
     * Method used to add mines to the board
     */
    private void setBombs() {
        while (bomb_count < NUMBOMBS) {
            int x = (int) (Math.random() * X_MAX - 1);
            int y = (int) (Math.random() * Y_MAX - 1);
            if (!(board[x][y] instanceof Mine)) {
                Mine mine = new Mine(x, y);
                board[x][y] = mine;
                mines.add(mine);
                bomb_count++;
            }
        }
    }

    /**
     * Method used to calculate number of mines in the neighbourhood.
     *
     * @param x the horizontal coordinate.
     * @param y the vertical coordinate.
     * @return A int value indicating number of bombs nearby.
     */
    private int calculateMines(int x, int y) {
        int count = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i >= 0 && x + i < X_MAX && y + j >= 0 && y + j < Y_MAX) {
                    if (board[x + i][y + j] instanceof Mine)
                        count++;
                }
            }
        }
        return count;
    }

    /**
     * Method used to start the board with a zone without bombs nearby, when the cell
     * is number and have 0 mines,calls stepCell method.
     */
    public void start() {
        for (; ; ) {
            int x = (int) (Math.random() * X_MAX - 1);
            int y = (int) (Math.random() * Y_MAX - 1);
            if (board[x][y] instanceof Number) {
                Number cell = (Number) board[x][y];
                if (cell.mines == 0) {
                    stepCell(cell.position.x, cell.position.y);
                    return;
                }
            }
        }
    }

    /**
     * Method is responsible  for the logic of the clicks.
     * If the cell as a Flag, returns false, that means that this cell cannot be clicked on.
     * Else, calls the method stepAround
     *
     * @param x the horizontal coordinate.
     * @param y the vertical coordinate.
     * @return A boolean that returns true if the player clicks on a bombs, and
     * false if not.
     */
    public boolean stepCell(int x, int y) {
        Cell cell = board[x][y];
        if (cell.getFlag() || cell.isVisible()) return false;
        if (cell instanceof Mine) {
            cell.stepOn();
            listener.onCellStep(cell);
            return true;
        }
        cell.stepOn();
        listener.onCellStep(cell);
        Number num = (Number) cell;
        if (num.mines == 0) stepAround(x, y);
        isVictory();
        return false;
    }

    /**
     * Steps on all the cells surrounding the given coordinates
     *
     * @param x horizontal coordinate
     * @param y vertical coordinate
     */
    private void stepAround(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (x + i >= 0 && x + i < X_MAX && y + j >= 0 && y + j < Y_MAX) {
                    if (!(board[x + i][y + j].isVisible())) stepCell(x + i, y + j);
                }
            }
        }
    }

    /**
     * Sets flag at given coordinate
     *
     * @param x The horizontal coordinate
     * @param y The vertical coordinate
     */
    public void setFlag(int x, int y) {
        if (board[x][y].isVisible() || (!board[x][y].getFlag() && bomb_count == 0)) return;
        board[x][y].changeFlag();
        bomb_count += board[x][y].getFlag() ? -1 : +1;
        listener.onFlagChange(board[x][y]);
    }

    /**
     * Checks whether the player won or not
     *
     * @return A boolean value indicating if the game was won
     */
    public boolean isVictory() {
        for (Number num : numbers) {
            if (!num.isVisible()) return false;
        }
        listener.onWin();
        return true;
    }

    /**
     * Register the given listener to be notified when relevant game events occur
     *
     * @param listener the listener instance
     */

    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    public void save(PrintWriter out) {
        StringBuilder s;
        for (int i = 0; i < Y_MAX ; i++) {
            for (int j = 0; j < X_MAX; j++) {
                s = new StringBuilder();
                Cell cell = board[j][i];
                if (cell instanceof Number)
                    s.append('N');//Numbers
                else
                    s.append('M');//Mines
                s.append(cell.getFlag() ? "F" : "U");
                s.append(cell.isVisible() ? "V" : "I");
                out.println(s.toString());
            }
        }
        Integer b = bomb_count;
        out.println(b.toString());
    }

    public void load(Scanner in) {
        Cell cell;
        String s;
        mines = new LinkedList<>();
        numbers = new LinkedList<>();
        for (int y = 0; y <Y_MAX; y++) {
            for (int x = 0; x < X_MAX; x++) {
                s = in.next();
                if (s.charAt(0)=='N') {
                    cell = new Number(x, y);
                    numbers.add((Number)cell);
                }else {
                    cell = new Mine(x, y);
                    mines.add((Mine)cell);
                }
                board[x][y] = cell;
                cell.setFlag(s.charAt(1)=='F'? true : false);
                cell.setVisible(s.charAt(2)=='V' ? true : false);
            }
        }
        calculateAllNum();
        s = in.next();
        int b = s.charAt(0) - '0';
        for (int i = 1; i <s.length(); i++) {
            b*=10;
            b+=s.charAt(i) - '0';
        }
        this.bomb_count = b;
    }

    private void calculateAllNum(){
        for (int y = 0; y < Y_MAX; y++) {
            for (int x = 0; x < X_MAX; x++) {
                if(board[x][y] instanceof Number) {
                    Number n = (Number) board[x][y];
                    n.setMines(calculateMines(x,y));
                }
            }
        }

    }

    public String saveGame() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < Y_MAX ; i++) {
            for (int j = 0; j < X_MAX; j++) {
                Cell cell = board[j][i];
                if (cell instanceof Number)
                    s.append('N');//Numbers
                else
                    s.append('M');//Mines
                s.append(cell.getFlag() ? "F" : "U");
                s.append(cell.isVisible() ? "V" : "I");
            }
        }
        s.append(bomb_count);
        return s.toString();
    }

    public void loadGame(String g) {
        Cell cell;
        int runner = 0;
        mines = new LinkedList<>();
        numbers = new LinkedList<>();
        for (int y = 0; y < Y_MAX; y++) {
            for (int x = 0; x < X_MAX; x++) {

                if (g.charAt(runner++) == 'N') {
                    cell = new Number(x, y);
                    numbers.add((Number) cell);
                } else {
                    cell = new Mine(x, y);
                    mines.add((Mine) cell);
                }
                board[x][y] = cell;
                cell.setFlag(g.charAt(runner++) == 'F' ? true : false);
                cell.setVisible(g.charAt(runner++) == 'V' ? true : false);
            }
        }
        calculateAllNum();
        int b = g.charAt(runner++) - '0';
        for (; runner < g.length(); runner++) {
            b *= 10;
            b += g.charAt(runner) - '0';
        }
        this.bomb_count = b;

    }
}
