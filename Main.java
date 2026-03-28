import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private boolean isDarkMode = false;
    private VBox chatArea;
    private VBox mainLayout;
    private ScrollPane scrollPane;
    private TextField inputField;

    // Create an instance of your separate logic class
    private ChatLogic botLogic = new ChatLogic();

    @Override
    public void start(Stage stage) {
        // --- 1. CHAT DISPLAY AREA ---
        chatArea = new VBox(15);
        chatArea.setPadding(new Insets(20));

        scrollPane = new ScrollPane(chatArea);
        scrollPane.setFitToWidth(true);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // --- 2. INPUT AREA ---
        inputField = new TextField();
        inputField.setPromptText("Type a message...");
        inputField.setPrefHeight(45);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        Button sendButton = new Button("➤");
        sendButton.setPrefSize(45, 45);
        sendButton.setCursor(javafx.scene.Cursor.HAND);

        // --- 3. TOP BAR (Header) ---
        Label title = new Label("ChatBotFX Pro");
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        ToggleButton themeBtn = new ToggleButton("🌙 Dark Mode");
        themeBtn.setCursor(javafx.scene.Cursor.HAND);
        themeBtn.setOnAction(e -> toggleTheme(themeBtn));

        // --- Clear Chat Button ---
        Button clearBtn = new Button("🗑 Clear");
        clearBtn.setCursor(javafx.scene.Cursor.HAND);
        clearBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #888; -fx-font-size: 13;");

        clearBtn.setOnAction(e -> {
            chatArea.getChildren().clear();

            PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
            delay.setOnFinished(event -> {
                addMessage("Chat cleared! How can I help you now?", false);
            });
            delay.play();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Define topBar only ONCE and include all elements
        HBox topBar = new HBox(15, title, spacer, clearBtn, themeBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 15, 10, 15));

        HBox inputLayout = new HBox(10, inputField, sendButton);
        inputLayout.setPadding(new Insets(15));
        inputLayout.setAlignment(Pos.CENTER);

        // --- 4. EVENT HANDLING ---
        sendButton.setOnAction(e -> handleSend());
        inputField.setOnAction(e -> handleSend());

        // --- 5. SETUP SCENE ---
        mainLayout = new VBox(topBar, scrollPane, inputLayout);
        applyLightTheme();

        Scene scene = new Scene(mainLayout, 450, 650);
        stage.setTitle("ChatBotFX - Placement Edition");
        stage.setScene(scene);
        stage.show();

        // Initial greeting
        addMessage("Hello! I'm your AI assistant. How can I help you today?", false);
    }

    private void handleSend() {
        String text = inputField.getText().trim();

        if (!text.isEmpty()) {
            inputField.clear();
            inputField.requestFocus();

            // 1. Show User Message
            addMessage(text, true);

            String response = botLogic.getResponse(text);

            // 2. Typing Indicator with Dynamic Color
            String typingColor = isDarkMode ? "white" : "gray";
            Label typingLabel = new Label("Bot is typing...");
            typingLabel.setStyle("-fx-text-fill: " + typingColor + "; -fx-padding: 5; -fx-font-style: italic;");

            HBox typingBox = new HBox(typingLabel);
            typingBox.setAlignment(Pos.CENTER_LEFT);
            chatArea.getChildren().add(typingBox);

            // 3. Delay and Show Response
            PauseTransition delay = new PauseTransition(Duration.seconds(1.2));
            delay.setOnFinished(event -> {
                chatArea.getChildren().remove(typingBox);
                addMessage(response, false);
                Platform.runLater(() -> scrollPane.setVvalue(1.0));
            });
            delay.play();
        }
    }
    private void addMessage(String text, boolean isUser) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(280);

        if (isUser) {
            label.setStyle("-fx-background-color: #0084FF; -fx-text-fill: white; -fx-padding: 12 16; -fx-background-radius: 18 18 2 18; -fx-font-size: 14;");
        } else {
            String bg = isDarkMode ? "#3A3B3C" : "#E9E9EB";
            String tx = isDarkMode ? "white" : "black";
            label.setStyle("-fx-background-color: "+bg+"; -fx-text-fill: "+tx+"; -fx-padding: 12 16; -fx-background-radius: 18 18 18 2; -fx-font-size: 14;");
        }

        HBox container = new HBox(label);
        container.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
        chatArea.getChildren().add(container);
    }

    private void toggleTheme(ToggleButton btn) {
        isDarkMode = !isDarkMode;
        if (isDarkMode) applyDarkTheme(); else applyLightTheme();
        btn.setText(isDarkMode ? "☀️ Light Mode" : "🌙 Dark Mode");
    }

    private void applyLightTheme() {
        mainLayout.setStyle("-fx-background-color: #F0F2F5; -fx-border-color: #D1D9E0; -fx-border-width: 1;");
        chatArea.setStyle("-fx-background-color: #FFFFFF;");
        scrollPane.setStyle("-fx-background: #FFFFFF; -fx-background-color: transparent;");
        inputField.setStyle("-fx-background-radius: 25; -fx-border-color: #CCD0D5; -fx-background-color: white;");
    }

    private void applyDarkTheme() {
        mainLayout.setStyle("-fx-background-color: #18191A; -fx-border-color: #3E4042; -fx-border-width: 1;");
        chatArea.setStyle("-fx-background-color: #242526;");
        scrollPane.setStyle("-fx-background: #242526; -fx-background-color: transparent;");
        inputField.setStyle("-fx-background-radius: 25; -fx-border-color: #3E4042; -fx-background-color: #3A3B3C; -fx-text-fill: white;");
    }

    public static void main(String[] args) { launch(args); }
}