import java.util.Iterator;
import java.util.Scanner;
import java.lang.Object;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

public class testing{
    public static void main(String[] args) throws IOException {
        Scanner reader = new Scanner(System.in);
        System.out.print("Please enter the filename: ");
        String fileName = reader.next();
        System.out.println(fileName);
        File file = new File(fileName);
        LineIterator it = FileUtils.lineIterator(file, "UTF-8");
        String lastLine = "";
        try{
            while (it.hasNext()){
                lastLine = it.nextLine();
            }
        } finally{
            it.close();
        }
        System.out.println(lastLine);
        reader.close();
    }
}