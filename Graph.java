import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * <p>
 * Graph
 * <p/>
 * <p>
 * Reference：《Introduction to Algorithms》Section 22.1.
 * <p/>
 * */
public abstract class Graph<T> {
    protected int[][] adjacencyMatrix;
    protected int size = 0;// number of points
    public Set<Vertex<T>> vertices;// all vertex
    public Set<Edge<T>> edges;// all edges
    private int time;
    private Stack<Vertex<T>> topologicalStack;//topological stack
    public Graph() {
        adjacencyMatrix = new int[size][size];
        vertices = new LinkedHashSet<Vertex<T>>();
        edges = new LinkedHashSet<Edge<T>>();
        topologicalStack=new Stack<Vertex<T>>();
    }

    public Graph<T> addVertex(T t) {
        Vertex<T> vertex = new Vertex<T>(t);
        // if contain the vertex, throw exception
        if (vertices.contains(vertex)) {
            throw new IllegalArgumentException("This vertex already exists，it cannot be added anymore！");
        }
        size++;
        vertices.add(vertex);
        adjustMatrix(t);
        return this;
    }

    // add edge，t1-t2
    public abstract void addEdge(T t1, T t2);

    // adjust adjacenvy matrix
    private void adjustMatrix(T t) {
        int[][] tempyMatrix = new int[size][size];
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1; j++) {
                tempyMatrix[i][j] = adjacencyMatrix[i][j];
            }
        }
        adjacencyMatrix = tempyMatrix;
    }

    protected void setMatrixValue(int row, int column) {
        adjacencyMatrix[row][column] = 1;
    }

    // print graph in the adjacency matrix form
    public void printAdjacencyMatrix() {
        System.out.println("Adjacency Matrix：");
        for (int[] is : adjacencyMatrix) {
            for (int i : is) {
                System.out.print(i);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    // Print Graph by adjacency List
    public void printAdjacencyVertices() {
        System.out.println("Adjacency List：");
        for (Vertex<T> vertex : vertices) {
            System.out.print(vertex);
            for (Vertex<T> vertex2 : vertex.adjacencyVertices) {
                System.out.print("→");
                System.out.print(vertex2);
            }
            System.out.println();
        }
    }

    protected Vertex<T> isContainVertext(T t) {
        for (Vertex<T> v : vertices) {
            if (v.value.equals(t)) {
                return v;
            }
        }
        return null;
    }

    protected Edge<T> isContainEdge(T t1, T t2) {
        for (Edge<T> edge : edges) {
            if (edge.source.equals(t1) && edge.target.equals(t2)) {
                return edge;
            }
        }
        return null;
    }

    // Breadth First Search
    // Reffered to《Introduction to Algorithms》Section 22.2
    public void printBFS(T t) {
        Vertex<T> vertex = isContainVertext(t);
        if (vertex == null) {
            throw new IllegalArgumentException("This vertex cannot be included，BFS cannot be processed！");
        }
        reSet();
        System.out.println("BFS：");
        vertex.color = Color.GRAY;
        vertex.distance = 0;
        Queue<Vertex<T>> queue = new LinkedList<Vertex<T>>();
        queue.offer(vertex);
        while (queue.size() > 0) {
            Vertex<T> u = queue.poll();
            for (Vertex<T> v : u.adjacencyVertices) {
                if (v.color == Color.WHITE) {
                    v.color = Color.GRAY;
                    v.distance = u.distance + 1;
                    v.parent = u;
                    queue.offer(v);
                }
            }
            u.color = Color.BLACK;
            System.out.print(u);
            System.out.print("→");
        }
        System.out.println();
    }
    public void printDFS(T t){
        Vertex<T> vertex = isContainVertext(t);
        if (vertex == null) {
            throw new IllegalArgumentException("This vertex cannot be included，BFS cannot be processed！");
        }
        reSet();
        System.out.println("DFS：");
        time=0;
        dfsVisit(vertex);
        for (Vertex<T> u : vertices) {
            if (u.equals(vertex)) {
                continue;
            }
            if (u.color==Color.WHITE) {
                dfsVisit(u);
            }
        }
        System.out.println();
    }
    private void dfsVisit(Vertex<T> u){
        System.out.print(u);
        System.out.print("→");
        time++;
        u.discover=time;
        u.color=Color.GRAY;
        for (Vertex<T> v : u.adjacencyVertices) {
            if (v.color==Color.WHITE) {
                v.parent=u;
                dfsVisit(v);
            }
        }
        u.color=Color.BLACK;
        time++;
        u.finish=time;

        topologicalStack.push(u);

    }
    /**
     * Print topology
     * */
    public void printTopology(T t){
        if (topologicalStack.size()==0) {
            printDFS(t);
        }
        while (topologicalStack.size()>0) {
            System.out.print(topologicalStack.pop());
            System.out.print("→");
        }
        System.out.println();

    }
    private void reSet(){
        for (Vertex<T> vertex : vertices) {
            vertex.color=Color.WHITE;
        }
        topologicalStack.clear();
    }

}
