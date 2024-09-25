package pt.isel.poo.li21d.g10.minesweeper.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import pt.isel.poo.li21d.g10.minesweeper.R;
import pt.isel.poo.li21d.g10.minesweeper.tile.Img;
import pt.isel.poo.li21d.g10.minesweeper.tile.Tile;

public class FlagView extends CellView {

    private final Img image;
    private final Paint brush;

    public FlagView(Context context) {
        image = new Img(context, R.drawable.flag);
        brush = new Paint();
    }

    @Override
    public void draw(Canvas canvas, int side) {
        canvas.drawColor(Color.GRAY);
        image.draw(canvas, side, side, brush);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
