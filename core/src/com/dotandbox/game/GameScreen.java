package com.dotandbox.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    final DotAndBox game;
    List<Vector2[]> availableLines;
    int gridSize = 6;
    OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    float dotRadious = 10;
    float dotSpacing = 50;
    int[][] verticalLines;
    int[][] horizontalLines;
    int[][] boxes;
    boolean isPlayer1Turn = true;

    int player1Score = 0;
    int player2Score = 0;
    private SpriteBatch batchMove;
    private BitmapFont move;
    private Vector2 selectedDot = null;
    private Vector2 hoverAdjacentDot = null;
    private Texture[] backgrounds;
    private int currentBackgroundIndex;
    private Timer backgroundTimer;
    private float backgroundSlideOffset;
    private float backgroundSlideDuration;
    private boolean isSliding;
    Music menuMusic;
    Music boxMusic;
    Stage stage;
    int edgeSpace = 100;

    public GameScreen(DotAndBox game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        font = new BitmapFont();
        batchMove = new SpriteBatch();
        move = new BitmapFont();
        horizontalLines = new int[gridSize][gridSize];
        verticalLines = new int[gridSize][gridSize];
        boxes = new int[gridSize - 1][gridSize - 1];

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("play.ogg"));
        boxMusic = Gdx.audio.newMusic(Gdx.files.internal("Box.ogg"));

        // Load background textures
        backgrounds = new Texture[] {
                new Texture(Gdx.files.internal("Back.png")),
                new Texture(Gdx.files.internal("Back.png"))
        };
        currentBackgroundIndex = 0;
        backgroundSlideOffset = 0;
        backgroundSlideDuration = 3.0f; // Duration of the slide in seconds
        isSliding = false;

        backgroundTimer = new Timer();
        backgroundTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                startBackgroundSlide();
            }
        }, 0, 3);

        // Create the button style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font;
        buttonStyle.fontColor= Color.BLACK;

        // Create the button
        TextButton backButton = new TextButton("Back to Menu", buttonStyle);
        backButton.setPosition( 10,  450);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(backButton);
    }

    private void startBackgroundSlide() {
        isSliding = true;
        backgroundSlideOffset = 0;
    }

    private void updateBackgroundSlide(float delta) {
        if (isSliding) {
            backgroundSlideOffset += (Gdx.graphics.getWidth() * delta) / backgroundSlideDuration;
            if (backgroundSlideOffset >= Gdx.graphics.getWidth()) {
                isSliding = false;
                currentBackgroundIndex = (currentBackgroundIndex + 1) % backgrounds.length;
                backgroundSlideOffset = 0;
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();

        updateBackgroundSlide(delta);

        // Draw the current background
        batch.begin();
        if (isSliding) {
            int nextBackgroundIndex = (currentBackgroundIndex + 1) % backgrounds.length;

            // Draw the current background sliding out
            batch.draw(backgrounds[currentBackgroundIndex], -backgroundSlideOffset, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            // Draw the next background sliding in
            batch.draw(backgrounds[nextBackgroundIndex], Gdx.graphics.getWidth() - backgroundSlideOffset, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        } else {
            batch.draw(backgrounds[currentBackgroundIndex], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        batch.end();


        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw dots
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                float x = i * dotSpacing + edgeSpace;
                float y = j * dotSpacing + edgeSpace;
                if (selectedDot != null && selectedDot.epsilonEquals(x, y, dotRadious)) {
                    shapeRenderer.setColor(Color.RED);
                } else if (hoverAdjacentDot != null && hoverAdjacentDot.epsilonEquals(x, y, dotRadious)) {
                    shapeRenderer.setColor(Color.YELLOW);
                } else {
                    shapeRenderer.setColor(Color.BLACK);
                }
                shapeRenderer.circle(x, y, dotRadious);
            }
        }

        // Draw horizontal lines
        for (int i = 0; i < gridSize - 1; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (horizontalLines[i][j] != 0) {
                    float x1 = i * dotSpacing + edgeSpace;
                    float y1 = j * dotSpacing + edgeSpace;
                    float x2 = x1 + dotSpacing;
                    float y2 = y1;
                    if (horizontalLines[i][j] == 1)
                        shapeRenderer.setColor(Color.BLUE);
                    else if (horizontalLines[i][j] == 2)
                        shapeRenderer.setColor(Color.GREEN);
                    shapeRenderer.rectLine(x1, y1, x2, y2, 5);
                }
            }
        }

        // Draw vertical lines
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                if (verticalLines[i][j] != 0) {
                    float x1 = i * dotSpacing + edgeSpace;
                    float y1 = j * dotSpacing + edgeSpace;
                    float x2 = x1;
                    float y2 = y1 + dotSpacing;
                    if (verticalLines[i][j] == 1)
                        shapeRenderer.setColor(Color.BLUE);
                    else if (verticalLines[i][j] == 2)
                        shapeRenderer.setColor(Color.GREEN);
                    shapeRenderer.rectLine(x1, y1, x2, y2, 5);
                }
            }
        }

        // Draw boxes
        for (int i = 0; i < gridSize - 1; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                if (boxes[i][j] != 0) {
                    float x = i * dotSpacing + edgeSpace;
                    float y = j * dotSpacing + edgeSpace;
                    if (boxes[i][j] == 1)
                        shapeRenderer.setColor(new Color(0, 0, 1, 1));
                    else if (boxes[i][j] == 2)
                        shapeRenderer.setColor(new Color(0, 1, 0, 1));
                    shapeRenderer.rect(x + 10, y + 10, dotSpacing - 20, dotSpacing - 20);
                }
            }
        }


        // Draw temporary line from selected dot to mouse pointer
        if (selectedDot != null) {
            Vector3 cursor = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(cursor);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rectLine(selectedDot, new Vector2(cursor.x, cursor.y), 5);
        }
        shapeRenderer.end();

        // Update hoverAdjacentDot
        if (selectedDot != null) {
            Vector3 cursor3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(cursor3D);
            Vector2 cursorPos = new Vector2(cursor3D.x, cursor3D.y);
            hoverAdjacentDot = getHoverAdjacentDot(selectedDot, cursorPos);
        } else {
            hoverAdjacentDot = null;
        }

        // Handle mouse release event
        if (!Gdx.input.isTouched() && selectedDot != null) {
            if (hoverAdjacentDot != null && !isLinePresent(selectedDot, hoverAdjacentDot)) {
                placeLine(selectedDot, hoverAdjacentDot);
            }
            selectedDot = null;
            hoverAdjacentDot = null;
        }

        // Handle mouse click event to select a dot
        if (Gdx.input.justTouched()) {
            Vector3 touchPos3D = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos3D);
            Vector2 touchPos = new Vector2(touchPos3D.x, touchPos3D.y);

            Vector2 nearestDot = getNearestDot(touchPos);
            if (selectedDot == null && nearestDot.y < gridSize * dotSpacing + edgeSpace && nearestDot.x < gridSize * dotSpacing + edgeSpace) {
                selectedDot = nearestDot;
            }
        }

        // Draw scores
        batch.begin();
        font.setColor(Color.BLACK);
        font.getData().setScale(2);
        font.draw(batch, "Player 1: " + player1Score, 480, 460);
        font.draw(batch, "Player 2: " + player2Score, 480, 420);
        batch.end();

        // Draw player move
        batchMove.begin();
        move.setColor(Color.BLACK);
        move.getData().setScale(1);
        move.draw(batchMove, "Player" + (isPlayer1Turn ? "1" : "2") +" Turn",150, 460);
        batchMove.end();

        stage.act(delta);
        stage.draw();
    }

    // Find nearest dot when have to select dot
    Vector2 getNearestDot(Vector2 pos) {
        int nearestX = Math.round((pos.x - edgeSpace) / dotSpacing);
        int nearestY = Math.round((pos.y - edgeSpace) / dotSpacing);
        return new Vector2(nearestX * dotSpacing + edgeSpace, nearestY * dotSpacing + edgeSpace);
    }

    // Find valid adjacent dot
    Vector2 getHoverAdjacentDot(Vector2 selectedDot, Vector2 cursorPos) {
        Vector2[] possibleDots = {
                new Vector2(selectedDot.x + dotSpacing, selectedDot.y),
                new Vector2(selectedDot.x - dotSpacing, selectedDot.y),
                new Vector2(selectedDot.x, selectedDot.y + dotSpacing),
                new Vector2(selectedDot.x, selectedDot.y - dotSpacing)
        };
        for (Vector2 dot : possibleDots) {
            if (dot.dst(cursorPos) <= dotRadious) {
                return dot;
            }
        }
        return null;
    }

    // Check if there have any line between two dots
    private boolean isLinePresent(Vector2 start, Vector2 end) {
        int startX = (int) ((start.x - edgeSpace) / dotSpacing);
        int startY = (int) ((start.y - edgeSpace) / dotSpacing);
        int endX = (int) ((end.x - edgeSpace) / dotSpacing);
        int endY = (int) ((end.y - edgeSpace) / dotSpacing);

        if (startX == endX) {
            // Vertical line
            return verticalLines[startX][Math.min(startY, endY)] != 0;
        } else if (startY == endY) {
            // Horizontal line
            return horizontalLines[Math.min(startX, endX)][startY] != 0;
        }
        return false;
    }

    private void endGame() {
        game.setScreen(new GameOverScreen(game, true));
    }
    // Place line if valid
    void placeLine(Vector2 start, Vector2 end) {
        int startX = (int) ((start.x - edgeSpace) / dotSpacing);
        int startY = (int) ((start.y - edgeSpace) / dotSpacing);
        int endX = (int) ((end.x - edgeSpace) / dotSpacing);
        int endY = (int) ((end.y - edgeSpace) / dotSpacing);

        if (startX == endX) {
            // Vertical line
            verticalLines[startX][Math.min(startY, endY)] = isPlayer1Turn ? 1 : 2;
            menuMusic.play();
            menuMusic.setLooping(false);
        } else if (startY == endY) {
            // Horizontal line
            horizontalLines[Math.min(startX, endX)][startY] = isPlayer1Turn ? 1 : 2;
            menuMusic.play();
            menuMusic.setLooping(false);
        }

        // Check if any boxes are completed
        boolean completedBox = checkCompletedBoxes();

        if (completedBox) {
            boxMusic.play();
            boxMusic.setLooping(false);
        }

        if (!completedBox) {
            isPlayer1Turn = !isPlayer1Turn;
        }
        availableLines = getAvailableLines();
        if(availableLines.isEmpty()){
            endGame();
        }

    }

    // Check if box completed
    boolean checkCompletedBoxes() {
        boolean boxCompleted = false;
        for (int i = 0; i < gridSize - 1; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                if (boxes[i][j] == 0) {
                    if (horizontalLines[i][j] != 0 &&
                            horizontalLines[i][j + 1] != 0 &&
                            verticalLines[i][j] != 0 &&
                            verticalLines[i + 1][j] != 0) {
                        boxes[i][j] = isPlayer1Turn ? 1 : 2;
                        if (isPlayer1Turn) {
                            player1Score++;
                        } else {
                            player2Score++;
                        }

                        boxCompleted = true;
                    }
                }
            }
        }
        return boxCompleted;
    }

    public List<Vector2[]> getAvailableLines() {
        List<Vector2[]> availableLines = new ArrayList<>();

        // Check for available horizontal lines
        for (int i = 0; i < gridSize - 1; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (horizontalLines[i][j] == 0) {
                    float x1 = i * dotSpacing + edgeSpace;
                    float y1 = j * dotSpacing + edgeSpace;
                    float x2 = x1 + dotSpacing;
                    float y2 = y1;
                    availableLines.add(new Vector2[]{new Vector2(x1, y1), new Vector2(x2, y2)});
                }
            }
        }

        // Check for available vertical lines
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                if (verticalLines[i][j] == 0) {
                    float x1 = i * dotSpacing + edgeSpace;
                    float y1 = j * dotSpacing + edgeSpace;
                    float x2 = x1;
                    float y2 = y1 + dotSpacing;
                    availableLines.add(new Vector2[]{new Vector2(x1, y1), new Vector2(x2, y2)});
                }
            }
        }

        return availableLines;
    }
    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
        batchMove.dispose();
        move.dispose();
        menuMusic.dispose();
        boxMusic.dispose();
        stage.dispose();
    }
}
