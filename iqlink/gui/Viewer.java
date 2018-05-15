package game.iqlink.gui;

import game.iqlink.LinkGame;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


/**
 * A very simple viewer for piece placements in the link game.
 *
 * NOTE: This class is separate from your main game class.  This
 * class does not play a game, it just illustrates various piece
 * placements.
 *
 * This class has been edited by Yitao and Ye.
 */
public class Viewer extends Application {

    /* board layout */
    private static final int SQUARE_SIZE = 100;
    private static final int PIECE_IMAGE_SIZE = 3*SQUARE_SIZE;
    private static final double ROW_HEIGHT = SQUARE_SIZE * 0.8660254; // 60 degrees
    private static final int VIEWER_WIDTH = 933;
    private static final int VIEWER_HEIGHT = 700;
    private static final double RIGHT_OFFSET = SQUARE_SIZE/3;
    private static final double DOWN_OFFSET = ROW_HEIGHT/3;

    private static final String URI_BASE = "assets/";

    private final Group root = new Group();
    private final Group controls = new Group();
    private final Group board = new Group();
    TextField textField;


    /**
     * Draw a placement in the window, removing any previously drawn one
     *
     * @param placement  A valid placement string
     */
    void makePlacement(String placement) {
        // FIXME Task 5: implement the simple placement viewer
        addPlacement(placement);
    }


    /**
     * Create a basic text field for input and a refresh button.
     */
    private void makeControls() {
        Label label1 = new Label("Placement:");
        textField = new TextField ();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                makePlacement(textField.getText());
                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(VIEWER_HEIGHT - 50);
        controls.getChildren().add(hb);
    }

    public void displayImage(){
        GridPane gridPane = new GridPane();
        gridPane.setLayoutX(0);
        gridPane.setLayoutY(0);

        Image image1 = new Image(Viewer.class.getResourceAsStream(URI_BASE +"A"+ ".png"));
        ImageView imageView1 = new ImageView(image1);
        imageView1.setFitHeight(SQUARE_SIZE);
        imageView1.setFitWidth(SQUARE_SIZE);

        Image image2 = new Image(Viewer.class.getResourceAsStream(URI_BASE +"B"+ ".png"));
        ImageView imageView2 = new ImageView(image2);
        imageView2.setFitHeight(SQUARE_SIZE);
        imageView2.setFitWidth(SQUARE_SIZE);
        imageView2.setTranslateX(SQUARE_SIZE/6);
        imageView2.setTranslateY(ROW_HEIGHT/3);

        gridPane.getChildren().add(imageView1);
        gridPane.getChildren().add(imageView2);

        root.getChildren().add(gridPane);
    }

    /**
     * Display a piece according to the given pieceStr.
     *
     * @param pieceStr A string with 3 characters, the first c is the peg where the origin of the image is placed;
     *                 the second c is the image that will be placed;
     *                 the third c is the rotation of the piece.
     */
    public void addPiece(String pieceStr){
        char peg = pieceStr.charAt(0);
        char pieceName = pieceStr.charAt(1);
        char pieceRotation = pieceStr.charAt(2);
        Image image = new Image(Viewer.class.getResourceAsStream(URI_BASE+pieceName+".png"));
        // 可以使用一个类来包含以下三句语句
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(SQUARE_SIZE);
        imageView.setFitWidth(SQUARE_SIZE);
        // 根据peg移位, 往右移一位是在x轴右移‘RIGHT_OFFSET’, 往下移一位是在y轴下移‘DOWN_OFFSET’.
        // 先根据peg确定要往右和下各移多少位
        double right;
        double down;
        double offset=0;
        int line = (peg-'A')/6;
        if (line%2==1) offset = 0.5;
        // 要往右和下移多少位
        right = peg-'A'-6*line+offset;
        down = line;
        imageView.setTranslateX(RIGHT_OFFSET*right);
        imageView.setTranslateY(DOWN_OFFSET*down);
        // 旋转
        double degree = (pieceRotation-'A')%6*60;
        imageView.setRotate(degree);
        if (pieceRotation>='G') imageView.setScaleY(-1);

        board.getChildren().add(imageView);
    }

    /**
     * Add pieces according to the given placement string.
     * @param placementStr
     */
    public void addPlacement(String placementStr){
        if (!LinkGame.isPlacementWellFormed(placementStr)){
            throw new IllegalArgumentException("Illegal input.");
        }
        for(int i=0;i<placementStr.length();i+=3){
            addPiece(placementStr.substring(i,i+3));
        }
    }

    /**
     * Add the background of the game.
     */
    public void addBackground(){
        Group background = new Group();
        Image image = new Image(Viewer.class.getResourceAsStream(URI_BASE+"background.png"));
        for(int i=0;i<24;i++){
            ImageView imageView = new ImageView(image);
            double right;
            double down;
            double offset=0;
            int line = i/6;
            if (line%2==1) offset = 0.5;
            // 要往右和下移多少位
            right = i-6*line+offset;
            down = line;
            imageView.setTranslateX(RIGHT_OFFSET*right);
            imageView.setTranslateY(DOWN_OFFSET*down);

            background.getChildren().add(imageView);
        }
        background.setTranslateX(RIGHT_OFFSET*1.15);
        background.setTranslateY(DOWN_OFFSET*1.38);
        board.getChildren().add(background);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("LinkGame Viewer");
        Scene scene = new Scene(root, VIEWER_WIDTH, VIEWER_HEIGHT);
        board.setTranslateX(VIEWER_WIDTH/3.0);
        board.setTranslateY(VIEWER_HEIGHT/3.0);
        root.getChildren().add(board);
        root.getChildren().add(controls);

        addBackground();
        /*   addPlacement("BAAHBATCJRDKWEBEFDNGLPHEDIFMJJQKIKLJ");
         */
        makeControls();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
