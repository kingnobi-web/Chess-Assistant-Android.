package com.kingnobi.chessassistant;

import java.io.*;
import java.util.concurrent.*;

public final class StockfishEngine {
    private StockfishEngine() {}
    public static String analyze(File filesDir, String fen) {
        File engine = new File(filesDir, "stockfish");
        if (!engine.exists() || !engine.canExecute()) {
            return "Stockfish binary not installed. This starter APK builds successfully, but analysis requires a compatible Stockfish executable. See README.";
        }
        Process process = null;
        try {
            process = new ProcessBuilder(engine.getAbsolutePath()).redirectErrorStream(true).start();
            final Process p = process;
            ExecutorService pool = Executors.newSingleThreadExecutor();
            try {
                Future<String> result = pool.submit(() -> {
                    try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                         BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                        writer.write("uci\nsetoption name Threads value 1\nisready\nposition fen " + fen + "\ngo depth 12\n");
                        writer.flush();
                        String line;
                        String best = null;
                        while ((line = reader.readLine()) != null) {
                            if (line.startsWith("bestmove ")) {
                                best = line.split(" ")[1];
                                break;
                            }
                        }
                        writer.write("quit\n");
                        writer.flush();
                        return best == null ? "No best move returned." : "Best move (UCI): " + best;
                    }
                });
                return result.get(20, TimeUnit.SECONDS);
            } finally {
                pool.shutdownNow();
            }
        } catch (Exception e) {
            return "Engine error: " + e.getClass().getSimpleName() + ": " + e.getMessage();
        } finally {
            if (process != null) process.destroyForcibly();
        }
    }
}
