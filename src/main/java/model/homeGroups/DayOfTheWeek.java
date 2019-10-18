package model.homeGroups;

public enum DayOfTheWeek {
    MO ("Понедельник"),
    TU ("Вторник"),
    WE ("Среда"),
    TH	("Четверг"),
    FR	("Пятница"),
    SA	("Суббота"),
    SU	("Воскресенье");

    private String title;

    DayOfTheWeek(String name) {
        title = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
