package com.dotandbox.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.GL20;

public class MainMenuScreen implements Screen,ScreenActions {
    final DotAndBox game;
    OrthographicCamera camera;
    Stage stage;
    Texture backgroundTexture;

    public MainMenuScreen(final DotAndBox game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load background texture
        backgroundTexture = new Texture(Gdx.files.internal("manu.png"));

        // Create "Play Against Human" button
        stage.addActor(createPlayHumanButton());

        // Create "Play Against Computer" button
        stage.addActor(createPlayComputerButton());

        // Create "Game Rules" button
        stage.addActor(createGameRulesButton());

        // Add "Exit" button using reusable method
        stage.addActor(createExitButton(game));
    }


    // Method to create the "Play Against Human" button
    private TextButton createPlayHumanButton() {
        Texture buttonUpTexture = new Texture(Gdx.files.internal("Buttonback2.png"));
        Texture buttonHoverTexture = new Texture(Gdx.files.internal("Button2.png"));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font;
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonUpTexture));
        buttonStyle.over = new TextureRegionDrawable(new TextureRegion(buttonHoverTexture));

        TextButton playHumanButton = new TextButton("", buttonStyle);
        playHumanButton.setSize(240, 55);
        playHumanButton.setPosition(400, 270);
        playHumanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game)); // Switch to GameScreen
            }
        });

        return playHumanButton;
    }

    // Method to create the "Play Against Computer" button
    private TextButton createPlayComputerButton() {
        Texture buttonDownTexture = new Texture(Gdx.files.internal("Buttonback1.png"));
        Texture buttonHoverTexture = new Texture(Gdx.files.internal("Button1.png"));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font;
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonDownTexture));
        buttonStyle.over = new TextureRegionDrawable(new TextureRegion(buttonHoverTexture));

        TextButton playComputerButton = new TextButton("", buttonStyle);
        playComputerButton.setSize(240, 55);
        playComputerButton.setPosition(400, 207);
        playComputerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreenComputer(game)); // Switch to GameScreenComputer
            }
        });

        return playComputerButton;
    }

    // Method to create the "Game Rules" button
    private TextButton createGameRulesButton() {
        Texture buttonRulesTexture = new Texture(Gdx.files.internal("GameRules.png"));
        Texture buttonHoverRulesTexture = new Texture(Gdx.files.internal("GameRulesback.png"));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font;
        buttonStyle.fontColor = Color.BLACK;
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonRulesTexture));
        buttonStyle.over = new TextureRegionDrawable(new TextureRegion(buttonHoverRulesTexture));

        TextButton gameRulesButton = new TextButton("", buttonStyle);
        gameRulesButton.setSize(240, 58);
        gameRulesButton.setPosition(400, 144);
        gameRulesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameRules(game)); // Switch to GameRules screen
            }
        });

        return gameRulesButton;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.165f, 0.616f, 0.561f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, 800, 480);
        game.batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
    }
}
