package game.iqlink;
import java.util.HashSet;
import java.util.*;

import game.iqlink.gui.AllSolutions;
import game.iqlink.gui.startPlacement;

/**
 * This class provides the text interface for the Link Game
 *
 * The game is based directly on Smart Games' IQ-Link game
 * (http://www.smartgames.eu/en/smartgames/iq-link)
 *
 * This class has been edited by Yitao and Ye.
 */
public class LinkGame {
    /**
     * Determine whether a piece placement is well-formed according to the following:
     * - it consists of exactly three characters
     * - the first character is in the range A .. X
     * - the second character is in the range A .. L
     * - the third character is in the range A .. F if the second character is A, otherwise
     *   in the range A .. L
     *
     * @param piecePlacement A string describing a piece placement
     * @return True if the piece placement is well-formed
     */
    public static boolean isPiecePlacementWellFormed(String piecePlacement) {
        // FIXME Task 3: determine whether a piece placement is well-formed
        int check;
        if (piecePlacement.length()!= 3) return false;
        if (piecePlacement.charAt(0)<64 || piecePlacement.charAt(0)>88) return false;
        if (piecePlacement.charAt(1)<64 || piecePlacement.charAt(1)>76) return false;
        if (piecePlacement.charAt(1)=='A'){
            check = 70;
        }else{
            check = 76;
        }
        if (piecePlacement.charAt(2)<64 || piecePlacement.charAt(2)>check) return false;
        return true;
    }

    /**
     * Determine whether a placement string is well-formed:
     *  - it consists of exactly N three-character piece placements (where N = 1 .. 12);
     *  - each piece placement is well-formed
     *  - no piece appears more than once in the placement
     *
     * @param placement A string describing a placement of one or more pieces
     * @return True if the placement is well-formed
     */
    public static boolean isPlacementWellFormed(String placement) {

        // it consists of exactly N three-character piece placements (where N = 1 .. 12)
        if (placement==null) return false;
        if (placement.length()%3 !=0 || placement.length()>36 || placement.length()<3) return false;
        // each piece placement is well-formed
        for(int i=0;i<placement.length();i+=3){
            if(!isPiecePlacementWellFormed(placement.substring(i,i+3))) return false;
        }
        // no piece appears more than once in the placement
        HashSet set = new HashSet();
        char pieceIndex;
        for(int i=1;i<placement.length();i+=3){
            pieceIndex = placement.charAt(i);
            if(set.contains(pieceIndex)) return false;
            else set.add(pieceIndex);
        }
        return  true;
    }

    /**
     * Return a array of peg locations according to which pegs the given piece placement touches.
     * The values in the array should be ordered according to the units that constitute the
     * piece.
     * The code needs to account for the origin of the piece, the piece shape, and the piece
     * orientation.
     * @param piecePlacement A valid string describing a piece placement
     * @return An array of integers corresponding to the pegs which the piece placement touches,
     * listed in the normal order of units for that piece.   The value 0 corresponds to
     * peg 'A', 1 to peg 'B', etc.
     */
    public static int[] getPegsForPiecePlacement(String piecePlacement) {
        // FIXME Task 6: determine the pegs touched by a piece placement

        int[] pegList = new int[piecePlacement.length()];

        for(int i=0;i<piecePlacement.length();i+=3){
            char origin = piecePlacement.charAt(i);
            char piece = piecePlacement.charAt(i+1);
            char orientation = piecePlacement.charAt(i+2);
            int off_set = 0;

            int line = (origin-'A')/6;
            if(line==0||line==2)  off_set = -1;

            int[] onePiecePosition = transferPiece(piece,off_set,orientation,origin);
            pegList[i] = onePiecePosition[0];
            pegList[i+1] = onePiecePosition[1];
            pegList[i+2] = onePiecePosition[2];
        }
        return pegList;
    }

    /** transform the piece
     * @param piece which piece
     * @param off_set line1&3:off_set=0,line0&2=-1;
     * @param orientation direction
     * @param origin_o put on which cell?
     * @return An array contains the position of units 0,1,2
     */
    public static int[] transferPiece (char piece, int off_set, char orientation, char origin_o){
        int one=0, three =0;
        int origin = origin_o - 'A';
        // A,B,C has a same structure.
        if(piece >= 'A' && piece <= 'C'){
            switch (orientation){
                case 'A': one=left(origin);three=right(origin);break;
                case 'B': one=top_Left(origin);three=down_Right(origin);break;
                case 'C': one=top_Right(origin);three=down_Left(origin);break;
                case 'D': one=right(origin);three=left(origin);break;
                case 'E': one=down_Right(origin);three=top_Left(origin);break;
                case 'F': one=down_Left(origin);three=top_Right(origin);break;
                case 'G': one=left(origin);three=right(origin);break;
                case 'H': one=top_Left(origin);three=down_Right(origin);break;
                case 'I': one=top_Right(origin);three=down_Left(origin);break;
                case 'J': one=right(origin);three=left(origin);break;
                case 'K': one=down_Right(origin);three=top_Left(origin);break;
                case 'L': one=down_Left(origin);three=top_Right(origin);break;
            }
        }
        else if(piece >= 'D' && piece <= 'H'){
            switch (orientation){
                case 'A': one=left(origin);three=top_Right(origin);break;
                case 'B': one=top_Left(origin);three=right(origin);break;
                case 'C': one=top_Right(origin);three=down_Right(origin);break;
                case 'D': one=right(origin);three=down_Left(origin);break;
                case 'E': one=down_Right(origin);three=left(origin);break;
                case 'F': one=down_Left(origin);three=top_Left(origin);break;
                case 'G': one=left(origin);three=down_Right(origin);break;
                case 'H': one=top_Left(origin);three=down_Left(origin);break;
                case 'I': one=top_Right(origin);three=left(origin);break;
                case 'J': one=right(origin);three=top_Left(origin);break;
                case 'K': one=down_Right(origin);three=top_Right(origin);break;
                case 'L': one=down_Left(origin);three=right(origin);break;
            }
        }
        else if(piece >= 'I' && piece <= 'L'){
            switch (orientation){
                case 'A': one=left(origin);three=top_Left(origin);break;
                case 'B': one=top_Left(origin);three=top_Right(origin);break;
                case 'C': one=top_Right(origin);three=right(origin);break;
                case 'D': one=right(origin);three=down_Right(origin);break;
                case 'E': one=down_Right(origin);three=down_Left(origin);break;
                case 'F': one=down_Left(origin);three=left(origin);break;
                case 'G': one=left(origin);three=down_Left(origin);break;
                case 'H': one=top_Left(origin);three=left(origin);break;
                case 'I': one=top_Right(origin);three=top_Left(origin);break;
                case 'J': one=right(origin);three=top_Right(origin);break;
                case 'K': one=down_Right(origin);three=right(origin);break;
                case 'L': one=down_Left(origin);three=down_Right(origin);break;
            }
        }
        if(one<0 || one >23) one = -1;
        if(three<0 || three >23) three = -1;
        int [] returnValue = new int[]{one,origin,three};
        return returnValue;
    }

    /**
     * Determine whether a placement is valid.  To be valid, the placement must be well-formed
     * and each piece must correctly connect with each other.
     *
     * @param placement A placement string
     * @return True if the placement is valid
     */
    public static boolean isPlacementValid(String placement) {
        // FIXME Task 7: determine whether a placement is valid
        String[] pieces = breakPlacement(placement);
        int length = pieces.length;
        for(int i=0;i<length;i++){
            for(int j=i+1;j<length;j++){
                if(isOverLaps(pieces[i],pieces[j])) return false;
            }
        }
        return true;
    }

    //----------------------------------

    /**
     * Put on which line.
     * @param place: A integer that indicates where to put the piece
     * @return
     */
    public static int column(int place){
        return place/6;
    }

    /**
     * Check whether the place is out of the gird.
     * @param place
     * @return return -1 if the place is out of the grid, else return the place.
     */
    public static int offGrid(int place){
        if(place <0 || place >23) return -1;
        else return place;
    }

    /**
     * Need an offset?
     * @param place
     * @return
     */
    public static int offSet(int place){
        if(column(place)==0 || column(place)==2) return -1;
        else return 0;
    }

    /**
     * Get the top_left side of the place
     * @param place
     * @return return -1 if not valid else return the moved place.
     */
    public static int top_Left(int place){
        int off_set = offSet(place);
        if(place == 12) return -1;
        else {
            return offGrid(place - 6 + off_set);
        }
    }

    /**
     * Get the top_right side of the place
     * @param place
     * @return return -1 if not valid else return the moved place.
     */
    public static int top_Right(int place){
        int off_set = offSet(place);
        if(place == 11 || place == 23) return -1;
        else {
            return offGrid(place - 5 + off_set);
        }
    }

    /**
     * Get the down_Left side of the place
     * @param place
     * @return return -1 if not valid else return the moved place.
     */
    public static int down_Left(int place){
        int off_set = offSet(place);
        if(place == 12 || place == 0) return -1;
        else {
            return offGrid(place + 6 + off_set);
        }
    }

    /**
     * Get the down_Right side of the place
     * @param place
     * @return return -1 if not valid else return the moved place.
     */
    public static int down_Right(int place){
        int off_set = offSet(place);
        if(place == 11 ) return -1;
        else {
            return offGrid(place + 7 + off_set);
        }
    }

    /**
     * Get the left side of the place
     * @param place
     * @return return -1 if not valid else return the moved place.
     */
    public static int left(int place){
        // If the place is on the most left side of a line, return -1 directly
        if(place == 6 || place == 12 || place == 18 ) return -1;
        else {
            return offGrid(place -1);
        }
    }

    /**
     * Get the right side of the place
     * @param place
     * @return return -1 if not valid else return the moved place.
     */
    public static int right(int place){
        if(place == 5 || place == 11 || place == 17 ) return -1;
        else {
            return offGrid(place + 1);
        }
    }

    //------------------------------------------------------------
    public static boolean isOverLaps(String pieceOne, String pieceTwo){
        int[] one = getPegsForPiecePlacement(pieceOne);
        int[] two = getPegsForPiecePlacement(pieceTwo);
        char oneP = getPiece(pieceOne);
        char twoP = getPiece(pieceTwo);


        if(!isSame(one,two)){
            return false;
        }
        else{
            if(oneOverlap(one,two)){
                int p1 = whichBall(one,two);
                int p2 = whichBall(two,one);
                int t1 = getBallType(p1,oneP);
                int t2 = getBallType(p2,twoP);
                if((t1==1 && t2==2) || (t1==2 && t2==1)){
                    if(t1==1) {
                        int tellPlaceA = getOrigin(pieceOne);
                        int tellPlaceB = getTellPosition(ringType(twoP,p2), two[p2], getOrientation(pieceTwo));
                        if(tellPlaceA == tellPlaceB){
                             return false;
                        }
                        else return true;
                    }
                    else{
                        int tellPlaceA = getOrigin(pieceTwo);
                        int tellPlaceB = getTellPosition(ringType(oneP,p1), one[p1], getOrientation(pieceOne));
                        if(tellPlaceA == tellPlaceB){
                            return false;
                        }
                        else return true;
                    }

                }
                //this is special case for pieceA;
                else if(t1==4 || t2==4){
                    if(t1==4){
                        int tellPlaceB = getOrigin(pieceTwo);
                        int[] tellPositionA = ringA(getOrigin(pieceOne),getOrientation(pieceOne));

                        //this is special case for pieceI and J on position1;
                        if((twoP=='I' || twoP=='J') && p2 ==1){
                            tellPositionA[0]=getOrigin(pieceOne);
                            tellPositionA[1]=getOrigin(pieceOne);
                        }

                        if((tellPlaceB ==tellPositionA[0] || tellPlaceB ==tellPositionA[1]) && t2==1) return false;
                        else return true;
                    }
                    else{
                        int tellPlaceA = getOrigin(pieceOne);
                        int[] tellPositionB = ringA(getOrigin(pieceTwo),getOrientation(pieceTwo));

                        //this is special case for pieceI and J on position1;
                        if((oneP=='I' || oneP=='J') && p1 ==1){
                            tellPositionB[0]=getOrigin(pieceTwo);
                            tellPositionB[1]=getOrigin(pieceTwo);
                        }

                        if((tellPlaceA ==tellPositionB[0] || tellPlaceA ==tellPositionB[1]) && t1==1) return false;
                        else return true;
                    }
                }
                else return true;
            }
            else return true;
        }
    }
    //-----------------------------------------------------------------

    /**
     * determine whether two int arraya contain same element;
     * @return true if share same element
     */

    public static boolean isSame(int[] one, int[] two){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(one[i] == two[j]) {
                    if(one[i]!=-1){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * determine whether two arrays which contain the posiiton int of the piece has invalid overlap
     * @return true if there's an invalid overlap
     */

    public static boolean oneOverlap(int[] one, int[] two){
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(one[i] == two[j]) {
                    if(one[i]!=-1){
                        temp.add(i);
                    }
                }
            }
        }
        if(temp.size()==1) return true;
        else return false;
    }

    /**
     * once we find the overlap part of two pieces, we wanna know which particular part of the piece
     * @return which part of a piece, in terms of the 'one' (0,1,2)
     */
    public static int whichBall(int[] one,int[] two){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(one[i]==two[j]) {
                    if(one[i]!=-1){
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    //---------------------------------------------------------------

    /**
     * Break the placements
     * @return
     */
    public static String[] breakPlacement(String placement){
        ArrayList<String> array = new ArrayList<String>();
        for(int i=0;i<placement.length();i+=3){
            char origin = placement.charAt(i);
            char piece = placement.charAt(i+1);
            char orientation = placement.charAt(i+2);
            String temp = Character.toString(origin) + Character.toString(piece) + Character.toString(orientation);
            array.add(temp);
        }
        String[] newList = array.toArray(new String[placement.length()/3]);
        return newList;
    }


    public static int getOrigin(String piecePlacement){ return piecePlacement.charAt(0)-'A'; }
    public static char getOrientation(String piecePlacement){
        return piecePlacement.charAt(2);
    }
    public static char getPiece(String piecePlacement){
        return piecePlacement.charAt(1);
    }

    public static int getBallType(int position, char piece){
        int[] temp = getType(piece);
        return temp[position];
    }

    /**
     * Get type of the piece.
     * 1: a peg; 2: a hole with a loophole; 3: a hole without any loophole; 4: a hole with two loopholes.
     * @param piece: name of the piece
     * @return
     */
    public static int[] getType(char piece){
        switch (piece){
            case 'A': int[] temp1={1,4,1}; return temp1;
            case 'B': int[] temp2={1,3,2}; return temp2;
            case 'C': int[] temp3={1,3,2}; return temp3;
            case 'D': int[] temp4={1,3,2}; return temp4;
            case 'E': int[] temp5={1,3,2}; return temp5;
            case 'F': int[] temp6={1,3,2}; return temp6;
            case 'G': int[] temp7={1,2,1}; return temp7;
            case 'H': int[] temp8={2,3,2}; return temp8;
            case 'I': int[] temp9={1,1,2}; return temp9;
            case 'J': int[] temp10={1,1,2}; return temp10;
            case 'K': int[] temp11={1,2,1}; return temp11;
            case 'L': int[] temp12={1,2,2}; return temp12;
            default: int[] temp13={-1,-1,-1}; return temp13;
        }
    }


    /**
     * the situation is an overlap between a ball and a opening ring,
     * this function is to get the peg position that the opening ring facing with
     * @param ringType ball, closed ring, opening ring
     * @param position the position of the overlaped ball or ring
     * @param orientation the rotation of the piece
     * @return the peg position that the opening ring facing with
     */
    public static int getTellPosition(int ringType, int position,char orientation){
        switch (ringType){
            case 1: return ring1(position,orientation,offSet(position));
            case 2: return ring2(position,orientation,offSet(position));
            case 3: return ring3(position,orientation,offSet(position));
            case 4: return ring4(position,orientation,offSet(position));
            case 5: return ring5(position,orientation,offSet(position));
            case 6: return ring6(position,orientation,offSet(position));
            default: return -1;
        }
    }

    /**
     * Here's to get the opening ring type, there're 6 different types, distinguished by the opening direction
     * type 1 facing to Right, 2 top_Right, 3 top_Left, 4 Left, 5, down_left, 6 down_right
     * @param piece type of the piece
     * @param position position of the ring inside the piece
     * @return
     */
    public static int ringType(char piece, int position){
        if (piece == 'B')return 2;
        else if (piece == 'C') return 3;
        else if (piece == 'D') return 6;
        else if (piece == 'E') return 4;
        else if (piece == 'F') return 3;
        else if (piece == 'G') return 5;
        else if (piece == 'H') {
            switch (position){
                case 0: return 3;
                case 2: return 2;
            }
        }
        else if (piece == 'I') return 3;
        else if (piece == 'J') return 4;
        else if (piece == 'k') return 6;
        else {
            switch (position){
                case 1: return 6;
                case 2: return 1;
            }
        }
        return 0;
    }

    /**
     * it describes which direction the opening ring is facing with,
     *ring1 facing to Right, ring2 top_Right, ring3 top_Left, ring4 Left, ring5, down_left, ring6 down_right
     * @return
     */

    public static int ring1(int position, char orientation, int offSet){
        if(orientation=='A' || orientation=='G') return right(position);
        else if(orientation=='B' || orientation=='H') return down_Right(position);
        else if(orientation=='C' || orientation=='I') return down_Left(position);
        else if(orientation=='D' || orientation=='J') return left(position);
        else if(orientation=='E' || orientation=='K')return top_Left(position);
        else return top_Right(position);
    }
    public static int ring2(int position, char orientation, int offSet){
        if(orientation=='A' || orientation=='K') return top_Right(position);
        else if(orientation=='B' || orientation=='L') return right(position);
        else if(orientation=='C' || orientation=='G') return down_Right(position);
        else if(orientation=='D' || orientation=='H')return down_Left(position);
        else if(orientation=='E' || orientation=='I')return left(position);
        else return top_Left(position);
    }
    public static int ring3(int position, char orientation, int offSet){
        if(orientation=='A' || orientation== 'I') return position-6+offSet;
        else if(orientation=='B' || orientation=='J') return position-5+offSet;
        else if(orientation=='C' || orientation=='K') return position+1;
        else if(orientation=='D' || orientation== 'L')return position+7+offSet;
        else if(orientation=='E' || orientation=='G')return position+6+offSet;
        else return position-1;
    }
    public static int ring4(int position, char orientation, int offSet){
        if(orientation=='A' || orientation=='G') return position-1;
        else if(orientation=='B' || orientation=='H') return position-6+offSet;
        else if(orientation=='C' || orientation=='I') return position-5+offSet;
        else if(orientation=='D' || orientation=='J')return position+1;
        else if(orientation=='E' || orientation=='K')return position+7+offSet;
        else return position+6+offSet;
    }
    public static int ring5(int position, char orientation, int offSet){
        if(orientation=='A' || orientation=='K') return position+6+offSet;
        else if(orientation=='B' || orientation=='L') return position-1;
        else if(orientation=='C' || orientation=='G') return position-6+offSet;
        else if(orientation=='D' || orientation=='H')return position-5+offSet;
        else if(orientation=='E' || orientation=='I')return position+1;
        else return position+7+offSet;
    }
    public static int ring6(int position, char orientation, int offSet){
        if(orientation=='A' || orientation=='I') return position+7+offSet;
        else if(orientation=='B' || orientation=='J') return position+6+offSet;
        else if(orientation=='C' || orientation=='K') return position-1;
        else if(orientation=='D' || orientation=='L')return position-6+offSet;
        else if(orientation=='E' || orientation=='G')return position-5+offSet;
        else return right(position);
    }

    /**
     * this is special case for pieceA at position1, since the opening ring in pieceA is always facing with two direction
     * @param position
     * @param orientation
     * @return
     */
    public static int[] ringA(int position, char orientation){
        int[] temp = new int[2];
        if(orientation=='A'){
            temp[0] = top_Left(position);
            temp[1] = top_Right(position);
        }
        else if(orientation=='B'){
            temp[0] = top_Right(position);
            temp[1] = right(position);
        }
        else if(orientation=='C'){
            temp[0] = right(position);
            temp[1] = down_Right(position);
        }
        else if(orientation=='D'){
            temp[0] = down_Right(position);
            temp[1] = down_Left(position);
        }
        else if(orientation=='E'){
            temp[0] = down_Left(position);
            temp[1] = left(position);
        }
        else {
            temp[0] = left(position);
            temp[1] = top_Left(position);
        }
        return temp;
    }



    /**
     * Return an array of all solutions given a starting placement. Number of placed pieces is bigger than 3.
     *
     * @param placement  A valid piece placement string.
     * @return An array of strings, each describing a solution to the game given the
     * starting point provied by placement.
     */
    public static String[] getSolutions(String placement) {

        ArrayList<String> solutions = new ArrayList<String>();
        for(String temp: AllSolutions.allSolutions){
            if(temp.contains(placement)){
                solutions.add(temp);
            }
        }
        return solutions.toArray(new String[solutions.size()]);
    }
}
