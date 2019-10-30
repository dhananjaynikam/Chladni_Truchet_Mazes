package sample;


import java.util.*;

public class Graph {

    private HashMap<PositionId, List<PositionId>> adjListArray = new HashMap<>();

    public void addVertex(PositionId position){
        adjListArray.putIfAbsent(position, new ArrayList<>());
    }

    public void addEdge(PositionId source, PositionId dest){
        adjListArray.get(source).add(dest);
        adjListArray.get(dest).add(source);
    }

    public List<PositionId> getAdjVertices(PositionId key){
        return adjListArray.get(key);
    }


    public void printGraph(){
//        Iterator positionIds = this.adjListArray.entrySet().iterator();
//        while (positionIds.hasNext()){
//            Map.Entry adjacent = (Map.Entry) positionIds.next();
//            System.out.println(adjacent.getValue());
//        }
        for(Map.Entry mapElement : adjListArray.entrySet()){
            PositionId key = (PositionId) mapElement.getKey();

            List<PositionId> items = (List<PositionId>) mapElement.getValue();
            for(PositionId item : items)
                System.out.println("Key=(" + key.x + "," + key.y+ ") value = (" + item.x + "," + item.y + ")");
        }
    }
}
