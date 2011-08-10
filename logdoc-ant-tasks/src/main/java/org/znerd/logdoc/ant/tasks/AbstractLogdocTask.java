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
 * <dt>overwrite
 * <dd>Flag that indicates if each existing file should always be overwritten, even if it is newer than the source file. Default is <code>false</code>.
 * </dl>
 * <p>
 * This task supports more parameters and contained elements, inherited from {@link MatchingTask}, see <a href="http://ant.apache.org/manual/dirtasks.html">the Ant site</a>.
 */
public abstract class AbstractLogdocTask extends MatchingTask {

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

    protected boolean _overwrite;

    @Override
    public void execute() throws BuildException {
        sendInternalLoggingThroughAnt();
        File sourceDir = determineSourceDir();
        generate(sourceDir);
    }

    private void sendInternalLoggingThroughAnt() {
        InternalLogging.setLogger(new AntInternalLogging(this));
    }

    private File determineSourceDir() {
        File sourceDir = (_sourceDir != null) ? _sourceDir : getProject().getBaseDir();
        return sourceDir;
    }

    private void generate(File sourceDir) {
        File destDir = determineDestDir(sourceDir);
        checkDirs(sourceDir, destDir);
        processFiles(sourceDir, destDir);
    }

    private File determineDestDir(File sourceDir) {
        File destDir = (_destDir != null) ? _destDir : sourceDir;
        return destDir;
    }

    private void checkDirs(File sourceDir, File destDir) throws BuildException {
        try {
            IoUtils.checkDir("Source directory", sourceDir, true, false, false);
            IoUtils.checkDir("Destination directory", destDir, false, true, true);
        } catch (IOException cause) {
            throw new BuildException(cause.getMessage(), cause);
        }
    }

    private void processFiles(File sourceDir, File destDir) throws BuildException {
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

    protected AbstractLogdocTask() {
        // empty
    }
}
