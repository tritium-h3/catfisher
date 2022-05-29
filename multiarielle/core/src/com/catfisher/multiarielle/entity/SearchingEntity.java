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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.spongepowered.noise.module.modifier.Abs;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

@Log4j2
public class SearchingEntity extends MovingEntity {

    @JsonCreator
    public SearchingEntity(
            @JsonProperty("x") int x,
            @JsonProperty("y") int y,
            @JsonProperty("appearance") Sprite appearance,
            @JsonProperty("id") String id) {
        super(x, y, appearance, id);
    }

    @Value
    private static class PathElement {
        int newX, newY;
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

    List<PathElement> possibleNextStepsFrom(int x, int y, AbstractModel model, Set<PathElement> visited) {
        return Arrays.asList(new PathElement(x-1, y), new PathElement(x+1, y), new PathElement(x, y-1), new PathElement(x, y+1))
                .stream()
                .filter(pair -> (!model.isImpassable(pair.getNewX(), pair.getNewY()) && !visited.contains(pair)))
                .collect(Collectors.toList());
    }

    private List<PathElement> pathfind(AbstractModel abstractModel) {
        Pair<Integer, Integer> target = coordinatesOfClosestCharacter(abstractModel);
        if (target == null) {
            return new ArrayList<>();
        } 
        Set<PathElement> visited = new HashSet<>();
        Queue<List<PathElement>> partialPath = new LinkedList<>();
        List<PathElement> firstPath = new ArrayList<>();
        firstPath.add(new PathElement(x, y));
        partialPath.add(firstPath);

        while (true) {
            List<PathElement> nextToConsider = partialPath.remove();
            if (nextToConsider == null) {
                return new ArrayList<>();
            }
            PathElement lastElement = nextToConsider.get(nextToConsider.size() - 1);
            if ((lastElement.getNewX() == target.getLeft()) && (lastElement.getNewY() == target.getRight())) {
                return nextToConsider;
            } else {
                visited.add(lastElement);
                for (PathElement nextStep : possibleNextStepsFrom(lastElement.getNewX(), lastElement.getNewY(), abstractModel, visited)) {
                    ArrayList<PathElement> newPath = new ArrayList<>(nextToConsider);
                    newPath.add(nextStep);
                    partialPath.add(newPath);
                }
            }
        }
    }
}
