package org.atoiks.games.umst.scenes;

import java.io.IOException;

import java.awt.Font;
import java.awt.Color;
import java.awt.Image;

import java.awt.event.KeyEvent;

import java.util.Random;

import javax.imageio.ImageIO;

import org.atoiks.games.framework2d.Input;
import org.atoiks.games.framework2d.GameScene;
import org.atoiks.games.framework2d.IGraphics;

import static org.atoiks.games.umst.App.WIDTH;
import static org.atoiks.games.umst.App.HEIGHT;

import static org.atoiks.games.umst.scenes.DeliveryScene.BROWN;

public class WolfEatsGrandmaScene extends GameScene {

    public static final Color DARK_BROWN = new Color(0x654321);
    public static final Color DARKER_BROWN = DARK_BROWN.darker();

    private static final int WALL_INNER = WIDTH - 400;
    private static final int WALL_OUTER = WIDTH - 395;

    // [0]: x, [1]: y

    private final float[] wolfPos = new float[2];
    private final float[] wolfBox = new float[2];

    private Image[] wolfImg; // supplied by init
    private float wolfAnimTime = 0;
    private boolean wolfInvert = false;

    private final float[] girlPos = new float[2];

    private Image[] girlImg; // supplied by init
    private float girlAnimTime = 0;

    private final float[] grandmaPos = new float[2];
    private final float[] bedBox = new float[2];

    private Image grandmaImg; // supplied by init

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
        girlImg = (Image[]) scene.resources().get("girlImg");

        try {
            grandmaImg = ImageIO.read(this.getClass().getResourceAsStream("/grandma/sleep.png"));
        } catch (IOException ex) {
            System.err.println(ex);
        }

        grandmaPos[0] = -WIDTH / 2 + 10;
        grandmaPos[1] = (HEIGHT + grandmaImg.getHeight(null)) / 2;
        bedBox[0] = 1.75f * grandmaImg.getWidth(null);
        bedBox[1] = grandmaImg.getHeight(null);
    }

    @Override
    public void enter(int from) {
        wolfBox[0] = wolfImg[0].getWidth(null);
        wolfBox[1] = wolfImg[0].getHeight(null);
        wolfPos[0] = 0;
        wolfPos[1] = 0;

        girlPos[0] = WIDTH;
        girlPos[1] = (HEIGHT - girlImg[0].getHeight(null)) / 2 - 5;

        wolfAnimTime = 0;
        girlAnimTime = 0;
    }

    @Override
    public boolean update(final float dt) {
        // Play as Wolf:
        // - Can wander in grandma's house
        // Goal: Eat grandma before LRRH comes in
        // - success: story continues
        // - failure: restart this section

        wolfAnimTime += dt * 5;
        girlAnimTime += dt * 6;

        // if you touch grandma's bed, story continues
        if (rectCollide(wolfPos, wolfBox, grandmaPos, bedBox)) {
            return scene.gotoNextScene();
        }

        // if LRRH reaches house before, restart
        if (girlPos[0] < WALL_OUTER) {
            return scene.restartCurrentScene();
        }

        girlPos[0] -= 1;

        boolean resetWolfTimer = true;
        if (Input.isKeyDown(KeyEvent.VK_LEFT)) {
            if ((wolfPos[0] -= 5) < -WIDTH / 2) {
                wolfPos[0] = -WIDTH / 2;
            } else {
                resetWolfTimer = false;
                wolfInvert = false;
            }
        }
        if (Input.isKeyDown(KeyEvent.VK_RIGHT)) {
            if ((wolfPos[0] += 5) > WALL_INNER - wolfBox[0]) {
                wolfPos[0] = WALL_INNER - wolfBox[0];
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
        g.translate(clamp(WIDTH / 2 - wolfPos[0], 0, WIDTH / 2), 0);

        // Draw floor!?
        g.setColor(DARK_BROWN);
        g.fillRect(-WIDTH, 0, WALL_INNER, HEIGHT);
        g.setColor(DARKER_BROWN);
        for (int i = -WIDTH; i < WALL_INNER; i += 20) {
            g.drawLine(i, 0, i, HEIGHT);
            final int next = i + 20;
            for (int j = i - 180; j < HEIGHT; j += 60) {
                g.drawLine(i, j, next, j);
            }
        }

        // Draw the right-side wall
        g.setColor(BROWN);
        g.fillRect(WALL_INNER, 0, WALL_OUTER, HEIGHT);

        this.drawGrandmaInBed(g);
        this.drawWolf(g);
        this.drawGirl(g);
    }

    private void drawGirl(final IGraphics g) {
        final Image frame = girlImg[(int) girlAnimTime % girlImg.length];
        final float x = girlPos[0] - (frame.getWidth(null) - 25) / 2;
        final float y = girlPos[1];

        g.drawImage(frame, x, y);
    }

    private void drawGrandmaInBed(final IGraphics g) {
        final float x = grandmaPos[0];
        final float y = grandmaPos[1];

        final float frameX = x + 40;
        final float frameY = y + grandmaImg.getHeight(null);

        // Bed frame
        g.setColor(BROWN);
        g.fillRect(x, y, frameX, frameY);

        // GRANDMA!!!
        g.drawImage(grandmaImg, x, y);

        // Blanket
        g.setColor(Color.red);
        g.fillRect(frameX, y, x + bedBox[0], y + bedBox[1]);
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
