package view;

import algoritms.BranchAndBound;
import algoritms.FullCheck;
import tools.MapLoader;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

/**
 * Created by Quantum on 2015-10-27.
 */
public class Lab1 extends JFrame implements WindowListener {
    private JButton branchAndBoundButton;
    private JButton wybierzPlikButton;
    private JButton bruteForceButton;
    private JPanel mainPane;
    private JLabel status;

    private int[][] baseMap;
    private int[] solution;

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            Lab1 frame = new Lab1();
        });
    }

    public Lab1() {
        setTitle("Branch and Bound - 209773");
        setContentPane(mainPane);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
        addWindowListener(this);
        setVisible(true);
        setListeners();
    }

    private void setListeners() {
        branchAndBoundButton.addActionListener(e -> {
            if (baseMap != null && baseMap.length > 0) {
                solution = new BranchAndBound().performAlgorithm(baseMap);
                printSolution(solution);
            }
        });
        wybierzPlikButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.addActionListener(e1 -> {
                File file = chooser.getSelectedFile();
                if (file != null) {
                    baseMap = new MapLoader().loadMap(file);
                    if (baseMap.length == 0) {
                        status.setText("Błąd wczytywania pliku");
                    } else {
                        status.setText("Wybrano: " + file.getAbsolutePath());
                    }
                }
            });
            chooser.showOpenDialog(this);
        });
        bruteForceButton.addActionListener(e -> {
            if (baseMap != null && baseMap.length > 0) {
                solution = new FullCheck().performAlgorithm(baseMap);
                printSolution(solution);
            }
        });
    }

    private void printSolution(int[] solution) {
        StringBuilder builder = new StringBuilder("Rozwiązanie: ");
        for (int sol : solution) {
            builder.append(sol).append(" ");
        }
        status.setText(builder.toString());
    }


    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
