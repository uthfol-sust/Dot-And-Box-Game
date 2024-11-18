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

    public default TextButton createBackButton(DotAndBox game){
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
        return backButton;
    }
}
