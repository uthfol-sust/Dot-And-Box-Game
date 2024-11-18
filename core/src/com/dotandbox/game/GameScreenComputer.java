package com.dotandbox.game;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import java.util.List;
import java.util.Random;



public class GameScreenComputer extends GameScreen {

    BitmapFont font=new BitmapFont();
    SpriteBatch batch=new SpriteBatch();
    private SpriteBatch batchMove=new SpriteBatch();
    private BitmapFont move=new BitmapFont();
    private Random random;
    private float delayDuration=0.8f;
    private float elapsedTime=0.0f;



    public GameScreenComputer(DotAndBox game) {
        super(game);
        random = new Random();

    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // If it's the computer's turn, make a move after .9 s delay
        if (!isPlayer1Turn) {
            elapsedTime+=delta;
            if(elapsedTime>=delayDuration)
            {makeComputerMove();
                elapsedTime=0f;}

        }
    }

    private void makeComputerMove() {
        List<Vector2[]> availableLines = getAvailableLines();
        Vector2[] bestmove=findBestMove(availableLines);
        if(bestmove!=null){
            isPlayer1Turn=false;
            placeLine(bestmove[0],bestmove[1]);
          //  System.out.println(bestmove[0]);
            //System.out.println(bestmove[1]);


        }
        else if (!availableLines.isEmpty()) {
            Vector2[] selectedLine = availableLines.get(random.nextInt(availableLines.size()));
            placeLine(selectedLine[0], selectedLine[1]);
            isPlayer1Turn=true;
        }

    }




    private Vector2[] findBestMove(List<Vector2[]> availableLines) {
        Vector2[] bestMove = null;


        for (Vector2[] line : availableLines) {
            int score = evaluateMove(line);
            if(score==10){
                bestMove=line;

            }

        }

        return bestMove;
    }
    private int evaluateMove(Vector2[] line) {

        Vector2 start = line[0];
        Vector2 end = line[1];

        int completeBoxScore = 10;

        int randomMoveScore = 0;

        if (doesCompleteBox(start, end)) {
            return completeBoxScore;
        }

        // Random move (no immediate benefit)
        return randomMoveScore;
    }

    private boolean doesCompleteBox(Vector2 start, Vector2 end) {
        int startX = (int) ((start.x - edgeSpace) / dotSpacing);
        int startY = (int) ((start.y - edgeSpace) / dotSpacing);
        int endX = (int) ((end.x - edgeSpace) / dotSpacing);
        int endY = (int) ((end.y - edgeSpace) / dotSpacing);


        // Check horizontal line
        if (startX == endX) {
            return isBoxCompletedByVerticalLine(startX, Math.min(startY, endY));
        }

        // Check vertical line
        if (startY == endY) {

            return isBoxCompletedByHorizontalLine(Math.min(startX, endX), startY);
        }

        return false;
    }

    private boolean isBoxCompletedByHorizontalLine(int x, int y) {
        boolean completesBoxBelow = y > 0 && horizontalLines[x][y - 1] != 0 && verticalLines[x + 1][y - 1] != 0 && verticalLines[x][y-1] != 0;
        boolean completesBoxAbove = y < gridSize - 1 && verticalLines[x][y] != 0 && verticalLines[x + 1][y] != 0 && horizontalLines[x][y+1] != 0;

        return completesBoxAbove || completesBoxBelow;
    }
    private boolean isBoxCompletedByVerticalLine(int x, int y) {
        boolean completesBoxLeft = x > 0 && horizontalLines[x - 1][y] != 0 && horizontalLines[x - 1][y + 1] != 0 && verticalLines[x-1][y] != 0;
        boolean completesBoxRight = x < gridSize - 1 && horizontalLines[x ][y] != 0 && horizontalLines[x][y + 1] != 0 && verticalLines[x+1][y] != 0;

        return completesBoxLeft || completesBoxRight;
    }

    @Override
    public void drawScores() {
        batch.begin();
        font.setColor(Color.BLACK);
        font.getData().setScale(2); // Keep font size and alignment the same
        font.draw(batch, "HUMAN: " + player1Score, 460, 420);
        font.draw(batch, "ROBOT: " + player2Score, 460, 380); // Change label here
        batch.end();
    }
    public void drawMove()
    {
        batchMove.begin();
        move.setColor(Color.BLACK);
        move.getData().setScale(1.5f);
        move.draw(batchMove,  (isPlayer1Turn ? "HUMAN " : "ROBOT ") +"Turn",200, 460);
        batchMove.end();
    }

    public void displayGameOver() {
        batch.begin();

        // Set font properties
        font.setColor(Color.RED);
        font.getData().setScale(2.4f);

        // Center the text on the screen
        float xPosition = 410;
        float yPosition = 250;

        font.draw(batch, "Game Over!", xPosition, yPosition);
        font.draw(batch, player2Score > player1Score ? "ROBOT Wins!" : "HUMAN Wins!", xPosition, yPosition - 50);

        stage.addActor(ReplayButton(game,true));
        batch.end();
    }

}