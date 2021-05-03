package sample;


import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.geometry.Insets;
import javafx.scene.Scene;

// Java Program to create a button and add it to the stage
import javafx.scene.control.Button;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

class Triangle extends Polygon{
    Polygon triangle1;
    Triangle(double x,double y){
        triangle1=new Polygon(x,y-20,x-20,y+20,x+20,y+20);
    }

    public javafx.scene.shape.Polygon getTriangle() {
        return triangle1;
    }
}

class NodeArray{
    LinkedList<EdgeArray> edges;
    int n;
    String name,x,y;
    public boolean visited;
    NodeArray(int n, String name,String x,String y){
        this.n = n;
        this.name = name;
        this.x=x;
        this.y=y;
        visited =false;
        edges=new LinkedList<>();
    }
    boolean checkVisited() {
        return visited;
    }
    void visit(){
        visited =true;
    }
}

class EdgeArray{
    NodeArray source;
    NodeArray destination;
    double weight;
    EdgeArray(NodeArray s,NodeArray d,double w) {
        source =s;
        destination =d;
        weight =w;
    }
}

class Graph{
    public static ArrayList<NodeArray> nodes;
    Graph(boolean directed) {
        nodes =new ArrayList<>();
    }
    public void AddNode(NodeArray j){
        nodes.add(j);
    }
//    public void printNode(){
//        for(NodeArray nodes: nodes){
//            System.out.println(nodes.name);
//        }
//    }

    public void addEdge(NodeArray node1,NodeArray node2,double weight) {
        for (EdgeArray edge :node1.edges) {
            if(edge.source==node1 && edge.destination==node2){
                edge.weight = weight;
                return;
            }
        }
        node1.edges.add(new EdgeArray(node1, node2, weight));
    }

    public NodeArray findUnvisited(HashMap<NodeArray,Double> shortPath) {
        double dist =Double.POSITIVE_INFINITY;
        NodeArray node1 =null;
        for (NodeArray node:nodes) {
            if(node.checkVisited()) continue;
            double currentDistance=shortPath.get(node);
            if(currentDistance==Double.POSITIVE_INFINITY) continue;
            if(currentDistance< dist){
                dist=currentDistance;
                node1=node;
            }
        }
        return node1;
    }

    public String PathFind(NodeArray start,NodeArray end,TextField k){
        HashMap<NodeArray, NodeArray> pathChanged =new HashMap<>();
        pathChanged.put(start,null);
        HashMap<NodeArray, Double> shortPath =new HashMap<>();
        for(NodeArray node:nodes){
            if(node==start) shortPath.put(start,0.0);
            else shortPath.put(node,Double.POSITIVE_INFINITY);
            System.out.println(node.name);
        }
        for(EdgeArray edge:start.edges){
            shortPath.put(edge.destination, edge.weight);
            pathChanged.put(edge.destination, start);
        }
        start.visit();
        while(true){
            NodeArray currentNode = findUnvisited(shortPath);
            if(currentNode== null){
                k.setText("There isn't a path between " + start.name + " and " + end.name);
                for(NodeArray node: nodes){
                    node.visited=false;
                }
                return "a";
            }
            if(currentNode== end){
                NodeArray child=end;
                String path=end.name;
                while(true){
                    NodeArray parent= pathChanged.get(child);
                    if(parent == null) break;
                    path =parent.name +" ->"+path;
                    child= parent;
                }
                k.setText(path);
                System.out.println(path);
                for(NodeArray node:nodes){
                    node.visited=false;
                }
                return path;
            }
            currentNode.visit();
            for(EdgeArray edge :currentNode.edges){
                if(edge.destination.checkVisited())
                    continue;
                if(shortPath.get(currentNode)+ edge.weight <shortPath.get(edge.destination)){
                    shortPath.put(edge.destination,shortPath.get(currentNode) +edge.weight);
                    pathChanged.put(edge.destination,currentNode);
                }
            }
        }
    }
}

public class Main extends Application {
    int count =0;
    String Abhinav,Bansal;
    Pane pane=new Pane();
    public ArrayList<Line> removeLines;
    TextField query=new TextField();
    Button PlusButton=new Button("Plus");
    boolean plusB=false;
    Button CircleButton=new Button("Circle");
    boolean circleB=false;
    Button SquareButton=new Button("Square");
    boolean squareB=false;
    Button CrossButton=new Button("Cross");
    boolean crossB=false;
    Button TriangleButton=new Button("Triangle");
    boolean triangleB=false;
    Button removePath=new Button("Remove Path");
    public boolean dragActive = false;
    public Line currentLine = null;
    Line plus1=new Line();
    Line plus2=new Line();
    HashMap<String,Text> vertexText=new HashMap<>();
    HashMap<Line,Text> edgeText=new HashMap<>();
    public ArrayList<Line> lines=new ArrayList<>();
    private DoubleProperty mouseX = new SimpleDoubleProperty();
    private DoubleProperty mouseY = new SimpleDoubleProperty();
    public ArrayList<Circle> nodes=new ArrayList<>();
    ArrayList<EdgeArray> containEdge=new ArrayList<>();
    Graph graphWeighted = new Graph(true);
    Graph graphWeightedInteractive = new Graph(true);
    Stage window;
    Scene scene1,scene2,scene3,scene5,scene6,scene7,scene8,scene9,scene10,scene11,scene12;
    TextField weightText=new TextField();
    List<String> itemList;
    Polyline polyline;
    PathTransition pathTransition;
    Circle circle1;
    Rectangle rectangle;
    Line line1,line2,line4,line5;
    Group group,group1;
    Triangle obj;
    Polygon triangle;
    // launch the application
    public void start(Stage primaryStage)
    {
        window=primaryStage;
        // set title for the stage
        Button btn1 = new Button();
        btn1.setText("Add Vertex");
        btn1.setOnAction(e-> window.setScene(scene2));
        Button btn2 = new Button();
        btn2.setText("Search Vertex");
        btn2.setOnAction(e-> window.setScene(scene3));
        Button btn3 = new Button();
        btn3.setText("Delete Vertex");
        btn3.setOnAction(e->window.setScene(scene6));
        Button btn4 = new Button();
        btn4.setText("Modify Vertex");
        btn4.setOnAction(e->window.setScene(scene5));
        Button btn5 = new Button();
        btn5.setText("Add Edge");
        btn5.setOnAction(e->window.setScene(scene7));
        Button btn6 = new Button();
        btn6.setText("Search Edge");
        btn6.setOnAction(e->window.setScene(scene8));
        Button btn7 = new Button();
        btn7.setText("Modify Edge");
        btn7.setOnAction(e->window.setScene(scene9));
        Button btn8 = new Button();
        btn8.setText("Delete Edge");
        btn8.setOnAction(e->window.setScene(scene10));
        Button btn9 = new Button();
        btn9.setText("Search Path");
        btn9.setOnAction(e->window.setScene(scene11));
        Button btn12 = new Button();
        btn12.setText("Go To Interactive Session");
        btn12.setOnAction(e->window.setScene(scene12));
        Button btn10 = new Button();
        btn10.setText("Take Input from file");
        btn10.setOnAction(e->{
            JFileChooser fileChooser=new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int value=fileChooser.showOpenDialog(null);
            if(value==JFileChooser.APPROVE_OPTION){
                File file=fileChooser.getSelectedFile();
                Scanner sc=null;
                try{
                    sc=new Scanner(file);
                }catch(FileNotFoundException e1){
                    e1.printStackTrace();
                }
                try{
                    int noOfVertex,noOfEdge;
                    noOfVertex=sc.nextInt();
                    for(int i=0; i<noOfVertex; i++){
                        String name,a,b;
                        name=sc.next();
                        if(name.matches("^\\d+(\\.\\d+)?")) {
                            throw new Exception();
                        } else {
                            //System.out.println("Vertex Added");
                            a=sc.next();
                            b=sc.next();
                            if(a.matches("^\\d+(\\.\\d+)?") && b.matches("^\\d+(\\.\\d+)?")){
                                // System.out.println(a+ " "+b);
                                NodeArray zero = new NodeArray(count, name,a,b);
                                //System.out.println("New Vertex " + vertexInput.getText()+ " is added");
                                graphWeighted.AddNode(zero);
                            }else {
                                throw new Exception();
                            }
                        }
                    }
                    noOfEdge=sc.nextInt();
                    for(int i=0; i<noOfEdge; i++){
                        String source,dest;
                        double weight;
                        source=sc.next();
                        dest=sc.next();
                        weight=sc.nextDouble();
                        if(source.isEmpty() || dest.isEmpty() || Double.toString(weight).isEmpty()){
                            throw new Exception();
                        }else if(source.matches("^\\d+(\\.\\d+)?") || dest.matches("^\\d+(\\.\\d+)?") ){
                            throw new Exception();
                        }else{
                            NodeArray zero = null;
                            NodeArray one = null;
                            for(NodeArray node: graphWeighted.nodes){
                                if(node.name.equals(source)){
                                    zero=node;
                                    // System.out.println(zero.name);
                                }
                            }
                            for(NodeArray node: graphWeighted.nodes){
                                if(node.name.equals(dest)){
                                    one=node;
                                    // System.out.println(zero.name);
                                }
                            }
                            graphWeighted.addEdge(zero, one, weight);
                            //containEdge.add(new EdgeArray(zero,one,weight));
                            graphWeighted.addEdge(one, zero ,weight);
                            //containEdge.add(new EdgeArray(one,zero,weight));
                        }

                    }
                }catch(Exception e5){
                    System.out.println("Invalid File Input");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong File Input");
                    alert.showAndWait();
                }

            }
        });
        Button btn11 = new Button();
        btn11.setText("Export output to file");
        btn11.setOnAction(e->{
            JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
            int value = fileChooser.showOpenDialog(null);
            if (value == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    FileWriter fw=new FileWriter(file);
                    PrintWriter pw = new PrintWriter(fw);
                    Collections.sort(graphWeighted.nodes, new SortByName());
                    for(NodeArray node: graphWeighted.nodes){
                        LinkedList<EdgeArray> edges=node.edges;
                        for(EdgeArray edge: edges){
                            containEdge.add(edge);
                        }
                    }
                    Collections.sort(containEdge,new SortByClass());
                    pw.printf("%d\r\n", graphWeighted.nodes.size());
                    for(NodeArray nn:graphWeighted.nodes) {
                        pw.printf("%s %s %s\r\n",nn.name,nn.x,nn.y );
                    }
                    pw.printf("%d\r\n", containEdge.size());
                    for(EdgeArray ee:containEdge) {
                        pw.printf("%s %s %d\r\n",ee.source.name,ee.destination.name,(int)ee.weight);
                    }
                    fw.close();
                }catch(Exception e1){System.out.println(e1);}
            }
        });

        //Add Vertex
        Label label1 = new Label("Add Vertex");
        TextField vertexInput = new TextField();
        vertexInput.setPromptText("Vertex Name");
        TextField xInput = new TextField();
        xInput.setPromptText("X-coordinate");
        TextField yInput = new TextField();
        yInput.setPromptText("Y-coordinate");
        Button btnNext=new Button("Submit");
        btnNext.setOnAction(e->AddVertex(vertexInput,xInput,yInput));
        Button btnBack=new Button("Back");
        btnBack.setOnAction(e->window.setScene(scene1));
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(label1,vertexInput,xInput,yInput, btnNext,btnBack);
        scene2 = new Scene(layout, 600, 300);

        //Search Vertex
        Label label2 = new Label("Search Vertex");
        TextField searchVertex = new TextField();
        searchVertex.setPromptText("Vertex to be searched for");
        Button search=new Button("Submit");
        TextField l=new TextField();
        l.setPromptText("Here your vertex will print");
        search.setOnAction(e->{
            int flag=0;
            //System.out.println("Hello you got it");
            for(NodeArray nodes: graphWeighted.nodes) {
                //System.out.println("Hello you got it");
                if (nodes.name.equals(searchVertex.getText())) {
                    //l.setText("Vertex Name is " + nodes.name + " and x-coordinate is " + nodes.x + " and y-coordinate is " + nodes.y);
                    //System.out.println("Hello you got it");
                    flag=1;
                }
            }
            if (flag == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Vertex Name is found");
                alert.showAndWait();
            }else{
                for(NodeArray nodes: graphWeighted.nodes) {
                    //System.out.println("Hello you got it");
                    if (nodes.name.equals(searchVertex.getText())) {
                        l.setText("Vertex Name is " + nodes.name + " and x-coordinate is " + nodes.x + " and y-coordinate is " + nodes.y);
                    }
                }
            }
        });
        Button btnBack1=new Button("Back");
        btnBack1.setOnAction(e->window.setScene(scene1));
        VBox layout1 = new VBox(10);
        layout1.setPadding(new Insets(20, 20, 20, 20));
        layout1.getChildren().addAll(label2,searchVertex, search,btnBack1,l);
        scene3 = new Scene(layout1, 600, 300);


        // Modify Vertex
        Label label3 = new Label("Modify Vertex");
        TextField modifyVertex = new TextField();
        modifyVertex.setPromptText("Vertex To be modified(Name)");
        TextField modifyVertex1 = new TextField();
        modifyVertex1.setPromptText("New Vertex Name");
        TextField xInput1 = new TextField();
        xInput1.setPromptText("X-coordinate(New)");
        TextField yInput1 = new TextField();
        yInput1.setPromptText("Y-coordinate(New)");
        Button btnNext1=new Button("Submit");
        btnNext1.setOnAction(e->ModifyVertex(modifyVertex,modifyVertex1,xInput1,yInput1));
        Button btnBack2=new Button("Back");
        btnBack2.setOnAction(e->window.setScene(scene1));
        VBox layout2 = new VBox(10);
        layout2.setPadding(new Insets(20, 20, 20, 20));
        layout2.getChildren().addAll(label3,modifyVertex,modifyVertex1,xInput1,yInput1, btnNext1,btnBack2);
        scene5 = new Scene(layout2, 600, 300);

        // Delete Vertex
        Label label4 = new Label("Delete Vertex");
        TextField deleteVertex = new TextField();
        deleteVertex.setPromptText("Vertex To Be Deleted");
        Button delete=new Button("Delete");
        delete.setOnAction(e->DeleteVertex(deleteVertex));
        Button btnBack3=new Button("Back");
        btnBack3.setOnAction(e->window.setScene(scene1));
        VBox layout3 = new VBox(10);
        layout3.setPadding(new Insets(20, 20, 20, 20));
        layout3.getChildren().addAll(label4,deleteVertex, delete,btnBack3);
        scene6 = new Scene(layout3, 600, 300);

        // Add Edge
        Label label5 = new Label("Add Edge");
        TextField source = new TextField();
        source.setPromptText("Source Vertex");
        TextField dest = new TextField();
        dest.setPromptText("Destination Vertex");
        TextField weight = new TextField();
        weight.setPromptText("Weight");
        Button btnNext6=new Button("Submit");
        btnNext6.setOnAction(e->AddEdge(source,dest,weight));
        Button btnBack4=new Button("Back");
        btnBack4.setOnAction(e->window.setScene(scene1));
        VBox layout4 = new VBox(10);
        layout4.setPadding(new Insets(20, 20, 20, 20));
        layout4.getChildren().addAll(label5,source,dest,weight, btnNext6,btnBack4);
        scene7 = new Scene(layout4, 600, 300);

        //Search Edge
        Label label6 = new Label("Search Edge");
        TextField searchSource = new TextField();
        searchSource.setPromptText("Source Vertex");
        TextField searchDest = new TextField();
        searchDest.setPromptText("Destination Vertex");
        Button searchEdge=new Button("Submit");
        TextField q=new TextField();
        q.setPromptText("Your Edge details will print here");
        searchEdge.setOnAction(e->{
            q.setText("");
            int flag=0;
            int flag1=0;
            for(NodeArray node: graphWeighted.nodes){
                if(node.name.equals(searchSource.getText())){
                    flag=1;
                }
            }for(NodeArray node: graphWeighted.nodes){
                if(node.name.equals(searchDest.getText())){
                    flag1=1;
                }
            }
            if(flag==0 || flag1==0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Vertex Name is not found");
                alert.showAndWait();

            }else{
                for(NodeArray nodes: graphWeighted.nodes) {
                    if(nodes.name.equals(searchSource.getText())){
                        LinkedList<EdgeArray> edges = nodes.edges;
                        for (EdgeArray edge : edges) {
                            if (edge.destination.name.equals(searchDest.getText())) {
                                q.setText("This edge has source,destination,weight as "+ edge.source.name+", " + edge.destination.name+", "+edge.weight);
                            }
                        }
                    }
                }
            }

        });
        Button btnBack6=new Button("Back");
        btnBack6.setOnAction(e->window.setScene(scene1));
        VBox layout5 = new VBox(10);
        layout5.setPadding(new Insets(20, 20, 20, 20));
        layout5.getChildren().addAll(label6,searchSource, searchDest,searchEdge,btnBack6,q);
        scene8 = new Scene(layout5, 600, 300);

        //Modify Edge
        Label label7 = new Label("Modify Edge");
        TextField oldSource = new TextField();
        oldSource.setPromptText("OldSource Vertex Name");
        TextField oldDest = new TextField();
        oldDest.setPromptText("OldDestination Vertex Name");
        TextField newWeight = new TextField();
        newWeight.setPromptText("New Weight");
        Button btnNext9=new Button("Submit");
        btnNext9.setOnAction(e->ModifyEdge(oldSource,oldDest,newWeight));
        Button btnBack9=new Button("Back");
        btnBack9.setOnAction(e->window.setScene(scene1));
        VBox layout9 = new VBox(10);
        layout9.setPadding(new Insets(20, 20, 20, 20));
        layout9.getChildren().addAll(label7,oldSource,oldDest,newWeight,btnNext9,btnBack9);
        scene9 = new Scene(layout9, 600, 300);

        //Delete Edge
        Label label8 = new Label("Delete Edge");
        TextField sourceD = new TextField();
        sourceD.setPromptText("Source Vertex");
        TextField destD = new TextField();
        destD.setPromptText("Destination Vertex");
        Button btnNext10=new Button("Submit");
        btnNext10.setOnAction(e->DeleteEdge(sourceD,destD));
        Button btnBack10=new Button("Back");
        btnBack10.setOnAction(e->window.setScene(scene1));
        VBox layout10 = new VBox(10);
        layout10.setPadding(new Insets(20, 20, 20, 20));
        layout10.getChildren().addAll(label8,sourceD,destD,btnNext10,btnBack10);
        scene10 = new Scene(layout10, 600, 300);

        //Path
        Label label9 = new Label("Search Path Between two vertex");
        TextField start = new TextField();
        start.setPromptText("Source Vertex");
        TextField end = new TextField();
        end.setPromptText("Destination Vertex");
        TextField k=new TextField();
        k.setPromptText("Path will print here");
        Button btnNext11=new Button("Submit");
        btnNext11.setOnAction(e->{
            NodeArray zero = null;
            NodeArray one = null;
            for(NodeArray node: graphWeighted.nodes){
                if(node.name.equals(start.getText())) {
                    zero = node;
                }
            }
            for(NodeArray node: graphWeighted.nodes){
                if(node.name.equals(end.getText())) {
                    one = node;
                }
            }
            String path= graphWeighted.PathFind(zero, one,k);
        });
        Button btnBack11=new Button("Back");
        btnBack11.setOnAction(e->window.setScene(scene1));
        VBox layout11 = new VBox(10);
        layout11.setPadding(new Insets(20, 20, 20, 20));
        layout11.getChildren().addAll(label9,start,end,btnNext11,btnBack11,k);
        scene11 = new Scene(layout11, 600, 300);

        //Go To Interactive Session

        Button GoBack=new Button("Back");

        GoBack.setLayoutX(0);
        GoBack.setLayoutY(0);
        GoBack.setPrefSize(45, 20);
        weightText.setPromptText("Weight");
        weightText.setLayoutX(45);
        weightText.setLayoutY(0);
        PlusButton.setLayoutX(140);
        PlusButton.setLayoutY(0);
        PlusButton.setPrefSize(45, 20);
        CircleButton.setLayoutX(185);
        CircleButton.setLayoutY(0);
        CircleButton.setPrefSize(45, 20);
        CrossButton.setLayoutX(230);
        CrossButton.setLayoutY(0);
        CrossButton.setPrefSize(45, 20);
        SquareButton.setLayoutX(275);
        SquareButton.setLayoutY(0);
        SquareButton.setPrefSize(60, 20);
        TriangleButton.setLayoutX(335);
        TriangleButton.setLayoutY(0);
        TriangleButton.setPrefSize(70, 20);
        removePath.setLayoutX(405);
        removePath.setLayoutY(0);
        removePath.setPrefSize(100, 20);

        removePath.setOnAction(e->{
            for(Line line: lines){
                line.setStroke(Color.BLACK);
            }
            pane.getChildren().removeAll(circle1,group,group1,triangle,rectangle);
        });
        CircleButton.setOnAction(e->{
            circleB=true;
            squareB=false;
            crossB=false;
            triangleB=false;
            plusB=false;

        });
        SquareButton.setOnAction(e->{
            circleB=false;
            squareB=true;
            crossB=false;
            triangleB=false;
            plusB=false;

        });
        CrossButton.setOnAction(e->{
            circleB=false;
            squareB=false;
            crossB=true;
            triangleB=false;
            plusB=false;

        });
        TriangleButton.setOnAction(e->{
            circleB=false;
            squareB=false;
            crossB=false;
            triangleB=true;
            plusB=false;

        });
        PlusButton.setOnAction(e->{
            circleB=false;
            squareB=false;
            crossB=false;
            triangleB=false;
            plusB=true;

        });
        weightText.setPrefSize(100, 10);
        query.setPromptText("Enter Your query");
        GoBack.setOnAction(e->window.setScene(scene1));
        pane.setPrefSize(600,600);
        //pane.getChildren().add(GoBack);
        pane.getChildren().addAll(weightText,GoBack,PlusButton,CircleButton,SquareButton,CrossButton,TriangleButton,removePath);

        EventHandler<MouseEvent> addCircle = new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent event) {
                if(findNode(event.getX(),event.getY())){
                    return;
                }
                else if(event.getY()<100){
                    return;
                }
                Circle circle=new Circle(event.getX(),event.getY(),20,Color.BLUE);
                nodes.add(circle);
                String x=String.valueOf(event.getX());
                String y=String.valueOf(event.getY());
                double n=event.getX()*event.getY();
                int nameInt=(int) n;
                String name3=Integer.toString(nameInt);
                String name=String.valueOf(n);
                Text t=new Text(name3);
                t.setX(event.getX()-10);
                t.setY(event.getY()-25);
                System.out.println(name+ " "+ x + " "+ y);
                Addvertex(name,x,y);
                pane.getChildren().add(circle);
                pane.getChildren().add(t);
                vertexText.put(name,t);
            }
        };
        EventHandler<MouseEvent> mousePressed=new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(findNode(mouseEvent.getX(),mouseEvent.getY())){
                    //System.out.println(mouseEvent.getX()+ " "+mouseEvent.getY());
                    currentLine =new Line();
                    if(findNode(mouseEvent.getX(),mouseEvent.getY())){
                        for(Circle circle: nodes){
                            if(circle.contains(mouseEvent.getX(),mouseEvent.getY())){
                                currentLine.setStartX(circle.getCenterX());
                                currentLine.setStartY(circle.getCenterY());
                            }
                        }
                    }
                }
            }
        };
        EventHandler<MouseEvent> mouseReleased=new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Text t1;
                if(findNode(mouseEvent.getX(),mouseEvent.getY())){
                    if(weightText.getText().trim().isEmpty()){
                        currentLine=null;
                    }else{
                        try{
                            Integer.parseInt(weightText.getText());
                            for(Circle circle: nodes){
                                if(circle.contains(mouseEvent.getX(),mouseEvent.getY())){
                                    currentLine.setEndX(circle.getCenterX());
                                    currentLine.setEndY(circle.getCenterY());
                                    t1=new Text(weightText.getText());
                                    t1.setX((currentLine.getStartX()+currentLine.getEndX())/2 + 5);
                                    t1.setY((currentLine.getStartY()+currentLine.getEndY())/2 + 5);
                                    pane.getChildren().add(t1);
                                    edgeText.put(currentLine,t1);
                                }
                            }
                            double source1=currentLine.getStartX()*currentLine.getStartY();
                            String mainSource=String.valueOf(source1);
                            double dest1=currentLine.getEndX()*currentLine.getEndY();
                            String mainDest=String.valueOf(dest1);
                            pane.getChildren().add(currentLine);
                            lines.add(currentLine);
                            for(NodeArray node: graphWeightedInteractive.nodes){
                                if(node.name.equals(mainSource)){
                                    for(NodeArray node1: graphWeightedInteractive.nodes){
                                        if(node1.name.equals(mainDest)){
                                            graphWeightedInteractive.addEdge(node, node1, Double.parseDouble(weightText.getText()));
                                            graphWeightedInteractive.addEdge(node1, node, Double.parseDouble(weightText.getText()));
                                        }
                                    }
                                }
                            }
                            currentLine=null;
                        }catch(NumberFormatException e){
                            currentLine=null;
                            System.out.println("Exception occurs");
                        }
                    }
                    //System.out.println("mouse released");
                    //System.out.println(mouseEvent.getX()+ " "+mouseEvent.getY());

                }else{
                    currentLine=null;
                }
            }
        };
        EventHandler<MouseEvent> deletevertex=new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //Egde was delete
                for(Line line : lines){
                    if(line.contains(mouseEvent.getX(),mouseEvent.getY())){
                        double l=line.getStartX()*line.getStartY();
                        double m=line.getEndX()*line.getEndY();
                        String lineStart=String.valueOf(l);
                        String lineEnd=String.valueOf(m);
                        for(NodeArray node: graphWeightedInteractive.nodes){
                            if(node.name.equals(lineStart)){
                                for(NodeArray node1: graphWeightedInteractive.nodes){
                                    if(node1.name.equals(lineEnd)){
                                        LinkedList<EdgeArray> edges = node.edges;
                                        LinkedList<EdgeArray> edges1 = node1.edges;
                                        for(EdgeArray edge: edges){
                                            if(edge.destination.name.equals(lineEnd)){
                                                edges.remove(edge);
                                                break;
                                            }
                                        }
                                        for(EdgeArray edge1: edges1){
                                            if(edge1.destination.name.equals(lineStart)){
                                                edges1.remove(edge1);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        pane.getChildren().remove(edgeText.get(line));
                        edgeText.remove(line);
                        pane.getChildren().remove(line);
                    }
                }


                //Circle was delete
                for(Circle circle: nodes){
                    if(circle.contains(mouseEvent.getX(),mouseEvent.getY())){
                        double r=circle.getCenterX()*circle.getCenterY();
                        String y=String.valueOf(r);
                        pane.getChildren().remove(vertexText.get(y));
                        vertexText.remove(y);
                        pane.getChildren().remove(circle);
                        removeLines=new ArrayList<>();
                        for(Line line: lines){
                            if(line.contains(circle.getCenterX(),circle.getCenterY())){
                                pane.getChildren().remove(line);
                                pane.getChildren().remove(edgeText.get(line));
                                edgeText.remove(line);
                                removeLines.add(line);
                                //lines.remove(line);
                                //break;
                            }
                        }
                        for(Line removeline: removeLines){
                            lines.remove(removeline);
                        }
                        double n=circle.getCenterX()*circle.getCenterY();
                        String name=String.valueOf(n);
                        for(NodeArray node: graphWeightedInteractive.nodes){
                            if(node.name.equals(name)){
                                node.edges.clear();
                                graphWeightedInteractive.nodes.remove(node);
                                break;
                            }
                        }
                        for(NodeArray node: graphWeightedInteractive.nodes){
                            LinkedList<EdgeArray> edges = node.edges;
                            for(EdgeArray edge: edges){
                                if(edge.destination.name.equals(name)){
                                    edges.remove(edge);
                                    break;
                                }
                            }
                        }
                        nodes.remove(circle);
                        break;
                    }
                }
            }
        };
        EventHandler<MouseEvent> pathpressed=new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(findNode(mouseEvent.getX(),mouseEvent.getY())){
                    for(Circle circle: nodes){
                        if(circle.contains(mouseEvent.getX(),mouseEvent.getY())){
                            Double ankit=circle.getCenterX()*circle.getCenterY();
                            Abhinav=String.valueOf(ankit);
                        }
                    }

                }
            }
        };

        EventHandler<MouseEvent> pathreleased=new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(findNode(mouseEvent.getX(),mouseEvent.getY())){
                    for(Circle circle: nodes){
                        if(circle.contains(mouseEvent.getX(),mouseEvent.getY())){
                            double raj=circle.getCenterX()*circle.getCenterY();
                            Bansal=String.valueOf(raj);
                            NodeArray zero = null;
                            NodeArray one = null;
                            for(NodeArray node: graphWeightedInteractive.nodes){
                                if(node.name.equals(Abhinav)) {
                                    zero = node;
                                }
                            }
                            for(NodeArray node: graphWeightedInteractive.nodes){
                                if(node.name.equals(Bansal)) {
                                    one = node;
                                }
                            }
                            TextField k=new TextField();
                            String path=graphWeightedInteractive.PathFind(zero, one,k);
                            String[] items = path.split(" ->");
                            List<String> itemList = new ArrayList<String>(Arrays.asList(items));
                            Double[] points;
                            points=new Double[itemList.size()*2];
                            Double xcoI = null,ycoI=null,xcoF=null,ycoF=null,xcoI2=null,ycoI2=null;
                            int xcI,ycI,xcF,ycF;
                            int count1=0;
                            if(itemList.size()==1){
                                return ;
                            }
                            for(int i=0; i<itemList.size()-1; i++){
                                //System.out.println(itemList.get(i));
                                for(NodeArray node: graphWeightedInteractive.nodes){
                                    if(itemList.get(i).equals(node.name)){
                                        //System.out.println(node.name);
                                        String xcoI1=node.x;
                                        xcoI=Double.valueOf(xcoI1);
                                        //xcI=(int) Math.round(xcoI);
                                        String ycoI1=node.y;
                                        ycoI=Double.valueOf(ycoI1);
                                        //ycI=(int)Math.round(ycoI);
                                        // System.out.println(xcoI1+ " "+ycoI1);
                                        points[count1]=xcoI;
                                        count1++;
                                        points[count1]=ycoI;
                                        count1++;
                                        //System.out.println("Hell");
                                        for(NodeArray node1: graphWeightedInteractive.nodes){
                                            if(itemList.get(i+1).equals(node1.name)){
                                                //System.out.println(node.name);
                                                // System.out.println(node1.name);
                                                String xcoF1=node1.x;
                                                System.out.println(itemList.get(i));
                                                xcoF=Double.valueOf(xcoF1);
                                                //xcF=(int) Math.round(xcoF);
                                                String ycoF1=node1.y;
                                                ycoF=Double.valueOf(ycoF1);
                                                // ycF=(int)Math.round(ycoF);
                                                //  System.out.println(xcoF1+ " "+ycoF1);
                                            }
                                        }
                                    }
                                }
                                for(Line line: lines){
                                    System.out.println(xcoI+ " "+ycoI);
                                    System.out.println(xcoF+ " "+ycoF);
                                    //System.out.println(line.getStartX() + " "+line.getStartY()+" "+line.getEndX()+" "+line.getEndY());
                                    if(line.contains(xcoI,ycoI)){
                                        //System.out.println("hello user");
                                        if(line.contains(xcoF,ycoF)){
                                            //System.out.println("hell user");
                                            line.setStroke(Color.RED);
                                            //lines.remove(line);
                                            break;
                                        }
                                    }
                                }
                            }
                            for(NodeArray node: graphWeightedInteractive.nodes){
                                if(itemList.get(itemList.size()-1).equals(node.name)){
                                    String xcoI3=node.x;
                                    xcoI2=Double.valueOf(xcoI3);
                                    //xcI=(int) Math.round(xcoI);
                                    String ycoI3=node.y;
                                    ycoI2=Double.valueOf(ycoI3);
                                    //ycI=(int)Math.round(ycoI);
                                    // System.out.println(xcoI1+ " "+ycoI1);
                                    points[count1]=xcoI2;
                                    count1++;
                                    points[count1]=ycoI2;
                                    count1++;
                                }
                            }
                            polyline = new Polyline();
                            polyline.getPoints().addAll(points);
                            System.out.println(points[0]+ " "+points[1]);
                            //
                            //System.out.println(path);
                            //
                            pathTransition = new PathTransition();
                            pathTransition.setDuration(Duration.millis(10000));
                            pathTransition.setCycleCount(PathTransition.INDEFINITE);
                            if(circleB){
                                circle1=new Circle(points[0],points[1],10, Color.RED);
                                pane.getChildren().add(circle1);
                                pathTransition.setNode(circle1);
                            }else if(crossB){
                                line1=new Line(points[0]-20,points[1]-20,points[0]+20,points[1]+20);
                                line2=new Line(points[0]+20,points[1]-20,points[0]-20,points[1]+20);
                                group=new Group(line1,line2);

                                pane.getChildren().add(group);
                                pathTransition.setNode(group);
                            }else if(triangleB){
                                obj=new Triangle(points[0],points[1]);
                                triangle=obj.getTriangle();
                                pane.getChildren().add(triangle);
                                pathTransition.setNode(triangle);
                            }else if(squareB){
                                rectangle=new Rectangle(points[0]-10,points[1]-10,20,20);
                                pane.getChildren().add(rectangle);
                                pathTransition.setNode(rectangle);
                            }else if(plusB){
                                line4=new Line(points[0],points[1]-20,points[0],points[1]+20);
                                line5=new Line(points[0]-20,points[1],points[0]+20,points[1]);
                                group1=new Group(line4,line5);
                                pane.getChildren().add(group1);
                                pathTransition.setNode(group1);
                            }

                            pathTransition.setPath(polyline);

                            pathTransition.play();
                            Abhinav=null;
                            Bansal=null;
                        }
                    }

                }else Abhinav=null;
            }
        };

        EventHandler<MouseEvent> modifyedge=new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(weightText.getText().trim().isEmpty()){
                    return;
                }
                {
                    try{
                        Integer.parseInt(weightText.getText());
                        for(Line line: lines){
                            if(line.contains(mouseEvent.getX(),mouseEvent.getY())){
                                double i=line.getStartX()*line.getStartY();
                                double j=line.getEndX()*line.getEndY();
                                String name1=String.valueOf(i);
                                String name2=String.valueOf(j);
                                Text t1;
                                t1=new Text(weightText.getText());
                                t1.setX((line.getStartX()+line.getEndX())/2 + 5);
                                t1.setY((line.getStartY()+line.getEndY())/2 + 5);
                                Text tt=edgeText.get(line);
                                pane.getChildren().remove(edgeText.get(line));
                                pane.getChildren().add(t1);
                                edgeText.put(line,t1);
                                for(NodeArray node: graphWeightedInteractive.nodes){
                                    if(node.name.equals(name1)){
                                        LinkedList<EdgeArray> edges = node.edges;
                                        for (EdgeArray edge : edges) {
                                            if (edge.destination.name.equals(name2)) {
                                                edge.weight=Double.parseDouble(weightText.getText());
                                            }
                                        }
                                    }
                                }
                                for(NodeArray node: graphWeightedInteractive.nodes){
                                    if(node.name.equals(name2)){
                                        LinkedList<EdgeArray> edges = node.edges;
                                        for (EdgeArray edge : edges) {
                                            if (edge.destination.name.equals(name1)) {
                                                edge.weight=Double.parseDouble(weightText.getText());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }catch (NumberFormatException e){
                        System.out.println("Weight must be integer");
                    }
                }

            }
        };

        EventHandler<KeyEvent> keyInput=new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().equals( KeyCode.DELETE )){
                    pane.removeEventFilter(MouseEvent.MOUSE_CLICKED,addCircle);
                    pane.removeEventFilter(MouseEvent.MOUSE_PRESSED,mousePressed);
                    pane.removeEventFilter(MouseEvent.MOUSE_RELEASED,mouseReleased);
                    pane.removeEventFilter(MouseEvent.MOUSE_PRESSED,pathpressed);
                    pane.removeEventFilter(MouseEvent.MOUSE_RELEASED,pathreleased);
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED,deletevertex);
                }else if(keyEvent.getCode().equals(KeyCode.A)){
                    System.out.println("A print");
                    pane.removeEventFilter(MouseEvent.MOUSE_CLICKED,deletevertex);
                    pane.removeEventFilter(MouseEvent.MOUSE_PRESSED,pathpressed);
                    pane.removeEventFilter(MouseEvent.MOUSE_RELEASED,pathreleased);
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED,addCircle);
                    pane.addEventFilter(MouseEvent.MOUSE_PRESSED,mousePressed);
                    pane.addEventFilter(MouseEvent.MOUSE_RELEASED,mouseReleased);
                }else if(keyEvent.getCode().equals(KeyCode.P)){
                    System.out.println("P entered");
                    pane.removeEventFilter(MouseEvent.MOUSE_CLICKED,addCircle);
                    pane.removeEventFilter(MouseEvent.MOUSE_PRESSED,mousePressed);
                    pane.removeEventFilter(MouseEvent.MOUSE_RELEASED,mouseReleased);
                    pane.removeEventFilter(MouseEvent.MOUSE_CLICKED,deletevertex);
                    pane.addEventFilter(MouseEvent.MOUSE_PRESSED,pathpressed);
                    pane.addEventFilter(MouseEvent.MOUSE_RELEASED,pathreleased);
                }else if(keyEvent.getCode().equals(KeyCode.M)){
                    System.out.println("M entered");
                    pane.removeEventFilter(MouseEvent.MOUSE_CLICKED,addCircle);
                    pane.removeEventFilter(MouseEvent.MOUSE_PRESSED,mousePressed);
                    pane.removeEventFilter(MouseEvent.MOUSE_RELEASED,mouseReleased);
                    pane.removeEventFilter(MouseEvent.MOUSE_CLICKED,deletevertex);
                    pane.removeEventFilter(MouseEvent.MOUSE_PRESSED,pathpressed);
                    pane.removeEventFilter(MouseEvent.MOUSE_RELEASED,pathreleased);
                    pane.addEventFilter(MouseEvent.MOUSE_CLICKED,modifyedge);
                }
            }
        };

        pane.addEventFilter(MouseEvent.MOUSE_CLICKED,addCircle);
        pane.addEventFilter(MouseEvent.MOUSE_PRESSED,mousePressed);
        pane.addEventFilter(MouseEvent.MOUSE_RELEASED,mouseReleased);
        //pane.addEventFilter(KeyEvent.KEY_PRESSED,keyInput);

        scene12=new Scene(pane,600,600);
        scene12.addEventFilter(KeyEvent.KEY_PRESSED,keyInput);

        //Main Scene1
        VBox root =new VBox();
        btn1.setLayoutX(0);
        btn1.setLayoutY(0);
        btn1.setPrefSize(100, 100);
        root.getChildren().add(btn1);
        btn2.setLayoutX(100);
        btn2.setLayoutY(0);
        btn2.setPrefSize(100, 100);
        root.getChildren().add(btn2);
        btn3.setLayoutX(200);
        btn3.setLayoutY(0);
        btn3.setPrefSize(100, 100);
        root.getChildren().add(btn3);
        btn4.setLayoutX(0);
        btn4.setLayoutY(100);
        btn4.setPrefSize(100, 100);
        root.getChildren().add(btn4);
        btn5.setLayoutX(100);
        btn5.setLayoutY(100);
        btn5.setPrefSize(100, 100);
        root.getChildren().add(btn5);
        btn6.setLayoutX(200);
        btn6.setLayoutY(100);
        btn6.setPrefSize(100, 100);
        root.getChildren().add(btn6);
        btn7.setLayoutX(0);
        btn7.setLayoutY(200);
        btn7.setPrefSize(100, 100);
        root.getChildren().add(btn7);
        btn8.setLayoutX(100);
        btn8.setLayoutY(200);
        btn8.setPrefSize(100, 100);
        root.getChildren().add(btn8);
        btn9.setLayoutX(200);
        btn9.setLayoutY(200);
        btn9.setPrefSize(100, 100);
        root.getChildren().add(btn9);
        btn10.setPrefSize(150, 100);
        root.getChildren().add(btn10);
        btn11.setPrefSize(150, 100);
        root.getChildren().add(btn11);
        btn12.setPrefSize(150, 100);
        root.getChildren().add(btn12);
        scene1=new Scene(root, 480, 480);
        primaryStage.setScene(scene1);
        primaryStage.show();
    }

    public boolean findNode(double x, double y){
        for(Circle circle : nodes){
            if(circle.contains(x,y)){
                return true;
            }
        }
        return false;
    }


    public void Addvertex(String name,String x,String y){
        NodeArray zero = new NodeArray(count, name,x,y);
        //System.out.println("New Vertex " + vertexInput.getText()+ " is added");
        graphWeightedInteractive.AddNode(zero);
    }


    class SortByName implements Comparator<NodeArray> {
        @Override
        public int compare(NodeArray a, NodeArray b){
            return a.name.compareTo(b.name);
        }
    }
    class SortByClass implements Comparator<EdgeArray>{
        public int compare(EdgeArray a, EdgeArray b){
            if(a.source.name.equals(b.source.name)){
                return a.destination.name.compareTo(b.destination.name);
//                if(b.cost-a.cost>0.0001) return -1;
//                else if(Math.abs(a.cost-b.cost)<0.0001) return a.edgeName.compareTo(b.edgeName);
//                else{
//                    return 1;
//                }
            }else
                return a.source.name.compareTo(b.source.name);
        }
    }


    public void AddVertex(TextField vertexInput,TextField xInput,TextField yInput){
        String input1=xInput.getText();
        String input2=yInput.getText();
        try
        {
            Integer.parseInt(input1);
            Integer.parseInt(input2);
            if(vertexInput.getText().trim().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Vertex Name is empty");
                alert.showAndWait();
            }else{
                NodeArray zero = new NodeArray(count, vertexInput.getText(),xInput.getText(),yInput.getText());
                //System.out.println("New Vertex " + vertexInput.getText()+ " is added");
                graphWeighted.AddNode(zero);
                //graphWeighted.printNode();
            }

        }
        catch (NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("coordinates must not be empty");
            alert.showAndWait();
        }
    }

    public void ModifyVertex(TextField vertexInput,TextField modifyVertex,TextField xInput,TextField yInput){
        String input1=xInput.getText();
        String input2=yInput.getText();
        int flag=0;
        NodeArray zero = new NodeArray(count, modifyVertex.getText(),xInput.getText(),yInput.getText());
        for(NodeArray node: graphWeighted.nodes){
            if(node.name.equals(vertexInput.getText())){
                flag=1;
            }
        }
        if (flag == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Given Vertex Not Found");
            alert.showAndWait();
        }else{
            try
            {
                Integer.parseInt(input1);
                Integer.parseInt(input2);
                if(modifyVertex.getText().trim().isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("Vertex Name is empty");
                    alert.showAndWait();
                }else{
                    for(NodeArray node: graphWeighted.nodes){
                        if(node.name.equals(vertexInput.getText())){
                            graphWeighted.nodes.set( graphWeighted.nodes.indexOf(node) ,zero);
                        }
                    }
                }
            }
            catch (NumberFormatException e)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("coordinates must not be empty");
                alert.showAndWait();
            }
        }
    }

    public void DeleteVertex(TextField vertex){
        int flag=0;
        if(vertex.getText().trim().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Vertex Name is empty");
            alert.showAndWait();
        }else{
            for(NodeArray node: graphWeighted.nodes){
                if(node.name.equals(vertex.getText())){
                    flag=1;
                }
            }
            if (flag == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Vertex Name not found");
                alert.showAndWait();
            }else{
                for(NodeArray node: graphWeighted.nodes){
                    if(node.name.equals(vertex.getText())){
                        node.edges.clear();
                        graphWeighted.nodes.remove(node);
                        break;
                    }
                }
                for(NodeArray node: graphWeighted.nodes){
                    LinkedList<EdgeArray> edges = node.edges;
                    for(EdgeArray edge: edges){
                        if(edge.destination.name.equals(vertex.getText())){
                            edges.remove(edge);
                            break;
                        }
                    }
                }
//                ArrayList<EdgeArray> removeedge=new ArrayList<>();
//                for(EdgeArray edge: containEdge){
//                    if(edge.source.name.equals(vertex.getText())){
//                        removeedge.add(edge);
//                    }else if(edge.destination.name.equals(vertex.getText())){
//                        removeedge.add(edge);
//                    }
//                }
//                for(EdgeArray remove: removeedge){
//                    containEdge.remove(remove);
//                }
            }
        }
    }

    public void AddEdge(TextField source, TextField dest,TextField weight){
        int flag=0;
        int flag1=0;
        for(NodeArray node: graphWeighted.nodes){
            if(node.name.equals(source.getText())){
                flag=1;
                // System.out.println(zero.name);
            }
        }
        for(NodeArray node: graphWeighted.nodes){
            if(node.name.equals(dest.getText())){
                flag1=1;
                // System.out.println(zero.name);
            }
        }
        if(flag==0 || flag1==0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Vertex Name not found");
            alert.showAndWait();
        }else{
            String input1=weight.getText();
            try
            {
                Integer.parseInt(input1);
                for(NodeArray node: graphWeighted.nodes){
                    if(node.name.equals(source.getText())){
                        for(NodeArray node1: graphWeighted.nodes){
                            if(node1.name.equals(dest.getText())){
                                graphWeighted.addEdge(node, node1, Double.parseDouble(weight.getText()));
                                //containEdge.add(new EdgeArray(node,node1,Double.parseDouble(weight.getText())));
                                //containEdge.add(new EdgeArray(node1,node,Double.parseDouble(weight.getText())));
                                graphWeighted.addEdge(node1, node, Double.parseDouble(weight.getText()));
                            }
                        }
                    }
                }
            }
            catch (NumberFormatException e)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Weight should be in integer format");
                alert.showAndWait();
            }
        }
    }

    public void ModifyEdge(TextField oldS, TextField oldD,TextField newW){
        int flag=0;
        for(NodeArray node: graphWeighted.nodes){
            if(node.name.equals(oldS.getText())){
                LinkedList<EdgeArray> edges = node.edges;
                for (EdgeArray edge : edges) {
                    if (edge.destination.name.equals(oldD.getText())) {
                        flag=1;
                        // edge.weight=Double.parseDouble(newW.getText());
                    }
                }
            }
        }
        int flag1=0;
        for(NodeArray node: graphWeighted.nodes){
            if(node.name.equals(oldD.getText())){
                LinkedList<EdgeArray> edges = node.edges;
                for (EdgeArray edge : edges) {
                    if (edge.destination.name.equals(oldS.getText())) {
                        // edge.weight=Double.parseDouble(newW.getText());
                        flag1=1;
                    }
                }
            }
        }
        if(flag==0 || flag1==0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Vertex Name not found");
            alert.showAndWait();
        }else{
            String input1=newW.getText();
            try
            {
                Integer.parseInt(input1);
//                for(EdgeArray edge: containEdge){
//                    if(edge.source.name.equals(oldD.getText())){
//                        edge.weight=Double.parseDouble(newW.getText());
//                    }
//                }
//                for(EdgeArray edge: containEdge){
//                    if(edge.destination.name.equals(oldS.getText())){
//                        edge.weight=Double.parseDouble(newW.getText());
//                    }
//                }
//                for(EdgeArray edge: containEdge){
//                    if(edge.destination.name.equals(oldS.getText()) && edge.source.name.equals(oldD.getText())){
//                        edge.weight=Double.parseDouble(newW.getText());
//                    }
//                }
//                for(EdgeArray edge: containEdge){
//                    if(edge.source.name.equals(oldS.getText()) && edge.destination.name.equals(oldD.getText())){
//                        edge.weight=Double.parseDouble(newW.getText());
//                    }
//                }

                for(NodeArray node: graphWeighted.nodes){
                    if(node.name.equals(oldS.getText())){
                        LinkedList<EdgeArray> edges = node.edges;
                        for (EdgeArray edge : edges) {
                            if (edge.destination.name.equals(oldD.getText())) {
                                edge.weight=Double.parseDouble(newW.getText());
                            }
                        }
                    }
                }
                for(NodeArray node: graphWeighted.nodes){
                    if(node.name.equals(oldD.getText())){
                        LinkedList<EdgeArray> edges = node.edges;
                        for (EdgeArray edge : edges) {
                            if (edge.destination.name.equals(oldS.getText())) {
                                edge.weight=Double.parseDouble(newW.getText());
                            }
                        }
                    }
                }
            }
            catch (NumberFormatException e)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Weight should be in integer format");
                alert.showAndWait();
            }
        }
    }

    public void DeleteEdge(TextField source,TextField dest){
        int flag=0;
        int flag1=0;
        for(NodeArray node: graphWeighted.nodes){
            if(node.name.equals(source.getText())){
                LinkedList<EdgeArray> edges=node.edges;
                for(EdgeArray edge: edges){
                    if(edge.destination.name.equals(dest.getText())){
                        //edges.remove(edge);
                        flag=1;
                    }
                }
            }
        }
        for(NodeArray node: graphWeighted.nodes){
            if(node.name.equals(dest.getText())){
                LinkedList<EdgeArray> edges=node.edges;
                for(EdgeArray edge: edges){
                    if(edge.destination.name.equals(source.getText())){
                        //edges.remove(edge);
                        flag1=1;
                    }
                }
            }
        }
        if(flag==0 || flag1==0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Vertex Name not found");
            alert.showAndWait();
        }else{
            for(NodeArray node: graphWeighted.nodes){
                if(node.name.equals(source.getText())){
                    LinkedList<EdgeArray> edges=node.edges;
                    for(EdgeArray edge: edges){
                        if(edge.destination.name.equals(dest.getText())){
                            edges.remove(edge);
                        }
                    }
                }
            }
            for(NodeArray node: graphWeighted.nodes){
                if(node.name.equals(dest.getText())){
                    LinkedList<EdgeArray> edges=node.edges;
                    for(EdgeArray edge: edges){
                        if(edge.destination.name.equals(source.getText())){
                            edges.remove(edge);
                        }
                    }
                }
            }
//            ArrayList<EdgeArray> removeEdges=new ArrayList<>();
//            for(EdgeArray edge: containEdge){
//                if(edge.destination.name.equals(source.getText())){
//                    //removeEdges.add(edge);
//                    containEdge.remove(edge);
//                    break;
//                }
//            }
//            for(EdgeArray edge: containEdge){
//                if(edge.destination.name.equals(dest.getText())){
//                    removeEdges.add(edge);
//                    containEdge.remove(edge);
//                    break;
//                }
//            }

        }
    }

    public static void main(String[] args)
    {
        // launch the application
        launch(args);
    }
}



