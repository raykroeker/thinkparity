/*
 * Created On:  26-Feb-07 7:04:59 PM
 */
package com.thinkparity.ophelia.model.index.lucene;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity OpheliaModel Lucene Utilities<br>
 * <b>Description:</b>A series of lucene utilities.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class LuceneUtil {

    /** A singleton instance of <code>LuceneUtil</code>. */
    private static final LuceneUtil INSTANCE;

    /** An apache <code>Log4JWrapper</code>. */
    private static final Log4JWrapper LOGGER;

    /** A list of replacement characters for the special characters that need to be escaped. */
    private static final String[] REPLACEMENT_STRINGS;

    /** A list of special characters that need to be escaped. */
    private static final String[] SPECIAL_STRINGS;

    static {
        LOGGER = new Log4JWrapper(LuceneUtil.class.getPackage().getName());
        INSTANCE = new LuceneUtil();
        REPLACEMENT_STRINGS = new String[] { "\\+", "\\-", "\\&", "\\|", "\\!",
                "\\(", "\\)", "\\{", "\\}", "\\[", "\\]", "\\^", "\\\"", "\\~",
                "\\*", "\\?", "\\:", "\\\\" };
        SPECIAL_STRINGS = new String[] { "+", "-", "&", "|", "!", "(", ")", "{",
                "}", "[", "]", "^", "\"", "~", "*", "?", ":", "\\" };        
    }

    /**
     * Obtain an instance of lucene util.
     * 
     * @return An instance of <code>LuceneUtil</code>.
     */
    public static LuceneUtil getInstance() {
        return INSTANCE;
    }

    /**
     * Create LuceneUtil.
     *
     */
    private LuceneUtil() {
        super();
    }

    /**
     * If the expression does not end with a wildcard character add one.
     * 
     * @param expression
     *            The search expression <code>String</code>.
     */
    String appendWildcard(final String expression) {
        if ('*' == expression.charAt(expression.length() - 1))
            return expression;
        else
            return expression + '*';
    }

    /**
     * Parse the expression and replace any special characters.
     * 
     * @param expression
     *            A search expression <code>String</code>.
     * @return A special lucene format expression <code>String</code> with
     *         special characters escaped.
     */
    String escapeSpecialCharacters(final String expression) {
        String escapedExpression = expression;
        for (int i = 0; i < SPECIAL_STRINGS.length; i++) {
            escapedExpression = escapedExpression.replace(SPECIAL_STRINGS[i],
                    REPLACEMENT_STRINGS[i]);
        }
        return escapedExpression;
    }

    /**
     * Log the value of a variable.
     * 
     * @param <V>
     *            A variable type.
     * @param name
     *            A variable name <code>String</code>.
     * @param value
     *            A variable value <code>V</code>.
     * @return The variable value.
     */
    final <V> V logVariable(final String name, final V value) {
        return LOGGER.logVariable(name, value);
    }

    /**
     * If the expression does not start with a wildcard character add one.
     * 
     * @param expression
     *            The search expression <code>String</code>.
     */
    String prependWildcard(final String expression) {
        if ('*' == expression.charAt(0))
            return expression;
        else
            return '*' + expression;
    }

    /**
     * Tokenize the expression.
     * 
     * @param expression
     *            An expression.
     * @return A list of tokens within the expression.
     */
    List<String> tokenizeExpression(final String expression) {
        final StringTokenizer tokenizer = new StringTokenizer(expression, " ");
        final List<String> tokenized = new ArrayList<String>(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            tokenized.add(tokenizer.nextToken());
        }
        return tokenized;
    }
}
