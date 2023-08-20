package com.example.sleepguard;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper  {

    private Context context;

    private static final String DatabaseName = "Tasks";
    private static final int DatabaseVersion = 1;

    private static final String tableName = "tasks";
    private static final String columnId = "id";
    private static final String columnTitle = "title";
    private static final String columnHours = "hours";
    private static final String columnMinutes = "minutes";

    public DatabaseHelper(@Nullable Context context) {
        // задание параметров (контекст, название БД, курсор, версия БД)
        super(context, DatabaseName,null, DatabaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + tableName + " (" + columnId + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + columnTitle + " TEXT, " + columnHours + " Text, " + columnMinutes + " Text);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // определение запроса на удаление таблицы базы данных
        db.execSQL("DROP TABLE IF EXISTS "+ tableName);
        // определение запроса на создание таблицы базы данных
        onCreate(db);
    }

    public void addTask(String title, String hours, String minutes) {

        /** с помощью getWritableDatabase() мы проверяем есть ли подключение к БД,
         * если есть то им пользуемся, если нет то создаём новое
         */
        SQLiteDatabase db = this.getWritableDatabase();

        /** нужно создать объект класса ContentValues для добавления и обновления существующих записей БД,
         * Данный объект представляет словарь, который содержит набор пар "ключ-значение"
         * Для добавления в этот словарь нового объекта применяется метод put
         */
        ContentValues cv = new ContentValues();

        cv.put(columnTitle, title);
        cv.put(columnHours, hours);
        cv.put(columnMinutes, minutes);

        // добавление новой записи в БД
        long resultValue = db.insert(tableName,null, cv);

        if (resultValue == -1){ // если resultValue возвращает -1, значит добавление записи в БД не удалось
            Toast.makeText(context, "Не удалось добавить задачу!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Задача сохранена!", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readTasks(){

        // формирование запроса к БД
        String query = "SELECT * FROM " +  tableName;

        // метод getReadableDatabase() получает БД для чтения
        SQLiteDatabase database= this.getReadableDatabase();

        // создаём нулевой курсор
        Cursor cursor = null;

        if (database != null){ // если БД существует, то инициализируем курсор
            cursor = database.rawQuery(query,null);
        }

        // возврат курсора
        return  cursor;
    }

    // 5) удаление записи по id
    public  void  deleteSingleItem(String id){

        // проверка подключения к БД
        SQLiteDatabase db = this.getWritableDatabase();

        // удаление записи по id, где в метод delete() подаются
        // (название таблицы, запись для проверки id, запись в текстовый массив id)
        long result = db.delete(tableName,"id=?", new String[]{id});

        if (result == -1) {
            Toast.makeText(context, "Ошибка!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Задача выполнена!", Toast.LENGTH_SHORT).show();
        }
        Intent intentCancel = new Intent(context.getApplicationContext(), ListActivity.class);
        intentCancel.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intentCancel);
    }

    public void deleteAllNotes() {

        // проверка подключения к БД
        SQLiteDatabase database = this.getWritableDatabase();

        // формирование запроса удаления таблицы БД
        String query = "DELETE FROM " + tableName;
        database.execSQL(query);

    }
}
