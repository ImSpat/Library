package library.app;

import java.util.Comparator;
import java.util.InputMismatchException;

import library.exception.DataExportException;
import library.exception.DataImportException;
import library.exception.InvalidDataException;
import library.exception.NoSuchOptionException;
import library.exception.UserAlreadyExistsException;
import library.io.ConsolePrinter;
import library.io.DataReader;
import library.io.file.FileManager;
import library.io.file.FileManagerBuilder;
import library.model.Book;
import library.model.Library;
import library.model.LibraryUser;
import library.model.Magazine;
import library.model.Publication;
import library.model.User;


public class LibraryControl {

    private ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader = new DataReader(printer);
    private FileManager fileManager;
    private Library library;

    LibraryControl() {
        fileManager = new FileManagerBuilder(printer, dataReader).build();
        try {
            library = fileManager.importData();
            printer.printLine("Zaimportowano dane z pliku");
        } catch (DataImportException | InvalidDataException e) {
            printer.printLine(e.getMessage());
            printer.printLine("Zainicjowano nową bazę");
            library = new Library();
        }
    }

    void controlLoop() {
        Option option;

        do {
            printOptions();
            option = getOption();
            switch (option) {
                case ADD_BOOK:
                    addBook();
                    break;
                case ADD_MAGAZINE:
                    addMagazine();
                    break;
                case PRINT_BOOKS:
                    printBooks();
                    break;
                case PRINT_MAGAZINES:
                    printMagazines();
                    break;
                case DELETE_BOOK:
                    deleteBook();
                    break;
                case DELETE_MAGAZINE:
                    deleteMagazine();
                    break;
                case ADD_USER:
                    addUser();
                    break;
                case PRINT_USERS:
                    printUsers();
                    break;
                case FIND_BOOK:
                    findBook();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    printer.printLine("Nie ma takiej opcji, wprowadź ponownie: ");
            }
        } while (option != Option.EXIT);
    }

    private void findBook() {
        printer.printLine("Podaj tytuł publikacji:");
        String title = dataReader.getString();
        String notFoundMessage = "Brak publikacji o takim tytule";
        library.findPublicationByTitle(title)
                .map(Publication::toString)
                .ifPresentOrElse(printer::printLine, () -> printer.printLine(notFoundMessage));
    }

    private void printUsers() {
        printer.printUsers(library.getSortedUsers(
//				(p1, p2) -> p1.getLastName().compareToIgnoreCase(p2.getLastName())
                Comparator.comparing(User::getFirstName, String.CASE_INSENSITIVE_ORDER)
        ));

    }

    private void addUser() {
        LibraryUser libraryUser = dataReader.createLibraryUser();
        try {
            library.addUser(libraryUser);
        } catch (UserAlreadyExistsException e) {
            printer.printLine(e.getMessage());
        }
    }

    private Option getOption() {
        boolean optionOk = false;
        Option option = null;
        while (!optionOk) {
            try {
                option = Option.createFromInt(dataReader.getInt());
                optionOk = true;
            } catch (NoSuchOptionException e) {
                printer.printLine(e.getMessage());
            } catch (InputMismatchException e) {
                printer.printLine("Wprowadzono wartość, która nie jest liczbą, podaj ponownie");
            }
        }
        return option;
    }

    private void addMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            library.addPublication(magazine);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utowrzyć magazynu, niepoprawne dane");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine(e.getMessage());
        }
    }

    private void deleteMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            if (library.removePublication(magazine)) {
                printer.printLine("Usunięto magazyn");
            } else {
                printer.printLine("Brak wskazanego magazynu");
            }
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się usunąć magazynu, niepoprawne dane");
        }

    }

    private void printMagazines() {
        printer.printMagazines(library.getSortedPublications(
                //(p1, p2) -> p1.getTitle().compareToIgnoreCase(p2.getTitle())
                Comparator.comparing(Publication::getTitle, String.CASE_INSENSITIVE_ORDER)
        ));
    }

    private void printOptions() {
        printer.printLine("Wybierz opcję: ");
        for (Option value : Option.values()) {
            printer.printLine(value.toString());
        }

    }

    private void addBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            library.addPublication(book);
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się utowrzyć książki, niepoprawne dane");
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine(e.getMessage());
        }
    }

    private void deleteBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            if (library.removePublication(book)) {
                printer.printLine("Usunięto książkę");
            } else {
                printer.printLine("Brak wskazanej książki");
            }
        } catch (InputMismatchException e) {
            printer.printLine("Nie udało się usunąć książki, niepoprawne dane");
        }

    }

    private void printBooks() {
        printer.printBooks(library.getSortedPublications(
//                (p1, p2) -> p1.getTitle().compareToIgnoreCase(p2.getTitle())
                Comparator.comparing(Publication::getTitle, String.CASE_INSENSITIVE_ORDER)
        ));
    }

    private void exit() {
        try {
            fileManager.exportData(library);
            printer.printLine("Export danych do pliku zkończony powodzeniem");
        } catch (DataExportException e) {
            printer.printLine(e.getMessage());
        }
        printer.printLine("Koniec programu, papa!");
        dataReader.close();
    }

    private enum Option {

        EXIT(0, "wyjście z programu"),
        ADD_BOOK(1, "dodanie nowej książki"),
        ADD_MAGAZINE(2, "dodanie nowego magazynu"),
        PRINT_BOOKS(3, "wyświetl dostępne książki"),
        PRINT_MAGAZINES(4, "wyświetl dostępne magazyny"),
        DELETE_BOOK(5, "Usuń książkę"),
        DELETE_MAGAZINE(6, "Usuń magazyn"),
        ADD_USER(7, "Dodaj czytelników"),
        PRINT_USERS(8, "Wyświetl czytelników"),
        FIND_BOOK(9, "Wyszukaj publikację");

        private final int value;
        private String description;

        private Option(int value, String description) {
            this.value = value;
            this.description = description;
        }

        public int getValue() {
            return value;
        }

        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException {
            try {
                return Option.values()[option];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException("Brak opcji o id " + option);
            }
        }

    }

}
