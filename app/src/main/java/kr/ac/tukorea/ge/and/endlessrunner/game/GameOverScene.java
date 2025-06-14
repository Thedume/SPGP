package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.endlessrunner.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class GameOverScene extends Scene {

    private final int score;
    private final float distance;
    private final boolean isMale;
    private Sprite gameOverSprite;

    private Button retryBtn;
    private Button toMainBtn;

    public GameOverScene(int score, float distance, boolean isMale) {
        this.score = score;
        this.distance = distance;
        this.isMale = isMale;

        initLayers(Layer.COUNT.ordinal());

        gameOverSprite = new Sprite(R.mipmap.game_over_2, Metrics.width / 2, Metrics.height * 0.15f, 800, 200);
        add(Layer.ui, gameOverSprite);

        retryBtn = new Button(
                R.mipmap.retry_btn,
                Metrics.width / 2, Metrics.height * 0.7f,
                400f, 150f,
                pressed -> {
                    new MainScene(isMale).change();
                    return true;
                }
        );
        retryBtn.setCustomSize(true);
        add(TitleScene.Layer.ui, retryBtn);


        toMainBtn = new Button(
                R.mipmap.to_main_btn,
                Metrics.width / 2, Metrics.height * 0.825f,
                400f, 150f,
                pressed -> {
                    new TitleScene().change();
                    return true;
                }
        );
        toMainBtn.setCustomSize(true);
        add(TitleScene.Layer.ui, toMainBtn);

    }

    public enum Layer {
        ui, COUNT
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setTextSize(60f);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);

        

        // canvas.drawText("Game Over", Metrics.width / 2, 400, paint);
        canvas.drawText("Score: " + score, Metrics.width / 2, 600, paint);
        canvas.drawText("Distance: " + (int)distance + " m", Metrics.width / 2, 700, paint);
        canvas.drawText("Character: " + (isMale ? "Male" : "Female"), Metrics.width / 2, 800, paint);
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.ui.ordinal(); // ui 레이어에 있는 버튼이 터치를 받도록 지정
    }

    @Override
    public void onEnter() {
        super.onEnter();

        SharedPreferences prefs = GameView.view.getContext().getSharedPreferences("score", Context.MODE_PRIVATE);
        String scoresStr = prefs.getString("records", "");
        ArrayList<Integer> scores = new ArrayList<>();

        if (!scoresStr.isEmpty()) {
            for (String s : scoresStr.split(",")) {
                scores.add(Integer.parseInt(s));
            }
        }

        scores.add(score);
        scores.sort((a, b) -> b - a);

        while (scores.size() > 5) scores.remove(scores.size() - 1);

        StringBuilder sb = new StringBuilder();
        for (int s : scores) {
            if (sb.length() > 0) sb.append(",");
            sb.append(s);
        }

        prefs.edit().putString("records", sb.toString()).apply();


        Log.d("SceneStack", "📦 Current Scene Stack:");
        for (Scene scene : GameView.view.getSceneStack()) {
            Log.d("SceneStack", " - " + scene.getClass().getSimpleName());
        }
    }
}
