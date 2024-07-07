package com.dotandbox.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class GameOverScreen extends MainMenuScreen{

    public GameOverScreen(DotAndBox game) {
        super(game);
        backgroundTexture=new Texture(Gdx.files.internal("GameOverImage.jpeg"));
    }
}
