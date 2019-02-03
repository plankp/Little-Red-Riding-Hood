package org.atoiks.games.umst.scenes;

import java.io.IOException;

import java.awt.Color;
import java.awt.Image;

import javax.imageio.ImageIO;

import org.atoiks.games.framework2d.Scene;
import org.atoiks.games.framework2d.IGraphics;

import static org.atoiks.games.umst.App.WIDTH;
import static org.atoiks.games.umst.App.HEIGHT;

public final class GameLoader extends Scene {

    private static final int BAR_START_X = 40;
    private static final int BAR_END_X = WIDTH - BAR_START_X;
    private static final int BAR_START_Y = HEIGHT * 2 / 3;
    private static final int BAR_END_Y = BAR_START_Y + 16;

    private float elapsed = 0;
    private float scale = 0.8f;

    @Override
    public void init() {
        final Image[] wolfImg = new Image[4];
        for (int i = 0; i < wolfImg.length; ++i) {
            try {
                wolfImg[i] = ImageIO.read(this.getClass().getResourceAsStream("/wolf/walk_" + i + ".png"));
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

        scene.resources().put("wolfImg", wolfImg);
    }

    @Override
    public void resize(int w, int h) {
        // fixed
    }

    @Override
    public boolean update(float dt) {
        elapsed += scale * dt;
        if (elapsed < -dt) {
            return scene.gotoNextScene();
        }

        if (elapsed > 1.1) {
            scale = -0.8f;
        } else if (elapsed > 0.95 && scale > 0) {
            scale = 0.05f;
        } else if (elapsed > 0.75 && scale > 0) {
            scale = 0.24f;
        }
        return true;
    }

    @Override
    public void render(final IGraphics g) {
        g.setClearColor(Color.black);
        g.clearGraphics();

        g.setColor(Color.white);
        g.drawRect(BAR_START_X, BAR_START_Y, BAR_END_X, BAR_END_Y);

        final float x2 = (BAR_END_X - BAR_START_X) * elapsed;
        if (x2 < BAR_START_X) {
            g.fillRect(x2, BAR_START_Y, BAR_START_X, BAR_END_Y);
        } else {
            g.fillRect(BAR_START_X, BAR_START_Y, x2, BAR_END_Y);
        }
    }
}