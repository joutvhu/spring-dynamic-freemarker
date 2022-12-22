package com.joutvhu.dynamic.freemarker.directive;

import com.joutvhu.dynamic.commons.directive.TrimSymbol;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.Writer;

public class TrimWriter extends Writer {
    private final Writer out;
    private final TrimSymbol symbol;
    private final StringBuilder contentBuilder = new StringBuilder();

    public static TrimWriter of(Writer out, TrimSymbol symbols) {
        return new TrimWriter(out, symbols);
    }

    public TrimWriter(Writer out, TrimSymbol symbol) {
        this.out = out;
        this.symbol = symbol;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        String content = String.copyValueOf(cbuf);
        this.contentBuilder.append(content);
    }

    public void afterWrite() throws IOException {
        String content = this.contentBuilder.toString();
        content = symbol.process(content);
        out.write(content);
    }

    public void render(TemplateDirectiveBody body) throws IOException, TemplateException {
        body.render(this);
        this.afterWrite();
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
