public class SearchStatistic {
    public int visitedNodes = 0;
    public int maxNodes = 0;
    public int minNodes = 0;
    public int chanceNodes = 0;

    public int maxDepthReached = 0;

    public void print() {
        System.out.println("===== Expectimax Search Statistics =====");
        System.out.println("Visited nodes     : " + visitedNodes);
        System.out.println("MAX nodes         : " + maxNodes);
        System.out.println("MIN nodes         : " + minNodes);
        System.out.println("CHANCE nodes      : " + chanceNodes);
        System.out.println("Max depth reached : " + maxDepthReached);
        System.out.println("========================================");
    }
}
