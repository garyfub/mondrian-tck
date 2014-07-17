package org.pentaho.mondrian.tck;

import mondrian.olap.MondrianProperties;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import static org.pentaho.mondrian.tck.MondrianExpectation.newBuilder;

public class BasicTest {
  private static final Logger logger = LoggerFactory.getLogger( BasicTest.class );

  @Test
  public void testSelectFromSales() throws SQLException, IOException, ExecutionException {
    final MondrianExpectation expectation =
      newBuilder()
        .query( "select from sales" )
        .result(
          "Axis #0:\n"
            + "{}\n"
            + "266,773" )
        .sql( "select\n"
          + "    time_by_day.the_year as c0,\n"
          + "    sum(sales_fact_1997.unit_sales) as m0\n"
          + "from\n"
          + "    time_by_day time_by_day,\n"
          + "    sales_fact_1997 sales_fact_1997\n"
          + "where\n"
          + "    sales_fact_1997.time_id = time_by_day.time_id\n"
          + "and\n"
          + "    time_by_day.the_year = 1997\n"
          + "group by\n"
          + "    time_by_day.the_year" )
        .build();
    MondrianContext context = MondrianContext.defaultContext();
    context.verify( expectation );
  }

  @SuppressWarnings( "UnusedDeclaration" )
  public void testExampleOverrideProperties() throws Exception {
    new PropertyContext()
      .withProperty( MondrianProperties.instance().ResultLimit, "5" )
      .execute(
        new Runnable() {
          @Override
          public void run() {
            MondrianExpectation expectation =
              MondrianExpectation.newBuilder().query( "Select from Sales" ).build();
            try {
              MondrianContext.defaultContext().verify( expectation );
            } catch ( Exception e ) {
              logger.error( "oops", e );
              throw new RuntimeException( e );
            }
          }
        } );
  }
}
