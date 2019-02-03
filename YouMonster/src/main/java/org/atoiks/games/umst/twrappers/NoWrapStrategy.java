package org.atoiks.games.umst.twrappers;

import org.atoiks.games.umst.TextWrapStrategy;

public class NoWrapStrategy implements TextWrapStrategy {

    public String[] wrapMessageText(String text) {
        return text == null ? new String[0] : new String[] { text };
    }

    public String[] wrapOptionText(String text) {
        return text == null ? new String[0] : new String[] { text };
    }
} 