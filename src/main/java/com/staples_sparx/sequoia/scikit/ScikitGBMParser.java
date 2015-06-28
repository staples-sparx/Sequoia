/*
 * Copyright 2014 Staples Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.staples_sparx.sequoia.scikit;

import com.staples_sparx.sequoia.Node;
import com.staples_sparx.sequoia.Planter;
import com.staples_sparx.sequoia.Tree;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ScikitGBMParser {

    public static ScikitGBM parseForestFromStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        ignoreHeader(reader);
        double initValue = parseInitialValue(reader);
        double learningRate = parseLearningRate(reader);
        String[] features = parseFeatures(reader);
        Int2ObjectMap<String> featureIndexMap = buildFeatureIndexMap(features);
        List<Tree<Integer, double[]>> trees = parseTrees(reader);


        return new ScikitGBM(Planter.createForestFromTrees(trees), featureIndexMap, initValue, learningRate);
    }

    private static void ignoreHeader(BufferedReader reader) throws IOException {
        reader.readLine();
    }

    private static double parseInitialValue(BufferedReader reader) throws IOException {
        return Double.parseDouble(readKeyVal(reader.readLine()));
    }

    private static double parseLearningRate(final BufferedReader reader) throws IOException {
        return Double.parseDouble(readKeyVal(reader.readLine()));
    }

    private static String[] parseFeatures(final BufferedReader reader) throws IOException {
        String[] featureNameTokens = readKeyVal(reader.readLine()).split(",");
        if (featureNameTokens.length == 0) {
            throw new RuntimeException("features from model file can't be empty");
        }
        return featureNameTokens;
    }

    private static Int2ObjectMap<String> buildFeatureIndexMap(String[] featuresFromModelFile) {
        Int2ObjectMap<String> result = new Int2ObjectOpenHashMap<>(featuresFromModelFile.length);
        for (int i = 0; i < featuresFromModelFile.length; i++) {
            result.put(i, featuresFromModelFile[i]);
        }
        return result;
    }

    private static List<Tree<Integer, double[]>> parseTrees(BufferedReader reader) throws IOException {
        int numberOfTrees = readNumberOfElements(reader.readLine());
        if (numberOfTrees <= 0) {
            throw new ScikitGBMParsingException("numberOfTrees has to be > 0");
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
                throw new ScikitGBMParsingException(message);
            }
            trees.add(Planter.createTreeFromNodes(nodes));
        }
        return trees;
    }

    private static int readNumberOfNodes(String line) {
        if (!"tree [0-9]+".matches(line)) {
            throw new ScikitGBMParsingException("Tree does not match pattern");
        }
        String[] tokens = line.split(" ");
        return Integer.parseInt(tokens[1].trim());
    }


    private static Node<Integer, double[]> parseNode(final BufferedReader reader) throws IOException {
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

    private static String[] parseNodeData(String line) {
        String[] nodeData = line.split(",");

        if (nodeData.length != 6) {
            throw new ScikitGBMParsingException("expected 6 fields to specify a node");
        }
        for (int i = 0; i < nodeData.length; i++) {
            nodeData[i] = nodeData[i].trim();
        }
        return nodeData;
    }

    private static int readNumberOfElements(String line) {
        return Integer.parseInt(readKeyVal(line));
    }

    private static String readKeyVal(String line) {
        String[] tokens = line.split("=", 2);
        if (tokens.length != 2) {
            throw new ScikitGBMParsingException(String.format("expected line in \"key = val\", got %s", line));
        }
        return tokens[1].trim();
    }
}
