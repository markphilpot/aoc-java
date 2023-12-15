package com.markphilpot;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class DishTest {
  private static final Logger log = LogManager.getLogger(DishTest.class);

  @Test
  public void testSamplePart1() {
    var inputStream = DishTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var grid = Dish.parse(inputStream);
    Dish.rollNorth(grid);
    var total = Dish.findTotalLoad(grid);

    var afterRollNorth =
        """
OOOO.#.O..
OO..#....#
OO..O##..O
O..#.OO...
........#.
..#....#.#
..O..#.O.O
..O.......
#....###..
#....#....""";

    assertThat(Dish.render(grid), is(afterRollNorth));
    assertThat(total, is(136));
  }

  @Test
  public void testInputPart1() {
    var inputStream = DishTest.class.getClassLoader().getResourceAsStream("input.txt");

    var grid = Dish.parse(inputStream);
    Dish.rollNorth(grid);
    var total = Dish.findTotalLoad(grid);

    log.info(total);
  }

  @Test
  public void testSpin() {
    var inputStream = DishTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var grid = Dish.parse(inputStream);

    var afterCycle =
        """
.....#....
....#...O#
...OO##...
.OO#......
.....OOO#.
.O#...O#.#
....O#....
......OOOO
#...O###..
#..OO#....""";

    Dish.spin(grid);

    assertThat(Dish.render(grid), is(afterCycle));

    afterCycle =
        """
.....#....
....#...O#
.....##...
..O#......
.....OOO#.
.O#...O#.#
....O#...O
.......OOO
#..OO###..
#.OOO#...O""";

    Dish.spin(grid);

    assertThat(Dish.render(grid), is(afterCycle));

    afterCycle =
        """
.....#....
....#...O#
.....##...
..O#......
.....OOO#.
.O#...O#.#
....O#...O
.......OOO
#...O###.O
#.OOO#...O""";

    Dish.spin(grid);

    assertThat(Dish.render(grid), is(afterCycle));
  }

  @Test
  public void testSamplePart2() {
    var inputStream = DishTest.class.getClassLoader().getResourceAsStream("sample.txt");

    var grid = Dish.parse(inputStream);
    var cycle = Dish.findCycle(grid);

    log.info(cycle);

    inputStream = DishTest.class.getClassLoader().getResourceAsStream("sample.txt");
    grid = Dish.parse(inputStream);

    var total = 0;
    var t = 1_000_000_000;
    var x = (t - cycle.offset()) / cycle.length();
    var n = t - cycle.offset() - (cycle.length() * x);

    log.info(n);

    for (var i = 1; i <= cycle.offset() + n; i++) {
      Dish.spin(grid);
      total = Dish.findTotalLoad(grid);
      log.info("%d %d".formatted(i, total));
    }

    assertThat(total, is(64));
  }

  @Test
  public void testInputPart2() {
    var inputStream = DishTest.class.getClassLoader().getResourceAsStream("input.txt");

    var grid = Dish.parse(inputStream);
    var cycle = Dish.findCycle(grid);

    log.info(cycle);

    inputStream = DishTest.class.getClassLoader().getResourceAsStream("input.txt");
    grid = Dish.parse(inputStream);

    var total = 0;
    var t = 1_000_000_000;
    var x = (t - cycle.offset()) / cycle.length();
    var n = t - cycle.offset() - (cycle.length() * x);

    log.info(n);

    for (var i = 1; i <= cycle.offset() + n; i++) {
      Dish.spin(grid);
      total = Dish.findTotalLoad(grid);
      log.info("%d %d".formatted(i, total));
    }
  }
}
