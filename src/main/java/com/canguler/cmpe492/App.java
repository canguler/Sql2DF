package com.canguler.cmpe492;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws JSQLParserException {

        Statement statement = CCJSqlParserUtil.parse("select\n" +
                "\tsum(l_extendedprice * (1 - l_discount) ) as revenue\n" +
                "from\n" +
                "\tlineitem,\n" +
                "\tpart\n" +
                "where\n" +
                "\t(\n" +
                "\t\tp_partkey = l_partkey\n" +
                "\t\tand p_brand = 'Brand#12'\n" +
                "\t\tand p_container in ('SM CASE', 'SM BOX', 'SM PACK', 'SM PKG')\n" +
                "\t\tand l_quantity >= 1 and l_quantity <= 1 + 10\n" +
                "\t\tand p_size between 1 and 5\n" +
                "\t\tand l_shipmode in ('AIR', 'AIR REG')\n" +
                "\t\tand l_shipinstruct = 'DELIVER IN PERSON'\n" +
                "\t)\n" +
                "or\n" +
                "\t(\n" +
                "\t\tp_partkey = l_partkey\n" +
                "\t\tand p_brand = 'Brand#23'\n" +
                "\t\tand p_container in ('MED BAG', 'MED BOX', 'MED PKG', 'MED PACK')\n" +
                "\t\tand l_quantity >= 10 and l_quantity <= 10 + 10\n" +
                "\t\tand p_size between 1 and 10\n" +
                "\t\tand l_shipmode in ('AIR', 'AIR REG')\n" +
                "\t\tand l_shipinstruct = 'DELIVER IN PERSON'\n" +
                "\t)\n" +
                "or\n" +
                "\t(\n" +
                "\t\tp_partkey = l_partkey\n" +
                "\t\tand p_brand = 'Brand#34'\n" +
                "\t\tand p_container in ( 'LG CASE', 'LG BOX', 'LG PACK', 'LG PKG')\n" +
                "\t\tand l_quantity >= 20 and l_quantity <= 20 + 10\n" +
                "\t\tand p_size between 1 and 15\n" +
                "\t\tand l_shipmode in ('AIR', 'AIR REG')\n" +
                "\t\tand l_shipinstruct = 'DELIVER IN PERSON'\n" +
                "\t)");
        Select selectStatement = (Select) statement;
        PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
        PlainSelectVisitor plainSelectVisitor = new PlainSelectVisitor();
        plainSelect.accept(plainSelectVisitor);
        System.out.println("Tables: " + plainSelectVisitor.fromItems);
        System.out.println("Columns: " + plainSelectVisitor.getColumnSet());


//        Expression a = CCJSqlParserUtil.parseExpression("p_container in ('SM CASE', 'SM BOX', 'SM PACK', 'SM PKG')");
//        System.out.println(a);

    }
}
