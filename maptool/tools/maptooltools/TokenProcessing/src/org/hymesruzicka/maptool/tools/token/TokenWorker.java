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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import nu.xom.ParsingException;
import static org.hymesruzicka.maptool.tools.MapToolZipType.isMapToolZip;
import org.hymesruzicka.maptool.tools.ReplacementPair;
import static org.hymesruzicka.maptool.tools.Shelf.DEFAULT_CONTENT_XML_FILENAME;
import static org.hymesruzicka.maptool.tools.ZipWrangler.extractZipFolder;
import static org.hymesruzicka.maptool.tools.ZipWrangler.replace;

/**
 *
 * @author chymes
 */
public class TokenWorker extends SwingWorker<String, Void> {

    private static final Logger LOG = Logger.getLogger(TokenWorker.class.getName());

    public static Path validateFile(String sourceFileName) throws IOException {
        if (sourceFileName.isEmpty()) {
            throw new IllegalArgumentException("Source filename cannot be empty.");
        }
        LOG.log(Level.FINER, sourceFileName);
        Path result = Paths.get(sourceFileName);
        if (!Files.exists(result)) {
            throw new IOException("Could not find file or directory " + sourceFileName);
        }
        LOG.log(Level.FINER, " source file {0} exists.", sourceFileName);
        if (Files.isRegularFile(result)) {
            boolean isZip = sourceFileName.endsWith("zip")
                    || isMapToolZip(sourceFileName)
                    || sourceFileName.endsWith("jar");
            if (!isZip) {
                throw new UnsupportedOperationException("Only Directories and Zips please.");
            }
            Path tmpDir = Files.createTempDirectory("tokenProcessorExtractionDir");
            extractZipFolder(sourceFileName, tmpDir);
            result = tmpDir;
        }
        LOG.log(Level.FINER, " source file {0} is a regular file .", sourceFileName);
        return result;
    }

    private String _sourceFileName;
    private final String _propertiesXMLFileName;
    private final String _macrosXMLFileName;
    private final String _destDirName;
    private Path _outFile;
    private final TokenProcessingFrame _mainFrame;
    private Dialog _dialog;
    private final List<TokenProcessing.TokenProcess> _processSelectionList = new ArrayList<>();

    public TokenWorker(TokenProcessingFrame mainFrame, String sourceDirName, String propertiesXMLFileName, String macrosXMLFileName, String destDirName, List<TokenProcessing.TokenProcess> processSelectionList) {
        this._mainFrame = mainFrame;
        this._sourceFileName = sourceDirName;
        this._propertiesXMLFileName = propertiesXMLFileName;
        this._macrosXMLFileName = macrosXMLFileName;
        this._destDirName = destDirName;
        this._outFile = null;
        this._processSelectionList.addAll(processSelectionList);
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
                        || isMapToolZip(getSourceFileName())
                        || getSourceFileName().endsWith("jar");
                if (!isZip) {
                    throw new UnsupportedOperationException("Only Directories and Zips please.");
                }
                Path tmpDir = Files.createTempDirectory("tokenProcessorExtractionDir");
                extractZipFolder(getSourceFileName(), tmpDir);
                sourceFile = tmpDir;
                setSourceFileName(tmpDir.toString());
            }
            LOG.log(Level.FINER, " source file {0} is a regular file .", getSourceFileName());
            /*properties*/
            Path propertiesXMLFile = Paths.get(getPropertiesXMLFileName());
            LOG.log(Level.FINER, "{0} is propertiesXML file name.", getPropertiesXMLFileName());
            if (!Files.exists(propertiesXMLFile)) {
                throw new IOException("Could not find file " + getPropertiesXMLFileName());
            }
            LOG.log(Level.FINER, "propertiesXML file {0} exists.", getPropertiesXMLFileName());
            if (!Files.isRegularFile(propertiesXMLFile)) {
                throw new IOException("File \"" + getPropertiesXMLFileName() + "\" is not a regular file.");
            }
            LOG.log(Level.FINER, "{0} is a regular file.", getPropertiesXMLFileName());
            /*macros*/
            Path macrosXMLFile = Paths.get(getMacrosXMLFileName());
            LOG.log(Level.INFO, "{0} is macrosXML file name.", getMacrosXMLFileName());
            if (!Files.exists(macrosXMLFile)) {
                throw new IOException("Could not find file " + getMacrosXMLFileName());
            }
            LOG.log(Level.INFO, "macrosXMLFile file {0} exists.", getMacrosXMLFileName());
            if (!Files.isRegularFile(macrosXMLFile)) {
                throw new IOException("File \"" + getMacrosXMLFileName() + "\" is not a regular file.");
            }
            LOG.log(Level.INFO, "{0} is a regular file.", getMacrosXMLFileName());
            /*unprocessed*/
            Path contentXMLFile = Paths.get(sourceFile.toString(), DEFAULT_CONTENT_XML_FILENAME);
            LOG.log(Level.FINE,
                    "contentXMLFile = {0}, propertiesXMLFile={1}, macrosXMLFile={2}",
                    new String[]{contentXMLFile.toString(),
                        propertiesXMLFile.toString(),
                        macrosXMLFile.toString()});

            TokenProcessing tokenProcessor
                    = new TokenProcessing(contentXMLFile.toString(),
                            propertiesXMLFile.toString(),
                            macrosXMLFile.toString());

            tokenProcessor.process(_processSelectionList);
            ReplacementPair pair = new ReplacementPair(contentXMLFile, tokenProcessor.getProcessedFile());
            setOutFile(replace(pair, getSourceFileName(), getDestDirName()));
        } catch (IllegalArgumentException
                | IOException
                | UnsupportedOperationException
                | ParsingException anException) {
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
     * @return the _propertiesXMLFileName
     */
    public String getPropertiesXMLFileName() {
        return _propertiesXMLFileName;
    }

    /**
     * @return the _propertiesXMLFileName
     */
    public String getMacrosXMLFileName() {
        return _macrosXMLFileName;
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
