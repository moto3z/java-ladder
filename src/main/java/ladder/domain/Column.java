package ladder.domain;

import ladder.exception.OutOfColumnRangeException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Column {
    private static final int COLUMN_CACHE_MAX = 50;

    private static final List<Column> COLUMNS_CACHE =
            IntStream.rangeClosed(0, COLUMN_CACHE_MAX)
                    .boxed()
                    .map(Column::new)
                    .collect(Collectors.toList());

    private final int value;

    private Column(int value) {
        this.value = value;
    }

    public static Column of(int value) {
        if (value < 0) {
            throw new OutOfColumnRangeException();
        }
        if (value <= COLUMN_CACHE_MAX) {
            return COLUMNS_CACHE.get(value);
        }
        return new Column(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column otherColumn = (Column) o;
        return this.hashCode() == otherColumn.hashCode();
    }

    @Override
    public int hashCode() {
        return value;
    }

    public boolean isAdjacent(Column other) {
        return Math.abs(this.value - other.value) <= 1;
    }

    public boolean isSame(Column other) {
        return equals(other);
    }

    public int getValue() {
        return this.value;
    }

    public boolean isGraterThan(Column other) {
        return this.value - 1 > other.value;
    }
}
