package com.dotandbox.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameOverScreen  extends MainMenuScreen implements ScreenActions {
 boolean mode;
    public GameOverScreen(DotAndBox game,boolean mode ) {
        super(game);
        this.mode=mode;

        backgroundTexture=new Texture(Gdx.files.internal("GameOverImage.jpeg"));

        stage.clear();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font;
        buttonStyle.fontColor = Color.BLACK;

        // Create "Back to Play Again" button
        TextButton playAgainButton = new TextButton("Play Again", buttonStyle);
        playAgainButton.setPosition(300, 240);
        playAgainButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(mode) {
                    System.out.println("Switching to GameScreen");
                    game.setScreen(new GameScreen(game));
                }// Navigate back to main menu or any other screen
                else {
                    System.out.println("Switching to GameScreenComputer");
                    game.setScreen(new GameScreenComputer(game));
                }
            }
        });

        TextButton exitButton = createExitButton(game);

        stage.clear();
        // Add the new button to the stage
        stage.addActor(playAgainButton);
        stage.addActor(exitButton);
    }
}
