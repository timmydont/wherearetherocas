package com.timmydont.wherearetherocas.services;

public interface LoadService {

    /**
     * Load the file of the given path
     * @param path the path
     * @return true if it worked, false if it doesn't
     */
    boolean load(String path);
}
