package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.Log;

import kr.ac.tukorea.ge.and.endlessrunner.R;
import kr.ac.tukorea.ge.and.endlessrunner.app.MainActivity;
import kr.ac.tukorea.ge.and.endlessrunner.game.MainScene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;

public class GameOverScene extends Scene {

    private final int score;
    private final float distance;
    private final boolean isMale;

    public GameOverScene(int score, float distance, boolean isMale) {
        this.score = score;
        this.distance = distance;
        this.isMale = isMale;

        initLayers(Layer.COUNT.ordinal());

        add(Layer.ui, new Button(
                R.mipmap.retry_button,
                Metrics.width / 2,
                Metrics.height * 0.65f,
                300, 120, // ✅ 크기 조절 (터치 영역 작게)
                pressed -> {
                    Log.d("GameOverScene", "🔁 Retry 버튼 눌림");
                    new MainScene(isMale).change();
                    return true;
                }
        ));

        add(Layer.ui, new Button(
                R.mipmap.to_main_btn,
                Metrics.width / 2,
                Metrics.height * 0.8f,
                300, 120, // 터치 감지용 영역
                pressed -> {
                    new TitleScene().change();
                    return true;
                }
        ));


    }

    public enum Layer {
        ui, COUNT
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        Paint paint = new Paint();
        paint.setTextSize(70f);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);

        canvas.drawText("Game Over", Metrics.width / 2, 400, paint);
        canvas.drawText("Score: " + score, Metrics.width / 2, 500, paint);
        canvas.drawText("Distance: " + (int)distance + "m", Metrics.width / 2, 600, paint);
        canvas.drawText("Character: " + (isMale ? "Male" : "Female"), Metrics.width / 2, 700, paint);
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.ui.ordinal(); // ui 레이어에 있는 버튼이 터치를 받도록 지정
    }

    @Override
    public void onEnter() {
        super.onEnter();

        Log.d("SceneStack", "📦 Current Scene Stack:");
        for (Scene scene : GameView.view.getSceneStack()) {
            Log.d("SceneStack", " - " + scene.getClass().getSimpleName());
        }
    }
}
