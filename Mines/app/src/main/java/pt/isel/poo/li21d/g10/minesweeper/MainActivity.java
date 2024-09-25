package pt.isel.poo.li21d.g10.minesweeper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import pt.isel.poo.li21d.g10.minesweeper.model.Game;
import pt.isel.poo.li21d.g10.minesweeper.tile.TilePanel;
import pt.isel.poo.li21d.g10.minesweeper.view.GameView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MINES_APP";

    private Game game;
    private GameView view;

    private String saveFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate() starts");
        saveFile = getFilesDir().getAbsolutePath() + "/savedData.txt";

        setContentView(R.layout.activity_main);

        final TilePanel board = findViewById(R.id.tilePanel);

        final TextView numBombs = findViewById((R.id.numBombs));

        game = new Game();
        view = new GameView(game, board, numBombs);



        final TextView bombs = findViewById((R.id.bombs));
        numBombs.setText("" + game.bomb_count);

        final Button newGame = findViewById(R.id.newGame);
        newGame.setVisibility(View.INVISIBLE);

        final TextView lmessage = findViewById(R.id.Lmessage);
        lmessage.setVisibility(View.INVISIBLE);
        final TextView wmessage = findViewById(R.id.Wmessage);
        wmessage.setVisibility(View.INVISIBLE);

        view.setViewListener(new GameView.ViewListener() {
            @Override
            public void onLoose() {
                lmessage.setVisibility(View.VISIBLE);
                final Button save = findViewById(R.id.save);
                save.setEnabled(false);
                final Button newGame = findViewById(R.id.newGame);
                newGame.setVisibility(View.VISIBLE);
                newGame.setOnClickListener(v -> {
                    restart();
                    save.setEnabled(true);
                    lmessage.setVisibility(View.INVISIBLE);
                    newGame.setVisibility(View.INVISIBLE);
                });
            }

            @Override
            public void onWin() {
                wmessage.setVisibility(View.VISIBLE);
                final Button save = findViewById(R.id.save);
                save.setEnabled(false);
                final Button newGame = findViewById(R.id.newGame);
                newGame.setVisibility(View.VISIBLE);
                newGame.setOnClickListener(v -> {
                    restart();
                    save.setEnabled(true);
                    wmessage.setVisibility(View.INVISIBLE);
                    newGame.setVisibility(View.INVISIBLE);
                });
            }
        });

        final Switch flag = findViewById(R.id.Flag);
        flag.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                view.setFlag(flag.isChecked());
            }
        });

        final Button restart = findViewById(R.id.restart);
        restart.setOnClickListener(v -> {
            restart();

        });

        final Button save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            onSave();
        });

        final Button load = findViewById(R.id.load);
        load.setOnClickListener(v -> {
            onLoad();
        });

        Log.v(TAG, "onCreate() ends");
    }

    public void restart() {
        final TilePanel board = findViewById(R.id.tilePanel);
        final TextView numBombs = findViewById((R.id.numBombs));
        game = new Game();
        view.setGame(game);
        view.reload();
        numBombs.setText("" + game.bomb_count);

    }
    public void onSave() {
        try (PrintWriter file = new PrintWriter(new FileWriter(saveFile))) {
            game.save(file);
            Log.v(TAG , "Saved");
        } catch (Exception ioe) {
            ioe.printStackTrace();
            Log.v(TAG , "Save Failed");
        }
    }


    public void onLoad() {
        try (BufferedReader file = new BufferedReader(new FileReader(saveFile))) {
            Scanner in = new Scanner(file);
            game.load(in);
            Log.v(TAG,"Loaded");
        } catch (Exception ioe) {
            ioe.printStackTrace();
            Log.v(TAG , "Load Failed");
        }
        view.reload();
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {
        super.onRestoreInstanceState (savedInstanceState);
        String g = savedInstanceState.getString("Game");
        game.loadGame(g);
        view.reload();

    }
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState (outState);
        outState.putString("Game",game.saveGame());
    }

}
