package com.example.lr10_mapsquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class QuizActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private Button btnA, btnB, btnC, btnD;

    private String currentCity = "";
    private int correctButtonId; // Переменная для хранения ID правильной кнопки

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Находим все элементы интерфейса (Шаг 6)
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        btnA = findViewById(R.id.btnA);
        btnB = findViewById(R.id.btnB);
        btnC = findViewById(R.id.btnC);
        btnD = findViewById(R.id.btnD);

        // Получаем информацию, на какой маркер кликнул пользователь
        currentCity = getIntent().getStringExtra("city_name");
        if (currentCity == null) currentCity = "Moscow";

        setupQuiz(); // Настраиваем вопросы и варианты под выбранный город
    }

    // Метод настройки вопросов и ответов
    private void setupQuiz() {
        switch (currentCity) {
            case "Moscow":
                imageView.setImageResource(R.drawable.moscow);
                textView.setText("Какая из достопримечательностей находится в Москве?");
                btnA.setText("Кремль"); // Правильный
                btnB.setText("Колизей");
                btnC.setText("Эйфелева Башня");
                btnD.setText("Тадж-Махал");
                correctButtonId = R.id.btnA;
                break;

            case "Paris":
                imageView.setImageResource(R.drawable.paris);
                textView.setText("Какая из достопримечательностей находится в Париже?");
                btnA.setText("Статуя Свободы");
                btnB.setText("Эйфелева Башня"); // Правильный
                btnC.setText("Кремль");
                btnD.setText("Пирамиды");
                correctButtonId = R.id.btnB;
                break;

            case "Rio":
                imageView.setImageResource(R.drawable.rio);
                textView.setText("Какая из достопримечательностей находится в Рио-де-Жанейро?");
                btnA.setText("Биг-Бен");
                btnB.setText("Великая Китайская стена");
                btnC.setText("Статуя Христа-Искупителя"); // Правильный
                btnD.setText("Парфенон");
                correctButtonId = R.id.btnC;
                break;

            case "Sydney":
                imageView.setImageResource(R.drawable.sydney);
                textView.setText("Какая из достопримечательностей находится в Сиднее?");
                btnA.setText("Стоунхендж");
                btnB.setText("Мачу-Пикчу");
                btnC.setText("Лувр");
                btnD.setText("Сиднейский оперный театр"); // Правильный
                correctButtonId = R.id.btnD;
                break;
        }
    }

    // Обработчик нажатия на кнопки вариантов ответов (Шаг 7)
    public void onAnswerClick(View view) {
        Button clickedButton = (Button) view;

        if (clickedButton.getId() == correctButtonId) {
            // Если ответ верный - красим кнопку в зеленый (стр. 14)
            clickedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.correct_green));
            showCorrectDialog(); // Показываем диалоговое окно
        } else {
            // Если неверный - красим нажатую кнопку в красный (стр. 14)
            clickedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.incorrect_red));
            // И блокируем её, чтобы нельзя было нажать снова
            clickedButton.setEnabled(false);
        }
    }

    // Обновленный метод диалога (Шаг 11)
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
                // Добавляем нейтральную кнопку для открытия WebView (Шаг 11)
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
                        // Возвращаемся на карту и передаем информацию, что ответили верно
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