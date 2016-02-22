package com.canguler.cmpe492;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitorAdapter;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;

import java.util.HashSet;
import java.util.Set;

public class ExpressionVisitorImpl extends ExpressionVisitorAdapter {
    public Set<Column> columnSet;

    public ExpressionVisitorImpl() {
        super();
        columnSet = new HashSet<Column>();
    }

    @Override
    public void visit(Column column) {
        columnSet.add(column);
    }

    @Override
    public void visit(InExpression expr) {
        Expression leftExpression = expr.getLeftExpression();
        ItemsList leftItemsList = expr.getLeftItemsList();
        ItemsList rightItemsList = expr.getRightItemsList();

        if (leftExpression != null) {
            leftExpression.accept(this);
        }

        if (leftItemsList != null) {
            leftItemsList.accept(this);
        }

        if (rightItemsList != null) {
            rightItemsList.accept(this);
        }
    }
}
