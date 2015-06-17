package org.sparx.sequoia;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timbrooks on 6/16/15.
 */
public class Parser<F, C> {

    private final FeatureParser<F> featureParser;
    private Int2ObjectMap<F> featureIndexMap;

    public Parser(FeatureParser<F> featureParser) {
        this.featureParser = featureParser;
    }

    public Forest<Integer, double[]> parseForestFromStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        ignoreHeader(reader);
        F[] features = featureParser.parseFeatures(reader);
        featureIndexMap = buildFeatureIndexMap(features);
        List<Tree<Integer, double[]>> trees = parseTrees(reader);


        return Planter.createForestFromTrees(trees);
    }

    private List<Tree<Integer, double[]>> parseTrees(BufferedReader reader) throws IOException {
        int numberOfTrees = readNumberOfElements(reader.readLine());
        if (numberOfTrees <= 0) {
            throw new RuntimeException("numTrees has to be > 0");
        }
        List<Tree<Integer, double[]>> trees = new ArrayList<>();
        for (int i = 0; i < numberOfTrees; i++) {
            int numNodes = readNumberOfNodes(reader.readLine());

            List<Node<Integer, double[]>> nodes = new ArrayList<>();
            for (int j = 0; j < numNodes; j++) {
                nodes.add(parseNode(reader));
            }

            if (numNodes != nodes.size()) {
                String message = String.format("Number of nodes does not match expected: [expected: %s, actual: %s].",
                        numNodes, nodes.size());
                throw new RuntimeException(message);
            }
            trees.add(Planter.createTreeFromNodes(nodes));
        }
        return trees;
    }

    private Int2ObjectMap<F> buildFeatureIndexMap(F[] featuresFromModelFile) {
        Int2ObjectMap<F> result = new Int2ObjectOpenHashMap<>(featuresFromModelFile.length);
        for (int i = 0; i < featuresFromModelFile.length; i++) {
            result.put(i, featuresFromModelFile[i]);
        }
        return result;
    }


    private Node<Integer, double[]> parseNode(final BufferedReader reader) throws IOException {
        String[] nodeDataTokens = parseNodeData(reader.readLine());
        int featureIndex = Integer.parseInt(nodeDataTokens[0]);
        double cutPoint = Double.parseDouble(nodeDataTokens[1]);
        int[] childNodes = {Integer.parseInt(nodeDataTokens[2]),
                Integer.parseInt(nodeDataTokens[3])};
        double value = Double.parseDouble(nodeDataTokens[5]);

        boolean isLeaf = featureIndex == -1;
        if (isLeaf) {
            return new Node<>(featureIndex, value, isLeaf, childNodes, new LeafCondition());
        } else {
            return new Node<>(featureIndex, cutPoint, isLeaf, childNodes, new NumericCondition(cutPoint));
        }
    }

    private String[] parseNodeData(String line) {
        String[] nodeData = line.split(",");

        if (nodeData.length != 6) {
            throw new RuntimeException("expected 6 fields to specify a node");
        }
        for (int i = 0; i < nodeData.length; i++) {
            nodeData[i] = nodeData[i].trim();
        }
        return nodeData;
    }

    private static int readNumberOfNodes(String line) {
        if (!"tree [0-9]+".matches(line)) {
            throw new RuntimeException("Tree does not match pattern");
        }
        String[] tokens = line.split(" ");
        return Integer.parseInt(tokens[1].trim());
    }

    private int readNumberOfElements(String line) {
        return Integer.parseInt(readKeyVal(line));
    }

    private void ignoreHeader(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    private String readKeyVal(String line) {
        String[] tokens = line.split("=", 2);
        if (tokens.length != 2) {
            throw new RuntimeException(String.format(
                    "expected line in \"key = val\", got %s", line));
        }
        return tokens[1].trim();
    }
}
