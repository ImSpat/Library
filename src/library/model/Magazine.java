package library.model;

import java.time.MonthDay;
import java.util.Objects;

public class Magazine extends Publication {

    public static final String TYPE = "Magazyn";
    private MonthDay monthDay;
    private String language;

    public Magazine(String title, String publisher, String language, int year, int month, int day) {
        super(title, publisher, year);
        this.language = language;
        this.monthDay = MonthDay.of(month, day);

    }

    public MonthDay getMonthDay() {
        return monthDay;
    }

    public void setMonthDay(MonthDay monthDay) {
        this.monthDay = monthDay;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + monthDay.getMonthValue() + ", " + monthDay.getDayOfMonth() + ", " + language;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(language, monthDay);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Magazine other = (Magazine) obj;
        return Objects.equals(language, other.language) && Objects.equals(monthDay, other.monthDay);
    }

    @Override
    public String toCsv() {
        return (TYPE + ";") + getTitle() + ";" + getPublisher() + ";" + getYear() + ";" + monthDay.getMonthValue() + ";" + monthDay.getDayOfMonth() + ";"
                + language + "";
    }

}
