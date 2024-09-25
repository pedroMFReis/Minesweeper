package pt.isel.poo.li21d.g10.minesweeper.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import pt.isel.poo.li21d.g10.minesweeper.model.Number;

public class NumberView extends CellView {
    private final Paint brush;
    private final Number number;
    private int setColor[] = {Color.CYAN, Color.GREEN, Color.RED, Color.BLUE, Color.MAGENTA, Color.BLACK, Color.YELLOW, Color.GRAY};

    public NumberView(Number number) {
        brush = new Paint();
        this.number = number;
    }

    @Override
    public void draw(Canvas canvas, int side) {
        brush.setColor(Color.DKGRAY);
        if (number.mines != 0 && !number.hidden)
            brush.setColor(setColor[number.mines - 1]);
        brush.setTextSize(((float) side) / 1.0f);
        brush.setTypeface(Typeface.DEFAULT_BOLD);
        brush.setTextAlign(Paint.Align.CENTER);

        char c = (char) (number.mines + '0');
        c = c == '0' ? ' ' : c;
        if (number.hidden) c = '?';
        canvas.drawText("" + c, ((float) side) / 2.0f, (((float) side) * 5.0f) / 6.0f, brush);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return super.setSelect(selected);
    }
}
