package kr.ac.tukorea.ge.and.endlessrunner.game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;

import kr.ac.tukorea.ge.and.endlessrunner.R;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Button;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.objects.Sprite;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.scene.Scene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.GameView;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.view.Metrics;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.res.Sound;

public class GameOverScene extends Scene {

    private final int score;
    private final float distance;
    private final boolean isMale;
    private Sprite gameOverSprite;
    private Sprite backgroundSprite;
    private Button retryBtn;
    private Button toMainBtn;

    public GameOverScene(int score, float distance, boolean isMale) {
        this.score = score;
        this.distance = distance;
        this.isMale = isMale;

        initLayers(Layer.COUNT.ordinal());

        // 배경 스프라이트 추가
        backgroundSprite = new Sprite(R.mipmap.gameover_background, Metrics.width / 2, Metrics.height / 2, Metrics.width, Metrics.height);
        add(Layer.ui, backgroundSprite);

        gameOverSprite = new Sprite(R.mipmap.game_over_2, Metrics.width / 2, Metrics.height * 0.15f, 800, 200);
        add(Layer.ui, gameOverSprite);

        retryBtn = new Button(
                R.mipmap.retry_btn,
                Metrics.width / 2, Metrics.height * 0.75f,
                400f, 150f,
                pressed -> {
                    if (pressed) {
                        Sound.playEffect(R.raw.touch);
                        new MainScene(isMale).change();
                    }
                    return true;
                }
        );
        retryBtn.setCustomSize(true);
        add(TitleScene.Layer.ui, retryBtn);


        toMainBtn = new Button(
                R.mipmap.to_main_btn,
                Metrics.width / 2, Metrics.height * 0.85f,
                400f, 150f,
                pressed -> {
                    if (pressed) {
                        Sound.playEffect(R.raw.touch);
                        new TitleScene().change();
                    }
                    return true;
                }
        );
        toMainBtn.setCustomSize(true);
        add(TitleScene.Layer.ui, toMainBtn);

    }

    @Override
    public void onEnter() {
        super.onEnter();
        // 배경음악 재생
        Sound.playMusic(R.raw.background);

        // 점수 저장 로직
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

    @Override
    public void onExit() {
        super.onExit();
        // 배경음악 정지
        Sound.stopMusic();
    }

    public enum Layer {
        ui, COUNT
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // 결과 표시 배경
        Paint bgPaint = new Paint();
        bgPaint.setColor(Color.argb(180, 0, 0, 0));
        float startY = Metrics.height * 0.3f;
        float endY = Metrics.height * 0.65f;
        canvas.drawRect(Metrics.width * 0.15f, startY, Metrics.width * 0.85f, endY, bgPaint);

        // 텍스트 스타일 설정
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // 제목 텍스트
        textPaint.setTextSize(80f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("GAME RESULT", Metrics.width / 2, startY + 80f, textPaint);

        // 구분선
        Paint linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3f);
        float lineY = startY + 100f;
        canvas.drawLine(Metrics.width * 0.3f, lineY, Metrics.width * 0.7f, lineY, linePaint);

        // 결과 텍스트
        textPaint.setTextSize(60f);
        float textY = startY + 250f;
        float spacing = 80f;
        float labelX = Metrics.width * 0.20f;  // 라벨 시작 위치를 왼쪽으로 더 조정
        float valueX = Metrics.width * 0.80f;  // 값 고정 위치를 더 오른쪽으로 조정

        // Score
        textPaint.setColor(Color.YELLOW);
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("SCORE", labelX, textY, textPaint);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(score), valueX, textY, textPaint);

        // Distance
        textY += spacing;
        textPaint.setColor(Color.YELLOW);
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("DISTANCE", labelX, textY, textPaint);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.format("%.0f m", distance), valueX, textY, textPaint);

        // Character
        textY += spacing;
        textPaint.setColor(Color.YELLOW);
        textPaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("CHARACTER", labelX, textY, textPaint);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(isMale ? "MALE" : "FEMALE", valueX, textY, textPaint);
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.ui.ordinal(); // ui 레이어에 있는 버튼이 터치를 받도록 지정
    }
}
