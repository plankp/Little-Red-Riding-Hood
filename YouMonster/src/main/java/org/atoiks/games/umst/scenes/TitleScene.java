package org.atoiks.games.umst.scenes;

import org.atoiks.games.umst.HorizontalPage;

public class TitleScene extends HorizontalPage {

    private byte phase;

    private static String[] title = {
        "Once upon a time...",
        "There was a dear little girl who was loved by everyone who looked at her,",
        "but most of all by her grandmother.",
        "Once she gave her a little riding hood of red velvet,",
        "which suited her so well that she would never wear anything else;",
        "so she was always called 'Little Red Riding Hood.'"
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
