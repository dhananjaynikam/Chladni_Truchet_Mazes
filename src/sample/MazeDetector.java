package sample;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MazeDetector {

    private static Hashtable<PositionId, DiagonalTile> posIdsToTile;
    private static Hashtable<PositionId, CircleTile> posIdxToCircleTile;
    private static Hashtable<PositionId, HexagonalTile> posIdHexTile;

    public static List<PositionId> checkDiagonalMaze(Graph diagonalTileGraph, PositionId root, PositionId end, Hashtable<PositionId, DiagonalTile> posIdToTile){
        posIdsToTile = posIdToTile;
        List<PositionId> traversed = new ArrayList<>();  //add the visited positionId to the traversed list
        List<PositionId> path = new ArrayList<>();
        traversed.add(root);
        path.add(root);

        if(!hasPathToEnd(diagonalTileGraph,root,end, path, traversed)){
            path.remove(path.size()-1);
        }

        return path;
    }

    public static List<PositionId> checkCircleMaze(Graph circleTileGraph, PositionId root, PositionId end, Hashtable<PositionId, CircleTile> posIdToTile){
        posIdxToCircleTile = posIdToTile;
        List<PositionId> traversed = new ArrayList<>();  //add the visited positionId to the traversed list
        List<PositionId> path = new ArrayList<>();
        traversed.add(root);
        path.add(root);

        if(!hasPathToEndCircle(circleTileGraph,root,end, path, traversed)){
            path.remove(path.size()-1);
        }

        return path;
    }

    public static List<PositionId> checkHexagonalMaze(Graph diagonalTileGraph, PositionId root, PositionId end, Hashtable<PositionId, HexagonalTile> posIdToTile){
        posIdHexTile = posIdToTile;
        List<PositionId> traversed = new ArrayList<>();  //add the visited positionId to the traversed list
        List<PositionId> path = new ArrayList<>();
        traversed.add(root);
        path.add(root);

        if(!hasPathToEndHex(diagonalTileGraph,root,end, path, traversed)){
            path.remove(path.size()-1);
        }

        return path;
    }

    private static boolean hasPathToEnd(Graph graph, PositionId node, PositionId end, List<PositionId> path, List<PositionId> traversed){
        if(node.equals(end)){
            return true;
        }
        List<PositionId> adjacentPositions = graph.getAdjVertices(node);
        for(PositionId adjacent : adjacentPositions){
            if(canMove(node,adjacent, graph,traversed)){
                traversed.add(adjacent);
                path.add(adjacent);
                if(hasPathToEnd(graph,adjacent, end,path,traversed)){
                    return true;
                }
                path.remove(path.size() - 1);
            }
        }
        return false;
    }

    private static boolean hasPathToEndCircle(Graph graph, PositionId node, PositionId end, List<PositionId> path, List<PositionId> traversed){
        if(node.equals(end)){
            return true;
        }
        List<PositionId> adjacentPositions = graph.getAdjVertices(node);
        for(PositionId adjacent : adjacentPositions){
            if(canMoveCircle(node,adjacent, graph,traversed)){
                traversed.add(adjacent);
                path.add(adjacent);
                if(hasPathToEndCircle(graph,adjacent, end,path,traversed)){
                    return true;
                }
                path.remove(path.size() - 1);
            }
        }
        return false;
    }

    private static boolean hasPathToEndHex(Graph graph, PositionId node, PositionId end, List<PositionId> path, List<PositionId> traversed){
        if(node.equals(end)){
            return true;
        }
        List<PositionId> adjacentPositions = graph.getAdjVertices(node);
        for(PositionId adjacent : adjacentPositions){
            if(canMoveHex(node,adjacent, graph,traversed)){
                traversed.add(adjacent);
                path.add(adjacent);
                if(hasPathToEndHex(graph,adjacent, end,path,traversed)){
                    return true;
                }
                path.remove(path.size() - 1);
            }
        }
        return false;
    }

    private static boolean canMove(PositionId node,PositionId adjacentNode, Graph graph,List<PositionId> traversed ){
       if(!traversed.contains(adjacentNode)) {
           DiagonalTile tile = posIdsToTile.get(node);
           DiagonalTile adjTile = posIdsToTile.get(adjacentNode);
           int adjTileOrientation = adjTile.getOrientation();
           int nodeTileOrientation = tile.getOrientation();
           switch (nodeTileOrientation) {
               case 1:
                   if (node.x == adjacentNode.x - 1 || node.y == adjacentNode.y + 1) {
                       if (adjTileOrientation == 3 || adjTileOrientation == 4 || adjTileOrientation == 2)
                           return true;
                   }
                   break;
               case 2:
                   if (node.x == adjacentNode.x - 1 || node.y == adjacentNode.y - 1) {
                       if (adjTileOrientation == 4 || adjTileOrientation == 1 || adjTileOrientation == 3)
                           return true;
                   }
                   break;
               case 3:
                   if (node.x == adjacentNode.x + 1 || node.y == adjacentNode.y - 1) {
                       if (adjTileOrientation == 1 || adjTileOrientation == 4 || adjTileOrientation == 2)
                           return true;
                   }
               case 4:
                   if (node.x == adjacentNode.x + 1 || node.y == adjacentNode.y + 1) {
                       if (adjTileOrientation == 3 || adjTileOrientation == 2 || adjTileOrientation == 1) {
                           return true;
                       }
                   }
                   break;
           }
       }
       return false;
    }

    private static boolean canMoveCircle(PositionId node,PositionId adjacentNode, Graph graph,List<PositionId> traversed ){
        if(!traversed.contains(adjacentNode)){
            CircleTile tile = posIdxToCircleTile.get(node);
            CircleTile adjTile = posIdxToCircleTile.get(adjacentNode);
            int adjTileOrientation = adjTile.getOrientation();
            int nodeTileOrientation = tile.getOrientation();
            switch (adjTileOrientation){
                case 1:
                    if(node.x == adjacentNode.x -1 || node.y == adjacentNode.y + 1){
                        if(nodeTileOrientation == 3 || nodeTileOrientation == 2)
                            return true;
                    }else if(node.x == adjacentNode.x + 1 || node.y == adjacentNode.y + 1){
                        if(nodeTileOrientation == 2 || nodeTileOrientation ==3)
                            return true;
                    }
                    break;
                case 2:
                    if(nodeTileOrientation == 4 || nodeTileOrientation == 1)
                        return true;
                    break;
                case 3:
                    if(node.x == adjacentNode.x - 1 || node.y == adjacentNode.y -1 ){
                       if(nodeTileOrientation == 4 || nodeTileOrientation == 1 )
                           return true;
                    }
                    break;
                case 4:
                    if(nodeTileOrientation == 2 || nodeTileOrientation == 3)
                        return true;
                    break;
            }
        }
        return false;
    }

    private static boolean canMoveHex(PositionId node,PositionId adjacentNode, Graph graph,List<PositionId> traversed){
       if(adjacentNode.x % 2 != 0 && adjacentNode.y == 9) return false; //not a hexagon Tile. edge case while creating graph

        if(!traversed.contains(adjacentNode)){
            HexagonalTile tile = posIdHexTile.get(node);
            HexagonalTile adjTile = posIdHexTile.get(adjacentNode);
            if(adjTile == null) return false;
            int adjTileOrientation = adjTile.getOrientation();
            int nodeTileOrientation = tile.getOrientation();
            if(adjTileOrientation == 2 && nodeTileOrientation == 2 )
                return true;
        }

        return false;
    }
}
