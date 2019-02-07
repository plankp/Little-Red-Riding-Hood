package org.atoiks.games.umst;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import java.awt.Font;
import java.awt.FontFormatException;

import org.atoiks.games.framework2d.FrameInfo;
import org.atoiks.games.framework2d.java2d.Frame;

import org.atoiks.games.umst.scenes.*;

public class App {

    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;

    public static final Font MONOSPACE_FONT;
    
    static {
        Font local = null;
        try {
            local = Font.createFont(Font.PLAIN, App.class.getResourceAsStream("/VT323-Regular.ttf"));
        } catch (IOException | FontFormatException ex) {
            // Fallback to using a generic Monospace font
            local = new Font("Monospace", Font.PLAIN, 16);
        } finally {
            MONOSPACE_FONT = local;
        }
    }

    public static void main(String[] args) {
        final FrameInfo info = new FrameInfo()
                .setTitle("Atoiks Games - You are the Monster")
                .setResizable(false)
                .setSize(WIDTH, HEIGHT)
                .setLoader(new GameLoader())
                .setGameScenes(new TitleScene(), new DeliveryScene(), new WolfMeetsGirlScene(), new WolfEatsGrandmaScene(), new WolfEatsGirlScene());
        final Frame frame = new Frame(info);
        try {
            frame.init();
            frame.loop();
        } finally {
            frame.close();
        }
    }
}
