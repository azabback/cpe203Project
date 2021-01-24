import processing.core.PImage;

import java.util.*;

public final class WorldModel {
    public int numRows;
    public int numCols;
    public Background background[][];
    public Entity occupancy[][];
    public Set<Entity> entities;

    public WorldModel(int numRows, int numCols, Background defaultBackground) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();

        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }

    public Optional<Point> findOpenAround(Point pos) {
        for (int dy = -Functions.ORE_REACH; dy <= Functions.ORE_REACH; dy++) {
            for (int dx = -Functions.ORE_REACH; dx <= Functions.ORE_REACH; dx++) {
                Point newPt = new Point(pos.x + dx, pos.y + dy);
                if (this.withinBounds(newPt) && !this.isOccupied(newPt)) {
                    return Optional.of(newPt);
                }
            }
        }

        return Optional.empty();
    }

    public void setBackground(Point pos, Background background) {
        if (this.withinBounds(pos)) {
            this.setBackgroundCell(pos, background);
        }
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < this.numRows && pos.x >= 0
                && pos.x < this.numCols;
    }

    public boolean isOccupied(Point pos) {
        return this.withinBounds(pos) && this.getOccupancyCell(pos) != null;
    }

    public Optional<Entity> findNearest(Point pos, EntityKind kind) {
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : this.entities) {
            if (entity.kind == kind) {
                ofType.add(entity);
            }
        }

        return nearestEntity(ofType, pos);
    }

    /*
       Assumes that there is no entity currently occupying the
       intended destination cell.
    */
    public void addEntity(Entity entity) {
        if (this.withinBounds(entity.position)) {
            this.setOccupancyCell(entity.position, entity);
            this.entities.add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.position;
        if (this.withinBounds(pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell(oldPos, null);
            this.removeEntityAt(pos);
            this.setOccupancyCell(pos, entity);
            entity.position = pos;
        }
    }

    public void removeEntity(Entity entity) {
        this.removeEntityAt(entity.position);
    }

    private void removeEntityAt(Point pos) {
        if (this.withinBounds(pos) && this.getOccupancyCell(pos) != null) {
            Entity entity = this.getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.position = new Point(-1, -1);
            this.entities.remove(entity);
            this.setOccupancyCell(pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos) {
        if (this.withinBounds(pos)) {
            return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
        } else {
            return Optional.empty();
        }
    }

    public Optional<Entity> getOccupant(Point pos) {
        if (this.isOccupied(pos)) {
            return Optional.of(this.getOccupancyCell(pos));
        } else {
            return Optional.empty();
        }
    }

    private Entity getOccupancyCell(Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    private void setOccupancyCell(Point pos, Entity entity) {
        this.occupancy[pos.y][pos.x] = entity;
    }

    private Background getBackgroundCell(Point pos) {
        return this.background[pos.y][pos.x];
    }

    private void setBackgroundCell(Point pos, Background background) {
        this.background[pos.y][pos.x] = background;
    }

    private boolean parseBackground(String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.BGND_COL]),
                    Integer.parseInt(properties[Functions.BGND_ROW]));
            String id = properties[Functions.BGND_ID];
            this.setBackground(pt,
                    new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == Functions.BGND_NUM_PROPERTIES;
    }

    private boolean parseMiner(String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.MINER_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.MINER_COL]),
                    Integer.parseInt(properties[Functions.MINER_ROW]));
            Entity entity = Entity.createMinerNotFull(properties[Functions.MINER_ID],
                    Integer.parseInt(
                            properties[Functions.MINER_LIMIT]),
                    pt, Integer.parseInt(
                            properties[Functions.MINER_ACTION_PERIOD]), Integer.parseInt(
                            properties[Functions.MINER_ANIMATION_PERIOD]),
                    imageStore.getImageList(Functions.MINER_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == Functions.MINER_NUM_PROPERTIES;
    }

    private boolean parseObstacle(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.OBSTACLE_COL]),
                    Integer.parseInt(properties[Functions.OBSTACLE_ROW]));
            Entity entity = Entity.createObstacle(properties[Functions.OBSTACLE_ID], pt,
                    imageStore.getImageList(Functions.OBSTACLE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == Functions.OBSTACLE_NUM_PROPERTIES;
    }

    private boolean parseOre(String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.ORE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.ORE_COL]),
                    Integer.parseInt(properties[Functions.ORE_ROW]));
            Entity entity = Entity.createOre(properties[Functions.ORE_ID], pt, Integer.parseInt(
                    properties[Functions.ORE_ACTION_PERIOD]),
                    imageStore.getImageList(Functions.ORE_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == Functions.ORE_NUM_PROPERTIES;
    }

    private boolean parseSmith(String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.SMITH_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.SMITH_COL]),
                    Integer.parseInt(properties[Functions.SMITH_ROW]));
            Entity entity = Entity.createBlacksmith(properties[Functions.SMITH_ID], pt,
                    imageStore.getImageList(Functions.SMITH_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == Functions.SMITH_NUM_PROPERTIES;
    }

    private boolean parseVein(
            String[] properties, ImageStore imageStore) {
        if (properties.length == Functions.VEIN_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[Functions.VEIN_COL]),
                    Integer.parseInt(properties[Functions.VEIN_ROW]));
            Entity entity = Entity.createVein(properties[Functions.VEIN_ID], pt,
                    Integer.parseInt(
                            properties[Functions.VEIN_ACTION_PERIOD]),
                    imageStore.getImageList(Functions.VEIN_KEY));
            this.tryAddEntity(entity);
        }

        return properties.length == Functions.VEIN_NUM_PROPERTIES;
    }

    private boolean processLine(
            String line, ImageStore imageStore) {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[Functions.PROPERTY_KEY]) {
                case Functions.BGND_KEY:
                    return this.parseBackground(properties, imageStore);
                case Functions.MINER_KEY:
                    return this.parseMiner(properties, imageStore);
                case Functions.OBSTACLE_KEY:
                    return this.parseObstacle(properties, imageStore);
                case Functions.ORE_KEY:
                    return this.parseOre(properties, imageStore);
                case Functions.SMITH_KEY:
                    return this.parseSmith(properties, imageStore);
                case Functions.VEIN_KEY:
                    return this.parseVein(properties, imageStore);
            }
        }
        return false;
    }

    private void tryAddEntity(Entity entity) {
        if (this.isOccupied(entity.position)) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        this.addEntity(entity);
    }

    public void load(Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!this.processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    private static Optional<Entity> nearestEntity(
            List<Entity> entities, Point pos)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = nearest.position.distanceSquared(pos);

            for (Entity other : entities) {
                int otherDistance = other.position.distanceSquared(pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
}
