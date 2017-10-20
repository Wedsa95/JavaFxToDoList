package com.jonasolsson.todolist;

import java.io.IOException;

import com.jonasolsson.datamodel.ToDoData;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("Todolist.fxml"));
		primaryStage.setTitle("To Do List");
		primaryStage.setScene(new Scene(root,900,500));
		primaryStage.show();
	}
	
	@Override
	public void stop() throws Exception {
		try {
			ToDoData.getInstance().storeToDoItems();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	@Override
	public void init() throws Exception {
		try {
			ToDoData.getInstance().loadToDoItems();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}
	
	
}
