package com.github.romualdrousseau.archery.parser.layex.operations;

import com.github.romualdrousseau.archery.base.Symbol;
import com.github.romualdrousseau.archery.parser.layex.Lexer;
import com.github.romualdrousseau.archery.parser.layex.TableMatcher;
import com.github.romualdrousseau.archery.parser.layex.TableParser;

public class ValueNeg implements TableMatcher {

    public ValueNeg(final String v) {
        this.v = v.toLowerCase();
    }

    @Override
    public <S extends Symbol, C> boolean match(final Lexer<S, C> stream, final TableParser<S> context) {
        final var symbol = stream.read();
        final var c = symbol.getSymbol();
        if (!c.equals("") && c.charAt(0) >= 'a' && c.charAt(0) <= 'z' && !c.equals(this.v)) {
            if (context != null) {
                context.notify(symbol);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "!VALUE('" + this.v + "')";
    }

    private final String v;
}