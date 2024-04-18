import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UserDataApp {

    // Проверка корректности введенных данных
    private static void validateInput(String[] data) throws IllegalArgumentException {
        // Проверяем количество элементов
        if (data.length != 6) {
            throw new IllegalArgumentException("Неверное количество данных. Ожидается 6 полей.");
        }

        // Проверяем формат даты рождения (dd.mm.yyyy)
        String birthDate = data[3];
        if (!Pattern.matches("\\d{2}\\.\\d{2}\\.\\d{4}", birthDate)) {
            throw new IllegalArgumentException("Неверный формат даты рождения. Ожидается dd.mm.yyyy.");
        }

        // Проверяем номер телефона
        String phoneNumber = data[4];
        try {
            long phone = Long.parseLong(phoneNumber);
            if (phone <= 0) {
                throw new IllegalArgumentException("Номер телефона должен быть положительным целым числом.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Номер телефона должен быть целым числом.");
        }

        // Проверяем пол (m или f)
        String gender = data[5];
        if (!gender.equals("m") && !gender.equals("f")) {
            throw new IllegalArgumentException("Пол должен быть 'm' или 'f'.");
        }

        // Проверяем фамилию, имя и отчество, что они состоят только из латинских букв
        String surname = data[0];
        String name = data[1];
        String patronymic = data[2];

        if (!isLatinLetters(surname) || !isLatinLetters(name) || !isLatinLetters(patronymic)) {
            throw new IllegalArgumentException("Фамилия, имя и отчество должны состоять только из латинских букв.");
        }
    }

    // Функция для проверки, что строка состоит только из латинских букв
    private static boolean isLatinLetters(String text) {
        return Pattern.matches("[a-zA-Z]+", text);
    }

    public static void main(String[] args) {
        // Создаем сканер с кодировкой UTF-8
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());

        System.out.println("Введите данные в формате: Фамилия Имя Отчество дата_рождения номер_телефона пол");
        System.out.print("Ввод: ");

        try {
            // Считываем строку данных от пользователя
            String input = scanner.nextLine();

            // Разбиваем строку на массив данных
            String[] data = input.split(" ");

            // Проверяем введенные данные
            validateInput(data);

            // Получаем значения из массива
            String surname = data[0];
            String name = data[1];
            String patronymic = data[2];
            String birthDate = data[3];
            String phoneNumber = data[4];
            String gender = data[5];

            // Создаем файл с названием, равным фамилии
            File file = new File(surname + ".txt");

            // Используем try-with-resources для автоматического управления ресурсами
            try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8, true);
                    PrintWriter printWriter = new PrintWriter(fileWriter)) {

                // Записываем данные в файл в одну строку
                printWriter.printf("%s %s %s %s %s %s%n", surname, name, patronymic, birthDate, phoneNumber, gender);

                System.out.println("Данные успешно записаны в файл " + surname + ".txt");
            }

        } catch (IllegalArgumentException e) {
            // Обработка исключений, связанных с некорректными входными данными
            System.err.println("Ошибка: " + e.getMessage());
        } catch (IOException e) {
            // Обработка исключений, связанных с проблемами чтения-записи в файл
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Закрываем сканер
            scanner.close();
        }
    }
}
