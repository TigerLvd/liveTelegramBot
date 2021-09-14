package model.homegroups;

public enum DayOfTheWeek {
    MD("Понедельник"),
    TU("Вторник"),
    WE("Среда"),
    TH("Четверг"),
    FR("Пятница"),
    ST("Суббота"),
    SU("Воскресенье");

    private final String title;

    DayOfTheWeek(String name) {
        title = name;
    }

    public String getTitle() {
        return title;
    }
}
