package com.jonasolsson.todolist;


import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import com.jonasolsson.datamodel.ToDoData;
import com.jonasolsson.datamodel.ToDoItem;
import com.sun.javafx.collections.SortableList;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class Controller {
	
	@FXML
	private ListView<ToDoItem> todoListView;
	
	@FXML
	private TextArea detailTextArea;
	
	@FXML
	private Label deadLineLabel;
	
	@FXML
	private BorderPane mainBorderPane;
	
	@FXML
	private ContextMenu listContextMenu;
	
	@FXML
	private ToggleButton filterToggleButton;
	
	private FilteredList<ToDoItem> filterdList;
	
	private Predicate<ToDoItem> allItem;
	
	private Predicate<ToDoItem> toDayItem;
	
	public void initialize() {
		
		listContextMenu = new ContextMenu();
		MenuItem deleteMenuItem = new MenuItem("Delete");
		deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
				deleteItem(item);
				
			}

		});
		listContextMenu.getItems().addAll(deleteMenuItem);
		
		todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
			@Override
			public void changed(ObservableValue<? extends ToDoItem> observable, ToDoItem oldValue, ToDoItem newValue) {
				if(newValue != null) {
					ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
					
					detailTextArea.setText(item.getDetails());
					DateTimeFormatter df = DateTimeFormatter.ofPattern("d MMMM yyyy");
					deadLineLabel.setText(df.format(item.getDueDate()));
				}
				
			}
		});
		
		allItem = new Predicate<ToDoItem>() {
			@Override
			public boolean test(ToDoItem t) {
				return true;
			}
		};
		toDayItem = new Predicate<ToDoItem>() {
			@Override
			public boolean test(ToDoItem t) {
				return (t.getDueDate().equals(LocalDate.now()));
			}
		};
		filterdList = new FilteredList<ToDoItem>
		((ObservableList<ToDoItem>) ToDoData.getInstance().getToDoItems(),allItem);
		SortedList<ToDoItem> sortedList = new SortedList<ToDoItem>(filterdList,
				new Comparator<ToDoItem>() {
					@Override
					public int compare(ToDoItem o1, ToDoItem o2) {
						return o1.getDueDate().compareTo(o2.getDueDate());
					}
		});
				
		
		//todoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());
		todoListView.setItems(sortedList);
		
		todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		todoListView.getSelectionModel().selectFirst();
		
		todoListView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
			
			@Override
			public ListCell<ToDoItem> call(ListView<ToDoItem> param) {
				ListCell<ToDoItem> cell = new ListCell<ToDoItem>() {

					@Override
					protected void updateItem(ToDoItem item, boolean empty) {
						super.updateItem(item, empty);
						if(empty) {
							setText(null);
						}else {
							setText(item.getShortDesc());
							if(item.getDueDate().isBefore(LocalDate.now().plusDays(1))) {
								setTextFill(Color.RED);
							}else if(item.getDueDate().equals(LocalDate.now().plusDays(1))){
								setTextFill(Color.ORANGE);
							}
						}
					}
					
				};
				
				cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) ->	{
							if(isNowEmpty) {
								cell.setContextMenu(null);
							}else {
								cell.setContextMenu(listContextMenu);
							}
						}	
				);
				return cell;
			}
		});
	}
	public void showItems() {
		System.out.println("SHHEEE");
	}
	@FXML
	public void showNewItemDialog() {
		Dialog<ButtonType> dialog = new Dialog<>();
		dialog.initOwner(mainBorderPane.getScene().getWindow());
		dialog.setTitle("Add It");
		dialog.setHeaderText("Sim Salla Bim");
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("DialogBox.fxml"));
		
		try {
			dialog.getDialogPane().setContent(fxmlLoader.load());
			
		} catch (IOException e) {
			System.out.println("Nope" + e);
			e.printStackTrace();
			return;
		}
			dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
			dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
			
			Optional<ButtonType> result = dialog.showAndWait();
			
			if(result.isPresent() && result.get() == ButtonType.OK) {
				DialogController controller = fxmlLoader.getController();
				ToDoItem temp = controller.processResult();
				todoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());
				todoListView.getSelectionModel().select(temp);
			}else {
				System.out.println("Nooo");
			}
	}
	
	@FXML
	public void onKeyPressed(KeyEvent keyEvent) {
		ToDoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
		if(selectedItem != null) {
			if(keyEvent.getCode().equals(KeyCode.DELETE)) {
				deleteItem(selectedItem);
			}
		}
	}
	
	@FXML
	public void handleClickListView() {
		ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
		deadLineLabel.setText(item.getDueDate().toString());
		detailTextArea.setText(item.getDetails());
	}
	private void deleteItem(ToDoItem item) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("DElete Item");
			alert.setHeaderText("Delete this: "+ item.getShortDesc());
			alert.setContentText("YES OR NO ?");
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.isPresent() && (result.get() == ButtonType.OK)) {
			ToDoData.getInstance().deleteToDoItem(item);
		}
	}
	@FXML
	public  void handleFilterButton() {
		ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
		if(filterToggleButton.isSelected()) {
			filterdList.setPredicate(toDayItem);
			if(filterdList.isEmpty()) {
				detailTextArea.clear();
				deadLineLabel.setText("");
			}else if(filterdList.contains(item)){
				todoListView.getSelectionModel().select(item);
			}else {
				todoListView.getSelectionModel().selectFirst();
			}
		}else {
			filterdList.setPredicate(allItem);
			todoListView.getSelectionModel().select(item);
		}
	}
	@FXML
	public void handelExit() {
		Platform.exit();
	}
}
