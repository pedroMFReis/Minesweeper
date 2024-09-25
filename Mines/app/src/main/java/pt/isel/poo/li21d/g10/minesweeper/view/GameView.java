package pt.isel.poo.li21d.g10.minesweeper.view;

import android.widget.TextView;

import pt.isel.poo.li21d.g10.minesweeper.MainActivity;
import pt.isel.poo.li21d.g10.minesweeper.model.Cell;
import pt.isel.poo.li21d.g10.minesweeper.model.Coordinate;
import pt.isel.poo.li21d.g10.minesweeper.model.Game;
import pt.isel.poo.li21d.g10.minesweeper.model.Mine;
import pt.isel.poo.li21d.g10.minesweeper.model.Number;
import pt.isel.poo.li21d.g10.minesweeper.tile.OnTileTouchListener;
import pt.isel.poo.li21d.g10.minesweeper.tile.TilePanel;


public class GameView {
    private Game game;
    private TilePanel tilePanel;
    private TextView textView;
    CellView[][] tiles;
    private final int HEIGHT;
    private final int WIDTH;
    private boolean flag = false;
    private boolean over = false;

    private ViewListener listener;

    public GameView(Game model, TilePanel tile, TextView bombs) {
        this.game = model;
        this.tilePanel = tile;
        this.textView = bombs;

        HEIGHT = tilePanel.getHeightInTiles();
        WIDTH = tilePanel.getWidthInTiles();

        tiles = new CellView[WIDTH][HEIGHT];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                tiles[j][i] = new CellView();
            }
        }
        tilePanel.setAllTiles(tiles);

        begin();
        tilePanel.setListener(new OnTileTouchListener() {
            @Override
            public boolean onClick(int xTile, int yTile) {
                if (!flag)
                    setOver(game.stepCell(yTile, xTile));
                else game.setFlag(yTile, xTile);
                return over;
            }

            @Override
            public boolean onDrag(int xFrom, int yFrom, int xTo, int yTo) {
                game.setFlag(yTo, xTo);
                tilePanel.setTile(xTo, yTo, new FlagView(tilePanel.getContext()));
                return false;

            }

            @Override
            public void onDragEnd(int x, int y) {

            }

            @Override
            public void onDragCancel() {

            }
        });
    }

    private void begin() {
        game.setGameListener(new Game.GameListener() {
            @Override
            public void onCellStep(Cell cell) {
                CellView view;
                if (cell instanceof Number) {
                    view = new NumberView((Number) cell);
                    tilePanel.setTile(cell.getPosition().y, cell.getPosition().x, view);
                    if(game.isVictory()) listener.onWin();
                } else if (cell instanceof Mine) {
                    setOver(true);
                    showAll();
                    listener.onLoose();
                }
                tilePanel.invalidate();
            }

            @Override
            public void onFlagChange(Cell cell) {
                Coordinate spot = cell.getPosition();
                if (!cell.getFlag()) tilePanel.setTile(spot.y, spot.x, new CellView());
                else tilePanel.setTile(spot.y, spot.x, new FlagView(tilePanel.getContext()));

                textView.setText("" + game.bomb_count);
                tilePanel.invalidate();
            }

            @Override
            public void onWin() {
                //showAll();
            }
        });

        game.start();
    }

    private void showAll() {
        CellView tile;
        for (Cell cell : game) {
            if (cell instanceof Number) {
                tile = new NumberView((Number) cell);
            } else tile = new MineView(tilePanel.getContext(), (Mine) cell);
            tilePanel.setTile(cell.getPosition().y, cell.getPosition().x, tile);
            cell.stepOn();
        }
    }

    public void setGame(Game game){
        this.game = game;
        begin();
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public boolean isOver() {
        return over;
    }

    public void reload(){
        tiles = new CellView[WIDTH][HEIGHT];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                tiles[j][i] = new CellView();
            }
        }
        tilePanel.setAllTiles(tiles);

        CellView tile;
        for (Cell cell : game){
            if(cell.isVisible()){
                if (cell instanceof Number) {
                    tile = new NumberView((Number) cell);
                } else tile = new MineView(tilePanel.getContext(), (Mine) cell);
                tilePanel.setTile(cell.getPosition().y, cell.getPosition().x, tile);
            }
            if(cell.getFlag()){
                tilePanel.setTile(cell.getPosition().y, cell.getPosition().x, new FlagView(tilePanel.getContext()));
            }
        }
        textView.setText("" + game.bomb_count);
        tilePanel.invalidate();
    }

    public void setViewListener(ViewListener listener) {this.listener = listener;}

    public interface ViewListener {
        void onLoose();
        void onWin();
    }
}
