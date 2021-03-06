package woe.twin;

import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static woe.twin.WorldMap.*;

public class WorldMapTest {

  /*  4 + lat
   *    |
   *  3 +
   *    | 2,1
   *  2 +  +--+
   *    |  |  |
   *  1 +  +--+
   *    |    1,2
   *  0 +--+--+--+--+--+ lng
   *    0  1  2  3  4  5
   */
  @Test
  public void overlapRegionSelf() {
    WorldMap.Region region = region(1, topLeft(2, 1), botRight(1, 2));

    assertTrue(region.overlaps(region));
  }

  /*  4 + lat
   *    |
   *  3 +
   *    | 2,1   2,3
   *  2 +  +--+  +--+
   *    |  |  |  |  |
   *  1 +  +--+  +--+
   *    |    1,2   1,4
   *  0 +--+--+--+--+--+ lng
   *    0  1  2  3  4  5
   */
  @Test
  public void nonOverlappingRegionsSideBySide() {
    WorldMap.Region regionLeft = region(1, topLeft(2, 1), botRight(1, 2));
    WorldMap.Region regionRight = region(1, topLeft(2, 3), botRight(1, 4));

    assertFalse(regionRight.overlaps(regionLeft));
    assertFalse(regionLeft.overlaps(regionRight));
  }

  /*  6 + lat
   *    |       5,3
   *  5 +        +--+
   *    |        |  |
   *  4 +        +--+
   *    |          4,4
   *  3 +
   *    |       2,3
   *  2 +        +--+
   *    |        |  |
   *  1 +        +--+
   *    |          1,4
   *  0 +--+--+--+--+--+ lng
   *    0  1  2  3  4  5
   */
  @Test
  public void noOverlappingRegionsAboveAndBelow() {
    WorldMap.Region regionAbove = region(1, topLeft(5, 3), botRight(4, 4));
    WorldMap.Region regionBelow = region(1, topLeft(2, 3), botRight(1, 4));

    assertFalse(regionBelow.overlaps(regionAbove));
    assertFalse(regionAbove.overlaps(regionBelow));
  }

  /*            4,3
   *  4 + lat    +--+
   *    |        |  |
   *  3 +        +--+
   *    | 2,1      3,4
   *  2 +  +--+
   *    |  |  |
   *  1 +  +--+
   *    |    1,2
   *  0 +--+--+--+--+--+ lng
   *    0  1  2  3  4  5
   */
  @Test
  public void nonOverlappingRegionsLowerLeftAndUpperRight() {
    WorldMap.Region regionLowerLeft = region(1, topLeft(2, 1), botRight(1, 2));
    WorldMap.Region regionUpperRight = region(1, topLeft(4, 3), botRight(3, 4));

    assertFalse(regionUpperRight.overlaps(regionLowerLeft));
    assertFalse(regionLowerLeft.overlaps(regionUpperRight));
  }

  /*  5 + lat
   *    | 4,1
   *  4 +  +--+--+--+
   *    |  | 3,2    |
   *  3 +  +  +--+  +
   *    |  |  |  |  |
   *  2 +  +  +--+  +
   *    |  |    2,3 |
   *  1 +  +--+--+--+
   *    |          1,4
   *  0 +--+--+--+--+--+ lng
   *    0  1  2  3  4  5
   */
  @Test
  public void overlappingRegions() {
    WorldMap.Region regionOutside = region(1, topLeft(4, 1), botRight(1, 4));
    WorldMap.Region regionInside = region(1, topLeft(3, 2), botRight(2, 3));

    assertTrue(regionOutside.overlaps(regionInside));
    assertTrue(regionInside.overlaps(regionOutside));
  }

  /*  5 + lat
   *    |    4,2
   *  4 +     +--+
   *    | 3,1 |  |
   *  3 +  +--+--+--+
   *    |  |  |  |  |
   *  2 +  +--+--+--+
   *    |     |  | 2,4
   *  1 +     +--+
   *    |       1,3
   *  0 +--+--+--+--+--+ lng
   *    0  1  2  3  4  5
   */
  @Test
  public void nonOverlappingAdjoiningLeftRightAboveBelow() {
    WorldMap.Region regionCenter = region(1, topLeft(3, 2), botRight(2, 3));
    WorldMap.Region regionUpperAdjoining = region(1, topLeft(4, 2), botRight(3, 3));
    WorldMap.Region regionLowerAdjoining = region(1, topLeft(2, 2), botRight(1, 3));
    WorldMap.Region regionLeftAdjoining = region(1, topLeft(3, 1), botRight(2, 2));
    WorldMap.Region regionRightAdjoining = region(1, topLeft(3, 3), botRight(2, 4));

    assertFalse(regionCenter.overlaps(regionUpperAdjoining));
    assertFalse(regionCenter.overlaps(regionLowerAdjoining));
    assertFalse(regionCenter.overlaps(regionLeftAdjoining));
    assertFalse(regionCenter.overlaps(regionRightAdjoining));

    assertFalse(regionUpperAdjoining.overlaps(regionCenter));
    assertFalse(regionLowerAdjoining.overlaps(regionCenter));
    assertFalse(regionLeftAdjoining.overlaps(regionCenter));
    assertFalse(regionRightAdjoining.overlaps(regionCenter));
  }

  /*  5 + lat
   *    |    4,2
   *  4 +     +--+--+
   *    | 3,1 |     |
   *  3 +  +--+--+  +
   *    |  |  |  |  |
   *  2 +  +  +--+--+
   *    |  |     | 2,4
   *  1 +  +--+--+
   *    |       1,3
   *  0 +--+--+--+--+--+ lng
   *    0  1  2  3  4  5
   */
  @Test
  public void partiallyOverlappingRegions() {
    WorldMap.Region regionLowerLeft = region(1, topLeft(3, 1), botRight(1, 3));
    WorldMap.Region regionUpperRight = region(1, topLeft(4, 2), botRight(2, 4));

    assertTrue(regionLowerLeft.overlaps(regionUpperRight));
    assertTrue(regionUpperRight.overlaps(regionLowerLeft));
  }

  @Test
  public void regionAtLatLngContainsLatLng() {
    final LatLng latLng = latLng(51.5007541, -0.11688530);
    final WorldMap.Region region = regionAtLatLng(18, latLng);

    assertTrue(region.topLeft.lat >= latLng.lng);
    assertTrue(region.botRight.lat <= latLng.lat);
    assertTrue(region.topLeft.lng <= latLng.lng);
    assertTrue(region.botRight.lng >= latLng.lng);
  }

  @Test
  public void atCenterIsCenterOfRegion() {
    final LatLng latLngStart = latLng(51.5007541, -0.11688530);
    final WorldMap.Region regionStart = regionAtLatLng(18, latLngStart);
    final LatLng latLngCenter = atCenter(regionStart);
    final WorldMap.Region regionCenter = regionAtLatLng(regionStart.zoom, latLngCenter);

    assertTrue(regionStart.contains(latLngStart));
    assertTrue(regionCenter.contains(latLngCenter));

    assertEquals(regionStart, regionCenter);
  }

  @Test
  public void regionsInAreaContainsRegions() {
    final LatLng topLeft = latLng(85.24439622732126, -168.04687500000003);
    final LatLng botRight = latLng(-85.24439622732126, 167.87109375000003);
    final Region area = new Region(5, topLeft, botRight);
    final List<Region> regions = regionsIn(area);

    assertTrue(regions.size() > 0);
  }
}

