package com.catfisher.multiarielle.util;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public class LineAccumulator {
    List<String> completedLines = new ArrayList<>();
    StringBuilder pendingLine = new StringBuilder();

    public void addChunk(String chunk) {
        chunk.chars().forEach(
                cval -> {
                    char c = (char)cval;
                    pendingLine.append(c);

                    if (c == '\0') {
                        completedLines.add(pendingLine.toString());
                        pendingLine = new StringBuilder(pendingLine.capacity());
                    }
                }
        );
    }

    public List<String> getCompletedLines() {
        if (completedLines.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<String> toReturn = completedLines;
            completedLines = new ArrayList<>();
            return toReturn;
        }
    }
}
