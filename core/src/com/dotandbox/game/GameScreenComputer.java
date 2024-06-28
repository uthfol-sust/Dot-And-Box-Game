package com.dotandbox.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

public class GameScreenComputer implements Screen {

    final DotAndBox game;
    int gridSize = 10;
    private OrthographicCamera camera;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private BitmapFont font;
    private float dotRadius = 10;
    private float dotSpacing = 40;
    private int[][] verticalLines;
    private int[][] horizontalLines;
    private int[][] boxes;
    private boolean isPlayerTurn = true;
    private int playerScore = 0;
    private int computerScore = 0;
    private SpriteBatch batchMove;
    private BitmapFont move;
    private Vector2 selectedDot = null;
    private Vector2 hoverAdjacentDot = null;
    Music menuMusic;
    Music boxMusic;
    private Stage stage;
    int edgeSpace = 20;

    public GameScreenComputer(DotAndBox game) {
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

        // Create the button style
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font;
        buttonStyle.fontColor = Color.BLACK;

        // Create the button
        TextButton backButton = new TextButton("Back to Menu", buttonStyle);
        backButton.setPosition(10, 450);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Draw dots
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                float x = i * dotSpacing + edgeSpace;
                float y = j * dotSpacing + edgeSpace;
                if (selectedDot != null && selectedDot.epsilonEquals(x, y, dotRadius)) {
                    shapeRenderer.setColor(Color.RED);
                } else if (hoverAdjacentDot != null && hoverAdjacentDot.epsilonEquals(x, y, dotRadius)) {
                    shapeRenderer.setColor(Color.YELLOW);
                } else {
                    shapeRenderer.setColor(Color.BLACK);
                }
                shapeRenderer.circle(x, y, dotRadius);
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
                if (!isPlayerTurn) {
                    computerMove();
                }
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
        font.draw(batch, "Player: " + playerScore, 480, 460);
        font.draw(batch, "Computer: " + computerScore, 480, 420);
        batch.end();

        // Draw player move
        batchMove.begin();
        move.setColor(Color.BLACK);
        move.getData().setScale(1);
        move.draw(batchMove, "Turn: " + (isPlayerTurn ? "Player" : "Computer"), 150, 460);
        batchMove.end();

        stage.act(delta);
        stage.draw();
    }

    // Find nearest dot when have to select dot
    private Vector2 getNearestDot(Vector2 pos) {
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
            if (dot.dst(cursorPos) <= dotRadius) {
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

    // Place line if valid
    private void placeLine(Vector2 start, Vector2 end) {
        int startX = (int) ((start.x - edgeSpace) / dotSpacing);
        int startY = (int) ((start.y - edgeSpace) / dotSpacing);
        int endX = (int) ((end.x - edgeSpace) / dotSpacing);
        int endY = (int) ((end.y - edgeSpace) / dotSpacing);

        if (startX == endX) {
            // Vertical line
            verticalLines[startX][Math.min(startY, endY)] = isPlayerTurn ? 1 : 2;
            menuMusic.play();
            menuMusic.setLooping(false);
        } else if (startY == endY) {
            // Horizontal line
            horizontalLines[Math.min(startX, endX)][startY] = isPlayerTurn ? 1 : 2;
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
            isPlayerTurn = !isPlayerTurn;
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
                        boxes[i][j] = isPlayerTurn ? 1 : 2;
                        if (isPlayerTurn) {
                            playerScore++;
                        } else {
                            computerScore++;
                        }
                        boxCompleted = true;
                    }
                }
            }
        }
        return boxCompleted;
    }

    private void computerMove() {
        Random rand = new Random();
        boolean moveMade = false;

        // First, try to find a move that completes a box
        for (int i = 0; i < gridSize - 1 && !moveMade; i++) {
            for (int j = 0; j < gridSize - 1 && !moveMade; j++) {
                if (boxes[i][j] == 0) {
                    if (horizontalLines[i][j] == 0) {
                        placeLine(new Vector2(i * dotSpacing + edgeSpace, j * dotSpacing + edgeSpace), new Vector2((i + 1) * dotSpacing + edgeSpace, j * dotSpacing + edgeSpace));
                        moveMade = true;
                    } else if (horizontalLines[i][j + 1] == 0) {
                        placeLine(new Vector2(i * dotSpacing + edgeSpace, (j + 1) * dotSpacing + edgeSpace), new Vector2((i + 1) * dotSpacing + edgeSpace, (j + 1) * dotSpacing + edgeSpace));
                        moveMade = true;
                    } else if (verticalLines[i][j] == 0) {
                        placeLine(new Vector2(i * dotSpacing + edgeSpace, j * dotSpacing + edgeSpace), new Vector2(i * dotSpacing + edgeSpace, (j + 1) * dotSpacing + edgeSpace));
                        moveMade = true;
                    } else if (verticalLines[i + 1][j] == 0) {
                        placeLine(new Vector2((i + 1) * dotSpacing + edgeSpace, j * dotSpacing + edgeSpace), new Vector2((i + 1) * dotSpacing + edgeSpace, (j + 1) * dotSpacing + edgeSpace));
                        moveMade = true;
                    }
                }
            }
        }

        // If no box completing move is found, make a random move
        if (!moveMade) {
            while (!moveMade) {
                int i = rand.nextInt(gridSize - 1);
                int j = rand.nextInt(gridSize);
                if (horizontalLines[i][j] == 0) {
                    placeLine(new Vector2(i * dotSpacing + edgeSpace, j * dotSpacing + edgeSpace), new Vector2((i + 1) * dotSpacing + edgeSpace, j * dotSpacing + edgeSpace));
                    moveMade = true;
                } else if (j < gridSize - 1 && verticalLines[i][j] == 0) {
                    placeLine(new Vector2(i * dotSpacing + edgeSpace, j * dotSpacing + edgeSpace), new Vector2(i * dotSpacing + edgeSpace, (j + 1) * dotSpacing + edgeSpace));
                    moveMade = true;
                }
            }
        }

        // If a box was completed, make another move
        if (checkCompletedBoxes()) {
            computerMove();
        }
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
