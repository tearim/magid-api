package com.zarterstein.magid.api.tags;

import java.util.Objects;

/**
 * Where something stands in a story's source: the file, its type, and the line and column. A tag gets the position of
 * its element (or of one of its attributes) from the engine and passes it to a {@link TagException}, so that the
 * error points the author to the right place.
 * <p>
 * For a story written in another format and translated to MagidML (Magid Markdown), the position is in the original
 * file, not in the generated XML: the author never sees the generated XML.
 * </p>
 *
 * <pre>{@code
 * throw new TagAttributeException("<roll> needs a number of sides", "sides", position);
 * // <roll> needs a number of sides (XML source (dice.xml): around line 12, column 4)
 * }</pre>
 *
 * @param file     the file's name, e.g. {@code story.xml}; {@link #UNKNOWN_FILE} if the engine doesn't know it
 * @param fileType the file's format, e.g. {@code XML} or {@code MMD} (Magid Markdown)
 * @param line     the line, from 1
 * @param column   the column, from 1
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public record SourcePosition(String file, String fileType, int line, int column) {

    /** {@link #file()} when the engine doesn't know the file's name. */
    public static final String UNKNOWN_FILE = "unknown";

    /**
     * @throws NullPointerException     if {@code file} or {@code fileType} is {@code null}
     * @throws IllegalArgumentException if {@code line} or {@code column} is less than 1
     */
    public SourcePosition {
        Objects.requireNonNull(file, "file");
        Objects.requireNonNull(fileType, "fileType");
        if (line < 1) throw new IllegalArgumentException("line must be at least 1: " + line);
        if (column < 1) throw new IllegalArgumentException("column must be at least 1: " + column);
    }

    /**
     * The position as an error message names it.
     *
     * @return e.g. {@code XML source (story.xml): around line 12, column 4}
     */
    public String describe() {
        return fileType + " source (" + file + "): around line " + line + ", column " + column;
    }
}
