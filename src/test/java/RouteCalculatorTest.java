import core.Line;
import core.Station;
import junit.framework.TestCase;

import java.util.*;
import java.util.stream.Collectors;

public class RouteCalculatorTest extends TestCase
{
    RouteCalculator routeCalculator;
    StationIndex stationIndex;
    private List<Station> getShortestRoute(String from, String to)
    {
        return routeCalculator.getShortestRoute(stationIndex.getStation(from),
                stationIndex.getStation(to));
    }
    private List<Station> expectedShortestRoute(String from, String to)
    {
        List<Station> list = new ArrayList<>(stationIndex.stations);
        if (list.indexOf(stationIndex.getStation(from)) <= list.indexOf(stationIndex.getStation(to)))
        {
            list = list.subList(list.
                    indexOf(stationIndex.getStation(from)), list.indexOf(stationIndex.getStation(to)) + 1);
        }
        else
        {
            list = list.subList(list
                    .indexOf(stationIndex.getStation(to)), list.indexOf(stationIndex.getStation(from)) + 1);
            Collections.reverse(list);
        }
        return list;
    }

    @Override
    protected void setUp() throws Exception
    {
        stationIndex = new StationIndex();

        stationIndex.addLine(new Line(1, "First"));
        stationIndex.addLine(new Line(2, "Second"));
        stationIndex.addLine(new Line(3, "Third"));
        List<Station> list = new ArrayList<>();
        for (int i = 0; i < 30; i++)
        {
            if (i < 10)
            {
                stationIndex.addStation(new Station("Station № " + i, stationIndex.getLine(1)));
                stationIndex.getLine(1).addStation(stationIndex.getStation("Station № " + i));
//                line1.addStation(new Station("Station № A" + i, line1));
//                route1.add(new Station("Station № A" + i, line1));
            }
            else if( i < 20)
            {
                stationIndex.addStation(new Station("Station № " + i, stationIndex.getLine(2)));
                stationIndex.getLine(2).addStation(stationIndex.getStation("Station № " + i));
//                if (i == 3) route2.addAll(route1);
//                route2.add(new Station("Station № B" + i, line2));
            }
            else
            {
                stationIndex.addStation(new Station("Station № " + i, stationIndex.getLine(3)));
                stationIndex.getLine(3).addStation(stationIndex.getStation("Station № " + i));
//                if (i == 6) route3.addAll(route2);
//                route3.add(new Station("Station № C" + i, line3));
            }
        }
        list.add(stationIndex.getStation("Station № 9"));
        list.add(stationIndex.getStation("Station № 10"));
        stationIndex.addConnection(list);
        list.clear();
        list.add(stationIndex.getStation("Station № 19"));
        list.add(stationIndex.getStation("Station № 20"));
        stationIndex.addConnection(list);
        routeCalculator = new RouteCalculator(stationIndex);

    }

    public void testGetRouteOnTheLine()
    {
        List<Station> actual = getShortestRoute("Station № 1", "Station № 8");
        List<Station> expected = expectedShortestRoute("Station № 1", "Station № 8");

        assertEquals(expected, actual);

    }

    public void testGetRouteWithOneConnection()
    {
        List<Station> actual = getShortestRoute("Station № 11", "Station № 8");
        List<Station> expected = expectedShortestRoute("Station № 11", "Station № 8");
        assertEquals(expected, actual);
    }

    public void testGetRouteWithTwoConnection()
    {
        List<Station> actual = getShortestRoute("Station № 21", "Station № 8");
        List<Station> expected = expectedShortestRoute("Station № 21", "Station № 8");
        assertEquals(expected, actual);
    }

    public void testCalculateDuration()
    {
        double actual = RouteCalculator.calculateDuration(getShortestRoute("Station № 8", "Station № 11"));
        double  expected = 8.5;
        assertEquals(expected, actual);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
