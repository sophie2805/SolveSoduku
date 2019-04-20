package com.sophiesuyutian;

import java.util.*;

public class SodukuEnder {

    private int[][] sodukuGrid = new int[9][9];
    private List<Index> footPrint = new ArrayList<>();

    private List<List<Index>> sodukuCube = new ArrayList<>();

    private SodukuEnder(int[] soduku) {
        for(int i = 0; i < 9; i ++){
            for(int j = 8; j >= 0; j --){
                this.sodukuGrid[i][j] = soduku[i]%10;
                soduku[i] /= 10;
            }
        }
        for (int i = 0; i < 9;) {
            for (int j = 0; j < 9;) {
                List<Index> cube = new ArrayList<>();
                for (int m = i; m < i + 3; m++) {
                    for (int n = j; n < j + 3; n++)
                        cube.add(new Index(m, n));
                }
                sodukuCube.add(cube);
                j += 3;
            }
            i += 3;
        }
        for (int row = 0; row < 9; row++)
            for (int col = 0; col < 9; col++)
                if (sodukuGrid[row][col] == 0)
                    footPrint.add(new Index(row, col));
    }

    private List<Integer> getCandidates(int row, int col) {
        Set<Integer> occupiedCells = new HashSet<>();
        for (int i = 0; i < 9; i++) {
            occupiedCells.add(sodukuGrid[row][i]);
            occupiedCells.add(sodukuGrid[i][col]);
        }
        int cubeIndex = 0;
        boolean found = false;
        for (; cubeIndex < 9;) {
            for (Index index : sodukuCube.get(cubeIndex)) {
                if (index.toString().equals(row + "," + col)) {
                    found = true;
                    break;
                }
            }
            if (found)
                break;
            cubeIndex++;
        }
        for (Index index : sodukuCube.get(cubeIndex))
            occupiedCells.add(sodukuGrid[index.getRow()][index.getCol()]);
        List<Integer> sodukuNumbers = new ArrayList<>();
        for(int i = 1; i <= 9; i ++){
            sodukuNumbers.add(i);
        }
        HashSet<Integer> candidates = new HashSet<>(sodukuNumbers);
        candidates.removeAll(occupiedCells);
        return new ArrayList<>(candidates);
    }

    private boolean sodukuSolver(int pointer) {
        if (pointer >= footPrint.size())
            return true;
        List<Integer> tryOut = getCandidates(footPrint.get(pointer).row, footPrint.get(pointer).col);
        if (tryOut.isEmpty()) { // no candidates, return false
            return false;
        }
        boolean flag;
        for (int tryNum : tryOut) {
            sodukuGrid[footPrint.get(pointer).row][footPrint.get(pointer).col] = tryNum;
            flag = sodukuSolver(pointer + 1);
            if(!flag){ // current candidate try out failed, erease it, try next candidate
                sodukuGrid[footPrint.get(pointer).row][footPrint.get(pointer).col] = 0;
            }
            else
                return true;
        }
        return false;
    }

    private void printSodukuGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.print(sodukuGrid[row][col]==0 ? " " : sodukuGrid[row][col]);
            }
            System.out.print(System.lineSeparator());
        }
        System.out.println("-------------");
    }

    class Index {
        private int row, col;

        private int getRow() {
            return row;
        }

        private int getCol() {
            return col;
        }

        private Index(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public String toString() {
            return (row + "," + col);
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {

        int[] soduku = {
                5004,
                90200,
                900300005,
                4700001,
                307629000,
                0,
                702053060,
                6000000,
                40030
        };

        long startTime = System.currentTimeMillis();
        SodukuEnder sodukuEnder = new SodukuEnder(soduku);
        sodukuEnder.sodukuSolver(0);
        sodukuEnder.printSodukuGrid();
        long endTime = System.currentTimeMillis();
        System.out.println("Total time cost: " + (endTime - startTime) + " ms");
    }
}
