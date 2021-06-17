/*
Java AT2.5
*/
package draganddropfinal;

import java.util.ArrayList;
import java.util.List;
 
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DragAndDropFinal extends Application {

    // Create the ListViews
    ListView<Task> sourceView = new ListView<>();
    ListView<Task> targetView = new ListView<>();
 
    // Create the LoggingArea
    //TextArea loggingArea = new TextArea("");
 
    // Set the Custom Data Format
    static final DataFormat TASK_LIST = new DataFormat("TaskList");
    
    @Override
    public void start(Stage window)
    {
        Label sourceListLbl = new Label("Tasks List: ");
        Label targetListLbl = new Label("To Do List: ");
        Label messageLbl = new Label("Select one or more tasks from either list, drag and drop them to the other list");
 
        // Set the Size of the Views
        sourceView.setPrefSize(200, 200);
        targetView.setPrefSize(200, 200);
        
        // Add the tasks to the Source List
        sourceView.getItems().addAll(this.getTasksList());
        
        // Allow multiple-selection in lists
        sourceView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        targetView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
 
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
 
        // Add the Labels and Views to the Pane
        pane.add(messageLbl, 0, 0, 3, 1);
        pane.addRow(1, sourceListLbl, targetListLbl);
        pane.addRow(2, sourceView, targetView);
        
        // Add mouse event handlers for the source
        sourceView.setOnDragDetected(new EventHandler <MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                dragDetected(event, sourceView);
            }
        });
        
        sourceView.setOnDragOver(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                dragOver(event, sourceView);
            }
        });
        
        sourceView.setOnDragDropped(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                dragDropped(event, sourceView);
            }
        });
 
        sourceView.setOnDragDone(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                dragDone(event, sourceView);
            }
        });
        
        // Add mouse event handlers for the target
        targetView.setOnDragDetected(new EventHandler <MouseEvent>()
        {
            public void handle(MouseEvent event)
            {
                dragDetected(event, targetView);
            }
        });
 
        targetView.setOnDragOver(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                dragOver(event, targetView);
            }
        });
 
        targetView.setOnDragDropped(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                dragDropped(event, targetView);
            }
        });
 
        targetView.setOnDragDone(new EventHandler <DragEvent>()
        {
            public void handle(DragEvent event)
            {
                dragDone(event, targetView);
            }
        });
    
        VBox root = new VBox();
        // Add the Pane to the VBox
        root.getChildren().addAll(pane);
        // Set the Style of the VBox
        root.setStyle("-fx-padding: 10;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 2;" +
            "-fx-border-insets: 5;" +
            "-fx-border-radius: 5;" +
            "-fx-border-color: red;");
 
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.setTitle("To Do List");
        window.show();
    }
    
    // Create the Task List
    private ObservableList<Task> getTasksList()
    {
        ObservableList<Task> list = FXCollections.<Task>observableArrayList();
 
        Task java = new Task("Submit Java");
        Task cSharp = new Task("Submit C#");
        Task rad = new Task("Submit RAD");
        Task webProg = new Task("Submit Web Program");
        Task softDeploy = new Task("Submit Software Deploy");
 
        list.addAll(java, cSharp, rad, webProg, softDeploy);
 
        return list;
    }
    
    private void dragDetected(MouseEvent event, ListView<Task> listView)
    {
        // Make sure at least one item is selected
        int selectedCount = listView.getSelectionModel().getSelectedIndices().size();
 
        if (selectedCount == 0)
        {
            event.consume();
            return;
        }
 
        // Initiate a drag-and-drop gesture
        Dragboard dragboard = listView.startDragAndDrop(TransferMode.COPY_OR_MOVE);
 
        // Put the the selected items to the dragboard
        ArrayList<Task> selectedItems = this.getSelectedTasks(listView);
 
        ClipboardContent content = new ClipboardContent();
        content.put(TASK_LIST, selectedItems);
 
        dragboard.setContent(content);
        event.consume();
    }
    
    private void dragOver(DragEvent event, ListView<Task> listView)
    {
        // If drag board has an ITEM_LIST and it is not being dragged
        // over itself, we accept the MOVE transfer mode
        Dragboard dragboard = event.getDragboard();
 
        if (event.getGestureSource() != listView && dragboard.hasContent(TASK_LIST))
        {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
        }
 
        event.consume();
    }
    
    @SuppressWarnings("unchecked")
    private void dragDropped(DragEvent event, ListView<Task> listView)
    {
        boolean dragCompleted = false;
 
        // Transfer the data to the target
        Dragboard dragboard = event.getDragboard();
 
        if(dragboard.hasContent(TASK_LIST))
        {
            ArrayList<Task> list = (ArrayList<Task>)dragboard.getContent(TASK_LIST);
            listView.getItems().addAll(list);
            // Data transfer is successful
            dragCompleted = true;
        }
 
        // Data transfer is not successful
        event.setDropCompleted(dragCompleted);
        event.consume();
    }
    
    private void dragDone(DragEvent event, ListView<Task> listView)
    {
        // Check how data was transfered to the target
        // If it was moved, clear the selected items
        TransferMode tm = event.getTransferMode();
 
        if (tm == TransferMode.MOVE)
        {
            removeSelectedTasks(listView);
        }
 
        event.consume();
    }
    
    private ArrayList<Task> getSelectedTasks(ListView<Task> listView)
    {
        // Return the list of selected Task in an ArratyList, so it is
        // serializable and can be stored in a Dragboard.
        ArrayList<Task> list = new ArrayList<>(listView.getSelectionModel().getSelectedItems());
 
        return list;
    }
 
    private void removeSelectedTasks(ListView<Task> listView)
    {
        // Get all selected Tasks in a separate list to avoid the shared list issue
        List<Task> selectedList = new ArrayList<>();
 
        for(Task task : listView.getSelectionModel().getSelectedItems())
        {
            selectedList.add(task);
        }
 
        // Clear the selection
        listView.getSelectionModel().clearSelection();
        // Remove items from the selected list
        listView.getItems().removeAll(selectedList);
    }
    
    public static void main(String[] args) {
        launch(args); // Application.
    }
    
}
