package org.atoiks.games.umst.twrappers;

import java.util.List;
import java.util.ArrayList;

import org.atoiks.games.umst.TextWrapStrategy;

public class FixedLocationWrapStrategy implements TextWrapStrategy {

    private final int msgWidth;
    private final int optWidth;

    public FixedLocationWrapStrategy(int msgWidth, int optWidth) {
        this.msgWidth = msgWidth;
        this.optWidth = optWidth;
    }

    public String[] wrapMessageText(String text) {
        return helper(text, msgWidth);
    }

    public String[] wrapOptionText(String text) {
        return helper(text, optWidth);
    }

    private static String[] helper(final String text, final int width) {
        if (text == null || width < 1) return new String[0];

        // break text down into width char-limits lines.
        final String[] msgln = text.split("\n");
        final List<String> list = new ArrayList<>();
        for (String msg : msgln) {
            while (msg.length() > width) {
                // try to split it at a space or tab that is the furthest away
                final int idxSpc = msg.lastIndexOf(' ', width);
                final int idxTab = msg.lastIndexOf('\t', width);

                int k = Math.max(idxSpc, idxTab);
                if (k < 0 || k > width) k = Math.min(idxSpc, idxTab);
                if (k < 0 || k > width) k = width - 1;
                ++k;
                list.add(msg.substring(0, k));
                msg = msg.substring(k);
            }
            list.add(msg);
        }

        return list.toArray(new String[list.size()]);
    }
}