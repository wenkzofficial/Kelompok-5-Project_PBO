/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXSwingMain.java to edit this template
 */
package uap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SOSGameGUI extends JFrame {
    private JButton[][] boardButtons;
    private char[][] board;
    private int size;
    private char currentPlayer;
    private JLabel statusLabel;
    private boolean gameOver;

    public SOSGameGUI(int size) {
        this.size = size;
        this.board = new char[size][size];
        this.currentPlayer = 'S';
        this.gameOver = false;

        initializeBoard();
        setupGUI();
    }

    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = ' ';
            }
        }
    }

    private void setupGUI() {
        setTitle("Game SOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel untuk papan permainan
        JPanel boardPanel = new JPanel(new GridLayout(size, size));
        boardButtons = new JButton[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                final int row = i;
                final int col = j;
                boardButtons[i][j] = new JButton("");
                boardButtons[i][j].setPreferredSize(new Dimension(60, 60));
                boardButtons[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        makeMove(row, col);
                    }
                });
                boardPanel.add(boardButtons[i][j]);
            }
        }

        // Status panel
        JPanel statusPanel = new JPanel();
        statusLabel = new JLabel("Giliran Pemain: " + currentPlayer);
        statusPanel.add(statusLabel);

        // Tombol reset
        JButton resetButton = new JButton("Reset Game");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        statusPanel.add(resetButton);

        // Tambahkan panel ke frame
        add(boardPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void makeMove(int row, int col) {
        if (gameOver || board[row][col] != ' ') return;

        // Tampilkan dialog untuk memilih huruf
        String[] options = {"S", "O"};
        int choice = JOptionPane.showOptionDialog(
            this, 
            "Pilih huruf untuk dimainkan", 
            "Pilih Huruf", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]
        );

        if (choice == -1) return; // Jika dialog ditutup
        
        char letter = options[choice].charAt(0);
        board[row][col] = letter;
        boardButtons[row][col].setText(String.valueOf(letter));
        boardButtons[row][col].setEnabled(false);

        if (checkForSOS(row, col)) {
            gameOver = true;
            statusLabel.setText("Pemain " + letter + " MENANG! SOS terbentuk!");
            disableAllButtons();
            showWinnerDialog(letter);
        } else if (isBoardFull()) {
            gameOver = true;
            statusLabel.setText("Permainan Seri! Papan penuh.");
            showDrawDialog();
        }
    }

    private void showWinnerDialog(char winner) {
        JOptionPane.showMessageDialog(
            this, 
            "Selamat! Pemain " + winner + " menang dengan membentuk SOS!", 
            "Game Over", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void showDrawDialog() {
        JOptionPane.showMessageDialog(
            this, 
            "Permainan berakhir seri. Tidak ada pemenang.", 
            "Game Over", 
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void disableAllButtons() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardButtons[i][j].setEnabled(false);
            }
        }
    }

    private boolean checkForSOS(int row, int col) {
        return checkHorizontal(row) || 
               checkVertical(col) || 
               checkDiagonals(row, col);
    }

    private boolean checkHorizontal(int row) {
        for (int col = 0; col <= size - 3; col++) {
            if (board[row][col] == 'S' && 
                board[row][col+1] == 'O' && 
                board[row][col+2] == 'S') {
                return true;
            }
        }
        return false;
    }

    private boolean checkVertical(int col) {
        for (int row = 0; row <= size - 3; row++) {
            if (board[row][col] == 'S' && 
                board[row+1][col] == 'O' && 
                board[row+2][col] == 'S') {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonals(int row, int col) {
        // Periksa diagonal kiri atas ke kanan bawah
        for (int i = 0; i <= size - 3; i++) {
            for (int j = 0; j <= size - 3; j++) {
                if (board[i][j] == 'S' && 
                    board[i+1][j+1] == 'O' && 
                    board[i+2][j+2] == 'S') {
                    return true;
                }
            }
        }
        
        // Periksa diagonal kanan atas ke kiri bawah
        for (int i = 0; i <= size - 3; i++) {
            for (int j = size - 1; j >= 2; j--) {
                if (board[i][j] == 'S' && 
                    board[i+1][j-1] == 'O' && 
                    board[i+2][j-2] == 'S') {
                    return true;
                }
            }
        }
        
        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void resetGame() {
        // Reset papan
        initializeBoard();
        
        // Reset tombol
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardButtons[i][j].setText("");
                boardButtons[i][j].setEnabled(true);
            }
        }
        
        // Reset status permainan
        gameOver = false;
        currentPlayer = 'S';
        statusLabel.setText("Giliran Pemain: " + currentPlayer);
    }

    public static void main(String[] args) {
        // Tampilkan dialog untuk memilih ukuran papan
        String[] sizes = {"3x3", "4x4", "5x5", "6x6"};
        String selectedSize = (String) JOptionPane.showInputDialog(
            null, 
            "Pilih Ukuran Papan", 
            "Konfigurasi Game SOS", 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            sizes, 
            sizes[0]
        );

        if (selectedSize != null) {
            int boardSize = Integer.parseInt(selectedSize.split("x")[0]);
            
            SwingUtilities.invokeLater(() -> {
                SOSGameGUI game = new SOSGameGUI(boardSize);
                game.setVisible(true);
            });
        }
    }
}
