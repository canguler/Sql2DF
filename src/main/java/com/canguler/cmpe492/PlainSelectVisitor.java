package com.canguler.cmpe492;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlainSelectVisitor implements SelectVisitor {
    public List<FromItem> fromItems;
    public ExpressionVisitorImpl expressionVisitor;

    public PlainSelectVisitor() {
        this.fromItems = new ArrayList<FromItem>();
        expressionVisitor = new ExpressionVisitorImpl();
    }

    public Set<Column> getColumnSet() {
        return expressionVisitor.columnSet;
    }

    public void visit(PlainSelect plainSelect) {
        Expression expression = plainSelect.getWhere();
        expression.accept(expressionVisitor);
        fromItems.add(plainSelect.getFromItem());

        List<Join> joins = plainSelect.getJoins();
        if (joins != null) {
            for (Join join : joins) {
                fromItems.add(join.getRightItem());
            }
        }
    }

    public void visit(SetOperationList setOpList) {

    }

    public void visit(WithItem withItem) {

    }
}
