/*
 * Created On: May 28, 2006 10:38:45 AM
 * $Id$
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.channels.WritableByteChannel;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.nio.ChannelUtil;

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
        if (null == root) {
            throw new NullPointerException();
        }
        if (!root.exists()) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "File system root does not exist.{0}{1}.",
                    Separator.SystemNewLine, root));
        }
        if (!root.canRead()) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "File system root cannot be read.{0}{1}.",
                    Separator.SystemNewLine, root));
        }
        if (!root.isDirectory()) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "File system root is not a directory.{0}{1}.",
                    Separator.SystemNewLine, root));
        }
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
    /**
     * Create a directory at the given path.
     * 
     * @param path
     *            A path.
     * @return The new directory.
     */
    public File createDirectory(final String path) {
        Assert.assertNotTrue(pathExists(path), "Path exists:  {0}", path);
        final String absolutePath = resolvePath(path);
        final File absoluteDirectory = new File(absolutePath);
        Assert.assertTrue(absoluteDirectory.mkdirs(),
                "Cannot create directory:  {0}", absoluteDirectory);
        return findDirectory(path);
    }

    /**
     * Create a file at the given path.
     * 
     * @param path
     *            A path.
     * @return The new file.
     */
    public File createFile(final String path) throws IOException {
        Assert.assertNotTrue(pathExists(path), "Path exists:  {0}", path);
        final String absolutePath = resolvePath(path);
        final File absoluteFile = new File(absolutePath);
        final File parentFile = absoluteFile.getParentFile();
        if (!parentFile.exists()) {
            Assert.assertTrue(parentFile.mkdirs(),
                    "Cannot create directory:  {0}", parentFile);
        }
        Assert.assertTrue(absoluteFile.createNewFile(),
                "Cannot create file:  {0}", absoluteFile);
        return findFile(path);
    }

    /**
     * Create a file at the given path and open a write channel.
     * 
     * @param path
     *            A path.
     * @return A <code>FileChannel</code>.
     */
    public WritableByteChannel createWriteChannel(final String path)
            throws IOException {
        return ChannelUtil.openWriteChannel(createFile(path));
    }

    /**
     * Delete the entire file system tree.
     *
     */
    public void deleteTree() {
        FileUtil.deleteTree(root);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    public boolean equals(final Object obj) {
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (FileSystem.class.isAssignableFrom(obj.getClass()))
            return ((FileSystem) obj).root.equals(root);
        else
            return false;
    }

    /**
     * Find a file at the given path.
     * 
     * @param path
     *            A relative path.
     * @return A <code>File</code>.
     */
    public File find(final String path) {
        return resolve(path);
    }

    /**
     * Find a directory at the given path.
     * 
     * @param path
     *            A relative path.
     * @return A directory.
     */
    public File findDirectory(final String path) {
        final File file = resolve(path);
        if (null == file) {
            return null;
        } else if (file.isFile()) {
            return null;
        }
        return file;
    }

    /**
     * Find a file at the given path.
     * 
     * @param path
     *            A relative path.
     * @return A directory.
     */
    public File findFile(final String path) {
        final File file = resolve(path);
        if (null == file) {
            return null;
        } else if (file.isDirectory()) {
            return null;
        }
        return file;
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
    public File[] list(final String path) {
        return list(path, Boolean.FALSE);
    }

    /**
     * List all files in the file system beneath the relative path. If a
     * recursive listing is required; see
     * {@link #list(String, Boolean) list(String,Boolean)}.
     * 
     * @param path
     *            A relative path.
     * @param recurse
     *            Whether or not to recursively pull out files.
     * @return A list of files.
     */
    public File[] list(final String path, final Boolean recurse) {
        final File file = resolve(path);
        if(null == file) { return null; }
        if(file.isFile()) { return null; }
        
        if (recurse) {
            return list(file).toArray(new File[] {});
        } else {
            return file.listFiles();
        }
    }

    /**
     * List all files (non-recursive) in the file system beneath the root that
     * match the filter.
     * 
     * @param filter
     *            A <code>FileFilter</code>.
     * @return A list of files.
     */
    public File[] list(final String path, final FileFilter filter) {
        return list(path, filter, Boolean.FALSE);
    }

    /**
     * List all files in the file system beneath the root that match the filter.
     * 
     * @param filter
     *            A <code>FileFilter</code>.
     * @param recurse
     *            Recurse the file system.
     * @return A list of files.
     */
    public File[] list(final String path, final FileFilter filter,
            final Boolean recurse) {
        final File file = resolve(path);
        if (null == file) {
            return null;
        }
        Assert.assertTrue(file.isDirectory(), "{0} is not a directory.", path);

        if (recurse) {
            return list(file, filter).toArray(new File[] {});
        } else {
            return file.listFiles(filter);
        }
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
     * @param recurse
     *            If set to true; a recursive list is obtained.
     * @return A list of directories.
     */
    public File[] listDirectories(final String path, final Boolean recurse) {
        final File file = resolve(path);
        if(null == file) { return null; }
        if(file.isFile()) { return null; }
    
        if (recurse) {
            return listDirectories(file).toArray(new File[] {});
        } else {
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
     * @param recurse
     *            Whether or not to recursively pull out files.
     * @return A list of files.
     */
    public File[] listFiles(final String path, final Boolean recurse) {
        final File file = resolve(path);
        if (null == file) {
            return null;
        } else if (file.isFile()) {
            return null;
        } else {
            if (recurse) {
                return listFiles(file).toArray(new File[] {});
            } else {
                return file.listFiles(new FileFilter() {
                    public boolean accept(final File pathname) {
                        return pathname.isFile();
                    }
                });
            }
        }
    }

    /**
     * Determine if the path exists for the file system.
     * 
     * @param path
     *            A relative path.
     * @return True if the path resolves; false otherwise.
     */
    public Boolean pathExists(final String path) {
        return null != resolve(path);
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

    /**
     * Rename a file.
     * 
     * @param path
     *            A source path <code>String</code>.
     * @param renameToPath
     *            A target path <code>String<code>.
     * @return The renamed <code>File</code>.
     */
    public File rename(final String path, final String renameToPath) {
        final File renameTo = new File(resolvePath(renameToPath));
        Assert.assertNotTrue(renameTo.exists(),
                "Rename to path {0} exists.", renameToPath);
        final File file = resolve(path);
        Assert.assertNotNull(file,
                "File path {0} does not exist.", path);
        Assert.assertTrue(file.renameTo(renameTo),
                "Cannot rename file {0} to {1}.", file, renameTo);
        return renameTo;
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
        return list(directory, new FileFilter() {
            public boolean accept(final File pathname) {
                return true;
            }
        });
    }

    private List<File> list(final File directory, final FileFilter filter) {
        final List<File> files = new ArrayList<File>();
        for(final File file : directory.listFiles(filter)) {
            files.add(file);
            if(file.isDirectory()) { files.addAll(list(file, filter)); }
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
     * Print the <code>File</code> within the
     * <code>FileSystem</code. to the given <code>PrintStream</code>.
     * 
     * @param printStream
     *            A print stream.
     * @param file
     *            A file.
     */
    private void print(final PrintStream printStream, final File file) {
        for (int i = 0; i < depth(file) + 1; i++) {
            printStream.print("  ");
        }
        printStream.print("[");
        printStream.print(file.getName());
        printStream.println("]");
    }

    /**
     * Print the given <code>File</code> within the <code>FileSystem</code>
     * to the <code>PrintWriter</code>
     * 
     * @param printWriter
     *            A print writer.
     * @param file
     *            A file.
     */
    private void print(final PrintWriter printWriter, final File file) {
        for (int i = 0; i < depth(file) + 1; i++) {
            printWriter.print("  ");
        }
        printWriter.print("[");
        printWriter.print(file.getName());
        printWriter.println("]");
    }

    /**
     * Resolve a relative path into an absolute file.
     * 
     * @param path
     *            A file system path.
     * @return The file; or null if the path does not match a absolute file.
     */
    private File resolve(final String path) {
        final File file = new File(resolvePath(path));
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    /**
     * Resolve the relative path into an absolute path.
     * 
     * @param path
     *            A relative path.
     * @return An absolute path.
     */
    private String resolvePath(final String path) {
        final StringBuffer resolvedPath = new StringBuffer(root.getAbsolutePath());
        final List<String> tokens = tokenize(path);
        for (final String token : tokens) {
            resolvedPath.append(File.separator).append(token);
        }
        return resolvedPath.toString();
    }

    /**
     * Tokenize the path.
     * 
     * @param path
     *            A path.
     * @return A list of tokens separating the path.
     */
    private List<String> tokenize(final String path) {
        final StringTokenizer tokenizer = new StringTokenizer(path, PATH_SEP);
        final List<String> tokens = new ArrayList<String>(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            tokens.add(tokenizer.nextToken());
        }
        return tokens;
    }
}
