package pt.isel.poo.li21d.g10.minesweeper.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import pt.isel.poo.li21d.g10.minesweeper.tile.Tile;

public class CellView implements Tile {
    @Override
    public void draw(Canvas canvas, int side) {
        canvas.drawColor(Color.GRAY);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
