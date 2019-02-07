package org.atoiks.games.umst.scenes;

import org.atoiks.games.umst.HorizontalPage;

public class WolfEatsGirlScene extends HorizontalPage {

    private byte phase;

    private static String[] title = {
        "When she went into the room,",
        "she had such a strange feeling that she said to herself:",
        "'Oh dear! how uneasy I feel today, and at other times I like being with grandmother so much.'",
        "She called out:",
        "'Good morning,'",
        "but received no answer;",
        "so she went to the bed.",
        "There lay her grandmother with her cap pulled far over her face,",
        "and looking very strange.",

        // dialogue starts:
        "'Oh! grandmother, what big ears you have!'",
        "'All the better to hear you with, my child,' was the reply.",
        "'But, grandmother, what big eyes you have!' she said.",
        "'All the better to see you with, my dear.'",
        "'But, grandmother, what large hands you have!'",
        "'All the better to hug you with.'",
        "'Oh! but, grandmother, what a terrible big mouth you have!'",
        "'All the better to eat you with!'",
        "And scarcely had the wolf said this,",
        "than with one bound he was out of bed and swallowed up Red Riding Hood.",
        "When the wolf had appeased his appetite,",
        "he lay down again in the bed,",
        "fell asleep and began to snore very loud..."
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
