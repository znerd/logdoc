// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.internal;

import java.io.File;
import java.io.IOException;
import static org.znerd.logdoc.internal.TextUtils.quote;

import org.znerd.logdoc.LogLevel;

public class CheckDirUtils {

    /**
     * Checks if the specified abstract path name refers to an existing directory and creates the directory if appropriate.
     * 
     * @param description the description of the directory, cannot be <code>null</code>.
     * @param path the abstract path name as a {@link File} object.
     * @param mustBeReadable <code>true</code> if the directory must be readable.
     * @param mustBeWritable <code>true</code> if the directory must be writable.
     * @throws IllegalArgumentException if <code>description == null || description.equals("") || path == null</code>.
     * @throws IOException if <code>! path.exists() || ! path.isDirectory() || (mustBeReadable &amp;&amp; !path.canRead()) || (mustBeWritable &amp;&amp; !path.canWrite())</code>.
     */
    public static final void checkDir(String description, File path, boolean mustBeReadable, boolean mustBeWritable, boolean createIfNonexistent) throws IllegalArgumentException, IOException {
        validateCheckDirPreconditions(description, path);
        createDirectoryIfAppropriate(description, path, createIfNonexistent);
        validatePathIsExistingDir(description, path);
        validatePathReadability(description, path, mustBeReadable);
        validatePathWritability(description, path, mustBeWritable);
    }

    private static void validatePathWritability(String description, File path, boolean mustBeWritable) throws IOException {
        if (mustBeWritable && (!path.canWrite())) {
            throw new IOException(description + " (\"" + path + "\") is not writable.");
        }
    }

    private static void validatePathReadability(String description, File path, boolean mustBeReadable) throws IOException {
        if (mustBeReadable && (!path.canRead())) {
            throw new IOException(description + " (\"" + path + "\") is not readable.");
        }
    }

    private static void validatePathIsExistingDir(String description, File path) throws IOException {
        if (!path.exists()) {
            throw new IOException(description + " (\"" + path + "\") does not exist.");
        } else if (!path.isDirectory()) {
            throw new IOException(description + " (\"" + path + "\") is not a directory.");
        }
    }

    private static void validateCheckDirPreconditions(String description, File path) {
        if (description == null) {
            throw new IllegalArgumentException("description == null");
        } else if (description.equals("")) {
            throw new IllegalArgumentException("description.equals(\"\")");
        } else if (path == null) {
            throw new IllegalArgumentException("path == null");
        }
    }

    private static void createDirectoryIfAppropriate(String description, File path, boolean createIfNonexistent) throws IOException {
        if (createIfNonexistent && !path.exists()) {
            InternalLogging.log(LogLevel.INFO, "Creating directory " + quote(path) + '.');
            if (!path.mkdirs()) {
                throw new IOException(description + " (\"" + path + "\") could not be created.");
            }
        }
    }
    
    private CheckDirUtils() {
    }
}
