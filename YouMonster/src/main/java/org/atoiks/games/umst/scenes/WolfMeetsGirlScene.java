package org.atoiks.games.umst.scenes;

import org.atoiks.games.umst.HorizontalPage;

public class WolfMeetsGirlScene extends HorizontalPage {

    private byte phase;

    // Anyway.. so you (as the wolf) talk to the girl
    private static String[] title = {
        "Good day!...",
    };

    @Override
    public void enter(int from) {
        phase = -1;
        nextPhase();
    }

    private boolean nextPhase() {
        final byte newPhase = ++phase;
        if (newPhase < title.length) {
            updateMessage(title[newPhase]);
            resetScrolling();
            return true;
        }
        return false;
    }

    @Override
    public boolean optionSelected(int opt) {
        if (!nextPhase()) {
            // last phase, so we move on to next scene
            return scene.gotoNextScene();
        }
        return true;
    }
}
