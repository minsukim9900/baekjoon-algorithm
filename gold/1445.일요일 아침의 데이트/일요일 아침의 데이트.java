
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

public class Main {
    private static int N, M;
    private static int[][] costMap;

    private static final int TRASH_COST = 10_000;
    private static final int NEAR_TRASH_COST = 1;
    private static final int INF = 100_000_000;

    private static final int[][] DIRECTIONS = {
        {-1, 0}, {1, 0}, {0, -1}, {0, 1}
    };

    private static boolean isInBounds(int row, int col) {
        return row >= 0 && row < N && col >= 0 && col < M;
    }

    private static boolean isEndpoint(
            int row, int col,
            int sr, int sc, int er, int ec) {

        return (row == sr && col == sc)
            || (row == er && col == ec);
    }

    private static void markCellsNearTrash(
            int sr, int sc, int er, int ec) {

        for (int row = 0; row < N; row++) {
            for (int col = 0; col < M; col++) {
                if (costMap[row][col] != TRASH_COST) {
                    continue;
                }

                for (int[] direction : DIRECTIONS) {
                    int nextRow = row + direction[0];
                    int nextCol = col + direction[1];

                    if (!isInBounds(nextRow, nextCol)) {
                        continue;
                    }

                    if (isEndpoint(nextRow, nextCol, sr, sc, er, ec)) {
                        continue;
                    }

                    if (costMap[nextRow][nextCol] == 0) {
                        costMap[nextRow][nextCol] = NEAR_TRASH_COST;
                    }
                }
            }
        }
    }

    private static int dijkstra(int sr, int sc, int er, int ec) {
        int[][] dist = new int[N][M];

        for (int[] row : dist) {
            Arrays.fill(row, INF);
        }

        PriorityQueue<int[]> pq = new PriorityQueue<>(
            (a, b) -> Integer.compare(a[2], b[2])
        );

        dist[sr][sc] = 0;
        pq.offer(new int[] {sr, sc, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();

            int row = current[0];
            int col = current[1];
            int currentCost = current[2];

            if (currentCost != dist[row][col]) {
                continue;
            }

            if (row == er && col == ec) {
                return currentCost;
            }

            for (int[] direction : DIRECTIONS) {
                int nextRow = row + direction[0];
                int nextCol = col + direction[1];

                if (!isInBounds(nextRow, nextCol)) {
                    continue;
                }

                int nextCost = currentCost + costMap[nextRow][nextCol];

                if (nextCost >= dist[nextRow][nextCol]) {
                    continue;
                }

                dist[nextRow][nextCol] = nextCost;
                pq.offer(new int[] {nextRow, nextCol, nextCost});
            }
        }

        return -1;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(
            new InputStreamReader(System.in)
        );

        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        costMap = new int[N][M];

        int sr = 0, sc = 0, er = 0, ec = 0;

        for (int row = 0; row < N; row++) {
            String line = br.readLine();

            for (int col = 0; col < M; col++) {
                char cell = line.charAt(col);

                if (cell == 'S') {
                    sr = row;
                    sc = col;
                } else if (cell == 'F') {
                    er = row;
                    ec = col;
                } else if (cell == 'g') {
                    costMap[row][col] = TRASH_COST;
                }
            }
        }

        markCellsNearTrash(sr, sc, er, ec);

        int minCost = dijkstra(sr, sc, er, ec);

        System.out.println(
            minCost / TRASH_COST + " " + minCost % TRASH_COST
        );
    }
}
