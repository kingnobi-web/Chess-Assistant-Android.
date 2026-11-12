# Chess Assistant Android

Android starter app for manually entering FEN positions while practicing against computer bots.

## Build on GitHub
1. Open Actions > Build Android APK.
2. Run workflow (or open the push-triggered run).
3. Once successful, download ChessAssistant-debug-apk artifact.
4. Extract the ZIP and install app-debug.apk on Android.

## Status
The UI and UCI engine interface are implemented. **Stockfish is not bundled**, so the APK does not yet calculate moves. The engine interface currently looks for a binary in private app storage; modern Android execution restrictions mean the engine must instead be packaged properly as an Android-compatible native executable/library before this feature can work. This is a scaffold, not a finished chess analyzer.

## Next steps
- Package ARM64 Stockfish with GPLv3 license/source compliance.
- Add interactive board and FEN editor.
- Display best move and evaluation.
- Limit use to computer-bot practice, not live competitive assistance.
