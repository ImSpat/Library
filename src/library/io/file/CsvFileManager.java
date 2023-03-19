package library.io.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import library.exception.DataExportException;
import library.exception.DataImportException;
import library.exception.InvalidDataException;
import library.model.Book;
import library.model.CsvConvertible;
import library.model.Library;
import library.model.LibraryUser;
import library.model.Magazine;
import library.model.Publication;

public class CsvFileManager implements FileManager {

    private static final String FILE_NAME = "Library.csv";
    private static final String USERS_FILE_NAME = "Library_users.csv";

    @Override
    public Library importData() {
        Library library = new Library();
        importPublications(library);
        importUsers(library);
        return library;


    }

    private void importUsers(Library library) {
        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader(USERS_FILE_NAME))
        ){
            bufferedReader.lines()
                    .map(this::createUserFromString)
                    .forEach(library::addUser);
        }

        catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku " + USERS_FILE_NAME);
        } catch (IOException e1) {
            throw new DataImportException("Błąd odczytu pliku " + USERS_FILE_NAME);
        }

    }

    private LibraryUser createUserFromString(String csvText) {
        String[] split = csvText.split(";");
        return new LibraryUser(split[0], split[1], split[2]);
    }

    private void importPublications(Library library) {
        try(
                BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))
//				Scanner fileReader = new Scanner(new File(FILE_NAME))
        ){
            bufferedReader.lines()
                    .map(this::createObjectFromString)
                    .forEach(library::addPublication);
//				while(fileReader.hasNextLine()) {
//					String line = fileReader.nextLine();
//					Publication publication = createObjectFromString(line);
//					library.addPublication(publication);
        }

        catch (FileNotFoundException e) {
            throw new DataImportException("Brak pliku " + FILE_NAME);
        } catch (IOException e1) {
            throw new DataImportException("Błąd odczytu pliku " + USERS_FILE_NAME);
        }
    }

    private Publication createObjectFromString(String line) {
        String[] split = line.split(";");
        String type = split[0];
        if(Book.TYPE.equals(type)) {
            return createBook(split);
        } else if (Magazine.TYPE.equals(type)) {
            return createMagazine(split);
        }
        throw new InvalidDataException("Nieznany typ publikacji " + type);
    }

    private Magazine createMagazine(String[] split) {
        String title = split[1];
        String publisher = split[2];
        int year = Integer.valueOf(split[3]);
        int month = Integer.valueOf(split[4]);
        int day = Integer.valueOf(split[5]);
        String language = split[6];
        return new Magazine(title, publisher, language, year, month, day);
    }

    private Book createBook(String[] split) {
        String title = split[1];
        String publisher = split[2];
        int year = Integer.valueOf(split[3]);
        String author = split[4];
        int pages = Integer.valueOf(split[5]);
        String isbn = split[6];
        return new Book(title, author, year, pages, publisher, isbn);
    }

    @Override
    public void exportData(Library library) {
        exportPublications(library);
        exportUsers(library);



    }

    private void exportUsers(Library library) {
        Collection<LibraryUser> users = library.getUsers().values();
        exportToCsv(users, USERS_FILE_NAME);


    }

    private void exportPublications(Library library) {
        Collection<Publication> publications = library.getPublications().values();
        exportToCsv(publications, FILE_NAME);
    }

    private <T extends CsvConvertible> void exportToCsv(Collection<T> collection, String fileName) {
        try (
                var fileWriter = new FileWriter(fileName);
                var bufferedWriter = new BufferedWriter(fileWriter)
        ) {
            for (T element : collection) {
                bufferedWriter.write(element.toCsv());
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            throw new DataExportException("Błąd zapisu danych do pliku " + fileName);
        }

    }

}
