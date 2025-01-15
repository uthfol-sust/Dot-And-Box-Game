package com.dotandbox.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public interface ScreenActions {
    public default TextButton createExitButton(DotAndBox game) {
        // Load textures for button states
        Texture exitButtonUpTexture = new Texture(Gdx.files.internal("ExitUp.png"));
        Texture exitButtonHoverTexture = new Texture(Gdx.files.internal("Exit.png"));

        // Define button style
        TextButton.TextButtonStyle exitButtonStyle = new TextButton.TextButtonStyle();
        exitButtonStyle.font = game.font;
        exitButtonStyle.fontColor = Color.BLACK;
        exitButtonStyle.up = new TextureRegionDrawable(new TextureRegion(exitButtonUpTexture));
        exitButtonStyle.over = new TextureRegionDrawable(new TextureRegion(exitButtonHoverTexture));

        // Create and configure the button
        TextButton exitButton = new TextButton("", exitButtonStyle);
        exitButton.setSize(60, 45); // Button size
        exitButton.setPosition(585, 8); // Button position
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit the application
            }
        });

        return exitButton; // Return the fully configured button
    }

    public default TextButton createBackButton(DotAndBox game) {
        // Load textures for button states
        Texture backButtonUpTexture = new Texture(Gdx.files.internal("backup.png")); // Reuse ExitUp texture
        Texture backButtonHoverTexture = new Texture(Gdx.files.internal("back.png")); // Reuse Exit texture

        // Define button style
        TextButton.TextButtonStyle backButtonStyle = new TextButton.TextButtonStyle();
        backButtonStyle.font = game.font;
        backButtonStyle.fontColor = Color.BLACK;
        backButtonStyle.up = new TextureRegionDrawable(new TextureRegion(backButtonUpTexture));
        backButtonStyle.over = new TextureRegionDrawable(new TextureRegion(backButtonHoverTexture));

        // Create and configure the button
        TextButton backButton = new TextButton("", backButtonStyle);
        backButton.setSize(70,  50); // Button size (same as Exit button)
        backButton.setPosition(10, 425); // Position the button
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game)); // Navigate to the Main Menu
            }
        });

        return backButton; // Return the configured button
    }


    public default TextButton ReplayButton(DotAndBox game, boolean isComputerPlay) {
        // Load textures for button states
        Texture replayButtonUpTexture = new Texture(Gdx.files.internal("replayup.png")); // Reuse ExitUp texture
        Texture replayButtonHoverTexture = new Texture(Gdx.files.internal("replay.png")); // Reuse Exit texture

        // Define button style
        TextButton.TextButtonStyle replayButtonStyle = new TextButton.TextButtonStyle();
        replayButtonStyle.font = game.font;
        replayButtonStyle.fontColor = Color.BLACK;
        replayButtonStyle.up = new TextureRegionDrawable(new TextureRegion(replayButtonUpTexture));
        replayButtonStyle.over = new TextureRegionDrawable(new TextureRegion(replayButtonHoverTexture));

        // Create and configure the button
        TextButton replayButton = new TextButton("", replayButtonStyle);
        replayButton.setSize(90, 45); // Button size (same as Exit button)
        replayButton.setPosition(400, 8); // Position the button
        replayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isComputerPlay)
                    game.setScreen(new GameScreenComputer(game));
                else
                    game.setScreen(new GameScreen(game));
            }
        });

        return replayButton; // Return the configured button
    }

}
