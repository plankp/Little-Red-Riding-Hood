package org.atoiks.games.umst.scenes;

import java.awt.Font;
import java.awt.Color;
import java.awt.Image;

import java.awt.event.KeyEvent;

import java.util.Random;

import org.atoiks.games.framework2d.Input;
import org.atoiks.games.framework2d.GameScene;
import org.atoiks.games.framework2d.IGraphics;

import static org.atoiks.games.umst.App.WIDTH;
import static org.atoiks.games.umst.App.HEIGHT;

public class WolfEatsGrandmaScene extends GameScene {

    public static final Color BROWN = new Color(0x966F33);

    // [0]: x, [1]: y

    private final float[] wolfPos = new float[2];
    private final float[] wolfBox = new float[2];

    private Image[] wolfImg; // supplied by init
    private float wolfAnimTime = 0;
    private boolean wolfInvert = false;

    private static boolean rectCollide(final float[] pos1, final float[] box1, final float[] pos2, final float[] box2) {
        final float x1 = pos1[0];
        final float y1 = pos1[1];
        final float x2 = pos2[0];
        final float y2 = pos2[1];

        return x1 < x2 + box2[0] && x1 + box1[0] > x2
            && y1 < y2 + box2[1] && y1 + box1[1] > y2;
    }

    private static float clamp(final float k, final float min, final float max) {
        return k > max ? max : (k < min ? min : k);
    }

    @Override
    public void init() {
        wolfImg = (Image[]) scene.resources().get("wolfImg");
    }

    @Override
    public void enter(int from) {
        wolfBox[0] = wolfImg[0].getWidth(null);
        wolfBox[1] = wolfImg[0].getHeight(null);
        wolfPos[0] = 0;
        wolfPos[1] = 0;

        wolfAnimTime = 0;
    }

    @Override
    public boolean update(final float dt) {
        // Play as Wolf:
        // - Can wander in grandma's house
        // Goal: Eat grandma before LRRH comes in
        // - success: story continues
        // - failure: restart this section

        wolfAnimTime += dt * 5;

        boolean resetWolfTimer = true;
        if (Input.isKeyDown(KeyEvent.VK_LEFT)) {
            if ((wolfPos[0] -= 5) < -WIDTH) {
                wolfPos[0] = -WIDTH;
            } else {
                resetWolfTimer = false;
                wolfInvert = false;
            }
        }
        if (Input.isKeyDown(KeyEvent.VK_RIGHT)) {
            if ((wolfPos[0] += 5) > WIDTH - wolfBox[0]) {
                wolfPos[0] = WIDTH - wolfBox[0];
            } else {
                resetWolfTimer = false;
                wolfInvert = true;
            }
        }
        if (Input.isKeyDown(KeyEvent.VK_UP)) {
            if ((wolfPos[1] -= 5) < 0) {
                wolfPos[1] = 0;
            } else {
                resetWolfTimer = false;
            }
        }
        if (Input.isKeyDown(KeyEvent.VK_DOWN)) {
            if ((wolfPos[1] += 5) > HEIGHT - wolfBox[1]) {
                wolfPos[1] = HEIGHT - wolfBox[1];
            } else {
                resetWolfTimer = false;
            }
        }

        if (resetWolfTimer) {
            wolfAnimTime = 0;
        }
        return true;
    }

    @Override
    public void render(IGraphics g) {
        g.setClearColor(Color.black);
        g.clearGraphics();

        // only scroll horizontally
        g.translate(clamp(WIDTH / 2 - wolfPos[0], 0, WIDTH), 0);

        this.drawWolf(g);
    }

    private void drawWolf(final IGraphics g) {
        final float x = wolfPos[0];
        final float y = wolfPos[1];

        final Image frame = wolfImg[(int) wolfAnimTime % wolfImg.length];
        if (wolfInvert) {
            g.drawImage(frame, x + wolfBox[0], y, x, y + wolfBox[1]);
        } else {
            g.drawImage(frame, x, y);
        }
    }

    @Override
    public void resize(int w, int h) {
        // fixed
    }
}
