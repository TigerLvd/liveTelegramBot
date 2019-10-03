package model;

public enum DayOfTheWeek {
    MO ("Понедельник"),
    TU ("Вторник"),
    WE ("Среда"),
    TH	("Четверг"),
    FR	("Пятница"),
    SA	("Суббота"),
    SU	("Воскресенье");

    final String title;

    DayOfTheWeek(String name) {
        title = name;
    }
}
