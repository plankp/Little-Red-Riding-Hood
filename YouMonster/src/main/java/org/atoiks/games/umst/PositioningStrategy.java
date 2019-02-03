package org.atoiks.games.umst;

public interface PositioningStrategy {

    public int getMessageX(Page p);

    public int getMessageY(Page p);

    public int getImageX(Page p);

    public int getImageY(Page p);

    public int getOptionX(Page p);

    public int getOptionY(Page p);
}