package com.catfisher.multiarielle.entity;

import com.catfisher.multiarielle.controller.delta.EntityChangeDelta;
import com.catfisher.multiarielle.model.AbstractModel;
import com.catfisher.multiarielle.model.Character;
import com.catfisher.multiarielle.model.Chunk;
import com.catfisher.multiarielle.sprite.Sprite;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.noise.module.modifier.Abs;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Log4j2
public class SearchingEntity extends MovingEntity {

    private final static int MAX_PATHLENGTH = 15;
    private final static double BIG_NEGATIVE = 1.0e-100;

    @JsonCreator
    public SearchingEntity(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y,
            @JsonProperty("appearance") Sprite appearance,
            @JsonProperty("id") String id) {
        super(x, y, appearance, id);
    }

    // Lower is better
    private static double heuristicScore(int startX, int startY, int currentX, int currentY, int endX, int endY) {
        return Math.hypot(Math.abs(endX-currentX), Math.abs(endY-currentY));
    }

    // Lower is better
    private static double heuristicScoreOfPath(List<PathElement> path) {
        if (path.isEmpty()) return BIG_NEGATIVE;
        return path.get(path.size() - 1).getScore() + path.size();
    }

    @Value
    private static class PathElement {
        int newX, newY;
        double score;

        public PathElement(int startX, int startY, int currentX, int currentY, int endX, int endY) {
            this.newX = currentX;
            this.newY = currentY;
            this.score = heuristicScore(startX, startY, currentX, currentY, endX, endY);
        }
    }

    private List<PathElement> currentPath = new ArrayList<>();

    public SearchingEntity(int x, int y, Sprite appearance) {
        super(x, y, appearance);
    }

    @Override
    public EntityChangeDelta update(AbstractModel abstractModel) {
        try {
            if (currentPath.isEmpty()) {
                currentPath = pathfind(abstractModel);
            }
            if (currentPath.isEmpty()) {
                return new EntityChangeDelta(getId(), Chunk.Address.ofAbsoluteCoords(x, y), objectMapper.writeValueAsString(new WalkDelta(x, y)));
            }
            PathElement nextStep = currentPath.remove(0);
            return new EntityChangeDelta(
                    getId(),
                    Chunk.Address.ofAbsoluteCoords(x, y),
                    objectMapper.writeValueAsString(new WalkDelta(nextStep.getNewX(), nextStep.getNewY()))
            );
        } catch (JsonProcessingException exn) {
            log.error(exn);
            throw new RuntimeException(exn);
        }
    }

    private Pair<Integer, Integer> coordinatesOfClosestCharacter(AbstractModel abstractModel) {
        AbstractModel.MutablePlacement currentCandidate = null;
        Integer currentDist = Integer.MAX_VALUE;
        for (AbstractModel.MutablePlacement candidate : abstractModel.getAllCharacters()) {
            Integer dist = Math.abs(x - candidate.getX()) + Math.abs(y - candidate.getY());
            if (dist < currentDist) {
                currentCandidate = candidate;
            }
        }
        return currentCandidate == null ? null : Pair.of(currentCandidate.getX(), currentCandidate.getY());
    }

    private List<PathElement> possibleNextStepsFrom(int startX, int startY, int x, int y, int endX, int endY, AbstractModel model, Set<PathElement> visited) {
        return Arrays.asList(new PathElement(startX, startY, x-1, y, endX, endY),
                        new PathElement(startX, startY, x+1, y, endX, endY),
                        new PathElement(startX, startY, x, y-1, endX, endY),
                        new PathElement(startX, startY, x, y+1, endX, endY))
                .stream()
                .filter(pair -> (!model.isImpassable(pair.getNewX(), pair.getNewY()) && !visited.contains(pair)))
                .collect(Collectors.toList());
    }

    private List<PathElement> pathfind(AbstractModel abstractModel) {
        Pair<Integer, Integer> target = coordinatesOfClosestCharacter(abstractModel);
        if (target == null) {
            return new ArrayList<>();
        }
        log.debug("Finding path from ({}, {}) to ({}, {})", x, y, target.getLeft(), target.getRight());
        Set<PathElement> visited = new HashSet<>();
        Queue<List<PathElement>> partialPath = new PriorityQueue<>(new Comparator<List<PathElement>>() {
            @Override
            public int compare(List<PathElement> o1, List<PathElement> o2) {
                // RETURN o1 > o2 if o2's score is greater than o1. The less a score is, the better.
                return (int)Math.signum(heuristicScoreOfPath(o1) - heuristicScoreOfPath(o2));
            }
        });
        List<PathElement> firstPath = new ArrayList<>();
        firstPath.add(new PathElement(x, y, x, y, target.getLeft(), target.getRight()));
        partialPath.add(firstPath);

        List<PathElement> currentBest = firstPath;

        while (true) {
            List<PathElement> nextToConsider;
            try {
                nextToConsider = partialPath.remove();
            } catch (NoSuchElementException exn) {
                log.debug("Could not find path within range, currentBest: {}", currentBest);
                return currentBest;
            }
            PathElement lastElement = nextToConsider.get(nextToConsider.size() - 1);
            if ((lastElement.getNewX() == target.getLeft()) && (lastElement.getNewY() == target.getRight())) {
                return nextToConsider;
            } else {
                if (heuristicScoreOfPath(nextToConsider) < heuristicScoreOfPath(currentBest)) {
                    currentBest = nextToConsider;
                }
                visited.add(lastElement);
                if (nextToConsider.size() < MAX_PATHLENGTH) {
                    for (PathElement nextStep : possibleNextStepsFrom(
                            x, y,
                            lastElement.getNewX(), lastElement.getNewY(),
                            target.getLeft(),
                            target.getRight(),
                            abstractModel, visited)) {
                        ArrayList<PathElement> newPath = new ArrayList<>(nextToConsider);
                        newPath.add(nextStep);
                        partialPath.add(newPath);
                    }
                }
            }
        }
    }
}
