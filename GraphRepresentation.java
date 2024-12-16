import java.io.*;
import java.util.*;

public class GraphRepresentation {

    public static void main(String[] args) throws IOException {
        // Input and output file names
        String inputFileName = "testFileGraph.txt";
        String outputFileName = "graphRepresentations.txt";

        // Read the graph from the input file
        List<Edge> edges = readGraph(inputFileName);

        // Create the graph representations
        Map<String, List<Edge>> adjacencyList = createAdjacencyList(edges);
        int[][] adjacencyMatrix = createAdjacencyMatrix(edges);
        List<List<Edge>> arrayOfLists = createArrayOfLists(adjacencyList);

        // Write the representations to the output file
        writeGraphRepresentations(outputFileName, adjacencyList, adjacencyMatrix, arrayOfLists);
    }

    static class Edge {
        String source;
        String target;
        int weight;

        Edge(String source, String target, int weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "(" + source + " -> " + target + ", weight: " + weight + ")";
        }
    }

    private static List<Edge> readGraph(String fileName) throws IOException {
        List<Edge> edges = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Parse the edge
                String[] parts = line.replace("<", "").replace(">", "").split(",");
                String source = parts[0].trim();
                String target = parts[1].trim();
                int weight = Integer.parseInt(parts[2].trim());

                // Add the edge (undirected, so add both directions)
                edges.add(new Edge(source, target, weight));
                edges.add(new Edge(target, source, weight));
            }
        }
        return edges;
    }

    private static Map<String, List<Edge>> createAdjacencyList(List<Edge> edges) {
        Map<String, List<Edge>> adjacencyList = new HashMap<>();
        for (Edge edge : edges) {
            adjacencyList.computeIfAbsent(edge.source, k -> new ArrayList<>()).add(edge);
        }
        return adjacencyList;
    }

    private static int[][] createAdjacencyMatrix(List<Edge> edges) {
        // Collect all vertices
        Set<String> vertices = new HashSet<>();
        for (Edge edge : edges) {
            vertices.add(edge.source);
            vertices.add(edge.target);
        }

        // Map vertices to indices
        List<String> vertexList = new ArrayList<>(vertices);
        Map<String, Integer> vertexIndices = new HashMap<>();
        for (int i = 0; i < vertexList.size(); i++) {
            vertexIndices.put(vertexList.get(i), i);
        }

        // Create the matrix
        int n = vertexList.size();
        int[][] matrix = new int[n][n];
        for (Edge edge : edges) {
            int sourceIndex = vertexIndices.get(edge.source);
            int targetIndex = vertexIndices.get(edge.target);
            matrix[sourceIndex][targetIndex] = edge.weight;
        }
        return matrix;
    }

    private static List<List<Edge>> createArrayOfLists(Map<String, List<Edge>> adjacencyList) {
        return new ArrayList<>(adjacencyList.values());
    }

    private static void writeGraphRepresentations(String fileName, Map<String, List<Edge>> adjacencyList,
                                                   int[][] adjacencyMatrix, List<List<Edge>> arrayOfLists) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Write adjacency list
            writer.write("Adjacency List:\n");
            for (Map.Entry<String, List<Edge>> entry : adjacencyList.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue() + "\n");
            }
            writer.write("\n");

            // Write adjacency matrix
            writer.write("Adjacency Matrix:\n");
            for (int[] row : adjacencyMatrix) {
                writer.write(Arrays.toString(row) + "\n");
            }
            writer.write("\n");

            // Write array of lists
            writer.write("Array of Lists:\n");
            for (List<Edge> list : arrayOfLists) {
                writer.write(list.toString() + "\n");
            }
        }
    }
}
