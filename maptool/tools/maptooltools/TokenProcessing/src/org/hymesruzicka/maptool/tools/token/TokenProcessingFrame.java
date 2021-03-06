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

import java.awt.Color;
import static java.awt.EventQueue.invokeLater;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import static javax.swing.UIManager.setLookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import static org.hymesruzicka.maptool.tools.MapToolZipType.EXTENSIONS_PLUS;
import static org.hymesruzicka.maptool.tools.Shelf.MACROS_TOKEN_NPC_CIVILIAN_FILE_DEFAULT;
import static org.hymesruzicka.maptool.tools.Shelf.MAPTOOL_INPUT_FILE_DIR_NAME;
import static org.hymesruzicka.maptool.tools.Shelf.PROPERTIES_NPC_CIVILIAN_FILE_DEFAULT;
import static org.hymesruzicka.maptool.tools.Shelf.XML_PROCESSED_DIR_NAME;

/**
 *
 * @author chymes
 */
public class TokenProcessingFrame extends javax.swing.JFrame {

    private static final Logger LOG = Logger.getLogger(TokenProcessingFrame.class.getName());
    private static final String DEST_BOX_NAME = "destinationDir";

    /**
     * Creates new form TokenProcessingFrame
     */
    public TokenProcessingFrame() {
        initComponents();
    }

    private TokenProcessingFrame init() {
        /**
         * After construction, and on the EDT*
         */
        sourcejTextField.setText(MAPTOOL_INPUT_FILE_DIR_NAME);
        tokenPropertiesjTextField.setText(PROPERTIES_NPC_CIVILIAN_FILE_DEFAULT.toString());
        tokenMacrosjTextField.setText(MACROS_TOKEN_NPC_CIVILIAN_FILE_DEFAULT.toString());
        destinationjTextField.setText(XML_PROCESSED_DIR_NAME);
        FocusListener checker = new FileNameFieldFocusListener();
        sourcejTextField.addFocusListener(checker);
        tokenPropertiesjTextField.addFocusListener(checker);
        tokenMacrosjTextField.addFocusListener(checker);        
        destinationjTextField.addFocusListener(checker);
        destinationjTextField.setName(DEST_BOX_NAME);
        resultjTextField.setVisible(false);
        checkAll();
        setEnabledAll(true);
        return this;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sourcejLabel = new javax.swing.JLabel();
        sourcejTextField = new javax.swing.JTextField();
        destinationjLabel = new javax.swing.JLabel();
        destinationjTextField = new javax.swing.JTextField();
        chooseSourcejButton = new javax.swing.JButton();
        chooseDestinationjButton = new javax.swing.JButton();
        commencejButton = new javax.swing.JButton();
        tokenPropertiesjLabel = new javax.swing.JLabel();
        tokenPropertiesjTextField = new javax.swing.JTextField();
        chooseTokenPropertiesjButton = new javax.swing.JButton();
        resultjTextField = new javax.swing.JTextField();
        tokenMacrosjLabel = new javax.swing.JLabel();
        tokenMacrosjTextField = new javax.swing.JTextField();
        choosetokenMacrosjButton = new javax.swing.JButton();
        togglePropertiesReplacementjCheckBox = new javax.swing.JCheckBox();
        toggleMacrosReplacementjCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("DandDDads Token Processor");
        setName("danddads_token_processor_frame"); // NOI18N

        sourcejLabel.setLabelFor(sourcejTextField);
        sourcejLabel.setText("Source File or Directory of campaign content:");
        sourcejLabel.setToolTipText("The  file or directory that contains the content.xml file");

        sourcejTextField.setText("<unset>");

        destinationjLabel.setLabelFor(destinationjTextField);
        destinationjLabel.setText("Destination directory:");
        destinationjLabel.setToolTipText("This is where a copy of the campaign archive will be written.");

        destinationjTextField.setText("<unset>");

        chooseSourcejButton.setText("...");
        chooseSourcejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseSourcejButtonActionPerformed(evt);
            }
        });

        chooseDestinationjButton.setText("...");
        chooseDestinationjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseDestinationjButtonActionPerformed(evt);
            }
        });

        commencejButton.setText("Process Tokens");
        commencejButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                commencejButtonActionPerformed(evt);
            }
        });

        tokenPropertiesjLabel.setLabelFor(sourcejTextField);
        tokenPropertiesjLabel.setText("Properties source XML ");
        tokenPropertiesjLabel.setToolTipText("The directory or archive that contains the content.xml file");

        tokenPropertiesjTextField.setText("<unset>");

        chooseTokenPropertiesjButton.setText("...");
        chooseTokenPropertiesjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseTokenPropertiesjButtonActionPerformed(evt);
            }
        });

        resultjTextField.setEditable(false);
        resultjTextField.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        resultjTextField.setText("nofink");
        resultjTextField.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(51, 255, 0), new java.awt.Color(0, 204, 51)));

        tokenMacrosjLabel.setLabelFor(sourcejTextField);
        tokenMacrosjLabel.setText("Macros source XML ");
        tokenMacrosjLabel.setToolTipText("The directory or archive that contains the content.xml file");

        tokenMacrosjTextField.setText("<unset>");

        choosetokenMacrosjButton.setText("...");
        choosetokenMacrosjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choosetokenMacrosjButtonActionPerformed(evt);
            }
        });

        togglePropertiesReplacementjCheckBox.setSelected(true);
        togglePropertiesReplacementjCheckBox.setText("Replace Properties");
        togglePropertiesReplacementjCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                togglePropertiesReplacementjCheckBoxActionPerformed(evt);
            }
        });

        toggleMacrosReplacementjCheckBox.setSelected(true);
        toggleMacrosReplacementjCheckBox.setText("Replace Macros");
        toggleMacrosReplacementjCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleMacrosReplacementjCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sourcejLabel)
                            .addComponent(destinationjLabel))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tokenPropertiesjLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(togglePropertiesReplacementjCheckBox)
                        .addGap(83, 339, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tokenMacrosjTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(choosetokenMacrosjButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(resultjTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(commencejButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(destinationjTextField)
                                    .addComponent(sourcejTextField))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chooseSourcejButton)
                                    .addComponent(chooseDestinationjButton)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tokenPropertiesjTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chooseTokenPropertiesjButton))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tokenMacrosjLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toggleMacrosReplacementjCheckBox)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sourcejLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourcejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chooseSourcejButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tokenPropertiesjLabel)
                    .addComponent(togglePropertiesReplacementjCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tokenPropertiesjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chooseTokenPropertiesjButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tokenMacrosjLabel)
                    .addComponent(toggleMacrosReplacementjCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tokenMacrosjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(choosetokenMacrosjButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(destinationjLabel)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(destinationjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chooseDestinationjButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(commencejButton)
                    .addComponent(resultjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chooseSourcejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseSourcejButtonActionPerformed
        checkAll();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Zip & Campaign files", EXTENSIONS_PLUS);
        Path parent = Paths.get(sourcejTextField.getText()).getParent();
        chooser.setCurrentDirectory(parent.toFile());
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            LOG.log(Level.INFO, "Source for campaign data = {0}", chooser.getSelectedFile().getName());
            sourcejTextField.setText(chooser.getSelectedFile().toString());
        }
        checkAll();
    }//GEN-LAST:event_chooseSourcejButtonActionPerformed

    private void chooseDestinationjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseDestinationjButtonActionPerformed
        checkAll();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setCurrentDirectory(new java.io.File(destinationjTextField.getText()));
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            LOG.log(Level.INFO, "Destination for campaign file = {0}", chooser.getSelectedFile().getName());
            destinationjTextField.setText(chooser.getSelectedFile().toString());
        }
        checkAll();
    }//GEN-LAST:event_chooseDestinationjButtonActionPerformed

    private void commencejButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_commencejButtonActionPerformed
        checkAll();
        List<TokenProcessing.TokenProcess> processList = new ArrayList<>();
        if (_replaceProperties) {
            processList.add(TokenProcessing.TokenProcess.REPLACE_PROPERTY_SET);
        }
        if (_replaceMacros) {
            processList.add(TokenProcessing.TokenProcess.REPLACE_MACRO_SET);
        }
        TokenWorker worker = new TokenWorker(this,
                sourcejTextField.getText(),
                tokenPropertiesjTextField.getText(),
                tokenMacrosjTextField.getText(),
                destinationjTextField.getText(),
                processList);
        LOG.log(Level.FINE, "Button pushed.");
        worker.addPropertyChangeListener(listener -> putOut(worker));
        setEnabledAll(false);
        worker.execute();
        worker.requestPatience();
    }//GEN-LAST:event_commencejButtonActionPerformed

    private void chooseTokenPropertiesjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseTokenPropertiesjButtonActionPerformed
        checkAll();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Replacement XML file", "xml");
        Path parent = Paths.get(tokenPropertiesjTextField.getText()).getParent();
        chooser.setCurrentDirectory(parent.toFile());
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            LOG.log(Level.INFO, "Source for properties replacement data = {0}", chooser.getSelectedFile().getName());
            tokenPropertiesjTextField.setText(chooser.getSelectedFile().toString());
        }
        checkAll();
    }//GEN-LAST:event_chooseTokenPropertiesjButtonActionPerformed

    private void choosetokenMacrosjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choosetokenMacrosjButtonActionPerformed
        checkAll();
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Replacement XML file", "xml");
        Path parent = Paths.get(tokenMacrosjTextField.getText()).getParent();
        chooser.setCurrentDirectory(parent.toFile());
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            LOG.log(Level.INFO, "Source for macros replacement data = {0}", chooser.getSelectedFile().getName());
            tokenMacrosjTextField.setText(chooser.getSelectedFile().toString());
        }
        checkAll();
    }//GEN-LAST:event_choosetokenMacrosjButtonActionPerformed

    private void togglePropertiesReplacementjCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_togglePropertiesReplacementjCheckBoxActionPerformed
        assignEnablement();
    }//GEN-LAST:event_togglePropertiesReplacementjCheckBoxActionPerformed

    private void toggleMacrosReplacementjCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleMacrosReplacementjCheckBoxActionPerformed
        assignEnablement();
    }//GEN-LAST:event_toggleMacrosReplacementjCheckBoxActionPerformed

    private void assignEnablement() {
        _replaceProperties = togglePropertiesReplacementjCheckBox.isSelected();
        _replaceMacros = toggleMacrosReplacementjCheckBox.isSelected();
        _commence = _replaceMacros || _replaceProperties;

        chooseTokenPropertiesjButton.setEnabled(_replaceProperties);
        tokenPropertiesjTextField.setEnabled(_replaceProperties);
        tokenPropertiesjLabel.setEnabled(_replaceProperties);

        choosetokenMacrosjButton.setEnabled(_replaceMacros);
        tokenMacrosjTextField.setEnabled(_replaceMacros);
        tokenMacrosjLabel.setEnabled(_replaceMacros);

        chooseDestinationjButton.setEnabled(_commence);
        destinationjTextField.setEnabled(_commence);
        destinationjLabel.setEnabled(_commence);
        commencejButton.setEnabled(_commence);
        checkAll();
    }

    private void setEnabledAll(boolean value) {
        chooseDestinationjButton.setEnabled(value);
        chooseTokenPropertiesjButton.setEnabled(value);
        chooseSourcejButton.setEnabled(value);
        commencejButton.setEnabled(value);
        destinationjTextField.setEnabled(value);
        tokenPropertiesjTextField.setEnabled(value);
        tokenMacrosjTextField.setEnabled(value);        
        sourcejTextField.setEnabled(value);
    }

    private final void checkField(JTextField textBox) {
        String boxName = textBox.getName();
        if (boxName != null && DEST_BOX_NAME.equals(boxName)) {
            checkField(textBox, true);
        } else {
            checkField(textBox, false);
        }
    }

    private final void checkField(JTextField textBox, boolean checkParent) {
        Path file = Paths.get(textBox.getText());
        boolean exists;
        if (checkParent) {
            exists = Files.exists(file.getParent());
        } else {
            exists = Files.exists(file);
        }
        if (exists) {
            textBox.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(51, 255, 0), new java.awt.Color(0, 204, 51)));
        } else {
            textBox.setBorder(javax.swing.BorderFactory.createEtchedBorder(Color.RED, Color.PINK));
        }
    }

    private final void checkAll() {
        checkField(sourcejTextField);
        checkField(tokenPropertiesjTextField);
        checkField(tokenMacrosjTextField);        
        checkField(destinationjTextField, true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            LOG.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        invokeLater(new Runnable() {
            @Override
            public void run() {
                TokenProcessingFrame guiFrame = new TokenProcessingFrame();
                guiFrame.init();
                guiFrame.setVisible(true);
            }
        });
    }

    private void putOut(TokenWorker worker) {
        if (worker.isDone()) {
            try {
                resultjTextField.setText(worker.get());
            } catch (InterruptedException | ExecutionException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
            resultjTextField.setVisible(true);
            setEnabledAll(true);
            revalidate();
            repaint();
        }
    }

    private class FileNameFieldFocusListener implements FocusListener {

        @Override
        public void focusGained(FocusEvent focusEvent) {
            checkField((JTextField) focusEvent.getSource());
        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            checkField((JTextField) focusEvent.getSource());
        }

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseDestinationjButton;
    private javax.swing.JButton chooseSourcejButton;
    private javax.swing.JButton chooseTokenPropertiesjButton;
    private javax.swing.JButton choosetokenMacrosjButton;
    private javax.swing.JButton commencejButton;
    private javax.swing.JLabel destinationjLabel;
    private javax.swing.JTextField destinationjTextField;
    private javax.swing.JTextField resultjTextField;
    private javax.swing.JLabel sourcejLabel;
    private javax.swing.JTextField sourcejTextField;
    private javax.swing.JCheckBox toggleMacrosReplacementjCheckBox;
    private javax.swing.JCheckBox togglePropertiesReplacementjCheckBox;
    private javax.swing.JLabel tokenMacrosjLabel;
    private javax.swing.JTextField tokenMacrosjTextField;
    private javax.swing.JLabel tokenPropertiesjLabel;
    private javax.swing.JTextField tokenPropertiesjTextField;
    // End of variables declaration//GEN-END:variables
    private boolean _replaceProperties = true;
    private boolean _replaceMacros = true;
    private boolean _commence = true;

}
