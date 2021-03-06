package bouskfil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        String fileName = "nadr1";


        TrainStation trainStation = parseInput("./input/" + fileName + ".txt");
        Axioms axioms = new Axioms(trainStation, fileName + "_axioms.p", "nothing");
        Axioms axiomsSwitch = new Axioms(trainStation, fileName + "_switch.p", "switch");
        Axioms axiomsBlock = new Axioms(trainStation, fileName + "_block.p", "block");
        Axioms axiomsCollision = new Axioms(trainStation, fileName + "_collision.p", "collision");

        fileName = "nadr2";
        trainStation = parseInput("./input/" + fileName + ".txt");
        axioms = new Axioms(trainStation, fileName + "_axioms.p", "nothing");
        axiomsSwitch = new Axioms(trainStation, fileName + "_switch.p", "switch");
        axiomsBlock = new Axioms(trainStation, fileName + "_block.p", "block");
        axiomsCollision = new Axioms(trainStation, fileName + "_collision.p", "collision");

        fileName = "nadr_small";
        trainStation = parseInput("./input/" + fileName + ".txt");
        axioms = new Axioms(trainStation, fileName + "_axioms.p", "nothing");
        axiomsSwitch = new Axioms(trainStation, fileName + "_switch.p", "switch");
        axiomsBlock = new Axioms(trainStation, fileName + "_block.p", "block");
        axiomsCollision = new Axioms(trainStation, fileName + "_collision.p", "collision");

    }

    private static TrainStation parseInput(String fileName){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))) {
            String line = bufferedReader.readLine();
            String name = line.substring(8, line.length()-2);

            line = bufferedReader.readLine();
            ArrayList<String> temp = new ArrayList<>();
            HashMap<String, Node> map = new HashMap<>();
            while (!line.contains("}")) {
                String from = line.split(" -> ")[0].trim();
                String to = line.split(" -> ")[1].trim();
                to = to.substring(0, to.length()-1);

                if(!map.containsKey(from)) {
                    map.put(from, new Node(from));
                }
                if(!map.containsKey(to)) {
                    map.put(to, new Node(to));
                }

                temp.add(from);
                temp.add(to);

                line = bufferedReader.readLine();
            }

            for (int i = 0; i < temp.size(); i = i + 2) {
                String from = temp.get(i);
                String to = temp.get(i + 1);
                Node fromNode = map.get(from);
                Node toNode = map.get(to);
                fromNode.addNext(toNode);
                toNode.addPred(fromNode);
            }

            // determine which nodes are entry, exit or crossing
            for (Node tempNode : map.values()) {
                if (tempNode.getPred().size() == 0) {
                    tempNode.setIn(true);
                }
                if (tempNode.getNext().size() == 0) {
                    tempNode.setOut(true);
                }
                if (tempNode.getNext().size() > 1) {
                    tempNode.setCrossing(true);
                }
            }
            return new TrainStation(name, map);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
