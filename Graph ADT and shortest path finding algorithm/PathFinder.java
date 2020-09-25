import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.net.URLDecoder;
import java.io.UnsupportedEncodingException;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Random;


    
public class PathFinder{ 
    
    private UnweightedGraph wiKi = new MysteryUnweightedGraphImplementation();
    private Map<String, Integer> realName = new HashMap<String, Integer>();
    private Map<Integer, String> reverseRealName = new HashMap<Integer, String>();//key and value are reversed from realName.
    private Map<String, String> lastNeighbor = new HashMap<String, String>();
    private Queue<Integer> vertexToProcess= new ArrayDeque<Integer>();
    private int pathLength;
    
    
/**
 * Constructs a PathFinder that represents the graph with nodes (vertices) specified as in
 * nodeFile and edges specified as in edgeFile.
 * @param nodeFile name of the file with the node names
 * @param edgeFile name of the file with the edge names
 */
    public PathFinder(String nodeFile, String edgeFile) {
        
        load(nodeFile, edgeFile);
        
    }
    
    /**
    * This method decodes the files.
    * Creates the directed unweighted graph.
    * @param nodeFile name of the file with the node names
    * @param edgeFile name of the file with the edge names
    *
    */
    public void load(String nodeFile, String edgeFile) {
        String inputFilePath1 = nodeFile;
        String inputFilePath2 = edgeFile;
        File inputFile1 = new File(inputFilePath1);
        File inputFile2 = new File(inputFilePath2);
        
        Scanner scanner1 = null;
        Scanner scanner2 = null;
        
        try{
            scanner1 = new Scanner(inputFile1);
            scanner2 = new Scanner(inputFile2);
        } catch (FileNotFoundException e) {
            System.err.println(e);
            System.exit(1);
        }
        try {
            
            while (scanner1.hasNextLine()) {//This read through the article file
                String articleName = scanner1.nextLine();
                String article = java.net.URLDecoder.decode(articleName, "UTF-8");
                int newVertex = wiKi.addVertex();
                realName.put(article, newVertex);
                reverseRealName.put(newVertex, article);
            }
            while (scanner2.hasNextLine()) { //This read through the link file
                String linkString = scanner2.nextLine();
                if (linkString.length() != 0 && linkString.charAt(0)!='#' ){
                    String[] split = linkString.split("\t");
                    String link1 =java.net.URLDecoder.decode(split[0], "UTF-8");
                    String link2 =java.net.URLDecoder.decode(split[1], "UTF-8");
                    wiKi.addEdge(realName.get(link1), realName.get(link2));
                }
     
            }
        } catch (UnsupportedEncodingException e) {
            System.err.println(e);
            System.exit(1);
        }
        scanner1.close();
        scanner2.close();
        
        if (realName.size() == 0){
            System.err.println("Empty graph!!!");
            System.exit(1);
        }
    }
/**
 * Returns a shortest path from node1 to node2, represented as list that has node1 at
 * position 0, node2 in the final position, and the names of each node on the path
 * (in order) in between. If the two nodes are the same, then the "path" is just a
 * single node. If no path exists, returns an empty list.
 * @param node1 name of the starting article node
 * @param node2 name of the ending article node
 * @return list of the names of nodes on the shortest path
 */

   public String randomNodeGenerator(){
        Random randomNumberGenerator;
        randomNumberGenerator = new Random();
        int randomVertex = randomNumberGenerator.nextInt(reverseRealName.size());
        return reverseRealName.get(randomVertex);
   }
   public List<String> getShortestPath(String node1, String node2) {
        if (!realName.containsKey(node1)||!realName.containsKey(node2)){
            System.err.println("Node not found!!");
        }
        pathLength = 0;
        lastNeighbor.clear();
        vertexToProcess.clear();
        int startVertex = realName.get(node1);
        int endVertex = realName.get(node2);
        int nowLooping;
        vertexToProcess.add(startVertex);
        if (node1.equals(node2)){
            List<String> single= new ArrayList<String>();
            single.add(node1);
            return single;
        }
        while(!vertexToProcess.isEmpty()){
            nowLooping = vertexToProcess.poll();     
            for (int i:wiKi.getNeighbors(nowLooping)){
                if (i == endVertex){
                    lastNeighbor.put(node2,reverseRealName.get(nowLooping));
                    return findBack(node1,node2);
                }
                else if (!lastNeighbor.containsKey(reverseRealName.get(i))){//vertex not in map lastNeighbor (unvisited)
                    lastNeighbor.put(reverseRealName.get(i),reverseRealName.get(nowLooping));
                    vertexToProcess.add(i);//add to queue    
                }
            }
        }
        pathLength = -1;
        return new ArrayList<String>();
    }
    
/**
 * This method trace back the route based on the last neighbor of each node. 
 * Count the path length.
 * @param node1 name of the starting article node
 * @param node2 name of the ending article node
 * @return list of the names of nodes on the shortest path
 */

    private List<String> findBack(String node1, String node2){
        String nowAt = lastNeighbor.get(node2);
        pathLength = 1;
        List<String> path = new ArrayList<String>();
        path.add(nowAt);
        path.add(node2);
        while(!nowAt.equals(node1)){
            pathLength += 1;
            nowAt = lastNeighbor.get(nowAt);
            path.add(0,nowAt);
        }
        return path;
    }
    
/**
 * This method find the length by calling getShortestPath method.
 * Returns the length of the shortest path from node1 to node2. If no path exists,
 * returns -1. If the two nodes are the same, the path length is 0.
 * @param node1 name of the starting article node
 * @param node2 name of the ending article node
 * @return length of shortest path
 */    
    public int getShortestPathLength(String node1, String node2) {
        getShortestPath(node1, node2);
        return pathLength;
    }
    
    
 /**
 * Returns a shortest path from node1 to node2 that includes the node intermediateNode.
 * This should be a shortest path given the constraint that intermediateNodeAppears in the path. If all
 * three nodes are the same, the "path" is just a single node.  If no path exists, returns
 * an empty list.
 * @param node1 name of the starting article node
 * @param node2 name of the ending article node
 * @return list that has node1 at position 0, node2 in the final position, and the names of each node 
 *      on the path (in order) in between. 
 */
    public List<String> getShortestPath(String node1, String intermediateNode, String node2){
        List<String> intermediatePath = getShortestPath(node1, intermediateNode);
        //int intermediateLength = getShortestPathLength(node1, intermediateNode);
        //lastNeighbor.clear();
        if (intermediatePath.isEmpty()){
            return intermediatePath;
        }
        intermediatePath.remove(intermediatePath.size()-1);
        List<String> theSecondHalf = getShortestPath(intermediateNode, node2);
        if (theSecondHalf.isEmpty()){
            return theSecondHalf;
        }
        intermediatePath.addAll(getShortestPath(intermediateNode, node2));
        return intermediatePath;
    
    }
    
    public static void main(String args[]) {
        PathFinder thisTime = new PathFinder(args[0], args[1]);
        
        String node1 = thisTime.randomNodeGenerator();
        String node2 = thisTime.randomNodeGenerator();
        if (args.length != 3){
            
            System.out.println("Path From " + node1 +" to " + node2 + ", length = " + thisTime.getShortestPathLength(node1, node2));
            System.out.println(thisTime.getShortestPath(node1, node2));
        }
        else{
            String intermediateNode = thisTime.randomNodeGenerator();
            System.out.println("From " + node1 +  " to " + intermediateNode + " to " + node2);
            System.out.println("get shortest path: " + thisTime.getShortestPath(node1,intermediateNode, node2));
        }
    }
}