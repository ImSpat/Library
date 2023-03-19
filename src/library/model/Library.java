package library.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import library.exception.PublicationAlreadyExistsException;
import library.exception.UserAlreadyExistsException;

public class Library implements Serializable {

    private Map<String, Publication> publications = new HashMap<>();
    private Map<String, LibraryUser> users = new HashMap<>();

    public Optional<Publication> findPublicationByTitle(String title){
        return Optional.ofNullable(publications.get(title));

    }

    public Collection<Publication> getSortedPublications(Comparator<Publication> comparator) {
        ArrayList<Publication> list = new ArrayList<>(this.publications.values());
        list.sort(comparator);
        return list;

    }

    public Collection<LibraryUser> getSortedUsers(Comparator<LibraryUser> comparator) {
        ArrayList<LibraryUser> list = new ArrayList<>(this.users.values());
        list.sort(comparator);
        return list;

    }

    public Map<String, Publication> getPublications() {
        return publications;
    }

    public Map<String, LibraryUser> getUsers() {
        return users;
    }

    public void addPublication(Publication publication) {
        if (publications.containsKey(publication.getTitle())) {
            throw new PublicationAlreadyExistsException("Publikacja o takim tytule już istnieje " + publication.getTitle());
        }

        publications.put(publication.getTitle(), publication);

    }

    public void addUser(LibraryUser user) {
        if (users.containsKey(user.getPesel())) {
            throw new UserAlreadyExistsException("Użytkownik o takim peselu już istnieje " + user.getPesel());
        }

        users.put(user.getPesel(), user);

    }

    public boolean removePublication(Publication pub) {
        if (publications.containsValue(pub)) {
            publications.remove(pub.getTitle());
            return true;
        } else {
            return false;
        }
    }

}
