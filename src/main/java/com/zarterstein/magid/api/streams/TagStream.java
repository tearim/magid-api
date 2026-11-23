package com.zarterstein.magid.api.streams;

/**
 * Text a tag goes on producing after its response has gone: a model's reply, word by word. The client shows what
 * exists so far and adds the rest as it comes (development-docs/STREAMING-DESIGN.md).
 * <p>
 * <b>How a tag streams.</b> It opens a stream ({@link TagStreams#open()}). If the stream {@linkplain #isOpen() is not
 * open} (the client doesn't take streams, or the session has too many), the tag computes its whole text, as without
 * streaming. Otherwise it returns {@link #placeholder()} as its text (where the stream's text is to stand) and hands
 * the work to a thread of its own, which calls {@link #append} as text comes and {@link #done} or {@link #fail} at
 * the end:
 * </p>
 * <pre>{@code
 * TagStream stream = tagContext.streams().open();
 * if (!stream.isOpen()) return TagProcessorResult.mapWithTextOnly(askWhole());
 * Thread.ofVirtual().start(() -> askStreaming(stream));   // append(...), then done(...) or fail(...)
 * return TagProcessorResult.mapWithTextOnly(stream.placeholder());
 * }</pre>
 * <p>
 * <b>The worker must never touch the story</b>: not the symbols, the story model or the stashes. A session handles
 * one request at a time, and the worker runs beside it. It only produces text; the stream is thread-safe.
 * </p>
 * <p>
 * <b>A warm-up message.</b> A model may take seconds before its first word. {@link #warmUp} shows a text meanwhile
 * ("The wizard ponders…"); the first {@link #append} replaces it. Clients mark the stream's element by its phase
 * ({@code magid-stream-warming-up}, {@code -receiving}, {@code -finished}), so a story's CSS can show the warm-up
 * differently.
 * </p>
 * <p>
 * <b>Plain text.</b> A client inserts a stream's text as text, never as markup.
 * </p>
 *
 * @author Mark-Marion Gondelman (Design, Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
public interface TagStream {

    /** Starts a placeholder in a narration text: {@code ⟪stream:<id>⟫}. */
    String PLACEHOLDER_START = "⟪stream:";
    /** Ends a placeholder. */
    String PLACEHOLDER_END = "⟫";

    /**
     * The stream of a context that can't stream: never open; {@link #append}, {@link #done} and {@link #fail} do
     * nothing.
     */
    TagStream NONE = new TagStream() {
        @Override public String id() { return ""; }
        @Override public boolean isOpen() { return false; }
        @Override public boolean isCancelled() { return false; }
        @Override public void append(String text) { }
        @Override public void done(String wholeText) { }
        @Override public void fail(String fallback) { }
        @Override public void warmUp(String text) { }
        @Override public String toString() { return "TagStream.NONE"; }
    };

    /**
     * @param id a stream's id
     * @return its placeholder: {@code ⟪stream:<id>⟫}
     */
    static String placeholderOf(String id) {
        return PLACEHOLDER_START + id + PLACEHOLDER_END;
    }

    /**
     * @return the stream's id ({@code str-} and a random UUID); {@code ""} for {@link #NONE}
     */
    String id();

    /**
     * @return whether text may still be added: false for {@link #NONE}, and once the stream is done, failed or
     *         cancelled
     */
    boolean isOpen();

    /**
     * @return whether the engine ended the stream (the scene changed, the session ended, it took too long): the
     *         worker should stop, which also stops paying for a model's tokens
     */
    boolean isCancelled();

    /**
     * @return what the tag puts into its text where the stream's text is to stand; {@code ""} for {@link #NONE}
     */
    default String placeholder() {
        return id().isEmpty() ? "" : placeholderOf(id());
    }

    /**
     * Adds text. Ignored once the stream isn't {@linkplain #isOpen() open}.
     *
     * @param text the next piece, plain text
     */
    void append(String text);

    /**
     * Ends the stream; the client replaces what it built with {@code wholeText}.
     *
     * @param wholeText the whole text; {@code null} for the text appended so far
     */
    void done(String wholeText);

    /**
     * Ends the stream as failed; the client shows {@code fallback} instead.
     *
     * @param fallback the text to show, e.g. the tag's {@code default}
     */
    void fail(String fallback);

    /**
     * Shows {@code text} in place of the stream until its first {@link #append}, which replaces it. Clients see the
     * stream as warming up meanwhile. Call it before the work starts; ignored once text was appended or the stream has
     * ended. Ending the stream without any text appended ends it with {@code ""} (or {@code done}'s or {@code fail}'s
     * text), never with the warm-up message.
     *
     * @param text what to show until the stream's text comes, plain text; {@code null} or {@code ""} for nothing
     */
    void warmUp(String text);
}
