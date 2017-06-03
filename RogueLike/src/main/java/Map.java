import java.util.*;

/**
 * Object of Map class contains randomly generated map,
 * coordinates of top left corner of rooms, their width, height.
 */
class Map {
    private int width;
    private int height;
    private List<Integer> data = new ArrayList<>();
    List<Room> rooms = new ArrayList<>();


    Map(int width, int height, int roomsNumber) {
        this.width = width;
        this.height = height;
        generate(roomsNumber);
        generatePassages();
        generateWalls();
    }

    /**
     * getter for data of map - field of 1 and 2 - if
     * 0: this cell is external for map
     * 1: this cell is internal for map
     * 2: this cell is wall
     * @return data - width*height size array
     */
    public List<Integer> getData() {
        return data;
    }

    /**
     * getter for width of map
     * @return width of map
     */
    public int getWidth() {
        return width;
    }

    private void generate(int roomsCount) {
        for (int i = 0; i < roomsCount; ++i)
            for (int j = 0; j < 1000; ++j) {
                final int w = 5 + randomInt(15);
                final int h = 5 + randomInt(5);
                Room room = new Room(3 + randomInt(width - w - 6),
                        3 + randomInt(height - h - 6), w, h);

                boolean isIntersection = false;
                for (Room room1 : rooms) {
                    if (room1.intersect(room)) isIntersection = true;
                }
                if (!isIntersection) rooms.add(room);
            }
        data = new ArrayList<>(Collections.nCopies(width * height, 0));
        for (Room room : rooms) {
            for (int x = 0; x < room.w; ++x)
                for (int y = 0; y < room.h; ++y) {
                    data.set((room.x + x) + (room.y + y) * width, 1);
                }
        }
    }

    private void generatePassages() {
        ListIterator<Room> itPrev = rooms.listIterator();
        ListIterator<Room> itNext = rooms.listIterator();
        itNext.next();
        while (itNext.hasNext()) {
            Room startRoom = itPrev.next();
            Room finishRoom = itNext.next();
            int startX = startRoom.x;
            int startY = startRoom.y;
            int finishX = finishRoom.x;
            int finishY = finishRoom.y;
            generatePassage(new Map.Point(startX, startY, 0), new Map.Point(finishX, finishY, 0));
        }
    }

    private void generatePassage(final Point start, final Point finish) {
        ArrayList<Integer> parents = new ArrayList<>(Collections.nCopies(width * height, -1));

        Queue<Point> active = new PriorityQueue<>();
        active.add(start);

        final int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        while (!active.isEmpty()) {
            final Point point = active.poll();

            if (point == finish)
                break;

            for (int i = 0; i < 4; ++i) {
                Point p = new Point(point.x - directions[i][0], point.y - directions[i][1], 0);
                if (p.x < 0 || p.y < 0 || p.x >= width || p.y >= height)
                    continue;

                if (parents.get(p.x + p.y * width) < 0) {
                    p.cost = calcCost(p, finish);
                    active.offer(p);
                    parents.set(p.x + p.y * width, i);
                }
            }
        }

        Point point = finish;
        while (!(point.equals(start))) {
            data.set(point.x + point.y * width, 1);

            final int[] direction = directions[parents.get(point.x + point.y * width)];
            point.x += direction[0];
            point.y += direction[1];
        }
    }

    private void generateWalls() {
        final int[][] offsets = {
                {-1, -1}, {0, -1}, {1, -1}, {1, 0},
                {1, 1}, {0, 1}, {-1, 1}, {-1, 0},
        };

        for (int x = 1; x < width - 1; ++x)
            for (int y = 1; y < height - 1; ++y) {
                if (data.get(x + y * width) == 0)
                    for (int i = 0; i < 8; ++i) {
                        if (data.get((x + offsets[i][0]) + (y + offsets[i][1]) * width) == 1) {
                            data.set(x + y * width, 2);
                            break;
                        }
                    }
            }
    }

    private int calcCost(Point p1, Point p2) {
        return (int) (Math.pow((double) (p1.x - p2.x), 2) + Math.pow((double) (p1.y - p2.y), 2));
    }

    private int randomInt(int max) {
        return (int) (Math.random() * ++max);
    }

    private static class Point implements Comparable<Point> {
        int x, y, cost;

        Point(int x, int y, int cost) {
            this.x = x;
            this.y = y;
            this.cost = cost;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Point) {
                Point other = (Point) o;
                return x == other.x && y == other.y;
            } else return false;
        }

        @Override
        public int compareTo(Point point) {
            if (cost > point.cost) return 1;
            else if (cost == point.cost) return 0;
            else return -1;
        }
    }

    class Room {
        int x, y, w, h;

        Room(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        /**
         * Has this room intersect another room
         * @param r room we want to check intersection with
         * @return if intersects the method will return true else false
         */
        public boolean intersect(final Room r) {
            return !(r.x >= (x + w) || x >= (r.x + r.w) || r.y >= (y + h) || y >= (r.y + r.h));
        }
    }
}
