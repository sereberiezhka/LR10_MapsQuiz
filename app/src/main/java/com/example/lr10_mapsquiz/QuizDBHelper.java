package com.example.lr10_mapsquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuizDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "quiz.db";
    private static final int DB_VERSION = 1;

    public QuizDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Создаем таблицу вопросов (id, город, текст вопроса, индекс правильной кнопки) (стр. 16)
        db.execSQL("CREATE TABLE questions (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "city TEXT NOT NULL, " +
                "question_text TEXT NOT NULL, " +
                "correct_answer_index INTEGER NOT NULL);");

        // 2. Создаем таблицу ответов (id, id_вопроса, текст ответа) (стр. 16)
        db.execSQL("CREATE TABLE answers (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "question_id INTEGER NOT NULL, " +
                "answer_text TEXT NOT NULL);");

        // 3. Заполняем базу вопросами для всех 4 городов (Шаг 10 из ЛР10)
        db.execSQL("INSERT INTO questions (city, question_text, correct_answer_index) VALUES ('Moscow', 'Какая из достопримечательностей находится в Москве?', 0);"); // 0 - кнопка А
        db.execSQL("INSERT INTO questions (city, question_text, correct_answer_index) VALUES ('Paris', 'Какая из достопримечательностей находится в Париже?', 1);"); // 1 - кнопка B
        db.execSQL("INSERT INTO questions (city, question_text, correct_answer_index) VALUES ('Rio', 'Какая из достопримечательностей находится в Рио-де-Жанейро?', 2);"); // 2 - кнопка C
        db.execSQL("INSERT INTO questions (city, question_text, correct_answer_index) VALUES ('Sydney', 'Какая из достопримечательностей находится в Сиднее?', 3);"); // 3 - кнопка D

        // 4. Заполняем базу ответами (по 4 штуки на каждый вопрос) (стр. 16)
        // Ответы для Москвы (question_id = 1)
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (1, 'Кремль');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (1, 'Колизей');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (1, 'Эйфелева Башня');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (1, 'Тадж-Махал');");

        // Ответы для Парижа (question_id = 2)
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (2, 'Статуя Свободы');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (2, 'Эйфелева Башня');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (2, 'Кремль');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (2, 'Пирамиды');");

        // Ответы для Рио (question_id = 3)
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (3, 'Биг-Бен');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (3, 'Великая Китайская стена');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (3, 'Статуя Христа-Искупителя');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (3, 'Парфенон');");

        // Ответы для Сиднея (question_id = 4)
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (4, 'Стоунхендж');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (4, 'Мачу-Пикчу');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (4, 'Лувр');");
        db.execSQL("INSERT INTO answers (question_id, answer_text) VALUES (4, 'Сиднейский оперный театр');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS questions;");
        db.execSQL("DROP TABLE IF EXISTS answers;");
        onCreate(db);
    }
}