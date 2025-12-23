package fileViewer;

import java.io.*;
import java.math.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.nio.*;
import java.nio.file.*;

import java.util.*;
import java.util.concurrent.*;

import java.lang.*;
import java.lang.Object.*;

class main
{
    public static void userInterface()
    {
        Frame base = new Frame("File Viewer");

        base.setSize(1280, 720);
        base.setVisible(true);
        base.setLayout(null);

        base.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                base.dispose();
            }
        });

        TextField fileInput = new TextField();

        fileInput.setBounds(485, 360, 325, 20);
        base.add(fileInput);

        Label instruction = new Label("Enter the path of the directory you want to search");

        instruction.setBounds(515, 340, 285, 20);
        base.add(instruction);

        Button search = new Button("Search Directory");

        search.setBounds(455, 380, 380, 80);
        base.add(search);

        search.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String path = fileInput.getText();
                boolean isPathUsable = checkPathValidity(path);

                if(isPathUsable == true)
                {
                    instruction.setText("Enter the path of the directory you want to search");
                    indexFiles(path);
                }
                else
                {
                    instruction.setText("Please enter a valid directory/folder path");
                }
            }
        });
    }

    public static void indexFiles(String directoryPath)
    {
        File directory = new File(directoryPath);
        File[] index = directory.listFiles();

        JFrame fileList = new JFrame("File List");

        fileList.setSize(1280, 720);
        fileList.setVisible(true);
        fileList.setExtendedState(JFrame.MAXIMIZED_BOTH);
        fileList.setLayout(null);

        fileList.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                fileList.dispose();
            }
        });

        JPanel listContainer = new JPanel();
        listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));
        listContainer.setBounds(0, 0, 1280, 720);

        JScrollPane scrollThroughList = new JScrollPane(listContainer);
        scrollThroughList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollThroughList.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollThroughList.setBounds(0, 0, 1265, 705);
        fileList.add(scrollThroughList);

        getFiles(index, listContainer);
    }

    public static void getFiles(File[] dir, JPanel container)
    {
        for(File i : dir)
        {
            String indexFilePath = i.getAbsolutePath();
            Boolean isDirectory = checkPathValidity(indexFilePath);

            Path filePathFormatted = Paths.get(indexFilePath);
            Boolean accessAllowed = Files.isReadable(filePathFormatted);

            if(!accessAllowed)
            {
                continue;
            }

            if(!isDirectory)
            {
                JButton fileInterface = new JButton(indexFilePath);
                container.add(fileInterface);

                String interfaceText = fileInterface.getText();

                fileInterface.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        fileView(interfaceText);
                    }
                });
            }
            else
            {
                File[] internalIndex = i.listFiles();
                getFiles(internalIndex, container);
            }
        }
    }

    public static boolean checkPathValidity(String directoryPath)
    {
        Path inputtedPath = Paths.get(directoryPath);

        if(Files.isDirectory(inputtedPath))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static void fileView(String filePath)
    {
        JFrame view = new JFrame("View File");

        view.setSize(1280, 720);
        view.setVisible(true);
        view.setLayout(null);
        view.setExtendedState(JFrame.MAXIMIZED_BOTH);

        JTextArea fileText = new JTextArea();
        fileText.setBounds(0, 0, 1280, 720);
        fileText.setEditable(false);
        view.add(fileText);

        JScrollPane scrollThroughText = new JScrollPane(fileText);
        scrollThroughText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollThroughText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollThroughText.setBounds(0, 0, 1265, 705);
        view.add(scrollThroughText);

        try(BufferedReader getFileText = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            int i=0;

            while((line = getFileText.readLine()) != null)
            {
                if(i>0)
                {
                    fileText.setText(fileText.getText() + "\n" + line);
                }
                else
                {
                    fileText.setText(fileText.getText() + line);
                }

                i++;
            }
        }
        catch(Exception e)
        {
            fileText.setText("This file cannot be read.");
        }
    }

    public static void main(String[] args)
    {
        userInterface();
    }
};
