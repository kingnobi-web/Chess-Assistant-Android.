package com.kingnobi.chessassistant;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.*;
import java.io.*;
import java.util.concurrent.*;

public class MainActivity extends Activity {
    private EditText fen;
    private TextView output;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final String START = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(24, 40, 24, 24);
        TextView title = new TextView(this);
        title.setText("Chess Assistant ♟");
        title.setTextSize(26);
        title.setTypeface(null, Typeface.BOLD);
        root.addView(title);
        TextView help = new TextView(this);
        help.setText("Enter a FEN position to analyze a computer-bot game. Stockfish must be installed as an executable in the app's private files directory.");
        root.addView(help);
        fen = new EditText(this);
        fen.setSingleLine(false);
        fen.setMinLines(3);
        fen.setText(START);
        root.addView(fen);
        Button analyze = new Button(this);
        analyze.setText("Analyze position");
        root.addView(analyze);
        output = new TextView(this);
        output.setText("Ready. Enter a FEN position.");
        output.setTextSize(18);
        root.addView(output);
        ScrollView scroll = new ScrollView(this);
        scroll.addView(root);
        setContentView(scroll);
        analyze.setOnClickListener(v -> analyzePosition());
    }

    private void analyzePosition() {
        String position = fen.getText().toString().trim();
        if (!position.matches("([^/ ]+/){7}[^ ]+ [wb] [KQkq-]+ ([a-h][36]|-) [0-9]+ [0-9]+")) {
            output.setText("Invalid FEN format. Enter all six FEN fields.");
            return;
        }
        output.setText("Analyzing...");
        executor.execute(() -> {
            String result = StockfishEngine.analyze(getFilesDir(), position);
            runOnUiThread(() -> output.setText(result));
        });
    }
    @Override protected void onDestroy() {
        executor.shutdownNow();
        super.onDestroy();
    }
}
