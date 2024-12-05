import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class SudokuGame extends JFrame {
    private static final int SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private int[][] board = new int[SIZE][SIZE];

    public SudokuGame() {
        setTitle("Sudoku Game");
        setSize(500, 500);
        setLayout(new GridLayout(SIZE, SIZE));
        initializeBoard();
        initializeButtons();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initializeBoard() {
        generateSudoku();
    }

    private void generateSudoku() {
        fillBoard();
        removeNumbers();
    }

    private boolean fillBoard() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(row, col, num)) {
                            board[row][col] = num;
                            if (fillBoard()) {
                                return true;
                            }
                            board[row][col] = 0; // backtrack
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }
        int startRow = row - row % SUBGRID_SIZE;
        int startCol = col - col % SUBGRID_SIZE;
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private void removeNumbers() {
        Random rand = new Random();
        int cellsToRemove = 40; // Number of cells to remove
        while (cellsToRemove > 0) {
            int row = rand.nextInt(SIZE);
            int col = rand.nextInt(SIZE);
            if (board[row][col] != 0) {
                board[row][col] = 0; // Remove number
                cellsToRemove--;
            }
        }
    }

    private void initializeButtons() {
        Color[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.MAGENTA};
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                buttons[row][col] = new JButton();
                buttons[row][col].setFont(new Font("Arial", Font.BOLD, 24));
                
                // Set color for each 3x3 subgrid
                int colorIndex = (row / SUBGRID_SIZE) * SUBGRID_SIZE + (col / SUBGRID_SIZE);
                buttons[row][col].setBackground(colors[colorIndex % colors.length]);
                
                if (board[row][col] != 0) {
                    buttons[row][col].setText(String.valueOf(board[row][col]));
                    buttons[row][col].setEnabled(false);
                } else {
                    buttons[row][col].addActionListener(new ButtonClickListener(row, col));
                }
                add(buttons[row][col]);
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private int row;
        private int col;

        public ButtonClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String input = JOptionPane.showInputDialog(SudokuGame.this, "Enter a number (1-9):");
            if (input != null) {
                try {
                    int num = Integer.parseInt(input);
                    if (num >= 1 && num <= 9) {
                        if (isValid(row, col, num)) {
                            board[row][col] = num;
                            buttons[row][col].setText(String.valueOf(num));
                            buttons[row][col].setEnabled(false);
                        } else {
                            JOptionPane.showMessageDialog(SudokuGame.this, "Invalid move!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(SudokuGame.this, "Number must be between 1 and 9!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SudokuGame.this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SudokuGame::new);
    }
}
