package com.jonasolsson.todolist;

import java.time.LocalDate;

import com.jonasolsson.datamodel.ToDoData;
import com.jonasolsson.datamodel.ToDoItem;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DialogController {
	@FXML
	private TextField shortTextField;
	
	@FXML
	private TextArea descField;
	
	@FXML
	private DatePicker dueDatePicker;
	
	public ToDoItem processResult() {
		String shortText = shortTextField.getText().trim();
		String desc = descField.getText().trim();
		LocalDate dueDate = dueDatePicker.getValue();
		
		ToDoItem temp = new ToDoItem(shortText,desc,dueDate);
		ToDoData.getInstance().addToDoItem(temp);
		return temp;
	}
}
