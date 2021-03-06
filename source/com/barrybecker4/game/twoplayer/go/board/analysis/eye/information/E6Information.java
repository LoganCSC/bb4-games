/** Copyright by Barry G. Becker, 2000-2011. Licensed under MIT License: http://www.opensource.org/licenses/MIT  */
package com.barrybecker4.game.twoplayer.go.board.analysis.eye.information;

import com.barrybecker4.common.geometry.Box;
import com.barrybecker4.game.twoplayer.go.board.GoBoard;
import com.barrybecker4.game.twoplayer.go.board.elements.eye.IGoEye;
import com.barrybecker4.game.twoplayer.go.board.elements.position.GoBoardPosition;
import com.barrybecker4.game.twoplayer.go.board.elements.position.GoBoardPositionList;

/**
 * Eye6Type containing MetaData for the different possible Eye shapes of size 6.
 * There are 8 different subtypes to consider.
 *
 * @author Barry Becker
 */
public class E6Information extends AbstractEyeSubtypeInformation {

    /** Different sorts of eye with 6 spaces. */
    public enum Eye6Type {
        E112222, E111223, E111133, E112233, E112233a, E112233b, E122223, E112224, E111124, E222233
    }

    private Eye6Type type;

    /**
     * Constructor
     * @param subTypeDesc description of the type - something like "E112223".
     */
    E6Information(String subTypeDesc) {
        type = Eye6Type.valueOf(subTypeDesc);
        switch(type) {
           case E112222 : initialize(true, 6);
               break;
           case E111223 : initialize(true, 6);
               break;
           case E111133 : initialize(true, 6);
               break;
           case E112233 : initialize(false, 6);
               break;
           case E112233a : initialize(false, 6, new float[] {3.06f, 3.06f});
               break;
           case E112233b : initialize(false, 6, new float[] {3.05f, 3.05f, 2.06f, 2.06f});
               break;
           case E122223 : initialize(false, 6, new float[] {2.04f, 3.06f, 2.05f, 2.05f, 2.04f},
                                               new float[] {1.02f});
               break;
           case E112224 : initialize(false, 6, new float[] {4.06f},
                                               new float[] {2.04f});
               break;
           case E111124 : initialize(false, 6, new float[] {2.05f, 4.05f},
                                               new float[] {1.02f});
               break;
           case E222233 : initialize(false, 6, new float[] {3.07f, 3.07f});
               break;
        }
    }


    /**
     * @return eye status for E6 types.
     */
    @Override
    public EyeStatus determineStatus(IGoEye eye, GoBoard board) {
        EyeNeighborMap nbrMap = new EyeNeighborMap(eye);
        switch (type) {
            case E112222 :
            case E111223 :
            case E111133 :
                return handleSubtypeWithLifeProperty(eye, board);
            case E112233 :
                Eye6Type E112233Subtype = determineE112233Subtype(nbrMap);

                if (E112233Subtype == Eye6Type.E112233a) {
                   return handleVitalPointCases(nbrMap, eye, 2);
                }
                else {
                   return handleVitalPointCases(nbrMap, eye, 4);
                }
            case E122223 :
                return handleVitalPointCases(nbrMap, eye, 5);
            case E112224 :
                GoBoardPositionList endFilledSpaces = findSpecialFilledSpaces(nbrMap, getEndPoints(), eye);
                switch (endFilledSpaces.size()) {
                    case 0 :  return handleVitalPointCases(nbrMap, eye, 2);
                    case 1 :  return handleVitalPointCases(nbrMap, eye, 1); // replace with handleLifeProp? see page 122
                    default : assert false : "unexpected number of end spaces filled";
                }
                break;
            case E111124 :
                return handleVitalPointCases(nbrMap, eye, 2);
            case E222233 :
                return handleVitalPointCases(nbrMap, eye, 2);
        }
        return EyeStatus.NAKADE; // never reached
    }

    /**
     * find the 2 spaces with only 1 nbr
     * if the box defined by those 2 positions contains the other 4 spaces, then case b, else a
     * @return the subtype E112233a or E112233b
     */
    private Eye6Type determineE112233Subtype(EyeNeighborMap nbrMap) {

        GoBoardPositionList oneNbrPoints = new GoBoardPositionList();
        GoBoardPositionList otherPoints = new GoBoardPositionList();

        for (GoBoardPosition pos : nbrMap.keySet()) {
            if (nbrMap.getNumEyeNeighbors(pos) == 1)  {
               oneNbrPoints.add(pos);
            }
            else {
               otherPoints.add(pos);
            }
        }
        assert oneNbrPoints.size() == 2 : "Did not get 2 one nbr points. Instead got "
                + oneNbrPoints.size()+ "\n nbrmap="+ nbrMap;
        Box bounds = new Box(oneNbrPoints.getFirst().getLocation(), oneNbrPoints.get(1).getLocation());

        for (GoBoardPosition otherPt : otherPoints) {
            if (!bounds.contains(otherPt.getLocation())) {
                return Eye6Type.E112233a;
            }
        }
        return Eye6Type.E112233b;
    }

    @Override
    public String getTypeName() {
        return type.toString();
    }
}