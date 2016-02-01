import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Morhun Mykola
 */
public class Main {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void main(String[] args) throws IOException {
        trimInFilesTree(Paths.get("/tmp/test"));
    }

    public static boolean fileFilter(Path file) {
        return file.toString().endsWith(".java");
    }

    public static void trimInFilesTree(Path start) throws IOException {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (fileFilter(file)) {
                    System.out.println("Processing: " + file.toString());
                    trimInFile(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void trimInFile(Path file) throws IOException {
        List<String> lines = new ArrayList<>();

        Files.readAllLines(file).forEach(str -> lines.add(removeTrailingWhitespaces(str)));
        if (lines.size() > 0 && "".equals(lines.get(lines.size() - 1))) {
            lines.remove(lines.size() - 1);
        }
        //lines.forEach(s -> System.out.println(":"+s));

        try (FileWriter writer = new FileWriter(file.toFile())) {
            for (String str : lines) {
                writer.write(str);
                writer.write(LINE_SEPARATOR);
            }
        }
    }

    private static String removeTrailingWhitespaces(String str) {
        return str.replaceFirst("\\s+$", "");
    }

}
