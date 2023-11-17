package org.ripple.test;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileUploader {
    public static void main(String[] args) {
        JFrame frame = new JFrame("File Uploader");
        JButton plusButton = new JButton("+");

        plusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and configure the file chooser
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select a file to upload");
                fileChooser.setFileFilter(new FileNameExtensionFilter("Files", "txt", "csv", "pdf")); // Optional: Set file filters

                // Open the file chooser dialog
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    java.io.File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    // Perform file upload logic here
                } else {
                    System.out.println("No file selected");
                }
            }
        });

        frame.add(plusButton);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

