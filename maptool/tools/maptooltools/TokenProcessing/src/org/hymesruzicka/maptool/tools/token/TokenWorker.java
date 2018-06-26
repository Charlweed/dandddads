/*
 * Copyright (C) 2018 chymes.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.hymesruzicka.maptool.tools.token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import nu.xom.ParsingException;
import org.hymesruzicka.maptool.tools.ReplacementPair;
import static org.hymesruzicka.maptool.tools.ZipWrangler.ORIGINAL_FILENAME;
import static org.hymesruzicka.maptool.tools.ZipWrangler.extractZipFolder;
import static org.hymesruzicka.maptool.tools.ZipWrangler.replace;

/**
 *
 * @author chymes
 */
public class TokenWorker extends SwingWorker<String, Void> {

    private static final Logger LOG = Logger.getLogger(TokenWorker.class.getName());

    private String _sourceFileName;
    private final String _replacementFileName;
    private final String _destDirName;
    private Path _outFile;
    private final TokenProcessingFrame _mainFrame;
    private Dialog _dialog;

    public TokenWorker(TokenProcessingFrame mainFrame, String sourceDirName, String replacementFileName, String destDirName) {
        this._mainFrame = mainFrame;
        this._sourceFileName = sourceDirName;
        this._replacementFileName = replacementFileName;
        this._destDirName = destDirName;
        this._outFile = null;
    }

    @Override
    protected String doInBackground() throws Exception {
        try {
            LOG.log(Level.FINER, "Running.");
            if (getSourceFileName().isEmpty()) {
                throw new IllegalArgumentException("Source filename cannot be empty.");
            }
            LOG.log(Level.FINER, getSourceFileName());
            Path sourceFile = Paths.get(getSourceFileName());
            if (!Files.exists(sourceFile)) {
                throw new IOException("Could not find file or directory " + getSourceFileName());
            }
            LOG.log(Level.FINER, " source file {0} exists.", getSourceFileName());
            if (Files.isRegularFile(sourceFile)) {
                boolean isZip = getSourceFileName().endsWith("zip")
                        || getSourceFileName().endsWith("cmpgn")
                        || getSourceFileName().endsWith("rptok")
                        || getSourceFileName().endsWith("jar");
                if (!isZip) {
                    throw new UnsupportedOperationException("Only Directories and Zips please/");
                }
                Path tmpDir = Files.createTempDirectory("tokenProcessorExtractionDir");
                extractZipFolder(getSourceFileName(), tmpDir);
                sourceFile = tmpDir;
                setSourceFileName(tmpDir.toString());
            }
            LOG.log(Level.FINER, " source file {0} is a regular file .", getSourceFileName());
            Path replacementFile = Paths.get(getReplacementFileName());
            LOG.log(Level.FINER, "{0} is replacement file name.", getReplacementFileName());
            if (!Files.exists(replacementFile)) {
                throw new IOException("Could not find file " + getReplacementFileName());
            }
            LOG.log(Level.FINER, "replacement file {0} exists.", getReplacementFileName());
            if (!Files.isRegularFile(replacementFile)) {
                throw new IOException("File \"" + getReplacementFileName() + "\" is not a regular file.");
            }
            LOG.log(Level.FINER, "{0} is a regular file.", getReplacementFileName());
            Path contentXMLFile = Paths.get(sourceFile.toString(), ORIGINAL_FILENAME);
            LOG.log(Level.FINE, "contentXMLFile = {0}, replacementXMLFile={1}", new String[]{contentXMLFile.toString(), replacementFile.toString()});
            TokenProcessing tokenProcessor = new TokenProcessing(contentXMLFile.toString(), replacementFile.toString());
            tokenProcessor.setDestDirName(getDestDirName());
            tokenProcessor.process();
            ReplacementPair pair = new ReplacementPair(contentXMLFile, tokenProcessor.getProcessedFile());
            setOutFile(replace(pair, getSourceFileName(), getDestDirName()));
        } catch (IllegalArgumentException | IOException | UnsupportedOperationException | ParsingException anException) {
            LOG.log(Level.SEVERE, null, anException);
        }
        String result = null;
        if (getOutFile() != null) {
            result = getOutFile().toAbsolutePath().toString();
            System.out.println(result);
        }

        return result;
    }

    private final void setSourceFileName(String sourceFileName) {
        _sourceFileName = sourceFileName;
    }

    /**
     * @return the _sourceFileName
     */
    public String getSourceFileName() {
        return _sourceFileName;
    }

    /**
     * @return the _destDirName
     */
    public String getDestDirName() {
        return _destDirName;
    }

    /**
     * @return the _replacementFileName
     */
    public String getReplacementFileName() {
        return _replacementFileName;
    }

    private void setOutFile(Path result) {
        _outFile = result;
    }

    /**
     * Call this when you want the result. Will be non-null AFTER successful
     * execution. Does not block.
     *
     * @return the outFile
     */
    public Path getOutFile() {
        return _outFile;
    }

    @Override
    protected void done() {
        super.done();
        if (_dialog != null) {
            _dialog.setVisible(false);
            _dialog.toBack();
            _dialog.dispose();
        }
        _dialog = null;
    }

    public final TokenProcessingFrame getMainFrame() {
        return _mainFrame;
    }

    protected void requestPatience() {
        _dialog = new Dialog("Processing", true);
        _dialog.setVisible(true);
    }

    private class Dialog extends JDialog {

        private Dialog(String title, boolean modal) {
            super(_mainFrame, title, modal);
            this.setSize(150, 100);
            JPanel panel = new JPanel();
            panel.add(new JLabel("Please wait..."));
            this.getContentPane().add(panel);
        }
    }
}
