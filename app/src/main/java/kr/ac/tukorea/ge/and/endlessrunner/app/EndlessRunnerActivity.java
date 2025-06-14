package kr.ac.tukorea.ge.and.endlessrunner.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import kr.ac.tukorea.ge.and.endlessrunner.game.TitleScene;
import kr.ac.tukorea.ge.spgp2025.a2dg.framework.activity.GameActivity;

public class EndlessRunnerActivity extends GameActivity {

    private static final String TAG = "EndlessRunnerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 캐릭터 인덱스 전달 받기
        Intent intent = getIntent();
        //int characterIndex = intent.getIntExtra("characterIndex", 0);
        int characterIndex = getIntent().getIntExtra("characterIndex", -1);
        Log.d(TAG, "characterIndex = " + characterIndex);

        // MainGameScene 생성 및 push
        new TitleScene().push();
    }
}