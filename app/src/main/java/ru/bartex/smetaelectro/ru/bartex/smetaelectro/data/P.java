package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class P {

    public static final String TAG = "33333";

    //имя файла  по умолчанию
    public static final String FILENAME_DEFAULT = "Первая смета";
    //имя текущего файла сметы
    public static final String FILENAME_CURRENT = "Имя файла сметы";
    //Название объекта по умолчанию
    public static final String ADRESS_DEFAULT = "Новый объект";
    //Описание сметы объекта по умолчанию
    public static final String DESCRIPTION_DEFAULT = "Смета на работу на новом объекте";
    //id файла по умолчанию из таблицы файлов
    public static final String ID_FILE_DEFAULT = "ID файла на смету на работу на новом объекте";
    //id файла по умолчанию из таблицы файлов
    public static final String ID_FILE = "ID файла сметы";
    //id категории из таблицы категорий
    public static final String ID_CATEGORY = "ID категории работ";
    //позиция категории в списке категорий
    public static final String POSITION_CATEGORY = "POSITION_CATEGORY";
    //позиция типа работы  в списке типов
    public static final String POSITION_TYPE = "POSITION_TYPE";
    //id типа работ
    public static final String ID_TYPE = "ID типа работ";
    //id выбранной работы
    public static final String ID_WORK = "ID выбранной работы";
    //есть ли в смете такой пункт с работой
    public static final String IS_WORK = "Есть такой пункт?";

    // номер выбранной работы
    public static final String WORK_NUMBER = "Номер выбранной работы";
    // Имя выбранной работы
    public static final String WORK_NAME = "Имя выбранной работы";
    // Расценка для выбранной работы
    public static final String WORK_COST = "Расценка для выбранной работы";
    // Количество выбранной работы
    public static final String WORK_AMOUNT = "Количество выбранной работы";
    // Единицы измерения выбранной работы
    public static final String WORK_UNITS = "Единицы измерения выбранной работы";
    // Стоимшсть выбранной работы
    public static final String WORK_SUMMA = "Стоимшсть выбранной работы";



    //Ключ имени категории
    public static final String ATTR_CATEGORY_NAME = "ATTR_CATEGORY_NAME";
    //Ключ чекбокса  категории
    public static final String ATTR_CATEGORY_MARK = "ATTR_CATEGORY_MARK";

    //Ключ имени типа работ
    public static final String ATTR_TYPE_NAME = "ATTR_TYPE_NAME";
    //Ключ чекбокса  типа работ
    public static final String ATTR_TYPE_MARK = "ATTR_TYPE_MARK";

    //Ключ имени  работ
    public static final String ATTR_WORK_NAME = "ATTR_WORK_NAME";
    //Ключ чекбокса   работ
    public static final String ATTR_WORK_MARK = "ATTR_WORK_MARK";

    public static final int SPECIFIC_ID = 1;
    public static final int CHANGE_NAME_ID = 2;
    public static final int DELETE_ID = 3;

    // Удалить пункт сметы
    public static final int DELETE_ITEM_SMETA = 1;
    // Отменить
    public static final int CANCEL = 4;


    public static String setDateTimeString() {
        Calendar calendar = new GregorianCalendar();
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        int sec = calendar.get(Calendar.SECOND);
        return String.format(Locale.ENGLISH, "%02d.%02d.%04d_%02d:%02d:%02d",
                date, month + 1, year, hour, min, sec);
    }
}
