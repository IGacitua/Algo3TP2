package game.units.behavior.player;

public class TurnUndoTracker {
    private boolean undoUsed = false;

    public void consumeUndo() {
        if (undoUsed) {
            throw new IllegalStateException("Undo has already been used this turn.");
        }
        undoUsed = true;
    }

    public void reset() {
        undoUsed = false;
    }

    public boolean isUndoAvailable() {
        return !undoUsed;
    }
}
