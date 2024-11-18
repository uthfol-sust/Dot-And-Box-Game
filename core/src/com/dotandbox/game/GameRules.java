package com.dotandbox.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameRules extends MainMenuScreen implements ScreenActions {


    public GameRules(DotAndBox game) {
        super(game);

        backgroundTexture=new Texture(Gdx.files.internal("Rules.png"));

        // Add buttons to the stage
        stage.clear();
        stage.addActor(createBackButton(game));
        stage.addActor(createExitButton(game));
    }
}
