package com.joutvhu.dynamic.freemarker.directive;

import com.joutvhu.dynamic.commons.directive.TrimSymbol;
import freemarker.core.Environment;
import freemarker.ext.beans.InvalidPropertyException;
import freemarker.template.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The trim directive knows to only insert the prefix and suffix if there is any content returned by the containing tags
 * And the trim directive will remove prefixOverrides and suffixOverrides in the content
 * They are used in templates like {@code <@trim prefix="where (" prefixOverrides=["and ", "or "] suffix=")" suffixOverrides=[" and", " or"]>...</@trim>}
 *
 * @author Giao Ho
 * @since 1.0.0
 */
public class TrimDirective implements TemplateDirectiveModel {
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (body != null)
            TrimWriter.of(env.getOut(), createSymbol(params)).render(body);
    }

    private TrimSymbol createSymbol(Map params) throws TemplateModelException {
        try {
            return new TrimSymbol(
                    getStringParam(params, "prefix"),
                    getListParam(params, "prefixOverrides"),
                    getStringParam(params, "suffix"),
                    getListParam(params, "suffixOverrides"));
        } catch (NullPointerException | TemplateException e) {
            throw new TemplateModelException(e.getMessage());
        }
    }

    private String getStringParam(Map<String, Object> params, String name) throws TemplateException {
        if (params.containsKey(name)) {
            Object param = params.get(name);
            if (param instanceof SimpleScalar)
                return getStringFromSimpleScalar((SimpleScalar) param, name);
        }
        return null;
    }

    private String getStringFromSimpleScalar(SimpleScalar simpleScalar, String name) throws TemplateException {
        String value = simpleScalar.getAsString();
        if (value != null && !value.trim().isEmpty())
            return value;
        else throw new InvalidPropertyException("The " + name + " param cannot be empty string or spaces.");
    }

    private List<String> getListParam(Map<String, Object> params, String name) throws TemplateException {
        List<String> result = new ArrayList<>();
        if (params.containsKey(name)) {
            Object param = params.get(name);
            if (param instanceof SimpleScalar)
                result.add(getStringFromSimpleScalar((SimpleScalar) param, name));
            else if (param instanceof SimpleSequence)
                return getListFromSimpleSequence((SimpleSequence) param, name);
        }
        return result;
    }

    private List<String> getListFromSimpleSequence(SimpleSequence simpleSequence, String name) throws TemplateException {
        List<String> result = new ArrayList<>();
        for (int i = 0, len = simpleSequence.size(); i < len; i++) {
            String value = simpleSequence.get(i).toString();
            if (value != null && !value.trim().isEmpty())
                result.add(value);
            else throw new InvalidPropertyException("The " + name + " param cannot be contains empty or spaces.");
        }
        return result;
    }
}
