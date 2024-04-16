public class Geometry {

    static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    static class Rectangle {
        Point[] vertices = new Point[4]; // 사각형의 네 꼭지점

        public Rectangle(Point diagonalPoint1, Point diagonalPoint2) {
            vertices[0] = diagonalPoint1;
            vertices[2] = diagonalPoint2;
            calculateRemainingVertices();
        }

        private void calculateRemainingVertices() {
            // 나머지 두 꼭지점 계산
            vertices[1] = new Point(vertices[0].x, vertices[2].y);
            vertices[3] = new Point(vertices[2].x, vertices[0].y);
        }

        public void printVertices() {
            System.out.println("사각형의 꼭지점:");
            for (Point vertex : vertices) {
                System.out.println(vertex);
            }
        }
    }

    public static void main(String[] args) {
        Point diagonalPoint1 = new Point(1, 3); // 대각선의 첫 번째 꼭지점
        Point diagonalPoint2 = new Point(4, 1); // 대각선의 두 번째 꼭지점

        Rectangle rectangle = new Rectangle(diagonalPoint1, diagonalPoint2);
        rectangle.printVertices(); // 사각형의 모든 꼭지점 출력
    }
}