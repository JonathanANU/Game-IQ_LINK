package game.iqlink.gui;

import game.iqlink.LinkGame;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.*;
import javafx.scene.text.*;
import javafx.scene.paint.*;
import javafx.util.Duration;
import javafx.scene.input.KeyCode.*;
import java.util.*;


/**
 * This is a JavaFX application that gives a graphical user interface (GUI) to the
 * IQ-link game.
 *
 * This class is a team work product.
 */
public class Board extends Application {
    private static final int BOARD_WIDTH = 933;
    private static final int BOARD_HEIGHT = 700;
    private static final int SQUARE_SIZE = 100;
    private static final double ROW_HEIGHT = SQUARE_SIZE * 0.8660254;
    private static final String URI_BASE = "assets/";
    private static final double RIGHT_OFFSET = SQUARE_SIZE/3;
    private static final double DOWN_OFFSET = ROW_HEIGHT/3;
    private static final int MARGIN = 50;
    TextField textField;
    public String start = "";
    Random rand = new Random();
    int choose = rand.nextInt(startPlacement.SOLUTIONS.length);
    public String solve = startPlacement.SOLUTIONS[choose];

    public ArrayList<String> history = new ArrayList<String>();

    private final Group root = new Group();
    private final Group pieces = new Group();
    private final Group board = new Group();
    private final Group controls = new Group();
    private final Group solution = new Group();
    private final Group warning = new Group();
    private final Group warningS = new Group();
    private final Group warningO = new Group();
    private final Group complete = new Group();
    private final Group instruction = new Group();
    private final Group hint = new Group();

    private final Slider difficulty = new Slider();


    /**
     * Place pieces according to the given placement string.
     * @param placement A string that refer to how pieces would be placed.
     */
    public void placePieces(String placement){
        if (!LinkGame.isPlacementWellFormed(placement)){
            throw new IllegalArgumentException("Illegal input.");
        }
        String[] pieceArray = LinkGame.breakPlacement(placement);


        for (Node n : pieces.getChildren()) {
            warning.getChildren().clear();
            for(int i=0;i<pieceArray.length;i++){
                if(((DraggableFXPiece) n).piece == pieceArray[i].charAt(1))
                    ((DraggableFXPiece) n).makePlacement(pieceArray[i]);
                checkMove();
            }
        }
    }
    //-------------------------------------------------

    private void resetPieces() {
        history.clear();
        pieces.toFront();
        for (Node n : pieces.getChildren()) {
            ((DraggableFXPiece) n).snapToHome();
        }
    }

    private void showInstruction(){
        Text t = new Text();
        t.setText("Instruction: \ndrag to move piece\nscroll to rotate\ndouble click to flip\npress key '/' get hint");
        t.setLayoutX(350);
        t.setLayoutY(450);
        t.setFill(Color.BLUE);
        t.setFont(Font.font(null, FontWeight.BOLD, 18));
        instruction.getChildren().add(t);
    }

    /**
     * If the placement is invalid, make a warn.
     */
    private void makeWarning(){
        Text t = new Text();
        t.setText("Warning: The placement is invalid");
        t.setFont(Font.font ("Verdana", 20));
        t.setFill(Color.RED);
        t.setLayoutX(320);
        t.setLayoutY(260);

        ScaleTransition scaleTransition =
                new ScaleTransition(Duration.millis(100), t);
        scaleTransition.setToX(1.3);
        scaleTransition.setToY(1.3);
        scaleTransition.setCycleCount(4);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();

        warning.getChildren().add(t);
    }

    /**
     * If a piece is out of the grid, make a warn.
     */
    private void makeWarningOffGrid(){
        Text t = new Text();
        t.setText("Warning: The piece is off grid");
        t.setFont(Font.font ("Verdana", 20));
        t.setFill(Color.RED);
        t.setLayoutX(320);
        t.setLayoutY(240);

        ScaleTransition scaleTransition =
                new ScaleTransition(Duration.millis(100), t);
        scaleTransition.setToX(1.3);
        scaleTransition.setToY(1.3);
        scaleTransition.setCycleCount(4);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
        warningO.getChildren().add(t);
    }

    private void makeWarningString(){
        Text t = new Text();
        t.setText("Warning: The string is invalid");
        t.setFont(Font.font ("Verdana", 20));
        t.setFill(Color.RED);
        t.setLayoutX(300);
        t.setLayoutY(650);

        ScaleTransition scaleTransition =
                new ScaleTransition(Duration.millis(100), t);
        scaleTransition.setToX(1.3);
        scaleTransition.setToY(1.3);
        scaleTransition.setCycleCount(4);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();
        warningS.getChildren().add(t);
    }
    public void undo(){

        if(history.size()==0 ){
            resetPieces();
        }
        else if(history.size()==1){
            history.remove(history.size()-1);
            resetPieces();
        }
        else{

            String[] temp = history.toArray(new String[history.size()]);
            System.out.println(history);
            history.clear();
            resetPieces();
            for(int i=0;i<temp.length-1;i++){
                placePieces(temp[i]);
            }
        }

    }


    private void makeControls() {
        Button butttonUndo = new Button("Undo");
        butttonUndo.setLayoutX(670);
        butttonUndo.setLayoutY(550);
        butttonUndo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                undo();
            }
        });
        controls.getChildren().add(butttonUndo);

        Button button1 = new Button("Restart");
        button1.setLayoutX(600);
        button1.setLayoutY(600);
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                warningS.getChildren().clear();
                warning.getChildren().clear();
                warningO.getChildren().clear();
                complete.getChildren().clear();

                resetPieces();

                //difficulty.getValue();
            }
        });
        // Add text fileds.
        controls.getChildren().add(button1);
        Label label1 = new Label("Placement:");
        textField = new TextField();
        textField.setPrefWidth(300);
        Button button = new Button("Refresh");
        // Set click event
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                resetPieces();
                warningS.getChildren().clear();
                complete.getChildren().clear();
                if(LinkGame.isPlacementWellFormed(textField.getText())){
                    // Place the piece if is valid.
                    placePieces(textField.getText());
                    checkMove();
                }
                else{
                    makeWarningString();
                }
                textField.clear();
            }
        });
        HBox hb = new HBox();
        hb.getChildren().addAll(label1, textField, button);
        hb.setSpacing(10);
        hb.setLayoutX(130);
        hb.setLayoutY(600);
        controls.getChildren().add(hb);

        // Set difficulties.
        difficulty.setMin(1);
        difficulty.setMax(8);
        difficulty.setValue(0);
        difficulty.setShowTickLabels(true);
        difficulty.setShowTickMarks(true);
        difficulty.setMajorTickUnit(1);
        difficulty.setMinorTickCount(0);
        difficulty.setSnapToTicks(true);
        difficulty.setMaxWidth(300);

        difficulty.setLayoutX(300);
        difficulty.setLayoutY(550);
        controls.getChildren().add(difficulty);

        final Label difficultyCaption = new Label("Difficulty:");
        difficultyCaption.setTextFill(Color.GREY);
        difficultyCaption.setLayoutX(200);
        difficultyCaption.setLayoutY(550);
        controls.getChildren().add(difficultyCaption);

        Button buttonH = new Button("start placement");
        buttonH.setLayoutX(460);
        buttonH.setLayoutY(550);
        buttonH.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                resetPieces();
                warningS.getChildren().clear();
                warning.getChildren().clear();
                warningO.getChildren().clear();

                showStartingPlacements();

                //difficulty.getValue();
            }
        });
        controls.getChildren().add(buttonH);

        Button buttonS = new Button("get answer");
        buttonS.setLayoutX(580);
        buttonS.setLayoutY(550);
        buttonS.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                warningS.getChildren().clear();
                warning.getChildren().clear();
                warningO.getChildren().clear();
                resetPieces();
                placePieces(start);
                placePieces(solve);
            }
        });
        controls.getChildren().add(buttonS);

    }

    class FXPiece extends ImageView {
        public char piece;
        FXPiece(char piece) {
            setImage(new Image(Board.class.getResource(URI_BASE + piece + ".png").toString()));
            this.piece = piece;
            setFitHeight(SQUARE_SIZE);
            setFitWidth(SQUARE_SIZE);
        }
    }

    /**
     * Piece that can be dragged.
     */
    class DraggableFXPiece extends FXPiece{
        // the current game position of the piece  (-1 is off-board)
        int position;
        double rotation;
        double scale;
        // the position in the window where the piece should be when not on the board
        int homeX, homeY;
        // the last known mouse positions (used when dragging)
        double mouseX, mouseY;
        DraggableFXPiece(char piece) {
            super(piece);

            position = -1; // off screen
            homeX =MARGIN + (SQUARE_SIZE * (((piece - 'A') % 2)*7));
            setLayoutX(homeX);
            homeY= MARGIN + (SQUARE_SIZE * ((piece - 'A') / 2));
            setLayoutY(homeY);

            /**
             * Flip the piece if a user double click the mouse.
             */
            setOnMouseClicked(event -> {
                if(event.getClickCount()==2){
                scale();
                event.consume();}
            });

            /**
             * Rotate the piece if a user scroll the mouse.
             */
            setOnScroll(event -> {            // scroll to change orientation
                rotate();
                event.consume();
            });

            /**
             * Mouse press indicates begin of drag.
             * Record the piece's current position when the mouse is clicked.
             */
            setOnMousePressed(event -> {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            });

            /**
             * After dragging a piece, record its current position.
             */
            setOnMouseDragged(event -> {
                toFront();
                double movementX = event.getSceneX() - mouseX;
                double movementY = event.getSceneY() - mouseY;
                setLayoutX(getLayoutX() + movementX);
                setLayoutY(getLayoutY() + movementY);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                event.consume();
            });

            /**
             * Place the piece when the mouse is released.
             */
            setOnMouseReleased(event -> {     // drag is complete
                snapToGrid();
            });

        }

        /**
         * Put the piece according to the given placement string.
         * @param placement
         */
        private void makePlacement(String placement){
            // Clean warns.
            warning.getChildren().clear();
            warningO.getChildren().clear();
            char origin = placement.charAt(0);
            char piece = placement.charAt(1);
            char orientation = placement.charAt(2);

            double right;
            double down;
            double offset=0;
            // Put it on which line
            int line = (origin-'A')/6;
            // If one line 1 and 3, need to offset.
            if (line%2==1) offset = 0.5;
            right = origin-'A'-6*line+offset;
            down = line;
            setLayoutX(312+RIGHT_OFFSET*right);
            setLayoutY(259.5+DOWN_OFFSET*down);
            double degree = (orientation-'A')%6*60;
            setRotate(degree);
            if (orientation-'A'>=6) setScaleY(-1);
            setPosition();
            checkMove();
        }

        /**
         * Method for rotate the piece
         */
        private void rotate() {
            setRotate((getRotate() + 60) % 360);
            setPosition();
            checkMove();
            //System.out.println("the rotation is "+ rotation);
            //System.out.println(toString());
        }

        /**
         * Method for flipping a piece.
         */
        private void scale(){
            double a = getScaleY();
            if(a==1){
                a=-1;
            }
            else{
                a=1;
            }
            setScaleY(a);
            setPosition();
            checkMove();
            //System.out.println("the scale is "+ scale);
            //System.out.println(toString());
        }


        /**
         * Method for placing the piece.
         */
        private void snapToGrid() {
            double offSet = 0;
            double gridX = ((RIGHT_OFFSET/2));
            double gridY = ((DOWN_OFFSET/2));

            // If it is on line 1 and line 3, it need an offset.
            if((getLayoutY()>=259.5+gridY && getLayoutY()<=259.5+3*gridY) ||
                    (getLayoutY()>=259.5+5*gridY && getLayoutY()<=259.5+7*gridY)){
                offSet = SQUARE_SIZE/6;
            }

            // If it's in the grid,
            if(isOnBoard()){
                // Put on which line?
                if(getLayoutY()>=259.5-gridY && getLayoutY()<=259.5+gridY) setLayoutY(259.5);
                else if(getLayoutY()>=259.5+gridY && getLayoutY()<=259.5+3*gridY) setLayoutY(259.5 + DOWN_OFFSET);
                else if(getLayoutY()>=259.5+3*gridY && getLayoutY()<=259.5+5*gridY)setLayoutY(259.5 + 2*DOWN_OFFSET);
                else setLayoutY(259.5 + 3*DOWN_OFFSET);

                setLayoutX( 312 + offSet + RIGHT_OFFSET*((int)(getLayoutX()-312)/(int)RIGHT_OFFSET));
            }
            else{
                snapToHome();
            }
            setPosition();
            checkMove();
            //System.out.println("the position is "+ position);
            //System.out.println(toString());
        }

        /**
         * Check if the piece is on the gird.
         * @return
         */
        private boolean isOnBoard(){
            if( (getLayoutX()>311.5 + RIGHT_OFFSET*6 ) ||
                    (getLayoutX()<(311.5 - RIGHT_OFFSET/2)) ||
                    (getLayoutY()>259.5 + DOWN_OFFSET*3 + DOWN_OFFSET/2) ||
                    (getLayoutY()<259.5 - DOWN_OFFSET/2)){
                return false;
            }
            else return true;
        }

        /**
         * Move back to home.
         */
        private void snapToHome() {
            setLayoutX(homeX);
            setLayoutY(homeY);
            setRotate(0);
            setPosition();
            // If it has been flipped, flip it again.
            setScaleY(1);
        }

        private void setScale(int s){
            scale = s;
        }

        /**
         * Set the position attribution and other related attributions of the piece.
         */
        private void setPosition(){
            int offSet=0;
            // Need an offset? On line 1 or 3?
            if((getLayoutY()-259.5)/DOWN_OFFSET == 1 ||
                    (getLayoutY()-259.5)/DOWN_OFFSET == 3 ){
                offSet=SQUARE_SIZE/6;
            }

            if(isOnBoard()){
                // At which column in the grid.
                int X = (int)((getLayoutX()-311.5-offSet)/RIGHT_OFFSET);
                // At which row in the grid.
                int Y = (int)Math.round(((getLayoutY()-259.5)/DOWN_OFFSET));
                position = Y*6 + X;
                rotation = getRotate()/60;
                scale = getScaleY();
            }
            else{
                position = -1;
            }

            if(toString().charAt(0)!='@'){
                if(history.size()==0){
                    history.add(toString());
                    System.out.println(history);
                }
                else{
                    if(!toString().equals(history.get(history.size()-1))){
                        history.add(toString());
                        System.out.println(history);
                    }
                }
            }

    }

        /**
         * Represent the piece by a string with
         * @return
         */
        public String toString(){
            char origin = (char)(position + 65);
            char orientation;
            if(scale==1){
                orientation = (char)(rotation+65);
            }
            else{
                orientation = (char)(rotation+71);
            }

            return "" + origin + piece + orientation;

        }
    }

    /**
     * Check whether the placement is off grid. A grid is a area where a user put all pieces.
     * @param placement a String that contains 3 letters.
     *
     * @return
     */
    public static boolean isOffGrid(String placement){
        int[] array = LinkGame.getPegsForPiecePlacement(placement);
        for(int i=0;i<array.length;i++){
            if(array[i]==-1) return true;
        }
        return false;
    }

    /**
     * Check whether all moves are valid.
     */
    private void checkMove(){
        warning.getChildren().clear();
        warningO.getChildren().clear();
        complete.getChildren().clear();
        String placement = "";
        // Combine all placement strings.
        for(Node p : pieces.getChildren()) {
            // If it equals '@', the piece is not in the grid.
            if(p.toString().charAt(0)!='@'){
                placement += p.toString();
            }
        }
        // Check if all placements are valid.
        boolean pv= !LinkGame.isPlacementValid(placement);
        boolean og= isOffGrid(placement);
        if(pv){
            makeWarning();
        }
        // Check if all placements are in the grid.
        if(og){
            makeWarningOffGrid();
        }

        if(!pv && !og && placement.length()==36){
            completeGame();
        }



    }

    private void completeGame(){
        Image congratulation = new Image(Board.class.getResourceAsStream(URI_BASE + "Congratulations.png"));
        ImageView imageV = new ImageView(congratulation);
        imageV.setLayoutX(300);
        imageV.setLayoutY(100);
        imageV.setFitWidth(300);
        imageV.setFitHeight(200);

        FadeTransition ft = new FadeTransition(Duration.millis(500), imageV);
        ft.setFromValue(1.0);
        ft.setToValue(0.1);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();

        ScaleTransition scaleTransition =
                new ScaleTransition(Duration.millis(500),imageV);
        scaleTransition.setToX(1.3);
        scaleTransition.setToY(1.3);
        scaleTransition.setCycleCount(4);
        scaleTransition.setAutoReverse(true);
        scaleTransition.play();

        complete.getChildren().add(imageV);
    }

    /**
     * Create and add 12 pieces.
     */
    private void makePieces() {
        warning.getChildren().clear();
        warningO.getChildren().clear();
        pieces.getChildren().clear();
        for (char p = 'A'; p < 'M'; p++) {
            pieces.getChildren().add(new DraggableFXPiece(p));
            checkMove();
        }
    }


    /**
     * Add a background for the game.
     */
    public void addBackground(){
        Group background = new Group();
        // The 'dot' in a background.
        Image image = new Image(Board.class.getResourceAsStream(URI_BASE+"background.png"));
        // Add 24 dots.
        for(int i=0;i<24;i++){
            ImageView imageView = new ImageView(image);
            double right;
            double down;
            double offset=0;
            int line = i/6;
            if (line%2==1) offset = 0.5;
            right = i-6*line+offset;
            down = line;
            imageView.setLayoutX(RIGHT_OFFSET*right);
            imageView.setLayoutY(DOWN_OFFSET*down);

            background.getChildren().add(imageView);
        }
        board.getChildren().add(background);
    }



    /** this is for showing hint placement**/
    public void addPiece(String pieceStr){
        char peg = pieceStr.charAt(0);
        char pieceName = pieceStr.charAt(1);
        char pieceRotation = pieceStr.charAt(2);
        Image image = new Image(Board.class.getResourceAsStream(URI_BASE+pieceName+".png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(SQUARE_SIZE);
        imageView.setFitWidth(SQUARE_SIZE);
        double right;
        double down;
        double offset=0;
        int line = (peg-'A')/6;
        if (line%2==1) offset = 0.5;
        right = peg-'A'-6*line+offset;
        down = line;
        imageView.setLayoutX(312 + RIGHT_OFFSET*right);
        imageView.setLayoutY(259.5 + DOWN_OFFSET*down);
        double degree = (pieceRotation-'A')%6*60;
        imageView.setRotate(degree);
        imageView.setOpacity(0.3);
        if (pieceRotation>='G') imageView.setScaleY(-1);
        hint.getChildren().add(imageView);
    }


    /** this is for showing hint placement**/
    public void addPlacement(String placementStr){
        if (!LinkGame.isPlacementWellFormed(placementStr)){
            throw new IllegalArgumentException("Illegal input.");
        }
        for(int i=0;i<placementStr.length();i+=3){
            addPiece(placementStr.substring(i,i+3));
        }
    }

    //-------------------------------------------


    /**
     * Show starting places.
     */
    public void showStartingPlacements() {
        double pieces = 12 - difficulty.getValue();
        ArrayList<String> all = new ArrayList<String>();
        Random random = new Random();
        int selection = random.nextInt(startPlacement.SOLUTIONS.length);
        String temp = startPlacement.SOLUTIONS[selection];
        solve = temp;
        for(int i=0;i<12;i++){
            String one = "" + temp.substring(i*3,i*3+3);
            all.add(one);
        }


        Set selections = new HashSet();
        do{
            Random rand = new Random();
            selections.add(all.get(rand.nextInt(12)));
        }while(selections.size()!=pieces);
        StringBuilder t = new StringBuilder();
        for(Object n:selections){
            t.append(n);
        }
        placePieces(t.toString());
        start = t.toString();
        System.out.println(start);
    }

    /**
     * show hints.
     */
    public void showHints(Scene scene){
        scene.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.SLASH){
                //System.out.println("task11");
                addPlacement(solve);
            }
        });
        scene.setOnKeyReleased(ke -> {
            hint.getChildren().clear();
        });
    }

    /**
     * show interesting starting placements according to the difficulty.
     *
     * @param difficult Difficulty of the game.
     */
    public void showInterestingStartingPlacements(int difficult){
        double pieces = 12 - difficulty.getValue();


        boolean isOnlySolution = false;
        do{

            ArrayList<String> all = new ArrayList<String>();
            Random random = new Random();
            int selection = random.nextInt(AllSolutions.allSolutions.length);
            String temp = AllSolutions.allSolutions[selection];
            solve = temp;
            for(int i=0;i<12;i++){
                String one = "" + temp.substring(i*3,i*3+3);
                all.add(one);
            }


            Set selections = new HashSet();
            do{
                Random rand = new Random();
                selections.add(all.get(rand.nextInt(12)));
            }while(selections.size()!=pieces);
            StringBuilder t = new StringBuilder();
            for(Object n:selections){
                t.append(n);
            }
            placePieces(t.toString());
            start = t.toString();

            int count = 0;
            for(String one: AllSolutions.allSolutions){
                if(one.contains(start)){
                    count++;
                }
            }

            if(count==1){
                isOnlySolution=true;
            }


        }while(isOnlySolution==false);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("IQ-Link");
        Scene scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT);

        showHints(scene);
        //------test------
        //--------test-------
        board.setLayoutX(350);
        board.setLayoutY(300);
        solution.setLayoutX(312);
        solution.setLayoutY(259.5);
        root.getChildren().add(pieces);
        root.getChildren().add(board);
        root.getChildren().add(controls);
        root.getChildren().add(solution);
        root.getChildren().add(warning);
        root.getChildren().add(warningS);
        root.getChildren().add(warningO);
        root.getChildren().add(complete);
        root.getChildren().add(instruction);
        root.getChildren().add(hint);

        showInstruction();

        board.toBack();
        makePieces();
        makeControls();
        addBackground();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
