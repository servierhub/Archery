package com.github.romualdrousseau.any2json.classifier;

import java.util.List;
import java.util.regex.Pattern;

import com.github.romualdrousseau.any2json.Model;
import com.github.romualdrousseau.any2json.TagClassifier;
import com.github.romualdrousseau.shuju.preprocessing.tokenizer.ShingleTokenizer;
import com.github.romualdrousseau.shuju.strings.StringUtils;

public class SimpleTagClassifier implements TagClassifier {

    public SimpleTagClassifier() {
        this(null, TagClassifier.TagStyle.NONE);
    }

    public SimpleTagClassifier(final Model model, final TagClassifier.TagStyle tagStyle) {
        this.model = model;
        this.tagStyle = tagStyle;

        final List<String> lexicon = (model != null && model.getData().hasKey("lexicon"))
                ? model.getData().getList("lexicon")
                : StringUtils.getSymbols().stream().toList();
        this.tagTokenizer = new ShingleTokenizer(lexicon, 1);
    }

    @Override
    public void close() throws Exception {
    }

    public void updateModelData() {
    }

    @Override
    public Model getModel() {
        return this.model;
    }

    @Override
    public TagClassifier setModel(final Model model) {
        this.model = model;
        if (this.model != null) {
            this.updateModelData();
        }
        return this;
    }

    @Override
    public TagClassifier.TagStyle getTagStyle() {
        return this.tagStyle;
    }

    @Override
    public TagClassifier setTagStyle(final TagClassifier.TagStyle mode) {
        this.tagStyle = mode;
        return this;
    }

    @Override
    public String ensureTagStyle(final String text) {
        if (this.tagStyle == TagClassifier.TagStyle.SNAKE) {
            this.tagTokenizer.disableLemmatization();
            return StringUtils.toSnake(text, this.tagTokenizer);
        }
        if (this.tagStyle == TagClassifier.TagStyle.CAMEL) {
            this.tagTokenizer.disableLemmatization();
            return StringUtils.toCamel(text, this.tagTokenizer);
        }
        if (text.indexOf(" ") > 0 || text.indexOf("_") > 0) {
            this.tagTokenizer.enableLemmatization();
            return StringUtils.toSnake(text, this.tagTokenizer);
        } else {
            this.tagTokenizer.enableLemmatization();
            return StringUtils.toCamel(text, this.tagTokenizer);
        }
    }

    @Override
    public String predict(String name, List<String> entities, List<String> context) {
        final var m = pattern.matcher(name);
        if (m.find()) {
            return m.group(1);
        } else {
            return this.ensureTagStyle(name);
        }
    }

    private final Pattern pattern = Pattern.compile(" \\(\\$(.*)\\)$");
    private final ShingleTokenizer tagTokenizer;

    private Model model;
    private TagClassifier.TagStyle tagStyle;
}