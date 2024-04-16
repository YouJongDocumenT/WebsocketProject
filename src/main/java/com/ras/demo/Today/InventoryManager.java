package com.ras.demo.Today;

public class InventoryManager {
    private boolean[][] spaces; // 공간을 저장할 배열
    private int width;
    private int height;

    public InventoryManager(int width, int height) {
        this.height = height;
        this.width = width;
        this.spaces = new boolean[height][width];  // 초기값 'false', 사용 가능한 공간
    }

    // 판매재고 마스킹
    public void markSoldArea(int minX, int minY, int maxX, int maxY) {
        markArea(minX, minY, maxX, maxY, true);
    }

    private void markArea(int minX, int minY, int maxX, int maxY, boolean status) {
        int lowerX = Math.min(minX, maxX);
        int upperX = Math.max(minX, maxX);
        int lowerY = Math.min(minY, maxY);
        int upperY = Math.max(minY, maxY);

        // status = true, 판매한 공간만큼을 true로 변경
        for (int i = lowerY; i <= upperY; i++) {
            for (int j = lowerX; j <= upperX; j++) {
                if (i >= 0 && i < height && j >= 0 && j < width) {
                    spaces[i][j] = status;  // Set the area as sold/scrap (unavailable)
                }
            }
        }
    }

    // 총 사용한 공간 계산
    public int calculateTotalUnavailableArea() {
        return calculateArea(true) * 100;  // Each unit is 10cm x 10cm = 100 cm²
    }

    // 기존의 넓이에서 폐기한 만큼의 공간을 계산후 
    public int calculateLossArea(int minX, int minY, int maxX, int maxY) {
        int lossArea = 0;
        int lowerX = Math.min(minX, maxX);
        int upperX = Math.max(minX, maxX);
        int lowerY = Math.min(minY, maxY);
        int upperY = Math.max(minY, maxY);

        // spaces[i][j]의 값을 조사해서 false면 lossArea++하고 true로 변경
        for (int i = lowerY; i <= upperY; i++) {
            for (int j = lowerX; j <= upperX; j++) {
                if (i >= 0 && i < height && j >= 0 && j < width && !spaces[i][j]) {
                    lossArea++;  // Increment only if the space was available
                    spaces[i][j] = true;  // Now mark it as unavailable
                }
            }
        }

        return lossArea * 100;  // Each unit is 10cm x 10cm = 100 cm²
    }

    // cell의 값이 true인 인덱스 조사해서 aree++
    private int calculateArea(boolean status) {
        int area = 0;
        for (boolean[] row : spaces) {
            for (boolean cell : row) {
                if (cell == status) {
                    area++;
                }
            }
        }
        return area;
    }

    public void printInventory() {
        for (boolean[] row : spaces) {
            for (boolean cell : row) {
                System.out.print(cell ? "- " : "o ");  // -: Unavailable, o: Available
            }
            System.out.println();
        }
    }

}
