package com.dotandbox.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.GL20;

public class MainMenuScreen implements Screen {
    final DotAndBox game;
    OrthographicCamera camera;
    Stage stage;
    Texture backgroundTexture;

    // Textures for button states
    Texture buttonUpTexture;
    Texture buttonDownTexture;
    Texture buttonHoverTexture;
    Texture buttonHoverTexture1;

    Texture buttonRulesTexture;
    Texture buttonHoverRulesTexture;
    Texture exitButtonUpTexture;
    Texture exitButtonHoverTexture;
    ExitHandler exitHandler;

    public MainMenuScreen(final DotAndBox game) {
        this.game = game;
        this.exitHandler = new ExitHandler(); // Initialize ExitHandler

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load textures
        backgroundTexture = new Texture(Gdx.files.internal("manu.png"));

        // Create "Play Against Human" button
        buttonUpTexture = new Texture(Gdx.files.internal("Buttonback2.png"));
        buttonHoverTexture = new Texture(Gdx.files.internal("Button2.png"));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = game.font; // Use your existing font
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



        // Create "Play Against Computer" button
        buttonDownTexture = new Texture(Gdx.files.internal("Buttonback1.png"));
        buttonHoverTexture1 = new Texture(Gdx.files.internal("Button1.png"));

        TextButton.TextButtonStyle buttonStyle1 = new TextButton.TextButtonStyle();
        buttonStyle1.font = game.font; // Use your existing font
        buttonStyle1.fontColor = Color.BLACK;

        buttonStyle1.up = new TextureRegionDrawable(new TextureRegion(buttonDownTexture));
        buttonStyle1.over = new TextureRegionDrawable(new TextureRegion(buttonHoverTexture1));

        TextButton playComputerButton = new TextButton("", buttonStyle1);
        playComputerButton.setSize(240, 55);
        playComputerButton.setPosition(400, 207);
        playComputerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreenComputer(game)); // Switch to GameScreenComputer
            }
        });




        // Create Game Rules Button
        buttonRulesTexture = new Texture(Gdx.files.internal("GameRules.png"));
        buttonHoverRulesTexture = new Texture(Gdx.files.internal("GameRulesback.png"));

        TextButton.TextButtonStyle buttonStyleRules = new TextButton.TextButtonStyle();
        buttonStyleRules.font = game.font; // Use your existing font
        buttonStyleRules.fontColor = Color.BLACK;

        buttonStyleRules.up = new TextureRegionDrawable(new TextureRegion(buttonRulesTexture));
        buttonStyleRules.over = new TextureRegionDrawable(new TextureRegion(buttonHoverRulesTexture));

        TextButton gameRulesButton = new TextButton("", buttonStyleRules);
        gameRulesButton.setSize(240, 58);
        gameRulesButton.setPosition(400, 144);




        // Create "Exit" button
        exitButtonUpTexture = new Texture(Gdx.files.internal("ExitUp.png"));
        exitButtonHoverTexture = new Texture(Gdx.files.internal("Exit.png"));

        TextButton.TextButtonStyle exitButtonStyle = new TextButton.TextButtonStyle();
        exitButtonStyle.font = game.font;
        exitButtonStyle.fontColor = Color.BLACK;

        exitButtonStyle.up = new TextureRegionDrawable(new TextureRegion(exitButtonUpTexture));
        exitButtonStyle.over = new TextureRegionDrawable(new TextureRegion(exitButtonHoverTexture));

        TextButton exitButton = new TextButton("", exitButtonStyle);
        exitButton.setSize(60, 45);
        exitButton.setPosition(585, 8);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitHandler.exitGame(); // Call the exit method in ExitHandler
            }
        });




        // Add actors to stage
        stage.addActor(playHumanButton);
        stage.addActor(playComputerButton);
        stage.addActor(gameRulesButton);
        stage.addActor(exitButton);
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
        buttonUpTexture.dispose();
        buttonHoverTexture.dispose();
        buttonHoverTexture1.dispose();
        buttonDownTexture.dispose();
        exitButtonUpTexture.dispose();
        exitButtonHoverTexture.dispose();
        buttonRulesTexture.dispose();
        buttonHoverRulesTexture.dispose();
    }
}
