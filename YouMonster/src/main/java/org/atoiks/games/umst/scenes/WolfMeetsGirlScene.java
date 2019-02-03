package org.atoiks.games.umst.scenes;

import org.atoiks.games.umst.HorizontalPage;

public class WolfMeetsGirlScene extends HorizontalPage {

    private int phase;

    private int timesIntimidated;
    private int timesGreeted;

    // no need times advice offered since that automatically quits this scene

    @Override
    public void enter(int from) {
        timesIntimidated = 0;
        timesGreeted = 0;

        // only if girl is still on *track* we enter to phase 0, otherwise enter phase 1
        switchPhase((boolean) scene.resources().get("drawRoad") ? 0x0 : 0x1);
    }

    private boolean nextPhase() {
        return switchPhase(phase + 1);
    }

    private boolean switchPhase(final int newPhase) {
        phase = newPhase;
        switch (newPhase) {
            case 0x0:
                updateMessage("A wolf met with Little Red Riding Hood just as she entered the wood.");
                updateOptions();
                resetScrolling();
                return true;
            case 0x1:
                updateMessage("Red Riding Hood did not know what a wicked creature he was, and was not at all afraid of him.");
                updateOptions("Intimidate her", "Greet her", "Offer advice");
                resetOptionSelection();
                resetScrolling();
                return true;
            case 0x10:
                ++timesIntimidated;
                updateOptions();
                updateMessage("The wolf tried to intimidate her...");
                resetScrolling();
                return true;
            case 0x11:
                updateMessage("...");
                resetScrolling();
                return true;
            case 0x12:
                switch (timesIntimidated) {
                    case 1: updateMessage("It was not very effective..."); break;
                    case 2: updateMessage("It still was not very effective..."); break;
                    case 3: updateMessage("The wolf is intimidated by the fact that it was not effective..."); break;
                    default: updateMessage("(... How would you have reacted if this was a jump scare ...)"); break;
                }
                resetScrolling();
                return true;
// case 0x13: DONE
            case 0x20:
                ++timesGreeted;
                updateOptions();
                updateMessage("'Good day, Little Red Riding Hood,' said he.");
                resetScrolling();
                return true;
            case 0x21:
                updateMessage("'Thank you kindly, wolf.'");
                resetScrolling();
                return true;
            case 0x22:
                if (timesGreeted > 1) {
                    updateMessage("She is confused why the wolf greeted her twice...");
                } else {
                    updateMessage("'Whither away so early, Little Red Riding Hood?'");
                }
                resetScrolling();
                return true;
            case 0x23:
                updateMessage("'To my grandmother's.'");
                resetScrolling();
                return true;
            case 0x24:
                updateMessage("'What have you got in your apron?'");
                resetScrolling();
                return true;
            case 0x25:
                updateMessage("'Cake and wine; yesterday was baking-day, so poor sick grandmother is to have something good, to make her stronger.'");
                resetScrolling();
                return true;
            case 0x26:
                updateMessage("'Where does your grandmother live, Little Red Riding Hood?'");
                resetScrolling();
                return true;
            case 0x27:
                updateMessage("'A good quarter of a league farther on in the wood; her house stands under the three large oak-trees, the nut-trees are just below; you surely must know it,' replied Little Red Riding Hood.");
                resetScrolling();
                return true;
// case 0x28: DONE
            case 0x30:
                updateOptions();
                updateMessage("So he walked for a short time by the side of Little Red Riding Hood,");
                resetScrolling();
                return true;
            case 0x31:
                updateMessage("and then he said:");
                resetScrolling();
                return true;
            case 0x32:
                updateMessage("'See, Little Red Riding Hood, how pretty the flowers are about here - why do you not look round? I believe, too, that you do not hear how sweetly the little birds are singing; you walk gravely along as if you were going to school, while everything else out here in the wood is merry.'");
                resetScrolling();
                return true;
            case 0x33:
                updateMessage("Little Red Riding Hood raised her eyes,");
                resetScrolling();
                return true;
            case 0x34:
                updateMessage("and when she saw the sunbeams dancing here and there through the trees, and pretty flowers growing everywhere,");
                resetScrolling();
                return true;
            case 0x35:
                updateMessage("she thought:");
                resetScrolling();
                return true;
            case 0x36:
                updateMessage("'Suppose I take grandmother a fresh nosegay; that would please her too. It is so early in the day that I shall still get there in good time.'");
                resetScrolling();
                return true;
            case 0x37:
                updateMessage("So she ran from the path into the wood to look for flowers. And whenever she had picked one, she fancied that she saw a still prettier one farther on, and ran after it, and so got deeper and deeper into the wood.");
                resetScrolling();
                return true;
// case 0x38: DONE
        }
        return false;
    }

    @Override
    public boolean optionSelected(final int opt) {
        switch (phase) {
            case 0x1:
                // for example:
                //   opt = 0, switches to 0x10
                //   opt = 1, switches to 0x20
                return switchPhase((opt + 1) << 4);
            case 0x12:
            case 0x27:
                return switchPhase(0x1);
            case 0x37:
                if (timesGreeted < 1) {
                    // The story does say she wanders into the woods
                    scene.resources().put("drawRoad", false);
                    return scene.switchToScene(1);
                }
                return scene.gotoNextScene();
            case 0x22:
                if (timesGreeted > 1) {
                    return switchPhase(0x1);
                }
                break;
        }

        if (!nextPhase()) {
            // last phase, so we move on to scene according to condition
            return scene.gotoNextScene();
        }
        return true;
    }
}
