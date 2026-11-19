package com.zarterstein.magid.api.symbols;

/**
 * What a name written in MagidML points to, parsed from its text alone: which kind of symbol, whose, and which key.
 * It holds no value and doesn't know whether the symbol exists; the engine resolves it.
 *
 * <table>
 *   <caption>The forms</caption>
 *   <tr><th>Written</th><th>{@link Type}</th><th>owner</th><th>key</th></tr>
 *   <tr><td>{@code $gold}</td><td>VARIABLE</td><td>{@code ""}</td><td>{@code gold}</td></tr>
 *   <tr><td>{@code #maxHp}</td><td>CONSTANT</td><td>{@code ""}</td><td>{@code maxHp}</td></tr>
 *   <tr><td>{@code ^butler}</td><td>REFERENCE</td><td>{@code ""}</td><td>{@code butler}</td></tr>
 *   <tr><td>{@code ^rule/old-wizard}</td><td>REFERENCE</td><td>{@code rule}</td><td>{@code old-wizard}</td></tr>
 *   <tr><td>{@code player.gold}</td><td>MEMORY</td><td>{@code player}</td><td>{@code gold}</td></tr>
 *   <tr><td>{@code hero::title}</td><td>ATTRIBUTE</td><td>{@code hero}</td><td>{@code title}</td></tr>
 *   <tr><td>{@code gold} (bare)</td><td>VARIABLE</td><td>{@code ""}</td><td>{@code gold}</td></tr>
 * </table>
 * <p>
 * Surrounding spaces and one pair of braces ({@code {player.gold}}) are ignored. The first matching form wins, in
 * the order of the table: {@code hero::title.x} is an attribute whose key is {@code title.x}, and
 * {@code player.inventory.sword} a memory whose key is {@code inventory.sword}. Malformed input (an empty key, a
 * memory or attribute without owner, {@code null}) gives {@link Type#ERROR}, with the input as its key.
 * </p>
 * <p>
 * A bare name is a variable, although variables and constants share one name space: without context the parser can't
 * tell, and the engine resolves the name against the story's declarations.
 * </p>
 * <p>
 * A reference ({@code ^name}, {@code ^category/name}) is a read-only symbol: a text the story's author wrote (an LLM
 * rule, lore, a note) and that tags or text fill-ins read. Its category is in {@code owner}: {@code ""} is the general
 * category, or the reading tag's own. {@code ^/name} is the general category too; {@code ^rule/} is an error.
 * </p>
 *
 * @param type  the kind of name
 * @param owner the actor of a memory or an attribute, the category of a reference ({@code ""}: none given); {@code ""}
 *              for the other kinds
 * @param key   the name without prefix or owner; the input for {@link Type#ERROR}
 * @author Mark-Marion Gondelman (Creator)
 * @author Refactoring: Claude Code (agentic refactoring)
 * @author JavaDocs: Claude Code (Model: claude-opus-5-5)
 */
public record SymbolRef(Type type, String owner, String key) {

    /** The prefix of a variable: {@code $gold}. */
    public static final String VAR_RESOLVER = "$";
    /** The prefix of a constant: {@code #maxHp}. */
    public static final String CONST_RESOLVER = "#";
    /** The prefix of a reference: {@code ^butler}, {@code ^rule/old-wizard}. */
    public static final String REFERENCE_RESOLVER = "^";
    /** Between a reference's category and its name: {@code ^rule/old-wizard}. */
    public static final String REFERENCE_KEY_RESOLVER = "/";
    /** Between an actor and its memory: {@code player.gold}. */
    public static final String MEMORY_RESOLVER = ".";
    /** Between an actor and its attribute: {@code hero::title}. */
    public static final String ATTR_RESOLVER = "::";

    /** The kinds of name. */
    public enum Type {
        /** An actor's memory: {@code player.gold}. */
        MEMORY,
        /** An actor's attribute: {@code hero::title}. */
        ATTRIBUTE,
        /** A story variable: {@code $gold}, or a bare {@code gold}. */
        VARIABLE,
        /** A story constant: {@code #maxHp}. */
        CONSTANT,
        /** An entry in a registry that isn't the symbols: {@code ^butler}. */
        REFERENCE,
        /** Malformed input. */
        ERROR
    }

    /**
     * Parses a name as written in MagidML.
     *
     * @param fullSymbol the name, e.g. {@code player.gold} or {@code {$gold}}; may be {@code null}
     * @return what it points to; {@link Type#ERROR} for malformed input, never {@code null}
     */
    public static SymbolRef of(String fullSymbol) {
        SymbolRef result = null;
        try {
            fullSymbol = fullSymbol.strip();
            if (fullSymbol.startsWith("{") && fullSymbol.endsWith("}")) {
                fullSymbol = fullSymbol.substring(1, fullSymbol.length() - 1).strip();
            }
            if (fullSymbol.startsWith(VAR_RESOLVER)) {
                String key = fullSymbol.substring(VAR_RESOLVER.length());
                result = new SymbolRef(Type.VARIABLE, "", key);
            }
            else if (fullSymbol.startsWith(CONST_RESOLVER)) {
                String key = fullSymbol.substring(CONST_RESOLVER.length());
                result = new SymbolRef(Type.CONSTANT, "", key);
            }
            else if (fullSymbol.startsWith(REFERENCE_RESOLVER)) {
                String rest = fullSymbol.substring(REFERENCE_RESOLVER.length());
                int keyResolverPosition = rest.indexOf(REFERENCE_KEY_RESOLVER);
                result = keyResolverPosition < 0
                        ? new SymbolRef(Type.REFERENCE, "", rest)                                      // ^old-wizard
                        : new SymbolRef(Type.REFERENCE, rest.substring(0, keyResolverPosition), rest.substring(keyResolverPosition + REFERENCE_KEY_RESOLVER.length()));   // ^rule/old-wizard
            }
            else if (fullSymbol.contains(ATTR_RESOLVER)) {
                String[] info = fullSymbol.split(ATTR_RESOLVER, 2);
                result = new SymbolRef(Type.ATTRIBUTE, info[0], info[1]);
            }
            else if (fullSymbol.contains(MEMORY_RESOLVER)) {
                String[] info = fullSymbol.split("\\" + MEMORY_RESOLVER, 2);
                result =  new SymbolRef(Type.MEMORY, info[0], info[1]);
            }
            else {
                result = new SymbolRef(Type.VARIABLE, "", fullSymbol);   // a bare name is a variable
            }

        } catch (Exception e) {
            return new SymbolRef(Type.ERROR, "", fullSymbol);
        }
        boolean needsOwner = result.type() == Type.MEMORY || result.type() == Type.ATTRIBUTE;
        if ( result.key().isEmpty() || (needsOwner && result.owner().isEmpty()) ) {
            return new SymbolRef(Type.ERROR, "", fullSymbol);
        }
        return result;
    }
}
