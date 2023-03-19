package library.model;

import java.io.Serializable;
import java.time.Year;
import java.util.Objects;

public abstract class Publication implements Serializable, Comparable<Publication>, CsvConvertible {

    private Year year;
    private String title;
    private String publisher;


    public Publication(String title, String publisher, int year) {
        this.title = title;
        this.publisher = publisher;
        this.year = Year.of(year);
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    @Override
    public String toString() {
        return title + ", " + publisher + ", " + year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(publisher, title, year);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Publication other = (Publication) obj;
        return Objects.equals(publisher, other.publisher) && Objects.equals(title, other.title)
                && Objects.equals(year, other.year);
    }

    @Override
    public int compareTo(Publication p) {
        return title.compareToIgnoreCase(p.title);
    }



}
