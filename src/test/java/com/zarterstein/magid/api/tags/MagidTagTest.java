package com.zarterstein.magid.api.tags;

import com.zarterstein.magid.api.expressions.EvaluatorInterface;
import com.zarterstein.magid.api.som.NarrationValue;
import com.zarterstein.magid.api.som.StoryObjectModel;
import com.zarterstein.magid.api.symbols.SymbolPinax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link MagidTag}'s implementation of {@link MagidTagInterface}, over a fake element and context: an evaluator that
 * replaces {@code {name}} with {@code Ada}, and a story model that records what is narrated.
 *
 * @author Mark-Marion Gondelman (Orchestration)
 * @author Code: Claude Code (agentic implementation)
 */
class MagidTagTest {

    @MagidXmlTag(target = "greet, hello", types = {SemanticType.TEXT_GENERATOR})
    static class GreetTag extends MagidTag {
        @Override
        public Map<String, String> process(TagElement element, String context) {
            return TagProcessorResult.mapWithTextOnly("Hello, " + getAttributeAsString("to") + "!");
        }
        @Override public boolean newLine() { return false; }
        @Override public boolean zeroLength() { return false; }
    }

    static class UndeclaredTag extends GreetTag {}

    /** A fake element: attributes, text, no children. */
    record Element(String name, Map<String, String> attributes, String text) implements TagElement {
        @Override public String localName() { return name; }
        @Override public String attribute(String n) { return attributes.getOrDefault(n, ""); }
        @Override public boolean hasAttribute(String n) { return attributes.containsKey(n); }
        @Override public Set<String> attributeNames() { return attributes.keySet(); }
        @Override public boolean isTrue(String n, boolean fallback) { return fallback; }
        @Override public List<TagNode> children() { return List.of(); }
        @Override public SourcePosition position() { return new SourcePosition("test.xml", "XML", 1, 1); }
        @Override public SourcePosition position(String attribute) { return position(); }
    }

    private final List<Object> narrated = new ArrayList<>();
    private int evaluations = 0;
    private TagContext context;

    @BeforeEach
    void setUp() {
        StoryObjectModel som = (StoryObjectModel) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[]{StoryObjectModel.class}, (proxy, method, args) -> {
                    if (method.getName().equals("narrate")) narrated.add(args[0]);
                    return null;
                });
        EvaluatorInterface evaluator = new EvaluatorInterface() {
            public boolean isEnabled() { return true; }
            public boolean hasExpressions(String text) { return text.contains("{"); }
            public String evaluate(String text) {
                evaluations++;
                return text.equals("{missing}") ? null : text.replace("{name}", "Ada");
            }
            public Object run(String script) { return null; }
        };
        context = new TagContext() {
            public StoryObjectModel som() { return som; }
            public SymbolPinax symbols() { return null; }
            public StoryObjectStash stash() { return new StoryObjectStashTest.MapStash(); }
            public EvaluatorInterface evaluator() { return evaluator; }
            public String blockName() { return "description"; }
            public Outcome executeBlock(List<TagNode> nodes) { return Outcome.proceed(); }
            public TagProcessorResultInterface renderBlock(List<TagNode> nodes) { return TagProcessorResult.empty(); }
            public Logger logger() { return Logger.getLogger("test"); }
        };
    }

    private GreetTag greet(Map<String, String> attributes, String text) {
        GreetTag tag = new GreetTag();
        tag.bind(new Element("greet", attributes, text), context);
        return tag;
    }

    // ─── plugin settings ────────────────────────────────────────────────────

    @Test
    void contextWithoutSettingsGivesFallbacks() {
        assertEquals("http://localhost", context.pluginProps().get("endpoint", "http://localhost"));
        assertEquals("", context.pluginProps().get("endpoint"));
    }

    // ─── attributes ─────────────────────────────────────────────────────────

    @Test
    void attributeWithoutExpressionsIsRaw() {
        assertEquals("Bob", greet(Map.of("to", "Bob"), "").getAttributeValue("to"));
    }

    @Test
    void attributeWithExpressionsIsEvaluated() {
        assertEquals("Ada", greet(Map.of("to", "{name}"), "").getAttributeValue("to"));
    }

    @Test
    void staticTagNeverCallsTheEvaluator() {
        GreetTag tag = greet(Map.of("to", "Bob", "mood", "calm"), "Hi");
        tag.getAttributeValue("to");
        tag.getAttributeValue("mood");
        assertEquals(0, evaluations);
    }

    @Test
    void dynamicTagEvaluatesAllItsAttributes() {
        GreetTag tag = greet(Map.of("to", "Bob", "mood", "{name}"), "");
        assertAll(
                () -> assertEquals("Bob", tag.getAttributeValue("to")),
                () -> assertEquals("Ada", tag.getAttributeValue("mood")),
                () -> assertEquals(2, evaluations));
    }

    @Test
    void attributeEvaluatingToNothingIsEmpty() {
        assertEquals("", greet(Map.of("to", "{missing}"), "").getAttributeValue("to"));
    }

    @Test
    void attributeEvaluatingToNothingIsAnEmptyString() {
        assertEquals("", greet(Map.of("to", "{missing}"), "").getAttributeAsString("to"));
    }

    @Test
    void attributeEvaluatingToNothingIsFalse() {
        assertFalse(greet(Map.of("to", "{missing}"), "").isTrue("to"));
    }

    @Test
    void processWithoutArgumentsUsesTheBoundElementAndBlock() {
        assertEquals("Hello, Ada!", greet(Map.of("to", "{name}"), "").process().get("text"));
    }

    @Test
    void missingAttributeIsEmpty() {
        assertEquals("", greet(Map.of(), "").getAttributeAsString("to"));
    }

    @Test
    void isTrueReadsTheEvaluatedValue() {
        GreetTag tag = greet(Map.of("a", "true", "b", "TRUE", "c", "yes", "d", "false"), "");
        assertAll(
                () -> assertTrue(tag.isTrue("a")),
                () -> assertTrue(tag.isTrue("b")),
                () -> assertFalse(tag.isTrue("c"), "yes is a setting, not a condition"),
                () -> assertFalse(tag.isTrue("d")),
                () -> assertFalse(tag.isTrue("missing")));
    }

    @Test
    void nameAndBlockComeFromElementAndContext() {
        GreetTag tag = greet(Map.of(), "");
        assertAll(
                () -> assertEquals("greet", tag.getName()),
                () -> assertEquals("description", tag.blockName()));
    }

    // ─── dynamic ────────────────────────────────────────────────────────────

    @Test
    void plainTagIsNotDynamic() {
        assertFalse(greet(Map.of("to", "Bob"), "Hi").isDynamic());
    }

    @Test
    void expressionInAnAttributeMakesItDynamic() {
        assertTrue(greet(Map.of("to", "{name}"), "").isDynamic());
    }

    @Test
    void expressionInTheTextMakesItDynamic() {
        assertTrue(greet(Map.of(), "Hi {name}").isDynamic());
    }

    // ─── declaration ────────────────────────────────────────────────────────

    @Test
    void declarationIsReadFromTheAnnotation() {
        GreetTag tag = greet(Map.of(), "");
        assertAll(
                () -> assertEquals("greet, hello", tag.getTargets()),
                () -> assertTrue(tag.hasProcessingType(SemanticType.TEXT_GENERATOR)),
                () -> assertFalse(tag.hasProcessingType(SemanticType.WEB)));
    }

    @Test
    void classWithoutAnnotationDeclaresNothing() {
        UndeclaredTag tag = new UndeclaredTag();
        tag.bind(new Element("x", Map.of(), ""), context);
        assertAll(
                () -> assertEquals("", tag.getTargets()),
                () -> assertFalse(tag.hasProcessingType(SemanticType.TEXT_GENERATOR)));
    }

    // ─── run ────────────────────────────────────────────────────────────────

    @Test
    void processUsesTheEvaluatedAttribute() {
        assertEquals("Hello, Ada!", greet(Map.of("to", "{name}"), "").process().get("text"));
    }

    @Test
    void executeProceedsByDefault() {
        assertEquals(Outcome.proceed(), greet(Map.of("to", "Bob"), "").execute());
    }

    @Test
    void resultsStartANewRecordByDefault() {
        assertFalse(greet(Map.of(), "").doesStickToLastText());
    }

    @Test
    void bindRejectsNull() {
        GreetTag tag = new GreetTag();
        assertThrows(NullPointerException.class, () -> tag.bind(null, context));
    }

    // ─── narration ──────────────────────────────────────────────────────────

    @Test
    void narrateTextGoesToTheStoryModel() {
        greet(Map.of(), "").narrate("The Duke nods.");
        assertEquals(List.of("The Duke nods."), narrated);
    }

    @Test
    void narrateResultsNarratesOneLinePerResult() {
        TagProcessorResult result = TagProcessorResult.emptyText();
        result.appendTextToTheLast("One.");
        result.addResult(TagProcessorResult.commenceHandling("Two."));
        greet(Map.of(), "").narrate(result);

        Map<String, NarrationValue<String>> one = new LinkedHashMap<>();
        one.put("text", NarrationValue.of("One."));
        Map<String, NarrationValue<String>> two = new LinkedHashMap<>();
        two.put("text", NarrationValue.of("Two."));
        assertEquals(List.of(one, two), narrated);
    }
}
