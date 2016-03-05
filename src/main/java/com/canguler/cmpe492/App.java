package com.canguler.cmpe492;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.StatementDeParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws JSQLParserException, FileNotFoundException {
        Scanner scanner = new Scanner(new File("create_table.sql"));
        StringBuffer queryBuffer = new StringBuffer();
        List<String> queries = new ArrayList<String>();
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            int length = nextLine.length();
            if (length > 0) {
                queryBuffer.append(nextLine);
                queryBuffer.append('\n');
                if (nextLine.charAt(nextLine.length() - 1) == ';') {
                    queries.add(queryBuffer.toString());
                    queryBuffer = new StringBuffer();
                }
            }
        }

        HashMap<String, Set<String>> tablesOfColumn = new HashMap<String, Set<String>>();
        for (String query : queries) {
            learnTableSchema(tablesOfColumn, query);
        }
        func1(tablesOfColumn);
    }

    public static void learnTableSchema(HashMap<String, Set<String>> tablesOfColumn, String createTableQueryString) throws FileNotFoundException, JSQLParserException {
        CreateTable createTable = (CreateTable) CCJSqlParserUtil.parse(createTableQueryString);
        Table table = createTable.getTable();
        List<ColumnDefinition> columnDefinitions = createTable.getColumnDefinitions();
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            String columnName = columnDefinition.getColumnName().toLowerCase();
            if (!tablesOfColumn.containsKey(columnName)) {
                tablesOfColumn.put(columnName, new HashSet<String>());
            }
            tablesOfColumn.get(columnName).add(table.getName().toLowerCase());
        }
    }

    public static void func1(HashMap<String, Set<String>> tablesOfColumnMap) throws JSQLParserException {
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


        Set<String> tableNames = new HashSet<String>();
        for (FromItem fromItem : plainSelectVisitor.fromItems) {
            String tableName = fromItem.toString().toLowerCase();
            tableNames.add(tableName);
        }

        Set<Column> columnSet = plainSelectVisitor.getColumnSet();

        for (Column column : columnSet) {
            String columnName = column.getColumnName();
            if (tablesOfColumnMap.containsKey(columnName)) {
                Set<String> tablesOfColumn = tablesOfColumnMap.get(columnName);

                Set<String> intersection = new HashSet<String>(tablesOfColumn);
                intersection.retainAll(tableNames);

                if (intersection.size() == 1) {
                    System.out.println(columnName + " : " + intersection);
                } else {
                    System.out.println(columnName + " : " + intersection);
                }
            }
        }
//        System.out.println("Columns: " + plainSelectVisitor.getColumnSet());


//        Expression a = CCJSqlParserUtil.parseExpression("p_container in ('SM CASE', 'SM BOX', 'SM PACK', 'SM PKG')");
//        System.out.println(a);
    }
}
