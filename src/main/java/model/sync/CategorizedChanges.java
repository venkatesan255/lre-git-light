package model.sync;

import java.util.List;

public record CategorizedChanges(
        List<ScriptChange> uploaded,
        List<ScriptChange> deleted,
        List<ScriptChange> unchanged,
        List<ScriptChange> failed
) {

}
