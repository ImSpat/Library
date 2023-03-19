package library.io;

import java.util.Collection;

import library.model.Book;
import library.model.LibraryUser;
import library.model.Magazine;
import library.model.Publication;

public class ConsolePrinter {

    public void printBooks(Collection<Publication> publications) {
//		int booksCounter = 0;
//		for (Publication publication : publications) {
//			if (publication instanceof Book) {
//				printLine(publication.toString());
//				booksCounter++;
//			}
//		}

        long booksCounter = publications.stream()
                .filter(p -> p instanceof Book)
                .map(Publication::toString)
                .peek(this::printLine)
                .count();
        if (booksCounter == 0) {
            printLine("Brak książek w bibliotece");
        }
    }

    public void printMagazines(Collection<Publication> publications) {
        long magazinesCounter = publications.stream()
                .filter(p -> p instanceof Magazine)
                .map(Publication::toString)
                .peek(this::printLine)
                .count();
        if (magazinesCounter == 0) {
            printLine("Brak magazynów w bibliotece");

        }

    }

    public void printUsers(Collection<LibraryUser> users) {
//		for (LibraryUser user : users) {
//			printLine(user.toString());
//		}
        users.stream()
                .map(LibraryUser::toString)
                .forEach(this::printLine);
    }

    public void printLine(String text) {
        System.out.println(text);
    }

}
