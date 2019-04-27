package ru.bartex.smetaelectro.ru.bartex.smetaelectro.data;

import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import ru.bartex.smetaelectro.DialogWorkOrMatCosts;

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
    //диалог вызван из работы?
    public static final String IS_WORK_DIALOG = "Диалог вызван из работы?";


    //id категории материалов
    public static final String ID_CATEGORY_MAT = "ID_CATEGORY_MAT";
    //id типа материалов
    public static final String ID_TYPE_MAT = "ID_TYPE_MAT";
    //ID выбранного  материала
    public static final String ID_MAT = "ID выбранного  материала";
    //есть ли в смете такой пункт материала
    public static final String IS_MAT = "Есть такой пункт материала?";

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
    public static final String WORK_SUMMA = "Стоимость выбранной работы";
    // имя типа или ваботы в списке пунктов сметы
    public static final String TYPE_WORK_NAME = "Имя типа или работы";
    // Стоимость выбранной работы
    public static final String COST_OF_WORK = "Расценка для выбранной работы";

    // номер выбранного материала
    public static final String MAT_NUMBER = "Номер выбранного материала";
    // Имя выбранного материала
    public static final String MAT_NAME = "Имя выбранного материала";
    // Цена для выбранного материала
    public static final String MAT_COST = "Цена для выбранного материала";
    // Количество выбранного материала
    public static final String MAT_AMOUNT = "Количество выбранного материала";
    // Единицы измерения выбранного материала
    public static final String MAT_UNITS = "Единицы измерения выбранного материала";
    // Стоимшсть выбранного материала
    public static final String MAT_SUMMA = "Стоимость выбранного материала";

    //выбранная в материалах вкладка
    public static final String TAB_POSITION = "Выбранная_вкладка";
    //выбран тип материалов?
    public static final String IS_SELECTED_TYPE = "IS_SELECTED_TYPE";
    //выбрана категория   материала?
    public static final String IS_SELECTED_CAT = "IS_SELECTED_MAT";

    //Ключ имени категории
    public static final String ATTR_CATEGORY_NAME = "ATTR_CATEGORY_NAME";
    //Ключ чекбокса  категории
    public static final String ATTR_CATEGORY_MARK = "ATTR_CATEGORY_MARK";

    //Ключ имени типа работ
    public static final String ATTR_TYPE_NAME = "ATTR_TYPE_NAME";
    //Ключ чекбокса  типа работ
    public static final String ATTR_TYPE_MARK = "ATTR_TYPE_MARK";

    //Ключ имени типа материалов
    public static final String ATTR_TYPE_MAT_NAME = "ATTR_TYPE_MAT_NAME";
    //Ключ чекбокса  типа материалов
    public static final String ATTR_TYPE_MAT_MARK = "ATTR_TYPE_MAT_MARK";

    //Ключ имени материалов
    public static final String ATTR_MAT_NAME = "ATTR_MAT_NAME";
    //Ключ чекбокса  материалов
    public static final String ATTR_MAT_MARK = "ATTR_MAT_MARK";


    //Ключ имени  работ
    public static final String ATTR_WORK_NAME = "ATTR_WORK_NAME";
    //Ключ чекбокса   работ
    public static final String ATTR_WORK_MARK = "ATTR_WORK_MARK";

    public static final int SPECIFIC_ID = 1;
    public static final int CHANGE_NAME_ID = 2;
    public static final int DELETE_ID = 3;

    // Удалить пункт сметы работы
    public static final int DELETE_ITEM_SMETA = 4;
    // Отменить пункт сметы работы
    public static final int CANCEL = 5;
    // Удалить пункт сметы материалов
    public static final int DELETE_ITEM_SMETA_MAT = 6;
    // Отменить пункт сметы материалов
    public static final int CANCEL_MAT = 7;
    // для SmetaDetail риквест код
    public static final int REQUEST_COST = 8;



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

    public static float updateTotalSumma(float[] work_summa) {
        float totalSumma = 0;
        for (int i = 0; i < work_summa.length; i++) {
            totalSumma += work_summa[i];
        }
        return totalSumma;
    }



}
