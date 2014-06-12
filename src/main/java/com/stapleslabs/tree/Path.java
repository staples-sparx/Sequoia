package com.stapleslabs.tree;

/**
 * Created by timbrooks on 6/12/14.
 */
public class Path {

    public int root;
    public int[][] fastPath;

    public void setFastPath(final int[][] fastPath) {
        this.fastPath = fastPath;
    }

    public void setRoot(final int root) {
        this.root = root;
    }
}
