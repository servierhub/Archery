package com.github.romualdrousseau.archery;

import java.nio.file.Path;
import java.util.EnumSet;
import java.util.stream.StreamSupport;
import java.net.URL;
import java.io.File;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

/**
 * Unit test for simple App.
 */
public class Test_CSV {

    /**
     * Rigorous Test :-)
     */
    @Test
    @Tag("unit")
    public void testReadWithoutIntelliTag() {
        final File file = this.getResourceFile("Singapore - ZUELLIG - Sales - 202101.csv");

        final Model model = ModelDB.createConnection("sales-english");

        try (final Document doc = DocumentFactory.createInstance(file, "UTF-8").setModel(model)) {
            doc.sheets().forEach(s -> s.getTable().ifPresent(t -> t.headers().forEach(h -> System.out.print(h.getTag().getValue() + " "))));
            System.out.println();
            doc.sheets().forEach(s -> s.getTable().ifPresent(t -> StreamSupport.stream(t.rows().spliterator(), false).limit(5).forEach(r -> {
                r.cells().forEach(c -> System.out.print(c.getValue() + " "));
                System.out.println();
            })));
        }
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    @Tag("unit")
    public void testReadWithoutIntelliTagAndWithPivot() {
        final File file = this.getResourceFile("Bulgaria - IQVIA NATIONAL Other pharmacy - Product Sales - 202306.csv");

        final Model model = ModelDB.createConnection("sales-english");

        try (final Document doc = DocumentFactory.createInstance(file, "UTF-8")
                .setModel(model)
                .setHints(EnumSet.of(Document.Hint.INTELLI_LAYOUT))) {
            doc.sheets().forEach(s -> s.getTable().ifPresent(t -> t.headers().forEach(h -> System.out.print(h.getTag().getValue() + " "))));
            System.out.println();
            doc.sheets().forEach(s -> s.getTable().ifPresent(t -> StreamSupport.stream(t.rows().spliterator(), false).limit(5).forEach(r -> {
                r.cells().forEach(c -> System.out.print(c.getValue() + " "));
                System.out.println();
            })));
        }
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    @Tag("unit")
    public void testReadWithIntelliTag() {
        final File file = this.getResourceFile("Singapore - ZUELLIG - Sales - 202101.csv");

        final Model model = ModelDB.createConnection("sales-english");

        try (final Document doc = DocumentFactory.createInstance(file, "UTF-8")
                .setModel(model)
                .setHints(EnumSet.of(Document.Hint.INTELLI_TAG))) {
            doc.sheets().forEach(s -> s.getTable().ifPresent(t -> t.headers().forEach(h -> System.out.print(h.getTag().getValue() + " "))));
            System.out.println();
            doc.sheets().forEach(s -> s.getTable().ifPresent(t -> StreamSupport.stream(t.rows().spliterator(), false).limit(5).forEach(r -> {
                r.cells().forEach(c -> System.out.print(c.getValue() + " "));
                System.out.println();
            })));
        }
    }

    private File getResourceFile(String resourceName) {
        try {
            URL resourceUrl = this.getClass().getResource(String.format("/data/%s", resourceName));
            assert resourceUrl != null : resourceName + " not found";
            return Path.of(resourceUrl.toURI()).toFile();
        } catch (URISyntaxException x) {
            assert false : x.getMessage();
            return null;
        }
    }
}
