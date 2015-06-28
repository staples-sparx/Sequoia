# Sequoia

An efficient Java decision tree library.

## Getting Started

To require the current release version in Gradle, add the following to the dependencies section of your build file:
> compile 'com.staples-sparx:Sequoia:0.20.2'

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