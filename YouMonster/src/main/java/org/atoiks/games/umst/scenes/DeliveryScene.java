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

public class DeliveryScene extends GameScene {

    public static final Color BROWN = new Color(0x966F33);

    // [0]: x, [1]: y

    private final float[] wolfPos = new float[2];
    private final float[] wolfBox = new float[2];
    private final Image[] wolfImg = new Image[4];

    private float wolfAnimTime = 0;
    private byte wolfAnimFlag = 0;
    private boolean wolfInvert = false;

    private final float[] girlPos = new float[2];
    private final float[] girlBox = new float[2];
    private final Image[] girlImg = new Image[9];

    private float girlAnimTime = 0;

    private final int[] flowerPos = new int[120 * 2];
    private final int[] grassPos = new int[120 * 2];

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
        for (int i = 0; i < wolfImg.length; ++i) {
            try {
                wolfImg[i] = ImageIO.read(this.getClass().getResourceAsStream("/wolf/walk_" + i + ".png"));
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

        for (int i = 0; i < girlImg.length; ++i) {
            try {
                girlImg[i] = ImageIO.read(this.getClass().getResourceAsStream("/girl/walk_" + i + ".png"));
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }
    }

    @Override
    public void enter(int from) {
        wolfBox[0] = wolfImg[0].getWidth(null);
        wolfBox[1] = wolfImg[0].getHeight(null);
        wolfPos[0] = 0;
        wolfPos[1] = 0;

        girlBox[0] = girlImg[0].getWidth(null);
        girlBox[1] = girlImg[0].getHeight(null);
        girlPos[0] = WIDTH;
        girlPos[1] = (HEIGHT - girlBox[1]) / 2;

        final Random rnd = new Random();
        fillIntPos(rnd, flowerPos, -WIDTH, WIDTH, 0, HEIGHT);
        fillIntPos(rnd, grassPos, -WIDTH, WIDTH, 0, HEIGHT);

        final int k = grassPos.length;
    }

    private static void fillIntPos(final Random rnd, final int[] arr, final int xlo, final int xhi, final int ylo, final int yhi) {
        final int dx = xhi - xlo;
        final int dy = yhi - ylo;
        final int len = arr.length;
        for (int i = 0; i < len; i += 2) {
            arr[i] = rnd.nextInt(dx) + xlo;
            arr[i + 1] = rnd.nextInt(dy) + ylo;
        }
    }

    @Override
    public boolean update(final float dt) {
        // Play as Wolf:
        // - Can wander around town for a while
        // - See LRRH leave from home and go into forest
        // Goal: Reach LRRH before she reaches grandma
        // - success: story continues
        // - failure: restart this section

        wolfAnimTime += dt * 5;
        girlAnimTime += dt * 6;

        // if you reached LRRH, story continues
        if (rectCollide(wolfPos, wolfBox, girlPos, girlBox)) {
            return scene.gotoNextScene();
        }

        // if LRRH reaches grandma's house before you, restart
        if (girlPos[0] < -WIDTH) {
            return scene.restartCurrentScene();
        }

        girlPos[0] -= 1;

        // Assumes wolf is back to idle phase,
        // which no animating
        wolfAnimFlag = 0;

        if (Input.isKeyDown(KeyEvent.VK_LEFT)) {
            if ((wolfPos[0] -= 5) < -WIDTH) {
                wolfPos[0] = -WIDTH;
            } else {
                wolfAnimFlag = 1;
                wolfInvert = false;
            }
        }
        if (Input.isKeyDown(KeyEvent.VK_RIGHT)) {
            if ((wolfPos[0] += 5) > WIDTH - wolfBox[0]) {
                wolfPos[0] = WIDTH - wolfBox[0];
            } else {
                wolfAnimFlag = 1;
                wolfInvert = true;
            }
        }
        if (Input.isKeyDown(KeyEvent.VK_UP)) {
            if ((wolfPos[1] -= 5) < 0) {
                wolfPos[1] = 0;
            } else {
                wolfAnimFlag = 1;
            }
        }
        if (Input.isKeyDown(KeyEvent.VK_DOWN)) {
            if ((wolfPos[1] += 5) > HEIGHT - wolfBox[1]) {
                wolfPos[1] = HEIGHT - wolfBox[1];
            } else {
                wolfAnimFlag = 1;
            }
        }
        return true;
    }

    @Override
    public void render(IGraphics g) {
        g.setClearColor(Color.black);
        g.clearGraphics();

        // only scroll horizontally
        g.translate(clamp(WIDTH / 2 - wolfPos[0], 0, WIDTH), 0);

        {
            // Draw the beautiful flowers and fantastic grass!
            final int k = flowerPos.length;

            g.setColor(Color.yellow);
            for (int i = 0; i < k; i += 2) {
                g.drawCircle(flowerPos[i], flowerPos[i + 1], 1);
            }

            g.setColor(Color.green);
            for (int i = 0; i < k; i += 2) {
                drawGrass(g, flowerPos[i], flowerPos[i + 1]);
            }

            final int j = grassPos.length;
            for (int i = 0; i < j; i += 2) {
                drawGrass(g, grassPos[i], grassPos[i + 1]);
            }
        }

        // Road to from the village to grandma's place
        g.setColor(Color.gray);
        g.fillRect(-WIDTH, HEIGHT / 2 - 30, WIDTH, HEIGHT / 2 + 30);

        g.setColor(BROWN);
        g.fillRect(-WIDTH, HEIGHT / 2 - 18, -WIDTH + 30, HEIGHT / 2 + 18);
        g.fillRect(WIDTH - 30, HEIGHT / 2 - 18, WIDTH, HEIGHT / 2 + 18);

        g.setColor(Color.black);
        g.drawLine(-WIDTH, HEIGHT / 2 - 18, -WIDTH + 30, HEIGHT / 2 - 18);
        g.drawLine(-WIDTH, HEIGHT / 2 + 18, -WIDTH + 30, HEIGHT / 2 + 18);
        g.drawLine(WIDTH - 30, HEIGHT / 2 - 18, WIDTH, HEIGHT / 2 - 18);
        g.drawLine(WIDTH - 30, HEIGHT / 2 + 18, WIDTH, HEIGHT / 2 + 18);

        this.drawWolf(g);
        this.drawgirl(g);
    }

    private void drawGrass(final IGraphics g, final float px, final float py) {
        final float x = px;
        final float y = py + 0.5f;
        final float root = y + 2.5f;

        g.drawLine(x - 3, y, x, root);
        g.drawLine(x, root, x + 3, y);
    }

    private void drawWolf(final IGraphics g) {
        final float x = wolfPos[0];
        final float y = wolfPos[1];

        final Image frame = wolfImg[wolfAnimFlag * (int) wolfAnimTime % wolfImg.length];
        if (wolfInvert) {
            g.drawImage(frame, x + wolfBox[0], y, x, y + wolfBox[1]);
        } else {
            g.drawImage(frame, x, y);
        }
    }

    private void drawgirl(final IGraphics g) {
        final float x = girlPos[0];
        final float y = girlPos[1];

        g.drawImage(girlImg[(int) girlAnimTime % girlImg.length], x, y);
    }

    @Override
    public void resize(int w, int h) {
        // fixed
    }
}
