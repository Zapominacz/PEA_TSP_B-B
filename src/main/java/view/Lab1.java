package view;

import algoritms.BranchAndBound;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Created by Quantum on 2015-10-27.
 */
public class Lab1 extends JFrame implements WindowListener {
    private JButton branchAndBoundButton;
    private JButton wybierzPlikButton;
    private JButton bruteForceButton;
    private JPanel mainPane;

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
        branchAndBoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BranchAndBound().performAlgorithm(null);
            }
        });
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
