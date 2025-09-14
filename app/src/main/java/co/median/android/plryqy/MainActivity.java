package co.median.android.plryqy;



import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private AppCompatButton btnSpeak, btnCopy, btnShare, btnReset;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        btnSpeak = findViewById(R.id.btnSpeak);
        btnCopy = findViewById(R.id.btnCopy);
        btnShare = findViewById(R.id.btnShare);
        btnReset = findViewById(R.id.btnReset);

        btnCopy.setVisibility(View.GONE);
        btnShare.setVisibility(View.GONE);

        tts = new TextToSpeech(this, status -> {
            if (status != TextToSpeech.ERROR) {
                tts.setLanguage(Locale.getDefault());
            }
        });

        btnSpeak.setOnClickListener(v -> {
            String text = inputText.getText().toString().trim();
            if (TextUtils.isEmpty(text)) {
                Toast.makeText(this, "Please enter text to speak", Toast.LENGTH_SHORT).show();
                return;
            }
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            btnCopy.setVisibility(View.VISIBLE);
            btnShare.setVisibility(View.VISIBLE);
        });

        btnCopy.setOnClickListener(v -> {
            String text = inputText.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Text", text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        btnShare.setOnClickListener(v -> {
            String text = inputText.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

        btnReset.setOnClickListener(v -> {
            inputText.setText("");
            btnCopy.setVisibility(View.GONE);
            btnShare.setVisibility(View.GONE);
        });
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}