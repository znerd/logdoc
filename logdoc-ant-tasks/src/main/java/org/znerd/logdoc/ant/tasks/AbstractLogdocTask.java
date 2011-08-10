// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc.ant.tasks;

import java.io.File;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import static org.apache.tools.ant.Project.MSG_VERBOSE;
import org.apache.tools.ant.taskdefs.MatchingTask;

import org.znerd.logdoc.LogDef;
import org.znerd.logdoc.LogLevel;
import org.znerd.logdoc.internal.InternalLogging;
import org.znerd.logdoc.internal.IoUtils;
import static org.znerd.logdoc.internal.TextUtils.quote;

import org.znerd.logdoc.ant.tasks.internal.AntInternalLogging;

/**
 * Abstract base class for the Logdoc Ant task implementations.
 * <p>
 * The most notable parameters supported by task implementations derived from this class are:
 * <dl>
 * <dt>in
 * <dd>The input directory, to read the input files (the Logdoc definitions) from. Optional, defaults to project base directory.
 * <dt>out
 * <dd>The output directory, to write the output files to. Optional, defaults to source directory.
 * </dl>
 * <p>
 * This task supports more parameters and contained elements, inherited from {@link MatchingTask}, see <a href="http://ant.apache.org/manual/dirtasks.html">the Ant site</a>.
 */
public abstract class AbstractLogdocTask extends MatchingTask {

    @Override
    public void execute() throws BuildException {
        sendInternalLoggingThroughAnt();
        File sourceDir = determineSourceDir();
        File destDir = determineDestDir(sourceDir);
        checkDirs(sourceDir, destDir);
        processFiles(sourceDir, destDir);
    }

    private void sendInternalLoggingThroughAnt() {
        InternalLogging.setLogger(new AntInternalLogging(this));
    }

    private File determineSourceDir() {
        File sourceDir = (_sourceDir != null) ? _sourceDir : getProject().getBaseDir();
        return sourceDir;
    }

    private File determineDestDir(File sourceDir) {
        File destDir = (_destDir != null) ? _destDir : sourceDir;
        return destDir;
    }

    private void checkDirs(File sourceDir, File destDir) {
        checkDir("Source directory", sourceDir, true, false, false);
        checkDir("Destination directory", destDir, false, true, true);
    }

    private void processFiles(File sourceDir, File destDir) {
        long start = System.currentTimeMillis();
        logProcessingStart(sourceDir, destDir);
        LogDef logDef = loadAndValidateDefinitions(sourceDir);
        processFilesImpl(logDef);
        logProcessingFinish(start);
    }

    private void logProcessingStart(File sourceDir, File destDir) {
        InternalLogging.log(LogLevel.INFO, "Processing from " + sourceDir.getPath() + " to " + destDir.getPath() + '.');
    }

    private void processFilesImpl(LogDef logDef) {
        try {
            executeImpl(logDef);
        } catch (Exception cause) {
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof BuildException) {
                throw (BuildException) cause;
            } else {
                throw new BuildException(cause);
            }
        }
    }

    private void logProcessingFinish(long start) {
        long duration = System.currentTimeMillis() - start;
        InternalLogging.log(LogLevel.NOTICE, "Processed definitions in " + duration + " ms.");
    }

    private LogDef loadAndValidateDefinitions(File sourceDir) {
        LogDef logDef;
        try {
            logDef = LogDef.loadFromDirectory(sourceDir);
        } catch (Exception cause) {
            throw new BuildException("Failed to load log definition.", cause);
        }
        return logDef;
    }

    /**
     * Executes this task after all common validations and preparations have been done. This method must only be called from the {@link #execute()} method in the <code>AbstractLogdocTask</code> class.
     * 
     * @param logDef the {@link LogDef} to process, never <code>null</code>.
     * @throws Exception if anything goes wrong; this will be handled by the <code>AbstractLogdocTask</code> class.
     */
    protected abstract void executeImpl(LogDef logDef) throws Exception;

    /**
     * Checks if the specified string is either null or empty (after trimming the whitespace off).
     * 
     * @param s the string to check.
     * @return <code>true</code> if <code>s == null || s.trim().length() &lt; 1</code>; <code>false</code> otherwise.
     */
    static final boolean isEmpty(String s) {
        return s == null || s.trim().length() < 1;
    }

    public void setIn(File dir) {
        log("Setting \"in\" to: " + quote(dir) + '.', MSG_VERBOSE);
        _sourceDir = dir;
    }

    protected File _sourceDir;

    public void setOut(File dir) {
        log("Setting \"out\" to: " + quote(dir) + '.', MSG_VERBOSE);
        _destDir = dir;
    }

    protected File _destDir;

    public void setOverwrite(boolean flag) {
        log("Setting \"overwrite\" to: \"" + flag + "\".", MSG_VERBOSE);
        _overwrite = flag;
    }

    /**
     * Flag that indicates if each existing file should always be overwritten, even if it is newer than the source file. Default is <code>false</code>.
     */
    protected boolean _overwrite;

    /**
     * Checks if the specified abstract path name refers to an existing directory.
     * 
     * @param description the description of the directory, cannot be <code>null</code>.
     * @param path the abstract path name as a {@link File} object.
     * @param mustBeReadable <code>true</code> if the directory must be readable.
     * @param mustBeWritable <code>true</code> if the directory must be writable.
     * @throws IllegalArgumentException if <code>description == null || description.equals("") || path == null</code>.
     * @throws BuildException if <code>! path.exists() || ! path.isDirectory() || (mustBeReadable &amp;&amp; !path.canRead()) || (mustBeWritable &amp;&amp; !path.canWrite())</code>.
     */
    private static final void checkDir(String description, File path, boolean mustBeReadable, boolean mustBeWritable, boolean createIfNonexistent) throws IllegalArgumentException, BuildException {
        try {
            IoUtils.checkDir(description, path, mustBeReadable, mustBeWritable, createIfNonexistent);
        } catch (IOException cause) {
            throw new BuildException(cause.getMessage(), cause);
        }
    }

    protected AbstractLogdocTask() {
        // empty
    }
}
