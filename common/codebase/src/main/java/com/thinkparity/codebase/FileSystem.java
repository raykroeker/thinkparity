/*
 * Created On: May 28, 2006 10:38:45 AM
 * $Id$
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.FileFilter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A file system is a simple wrapper around a single root directory. It
 * abstracts certain file operations.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class FileSystem {

    /** The file system path separator. */
    private static final String PATH_SEP;

    static { PATH_SEP = "/"; }

    /** The file system root. */
    private final File root;

    /**
     * Create FileSystem.
     * 
     * @param root
     *            The file system root.
     */
    public FileSystem(final File root) {
        if(null == root) throw new NullPointerException();
        if(!root.canRead()) throw new IllegalArgumentException();
        if(!root.isDirectory()) throw new IllegalArgumentException();
        this.root = root;
    }

    /**
     * Clone the file system beneath a relative path.
     * 
     * @param path
     *            A relative path.
     * @return A file system.
     */
    public FileSystem cloneChild(final String path) {
        if(null == path) throw new NullPointerException();
        return new FileSystem(resolve(path));
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    public boolean equals(Object obj) {
        if(null != obj && obj instanceof FileSystem) {
            return root.equals(((FileSystem) obj).root);
        }
        else { return false; }
    }

    /**
     * Obtain the file system root.
     * 
     * @return The file system root.
     */
    public File getRoot() { return root; }

    /** @see java.lang.Object#hashCode() */
    public int hashCode() { return root.hashCode(); }

    /**
     * List all files in the file system beneath the relative path. If a
     * recursive listing is required; see
     * {@link #list(String, Boolean) list(String,Boolean)}.
     * 
     * @param path
     *            A relative path.
     * @return A list of files.
     */
    public File[] list(final String path) { return list(path, Boolean.FALSE); }

    /**
     * List all files in the file system beneath the relative path. If a
     * recursive listing is required; see
     * {@link #list(String, Boolean) list(String,Boolean)}.
     * 
     * @param path
     *            A relative path.
     * @param doRecurse
     *            Whether or not to recursively pull out files.
     * @return A list of files.
     */
    public File[] list(final String path, final Boolean doRecurse) {
        final File file = resolve(path);
        if(null == file) { return null; }
        if(file.isFile()) { return null; }
        
        if(doRecurse) { return list(file).toArray(new File[] {}); }
        else { return file.listFiles(); }
    }

    /**
     * List all directories in the file system beneath the relative path. If a
     * recursive list is required see
     * {@link #listDirectories(String, Boolean) listDirectories(String, Boolean)}.
     * 
     * @param path
     *            A relative path.
     * @return A list of files.
     */
    public File[] listDirectories(final String path) {
        return listDirectories(path, Boolean.FALSE);
    }

    /**
     * List all directories in the file system beneath the relative path.
     * 
     * @param path
     *            A relative path.
     * @param doRecurse
     *            If set to true; a recursive list is obtained.
     * @return A list of directories.
     */
    public File[] listDirectories(final String path, final Boolean doRecurse) {
        final File file = resolve(path);
        if(null == file) { return null; }
        if(file.isFile()) { return null; }
    
        if(doRecurse) { return listDirectories(file).toArray(new File[] {}); }
        else {
            return file.listFiles(new FileFilter() {
                public boolean accept(final File pathname) {
                    return pathname.isDirectory();
                }
            });
        }
    }

    /**
     * List all files in the file system beneath the relative path. If a
     * recursive listing is required; see
     * {@link #listFiles(String, Boolean) listFiles(String, Boolean)}.
     * 
     * @param path
     *            A relative path.
     * @return A list of files.
     */
    public File[] listFiles(final String path) {
        return listFiles(path, Boolean.FALSE);
    }

    /**
     * List all files in the file system beneath the relative path.
     * 
     * @param path
     *            A relative path.
     * @param doRecurse
     *            Whether or not to recursively pull out files.
     * @return A list of files.
     */
    public File[] listFiles(final String path, final Boolean doRecurse) {
        final File file = resolve(path);
        if(null == file) { return null; }
        else if(file.isFile()) { return null; }
        else {
            if(doRecurse) { return listFiles(file).toArray(new File[] {}); }
            else {
                return file.listFiles(new FileFilter() {
                    public boolean accept(final File pathname) {
                        return pathname.isFile();
                    }
                });
            }
        }
    }

    /**
     * Print the file system tree to standard out.
     *
     */
    public void printTree() { printTree(System.out); }

    /**
     * Print the file system tree to a print stream.
     * 
     * @param printStream
     *            A print stream.
     */
    public void printTree(final PrintStream printStream) {
        synchronized(printStream) {
            printStream.println(this);
            printStream.print("[");
            printStream.print(root.getName());
            printStream.println("]");
            final List<File> tree = list(root);
            for(int i = 0; i < tree.size(); i++) {
                print(printStream, tree.get(i));
            }
        }
    }

    /**
     * Print the file system tree to a print writer.
     * 
     * @param printWriter
     *            A print writer.
     */
    public void printTree(final PrintWriter printWriter) {
        synchronized(printWriter) {
            printWriter.println(this);
            printWriter.print("[");
            printWriter.print(root.getName());
            printWriter.println("]");
            final List<File> tree = list(root);
            for(int i = 0; i < tree.size(); i++) {
                print(printWriter, tree.get(i));
            }
        }
    }

    /** @see java.lang.Object#toString() */
    public String toString() { return root.toString(); }

    /**
     * Count the depth of the file in the file system.
     * 
     * @param file
     *            A file.
     * @return The depth of the file. Files in the root have a depth of 0.
     */
    private int depth(final File file) {
        int depth = 0;
        File parent = file.getParentFile();
        while(!parent.equals((root))) {
            depth++;
            parent = parent.getParentFile();
        }
        return depth;
    }

    /**
     * Recursively list the contents of a directory.
     * 
     * @param directory
     *            A directory.
     * @return A list of files.
     */
    private List<File> list(final File directory) {
        final List<File> files = new ArrayList<File>();
        for(final File file : directory.listFiles()) {
            files.add(file);
            if(file.isDirectory()) { files.addAll(list(file)); }
        }
        return files;
    }

    /**
     * Recursively list the directories of a directory.
     * 
     * @param directory
     *            A directory.
     * @return A list of directories.
     */
    private List<File> listDirectories(final File directory) {
        final List<File> directories = new ArrayList<File>();
        for(final File file : directory.listFiles()) {
            if(file.isDirectory()) {
                directories.add(file);
                directories.addAll(listDirectories(file));
            }
        }
        return directories;
    }

    /**
     * Recursively list the files of a directory.
     * 
     * @param directory
     *            A directory.
     * @return A list of files.
     */
    private List<File> listFiles(final File directory) {
        final List<File> files = new ArrayList<File>();
        for(final File file : directory.listFiles()) {
            if(file.isFile()) { files.add(file); }
            else if(file.isDirectory()) { files.addAll(listFiles(file)); }
        }
        return files;
    }

    /**
     * Create a relative path of the file to the root.
     * 
     * @param printStream
     *            A print stream.
     * @param file
     *            A file.
     * @return A path relative to the root.
     */
    private void print(final PrintStream printStream, final File file) {
        for(int i = 0; i < depth(file) + 1; i++) { printStream.print("  "); }
        printStream.print("[");
        printStream.print(file.getName());
        printStream.println("]");
    }

    /**
     * Create a relative path of the file to the root.
     * 
     * @param printStream
     *            A print stream.
     * @param file
     *            A file.
     * @return A path relative to the root.
     */
    private void print(final PrintWriter printWriter, final File file) {
        for(int i = 0; i < depth(file) + 1; i++) { printWriter.print("  "); }
        printWriter.print("[");
        printWriter.print(file.getName());
        printWriter.println("]");
    }

    /**
     * Resolve a given path in the file system.
     * 
     * @param path
     *            A file system path.
     * @return The file; or null if the path does not match a real file.
     */
    private File resolve(final String path) {
        final StringBuffer realPath = new StringBuffer();
        final StringTokenizer pathTokenizer = new StringTokenizer(path, PATH_SEP);
        while(pathTokenizer.hasMoreTokens()) {
            realPath.append(pathTokenizer.nextToken());
            if(pathTokenizer.hasMoreTokens()) { realPath.append(File.separator); }
        }
        final File file = new File(root, realPath.toString());
        if(file.exists()) { return file; }
        else { return null; }
    }
}
