import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
class Solution {
    class Node {
        String word;
        int level;

        public Node(String word, int level) {
            this.word = word;
            this.level = level;
        }
    }

    private String end;
    private List<List<String>> res;
    private Map<String, List<String>> maps;

    public List<List<String>> findLadders(String start, String end,
                                          Set<String> dict) {
        res = new ArrayList<>();
        // unvisited words set
        dict.add(end);
        dict.remove(start);
        // used to record the map info of <word : the words of next level>
        maps = new HashMap<>();
        for (String e : dict) {
            maps.put(e, new ArrayList<>());
        }

        // BFS to search from the end to start
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(new Node(start, 0));
        boolean found = false;
        int finalLevel = Integer.MAX_VALUE;
        int currentLevel = 0;
        Set<String> visitedWordsInThisLevel = new HashSet<>();

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            String word = node.word;
            int level  = node.level;
            if (level > finalLevel) {
                break;
            }
            if (level > currentLevel) {
                dict.removeAll(visitedWordsInThisLevel);
                visitedWordsInThisLevel.clear();
            }
            currentLevel = level;
            char[] wordCharArray = word.toCharArray();
            for (int i = 0; i < word.length(); ++i) {
                char originalChar = wordCharArray[i];
                boolean foundInThisCycle = false;
                for (char c = 'a'; c <= 'z'; ++c) {
                    wordCharArray[i] = c;
                    String newWord = new String(wordCharArray);
                    if (c != originalChar && dict.contains(newWord)) {
                        maps.get(newWord).add(word);
                        if (newWord.equals(end)) {
                            found = true;
                            finalLevel = currentLevel;
                            foundInThisCycle = true;
                            break;
                        }
                        if (visitedWordsInThisLevel.add(newWord)) {
                            queue.add(new Node(newWord, currentLevel + 1));
                        }
                    }
                }
                if (foundInThisCycle) {
                    break;
                }
                wordCharArray[i] = originalChar;
            }
        }
        if(found){
            List<String> tmplist = new LinkedList<>();
            generatePath(end,start,tmplist);
        }
        return res;
    }
    private void generatePath(String start,String end,List<String> list){
        if(start.equals(end)){
            List<String> tmplist = new LinkedList<>(list);
            tmplist.add(end);
            Collections.reverse(tmplist);
            res.add(tmplist);
            return;
        }
        list.add(start);
        List<String> tmplist = maps.get(start);
        for(String e:tmplist)
            generatePath(e,end,list);
        list.remove(list.size()-1);
    }
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入起始word：");
        String start = sc.nextLine();
        System.out.println("请输入目标word：");
        int end = sc.nextInt();
        Object temp=null;
        File file =new File("dictionary.txt");
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn=new ObjectInputStream(in);
            temp=objIn.readObject();//从文件当中读取对象
            Set<Object> dict = new HashSet<Object>();
            dict.add(temp);//添加对象到set集合里面
            objIn.close();
            System.out.println("read object success!");
        } catch (IOException e) {
            System.out.println("read object failed");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}