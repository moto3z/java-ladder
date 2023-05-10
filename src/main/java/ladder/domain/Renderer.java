package ladder.domain;

import ladder.control.Preferences;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Renderer {
    private final int column;
    private final int row;
    private final Lines lines;
    private final Users users;

    public Renderer(int column, int row, Lines lines, Users users) {
        this.column = column;
        this.row = row;
        this.lines = lines;
        this.users = users;
    }

    private List<String> renderingSceneLadderArea() {
        return IntStream.range(0, row)
                .mapToObj(rowIndex -> renderingHorizontal(Row.of(rowIndex), Column.of(column)))
                .collect(Collectors.toList());
    }

    private String renderingHorizontal(Row index, Column range) {
        return IntStream.range(0, range.getValue())
                .mapToObj(columnIndex -> renderingPixel( Column.of(columnIndex), index))
                .collect(Collectors.joining());
    }

    private String renderingPixel(Column columnIndex, Row rowIndex) {
        return Preferences.pillarShape()
                .concat(lineShape(columnIndex, rowIndex));
    }

    private String lineShape(Column columnIndex, Row rowIndex) {
        return lines.existLine(new Line(columnIndex,rowIndex)) ? Preferences.allocatedLineShape() : Preferences.emptyLineShape();
    }

    public Scene renderingSceneWithUser() {
        return new Scene(renderingSceneLadderArea(),renderingSceneUserArea());
    }

    private String renderingSceneUserArea() {
        return users.names().stream()
                .collect(Collectors.joining(Preferences.userDelimiter()));
    }
}
