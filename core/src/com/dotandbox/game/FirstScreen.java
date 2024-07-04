package com.dotandbox.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;

public class FirstScreen implements Screen {

    final DotAndBox game;

    Texture backgroundTexture;

    public FirstScreen(DotAndBox game) {
        this.game = game;
        backgroundTexture = new Texture(Gdx.files.internal("Firstscreen.png"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, 650, 480);
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        backgroundTexture.dispose();
    }
}
