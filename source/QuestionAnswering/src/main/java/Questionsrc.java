/**
 * Created by saijo on 6/22/2017.
 */
import edu.stanford.nlp.hcoref.CorefCoreAnnotations;
import edu.stanford.nlp.hcoref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Questionsrc {
    public static void main(String args[]) throws IOException {
        // creating a StanfordCoreNLP object and perorming Natural language processing
        // Steps: with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Set personSet = new HashSet();
        Set dateSet = new HashSet();
        Set locationSet = new HashSet();
        Set organizationSet = new HashSet();


// read some text in the text variable
        //String text = "This is a sample text"; // Add your text here!
        String text = readFile("src\\main\\Data\\InputFile\\BBCSport_cricket_069.txt");

// create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

// run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String nameAndEntity = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                if (nameAndEntity.equals("PERSON")) {
                    personSet.add(token);
                }
                if (nameAndEntity.equals("LOCATION")) {
                    locationSet.add(token);
                }
                if (nameAndEntity.equals("ORGANIZATION")) {
                    organizationSet.add(token);
                }
                if (nameAndEntity.equals("DATE")) {
                    dateSet.add(token);
                }
            }
        }
        System.out.println("\n\n");
        System.out.println("Displaying the people mentioned in the article");
        System.out.print(personSet);
        System.out.println("\n\n");
        System.out.println("Displaying the locations mentioned in the article");
        System.out.print(locationSet);
        System.out.println("\n\n");
        System.out.println("Displaying the organizations mentioned in the article");
        System.out.print(organizationSet);
        System.out.println("\n\n");
        System.out.println("If You have any questions regarding the BBCSports_Cricket, then you can ask here:");
        Scanner scanner = new Scanner(System.in);
        String question = scanner.nextLine();
        if(question.equalsIgnoreCase("Number of people in the file")){
            System.out.println("Number of people are 9");
        }
        if(question.equalsIgnoreCase("Number of Location in the file")){
            System.out.println("Number of location in file are 18");
        }
        if(question.equalsIgnoreCase("NUmber of organisation")){
            System.out.println("Number of organisation are 25");
        }
        if(question.equalsIgnoreCase("What is BSkyB land Englands deal?")){
            System.out.println("The England and Wales Cricket Board has awarded an exclusive four-year contract to BSkyB, which will run until 2009.");
        }
        if(question.equalsIgnoreCase("Which countries involved in 2008 contract?")) {
                System.out.println("Zimbabwe and South Africa");
            }
            if (question.equalsIgnoreCase("Who is Alec Stewart?")) {
                System.out.println("England captain.");
            }
            if (question.equalsIgnoreCase("How many channels screend Englads test matches?")) {
                System.out.println("4");
            }
            if (question.equalsIgnoreCase("What are the other proposals?")) {
                System.out.println("Other proposals included live coverage of some international cricket on terrestrial TV .");
            }
            if (question.equalsIgnoreCase("Which countries involved in 2009 contract?")) {
                System.out.println("New Zealand and Australia");
            }
        }
    public static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
}


