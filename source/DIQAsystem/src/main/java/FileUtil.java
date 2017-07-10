import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chait on 07/06/2017.
 */
public class FileUtil {

    public String readFromFile(String path){
        String everything=null;
        try {
        BufferedReader br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return everything;
    }

    public void writeInTOFile(String Path){

    }

    public List<String> getStopWordsFromList(String filePath){
        List<String> stopWords = null;
        try{
            BufferedReader reader = new BufferedReader(new FileReader("stopwords.txt"));
            String line = reader.readLine();
            stopWords = new ArrayList<>();
            while (line != null) {
                stopWords.add(line);
                line = reader.readLine();
            }
            reader.close();
        }
        catch (FileNotFoundException e){
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Exception occured while reading file!");
        }
        return stopWords;
    }
}
