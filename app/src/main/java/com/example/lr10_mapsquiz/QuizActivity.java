package com.example.lr10_mapsquiz; // <-- ТВОЙ ПАКЕТ

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private Button btnA, btnB, btnC, btnD;

    private String currentCity = "";
    private int correctButtonId; // Переменная для хранения ID правильной кнопки

    private QuizDBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        currentCity = getIntent().getStringExtra("city_name");
        if (currentCity == null) currentCity = "Moscow";

        // Инициализируем БД викторины (Задание 2)
        dbHelper = new QuizDBHelper(this);
        database = dbHelper.getReadableDatabase();

        setupQuizFromDB(); // Загружаем данные из БД! (Задание 2)
    }

    // Метод динамической сборки викторины из БД (Задание 2)
    private void setupQuizFromDB() {
        if (database == null) return;

        // 1. Устанавливаем соответствующую картинку на экране
        switch (currentCity) {
            case "Moscow": imageView.setImageResource(R.drawable.moscow); break;
            case "Paris": imageView.setImageResource(R.drawable.paris); break;
            case "Rio": imageView.setImageResource(R.drawable.rio); break;
            case "Sydney": imageView.setImageResource(R.drawable.sydney); break;
        }

        // 2. Достаем текст вопроса и индекс верной кнопки из таблицы questions
        Cursor qCursor = database.rawQuery("SELECT * FROM questions WHERE city = ?", new String[]{currentCity});
        int questionId = -1;
        int correctIndex = -1;

        if (qCursor.moveToFirst()) {
            questionId = qCursor.getInt(0); // Получаем ID вопроса (_id)
            String questionText = qCursor.getString(2); // Получаем текст вопроса (question_text)
            correctIndex = qCursor.getInt(3); // Получаем индекс верной кнопки (correct_answer_index)

            textView.setText(questionText);
        }
        qCursor.close();

        // 3. Достаем 4 варианта ответов из таблицы answers по нашему questionId
        if (questionId != -1) {
            Cursor aCursor = database.rawQuery("SELECT * FROM answers WHERE question_id = ?", new String[]{String.valueOf(questionId)});
            ArrayList<String> answers = new ArrayList<>();
            if (aCursor.moveToFirst()) {
                while (!aCursor.isAfterLast()) {
                    answers.add(aCursor.getString(2)); // Получаем текст ответа (answer_text)
                    aCursor.moveToNext();
                }
            }
            aCursor.close();

            // Раскладываем 4 ответа из БД по кнопкам!
            if (answers.size() == 4) {
                btnA.setText(answers.get(0));
                btnB.setText(answers.get(1));
                btnC.setText(answers.get(2));
                btnD.setText(answers.get(3));
            }
        }

        // Определяем верную кнопку по индексу из БД (0 - btnA, 1 - btnB, 2 - btnC, 3 - btnD)
        if (correctIndex == 0) correctButtonId = R.id.btnA;
        else if (correctIndex == 1) correctButtonId = R.id.btnB;
        else if (correctIndex == 2) correctButtonId = R.id.btnC;
        else if (correctIndex == 3) correctButtonId = R.id.btnD;
    }

    public void onAnswerClick(View view) {
        Button clickedButton = (Button) view;

        if (clickedButton.getId() == correctButtonId) {
            clickedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correct_green));
            showCorrectDialog();
        } else {
            clickedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrect_red));
            clickedButton.setEnabled(false);
        }
    }

    private void showCorrectDialog() {
        String messageText = "";
        String wikiUrl = "";

        switch (currentCity) {
            case "Moscow":
                messageText = "Это Московский Кремль!";
                wikiUrl = "https://ru.wikipedia.org/wiki/Московский_Кремль";
                break;
            case "Paris":
                messageText = "Это Эйфелева башня в Париже!";
                wikiUrl = "https://ru.wikipedia.org/wiki/Эйфелева_башня";
                break;
            case "Rio":
                messageText = "Это Статуя Христа в Рио-де-Жанейро!";
                wikiUrl = "https://ru.wikipedia.org/wiki/Статуя_Христа-Искупителя";
                break;
            case "Sydney":
                messageText = "Это Сиднейская опера в Австралии!";
                wikiUrl = "https://ru.wikipedia.org/wiki/Сиднейский_оперный_театр";
                break;
        }

        final String finalUrl = wikiUrl;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Верно!")
                .setMessage(messageText)
                .setCancelable(false)
                .setNeutralButton("Подробнее (Справка)", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent webIntent = new Intent(QuizActivity.this, WebActivity.class);
                        webIntent.putExtra("url", finalUrl);
                        startActivity(webIntent);
                    }
                })
                .setPositiveButton("Продолжить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(QuizActivity.this, MapsActivity.class);
                        intent.putExtra("answered_city", currentCity);
                        startActivity(intent);
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}