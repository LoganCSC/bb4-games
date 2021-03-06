// Copyright by Barry G. Becker, 2012. Licensed under MIT License: http://www.opensource.org/licenses/MIT
package com.barrybecker4.game.twoplayer.blockade.board.path;

import com.barrybecker4.common.geometry.ByteLocation;
import com.barrybecker4.game.twoplayer.blockade.BlockadeTestCase;
import com.barrybecker4.game.twoplayer.blockade.board.BlockadeBoard;
import com.barrybecker4.game.twoplayer.blockade.board.BlockadeBoardPosition;
import com.barrybecker4.game.twoplayer.blockade.board.move.wall.BlockadeWall;
import org.junit.Before;
import org.junit.Test;

import static com.barrybecker4.game.twoplayer.blockade.board.BlockadeTstUtil.createPath;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Barry Becker
 */
public class PathTest extends BlockadeTestCase {

    /** instance under test */
    private Path path;

    private BlockadeBoard board;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        board = new BlockadeBoard(7, 5);
    }

    @Test
    public void testUnblockedOneStepPath() {

        path = createPath(new ByteLocation(2, 2), new ByteLocation(4, 2));
        assertNotBlocked(path);
    }

    @Test
    public void testUnblockedTwoStepPath() {

        path = createPath(new ByteLocation(2, 2), new ByteLocation(4, 2), new ByteLocation(5, 3));
        assertNotBlocked(path);
    }

    @Test
    public void testBlockedOneStepPathHorzWall() {

        path = createPath(new ByteLocation(2, 2), new ByteLocation(4, 2));
        board.addWall(new BlockadeWall(new BlockadeBoardPosition(2, 2), new BlockadeBoardPosition(2, 3)));
        assertBlocked(path);
    }

    @Test
    public void testBlockedTwoStepPathHorzWall() {

        path = createPath(new ByteLocation(2, 2), new ByteLocation(4, 2), new ByteLocation(5, 3));
        board.addWall(new BlockadeWall(new BlockadeBoardPosition(3, 2), new BlockadeBoardPosition(3, 3)));
        assertBlocked(path);
    }

    @Test
    public void testBlockedTwoStepPathVertWallUpper() {

        path = createPath(new ByteLocation(2, 2), new ByteLocation(4, 2), new ByteLocation(5, 3));
        board.addWall(new BlockadeWall(new BlockadeBoardPosition(4, 2), new BlockadeBoardPosition(4, 3)));
        assertBlocked(path);
    }

    /** The wall does not block the path in this case */
    @Test
    public void testNotBlockedTwoStepPathVertWallLower() {

        path = createPath(new ByteLocation(2, 2), new ByteLocation(4, 2), new ByteLocation(5, 3));
        board.addWall(new BlockadeWall(new BlockadeBoardPosition(5, 2), new BlockadeBoardPosition(6, 2)));
        assertNotBlocked(path);
    }

    @Test
    public void testNotBlockedTwoStepPathHorzWallLower() {

        path = createPath(new ByteLocation(2, 2), new ByteLocation(4, 2), new ByteLocation(5, 3));
        board.addWall(new BlockadeWall(new BlockadeBoardPosition(5, 2), new BlockadeBoardPosition(5, 3)));
        assertNotBlocked(path);
    }

    @Test
    public void testBlockedTwoStepPathHorzWallLower() {

        path = createPath(new ByteLocation(2, 2), new ByteLocation(4, 2), new ByteLocation(5, 3));
        board.addWall(new BlockadeWall(new BlockadeBoardPosition(4, 2), new BlockadeBoardPosition(4, 3)));
        assertBlocked(path);
    }

    private void assertBlocked(Path path) {
        assertTrue("Path unexpectedly not blocked", path.isBlocked(board));
    }

    private void assertNotBlocked(Path path) {
        assertFalse("Path unexpectedly blocked", path.isBlocked(board));
    }
}
