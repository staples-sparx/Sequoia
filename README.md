# Sequoia

An efficient Java decision tree library.

## Getting Started

Clone the repository and building the project using mvn package.

Place the jar in your project.

Sequoia depends on it.unimi.dsi.fastutil version 7.0.2.

Note: at some point in the near future Sequoia will be added to Maven.

## Usage

This library is pre-Alpha. API is subject to change.

Nodes should be created calling the constructor directly.

```java
public Node(F feature, double value, boolean isLeaf, int[] childOffsets, Condition<F, C> condition)
```

Trees and Forests can be created using the Planter class.

```java
public static <F, C> Tree<F, C> createTreeFromNodes(List<Node<F, C>> nodes)

public static <F, C> Forest<F, C> createForestFromTrees(List<Tree<F, C>> trees)
```