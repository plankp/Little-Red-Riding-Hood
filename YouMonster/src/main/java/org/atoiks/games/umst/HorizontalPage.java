package org.atoiks.games.umst;

import org.atoiks.games.umst.twrappers.FixedLocationWrapStrategy;

public abstract class HorizontalPage extends Page {

    public static final int LINE_BREAK_WIDTH = 74;

    protected HorizontalPage() {
        this(DEFAULT_SCROLL_DELAY, null);
    }

    protected HorizontalPage(float scrollDelay) {
        this(scrollDelay, null);
    }

    public HorizontalPage(final String message, final String... options) {
        this(DEFAULT_SCROLL_DELAY, message, options);
    }

    public HorizontalPage(float scrollDelay, String message, String... options) {
        super(new FixedLocationWrapStrategy(LINE_BREAK_WIDTH, LINE_BREAK_WIDTH - 2));
        updateMessage(message);
        updateOptions(options);
        updateScrollDelay(scrollDelay);
        this.usePositioningStrategy(new PositioningStrat());
    }

    private class PositioningStrat implements PositioningStrategy {

        @Override
        public int getMessageX(Page p) {
            return 20;
        }

        @Override
        public int getMessageY(Page p) {
            return 3 * App.HEIGHT / 4 - Math.max(p.getLineCount() - (p.getOptionCount() == 0 ? 3 : 1), 1) * FONT_SIZE;
        }

        @Override
        public int getImageX(Page p) {
            return 0;
        }

        @Override
        public int getImageY(Page p) {
            return 0;
        }

        @Override
        public int getOptionX(Page p) {
            return getMessageX(p) + 30;
        }

        @Override
        public int getOptionY(Page p) {
            return 3 * App.HEIGHT / 4 + 3 * FONT_SIZE / 2;
        }
    }
}