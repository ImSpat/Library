package library.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LibraryUser extends User {

    private List<Publication> publicationHistory = new ArrayList<>();
    private List<Publication> borrowedPublications = new ArrayList<>();

    public LibraryUser(String firstName, String lastName, String pesel) {
        super(firstName, lastName, pesel);
    }

    public List<Publication> getPublicationHistory() {
        return publicationHistory;
    }

    public List<Publication> getBorrowedPublications() {
        return borrowedPublications;
    }

    public void addPublicationToHistory(Publication pub) {
        publicationHistory.add(pub);
    }

    public void borrowPublication(Publication pub) {
        borrowedPublications.add(pub);
    }

    public boolean returnPublication(Publication pub) {
        boolean result = false;
        if (borrowedPublications.contains(pub)) {
            borrowedPublications.remove(pub);
            addPublicationToHistory(pub);
            result = true;
        }
        return result;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(borrowedPublications, publicationHistory);
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
        LibraryUser other = (LibraryUser) obj;
        return Objects.equals(borrowedPublications, other.borrowedPublications)
                && Objects.equals(publicationHistory, other.publicationHistory);
    }

    @Override
    public String toCsv() {
        return getFirstName() + ";" + getLastName() + ";" + getPesel();
    }



}
