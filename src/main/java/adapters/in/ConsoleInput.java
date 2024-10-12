package adapters.in;

import java.util.Scanner;

public interface ConsoleInput<T> {
    T input(Scanner scanner);
}
