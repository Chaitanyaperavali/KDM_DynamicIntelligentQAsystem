import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Quadruple;
import rita.RiWordNet;

import java.util.*;

/**
 * Created by chait on 04/07/2017.
 */
public class NLProcessor {


    private Map<String,Set<String>> nerMap = new HashMap();
    List<List<Quadruple<String, String, String, Double>>> l = new ArrayList<>();
    private  FileUtil f = new FileUtil();
    Properties props = new Properties();
    StanfordCoreNLP pipeline;
    RiWordNet wordnet = new RiWordNet("C:\\Program Files (x86)\\WordNet\\2.1");
    String ans;

    public void initiateCoreNlp(){
        props.setProperty("annotators", "tokenize, ssplit,pos,lemma,ner");
        pipeline = new StanfordCoreNLP(props);
    }

    public void startSystem(){
        System.out.println("Initiating coreNLP Lib...");
        initiateCoreNlp();
        String path = "data\\input.txt";
        System.out.println("Reading input file at location:\n"+path);
        String line = f.readFromFile(path);//change file path here.
        System.out.println(line);
        parse(line);
        System.out.println("System stared....\n\nAsk questions,\nEnter 'quit' to exit");
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String questionInPut = scanner.nextLine();
            if(!questionInPut.equalsIgnoreCase("quit") && questionInPut != null){
                ans = null;
                extract(questionInPut);
            }
            else{
                break;
            }
        }
    }

    public void extract(String question){
        parseQuestion(question);
    }

    public void parseQuestion(String question) {
        Annotation annotation =  pipeline.process(question);
        List<String> qWords = new ArrayList<>();
        List<String> sWords= f.getStopWordsFromList("stopwords.txt");
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
            for (CoreLabel token: tokens) {
                String word = token.get(CoreAnnotations.LemmaAnnotation.class);
                if(!sWords.contains(word)){
                    qWords.add(word);
                    if(word.length()>3){
                        //getSynonyms(word);
                    }
                }
            }
        }
        printTriplets(qWords);
    }

    public void parse(String story){
        if (story != null && story.length() > 0) {
            Annotation annotation = pipeline.process(story);
            int id = 0;
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                //Tokenization
                id++;
                //System.out.println(id+" : "+sentence);
                returnTriplets(sentence.toString());
                List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
                for(CoreLabel token:tokens){
                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    // String v  = token.getString(CoreAnnotations.TextAnnotation.class);
                    //System.out.println("NE : "+ne);
                    if(!ne.equalsIgnoreCase("O")){
                        Set<String> nerList = nerMap.get(ne);
                        String[] t = token.toString().split("-");
                        if(nerList == null){
                            nerList = new HashSet<>();
                        }
                        nerList.add(t[0]);
                        nerMap.put(ne,nerList);
                    }
                }


            }
        }
    }

    //This piece of code fetches triplets using openie
    public void returnTriplets(String sentence){
        Document doc = new Document(sentence);
        for (Sentence sent : doc.sentences()) {
            l.add((List)sent.openie());
        }
    }
    // based on question type and keywords in question triplets are fetched.
    public void printTriplets(List<String> qWords){
                    Iterator it =  l.iterator();
                    int countPrevious = 0;
                    while(it.hasNext()){
                        List f = (List) it.next();
                        Iterator itr = f.listIterator();
                        while(itr.hasNext()){
                            int countCurrent = 0;
                            String s = itr.next().toString().toLowerCase()
                                    .replace(","," ")
                                    .replace("("," ")
                                    .replace(")"," ");
                           String lemmatized = CoreNLPLemma.returnLemma(s);
                           //System.out.println(lemmatized);
                            for(String ele : qWords){
                                if(lemmatized.contains(ele.toLowerCase())){
                                    countCurrent++;
                                }
                            }
                            if(countCurrent > countPrevious){
                                countPrevious =  countCurrent;
                                ans = s.substring(0,s.length()-4);
                            }
                        }
                    }
                    System.out.println(ans);
                }

    public void getSynonyms(String word){
        String[] poss = wordnet.getPos(word);
        for (int j = 0; j < poss.length; j++) {
            System.out.println("\n\nSynonyms for " + word + " (pos: " + poss[j] + ")");
            String[] synonyms = wordnet.getAllSynonyms(word,poss[j],3);
            for (int i = 0; i < synonyms.length; i++) {
                System.out.println(synonyms[i]);
            }
        }
    }

}
