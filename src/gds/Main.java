package gds;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {

    public static String FILE = "file.txt";
    public static String OUTPUT_FILE = "output.xml";

    public static String CSV_SPLIT_BY = "_STOP_";
    public static String RETURN = "_RETURN_";

    public static String[] tags = {
            "</recipeCategory>\n</recipe>\n<recipe>\n<id>",
            "</id>\n<name>",
            "</name>\n<prepTime>",
            "</prepTime>\n<cookTime>",
            "</cookTime>\n<recipeInstructions>",
            "</recipeInstructions>\n<recipeIngredient>",
            "</recipeIngredient>\n<recipeCategory>"
    };

    // For the open office export of "Il libro delle mie ricette", replace all the \n with _RETURN_ string (write all the file in one line)

    public static void main(String[] args) {

        String line = "";

        try (BufferedReader br = new BufferedReader(new FileReader(FILE))) {

            PrintWriter writer = new PrintWriter(OUTPUT_FILE, "UTF-8");

            while ((line = br.readLine()) != null) {

                int beginIndex = 0;
                int endIndex = line.indexOf(CSV_SPLIT_BY, 2);
                int i=0;

                while (endIndex > 0) {

                    int tagIndex = i % tags.length;

                    writer.println(tags[tagIndex]);

                    String content = line.substring(beginIndex + CSV_SPLIT_BY.length()-1, endIndex);

                    if (tagIndex==4) {
                        content = content.replace(RETURN, "\n");
                    } else if (tagIndex==5) {
                        content = content.replace(RETURN, "\n</recipeIngredient>\n<recipeIngredient>\n");
                    }
                    writer.println(content);

                    i++;

                    beginIndex = endIndex + 1;
                    endIndex = line.indexOf(CSV_SPLIT_BY, beginIndex);
                }

            }
            writer.println("</recipeCategory>\n</recipe>");
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
