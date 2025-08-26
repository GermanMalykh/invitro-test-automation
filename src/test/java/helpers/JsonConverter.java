package helpers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonConverter {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Десериализует объект из JSON-строки.
     *
     * @param jsonContent  строка с JSON-содержимым
     * @param expectedType класс типа, в который нужно десериализовать
     * @param <T>          тип возвращаемого объекта
     * @return объект, полученный из JSON
     * @throws RuntimeException если возникает ошибка при парсинге JSON
     */
    public static <T> T deserializeFromString(String jsonContent, Class<T> expectedType) {
        try {
            return mapper.readValue(jsonContent, expectedType);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при парсинге JSON-строки", e);
        }
    }

    /**
     * Десериализует объект из JSON-файла.
     *
     * @param file         файл с JSON-содержимым
     * @param expectedType класс типа, в который нужно десериализовать
     * @param <T>          тип возвращаемого объекта
     * @return объект, полученный из JSON
     * @throws RuntimeException если возникает ошибка при чтении файла или десериализации
     */
    public static <T> T deserialize(File file, Class<T> expectedType) {
        try {
            return mapper.readValue(file, expectedType);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении JSON-файла: " + file.getPath(), e);
        }
    }

    /**
     * Десериализует объект из JSON-файла по указанному пути.
     *
     * @param path         путь к файлу
     * @param expectedType класс типа, в который нужно десериализовать
     * @param <T>          тип возвращаемого объекта
     * @return объект, полученный из JSON
     * @throws RuntimeException если возникает ошибка при чтении файла или десериализации
     */
    public static <T> T deserialize(Path path, Class<T> expectedType) {
        try {
            return mapper.readValue(path.toFile(), expectedType);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении JSON-файла: " + path, e);
        }
    }

    /**
     * Десериализует объект из JSON-файла по строковому пути.
     *
     * @param pathToFile   путь к JSON-файлу в виде строки
     * @param expectedType класс типа, в который нужно десериализовать
     * @param <T>          тип возвращаемого объекта
     * @return объект, полученный из JSON
     * @throws RuntimeException если возникает ошибка при чтении файла или десериализации
     */
    public static <T> T deserialize(String pathToFile, Class<T> expectedType) {
        try {
            String contents = Files.readString(Path.of(pathToFile));
            return deserializeFromString(contents, expectedType);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении JSON по пути: " + pathToFile, e);
        }
    }

    /**
     * Считывает JSON-файл по указанному пути, нормализует его в компактную форму (удаляя пробелы и переносы),
     * а затем кодирует результат в формате URL, подходящем для использования в cookie.
     * <p>
     * Используется, например, для установки cookie с данными корзины на сайте.
     *
     * @param pathToFile путь к JSON-файлу
     * @return URL-encoded строка с компактным JSON
     * @throws RuntimeException если возникает ошибка при чтении или обработке JSON
     */
    public static String readCompactEncodedJson(String pathToFile) {
        try {
            String raw = Files.readString(Path.of(pathToFile));
            String compactJson = mapper.writeValueAsString(mapper.readTree(raw));
            return URLEncoder.encode(compactJson, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении и кодировании JSON из: " + pathToFile, e);
        }
    }
}
