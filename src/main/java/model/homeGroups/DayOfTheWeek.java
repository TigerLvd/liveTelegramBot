package model.homeGroups;

public enum DayOfTheWeek {
    MD("Понедельник"),
    TU ("Вторник"),
    WE ("Среда"),
    TH	("Четверг"),
    FR	("Пятница"),
    ST	("Суббота"),
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
