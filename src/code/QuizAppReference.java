import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class QuizAppReference extends Application
{

    private List<String[]> questionsAndAnswers;
    private int currentQuestionIndex;
    private int score;
    private Text questionLabel;
    private TextField answerField;
    private Button submitButton;
    private Text scoreLabel;
    private Text feedbackLabel;
    private Button startButton;

    public static void main(final String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize UI components
        questionLabel = new Text("Press 'Start Quiz' to begin.");
        answerField   = new TextField();
        answerField.setPromptText("Enter your answer here...");
        submitButton  = new Button("Submit");
        scoreLabel    = new Text("Score: 0");
        feedbackLabel = new Text();
        startButton   = new Button("Start Quiz");

        // Setup layout
        VBox vbox   = new VBox(10, questionLabel, answerField, submitButton, scoreLabel, feedbackLabel, startButton);
        Scene scene = new Scene(vbox, 400, 300);

        // Load questions from the file
        loadQuestions("resources/quiz.txt");

        // Disable input fields until the quiz starts
        answerField.setDisable(true);
        submitButton.setDisable(true);

        // Event handlers
        startButton.setOnAction(e -> startQuiz());
        submitButton.setOnAction(e -> handleAnswer());
        answerField.setOnKeyPressed(e ->
        {
            if (e.getCode() == KeyCode.ENTER)
            {
                handleAnswer();
            }
        });

        // Setup stage
        primaryStage.setTitle("JavaFX Quiz App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadQuestions(String filePath)
    {
        questionsAndAnswers = new ArrayList<>();
        try
        {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines)
            {
                String[] parts = line.split("\\|");
                if (parts.length == 2)
                {
                    questionsAndAnswers.add(parts);
                }
            }
            Collections.shuffle(questionsAndAnswers); // Shuffle questions for randomness
        }
        catch (Exception e)
        {
            feedbackLabel.setText("Failed to load questions.");
        }
    }

    private void startQuiz()
    {
        score = 0;
        currentQuestionIndex = 0;
        scoreLabel.setText("Score: " + score);
        feedbackLabel.setText("");
        Collections.shuffle(questionsAndAnswers); // Shuffle questions for a fresh start
        startButton.setDisable(true);
        answerField.setDisable(false);
        submitButton.setDisable(false);
        nextQuestion();
    }

    private void nextQuestion()
    {
        if (currentQuestionIndex < 10 && currentQuestionIndex < questionsAndAnswers.size())
        {
            String[] qa = questionsAndAnswers.get(currentQuestionIndex);
            questionLabel.setText(qa[0]); // Display the question
            answerField.clear();
            currentQuestionIndex++;
        }
        else
        {
            endQuiz();
        }
    }

    private void handleAnswer()
    {
        String userAnswer = answerField.getText().trim().toLowerCase();
        String correctAnswer = questionsAndAnswers.get(currentQuestionIndex - 1)[1].toLowerCase();

        if (userAnswer.equals(correctAnswer))
        {
            score++;
            feedbackLabel.setText("Correct!");
        }
        else
        {
            feedbackLabel.setText("Wrong! The correct answer was: " + correctAnswer);
        }

        scoreLabel.setText("Score: " + score);
        nextQuestion();
    }

    private void endQuiz()
    {
        questionLabel.setText("Quiz Over! Your final score: " + score + "/10");
        answerField.setDisable(true);
        submitButton.setDisable(true);
        startButton.setDisable(false);
        feedbackLabel.setText("Press 'Start Quiz' to play again.");
    }
}
