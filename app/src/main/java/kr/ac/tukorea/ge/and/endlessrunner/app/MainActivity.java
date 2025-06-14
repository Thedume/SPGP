package kr.ac.tukorea.ge.and.endlessrunner.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import kr.ac.tukorea.ge.and.endlessrunner.R;
import kr.ac.tukorea.ge.and.endlessrunner.databinding.ActivityMainBinding;
import kr.ac.tukorea.ge.and.endlessrunner.game.TitleScene;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding ui;
    private int characterIndex = 0; // 0 = 남자, 1 = 여자

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new TitleScene().push();
    }

    public void onBtnStartGame(View view) {
        Intent intent = new Intent(this, EndlessRunnerActivity.class);
        intent.putExtra("character", characterIndex);
        startActivity(intent);
    }

    public void onBtnPrevCharacter(View view) {
        setCharacterIndex((characterIndex + 1) % 2);
    }

    public void onBtnNextCharacter(View view) {
        setCharacterIndex((characterIndex + 1) % 2);
    }

    private void setCharacterIndex(int index) {
        characterIndex = index;

        int resId = (index == 0) ? R.mipmap.char_male_icon : R.mipmap.char_female_icon;
        ui.characterImageView.setImageResource(resId);
        ui.characterNameTextView.setText((index == 0) ? "남자 캐릭터" : "여자 캐릭터");
    }
}
