package pt.isel.poo.li21d.g10.minesweeper.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import pt.isel.poo.li21d.g10.minesweeper.R;
import pt.isel.poo.li21d.g10.minesweeper.model.Mine;
import pt.isel.poo.li21d.g10.minesweeper.tile.Img;
import pt.isel.poo.li21d.g10.minesweeper.tile.Tile;

public class MineView extends CellView {

    private final Img image;
    private final Paint brush;

    public MineView(Context context, Mine mine) {
        image = new Img(context, mine.isVisible() ? R.drawable.bombx : R.drawable.bomb);
        brush = new Paint();
    }

    @Override
    public void draw(Canvas canvas, int side) {
        image.draw(canvas, side, side, brush);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
