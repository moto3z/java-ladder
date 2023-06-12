package ladder.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import ladder.utils.ProbabilityStrategyRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Ladder {

  private static final Logger log = LoggerFactory.getLogger(Ladder.class);
  private final Column maxColumn;
  private final Row maxRow;
  private final Set<Line> lines;

  public Ladder(Column maxColumn, Row maxRow, Set<Line> lines) {
    this.maxColumn = maxColumn;
    this.maxRow = maxRow;
    this.lines = lines;
  }

  public static Ladder of(Set<Line> lines, int columMax, int rowMax) {
    log.debug("테스트를 위한 생성자");
    return new Ladder(
        Column.of(columMax),
        Row.of(rowMax),
        lines
    );
  }

  public static Ladder of(int column, int row) {
    Ladder ladder = new Ladder(Column.of(column), Row.of(row), new HashSet<>());
    IntStream.range(0, row)
        .boxed()
        .flatMap(i -> IntStream.range(0, column)
            .mapToObj(j -> new Line(j, i))
            .filter(ladder::isPossibilAddingLine)
        )
        .filter(line -> ProbabilityStrategyRandom.of().result())
        .forEach(ladder::append);
    return ladder;
  }

  private boolean isPossibilAddingLine(Line line) {
    return !this.hasCrossIntersection(line)
        && this.isEdgeExceeded(line);
  }

  private boolean isEdgeExceeded(Line line) {
    return this.maxColumn.isGraterThan(line.getColumn()) || this.maxRow.isGraterThan(line.getRow());
  }

  public void append(Line anyLine) {
    this.lines.add(anyLine);
  }

  public boolean hasCrossIntersection(Line otherLine) {
    return lines.stream()
        .filter(line -> line.isSameRow(otherLine))
        .anyMatch(line -> line.isAdjacentColumn(otherLine));
  }

  public Set<Line> allLines() {
    return lines;
  }

  public boolean existLine(Line otherLine) {
    return lines.stream().anyMatch(line -> line.isSame(otherLine));
  }

  public List<Integer> orderOnBottom() {
    return LadderSolver.calculate(this.maxColumn.getValue(), lines);
  }

  public Column getMaxColumn() {
    return maxColumn;
  }

  public Row getMaxRow() {
    return maxRow;
  }
}
