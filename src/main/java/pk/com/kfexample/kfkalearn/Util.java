package pk.com.kfexample.kfkalearn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Util {
    static String basepath = "/home/pk/wrkspace/gitprac/kprac1/data";


    public static List<String> getFile(String paratition) {
        Path path = Paths.get(basepath + "/" + paratition + ".txt");
        List contents = null;
        try {
            contents = Files.readAllLines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }
}
